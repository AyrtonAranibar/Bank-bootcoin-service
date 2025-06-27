package com.bank.ayrton.bootcoin_service.service.bootcoin;

import com.bank.ayrton.bootcoin_service.api.bootcoin.BootcoinWalletRepository;
import com.bank.ayrton.bootcoin_service.api.bootcoin.TradeRequestRepository;
import com.bank.ayrton.bootcoin_service.api.bootcoin.TradeService;
import com.bank.ayrton.bootcoin_service.dto.BootcoinTradeRequestedEvent;
import com.bank.ayrton.bootcoin_service.dto.BootcoinTransactionEvent;
import com.bank.ayrton.bootcoin_service.dto.TradeRequestDto;
import com.bank.ayrton.bootcoin_service.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeServiceImpl implements TradeService {

    private final TradeRequestRepository repository;
    private final BootcoinWalletRepository walletRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final WebClient yankiClient;

    @Override
    public Mono<TradeRequestDto> createTradeRequest(TradeRequestDto dto) {
        TradeRequest entity = new TradeRequest();
        entity.setRequesterWalletId(dto.getRequesterWalletId());
        entity.setAmount(dto.getAmount());
        entity.setTransferMethod(dto.getTransferMethod());
        entity.setTradeType(dto.getTradeType()); // nuevo campo
        entity.setStatus(TradeStatus.PENDING);
        entity.setRequestedAt(LocalDateTime.now());

        return repository.save(entity)
                .doOnNext(saved -> {
                    BootcoinTradeRequestedEvent event = new BootcoinTradeRequestedEvent(
                            saved.getId(),
                            saved.getRequesterWalletId(),
                            saved.getAmount(),
                            saved.getTransferMethod(),
                            saved.getRequestedAt()
                    );
                    kafkaTemplate.send("bootcoin.trade.requested", event);
                })
                .map(saved -> dto);
    }

    @Override
    public Mono<Void> acceptTrade(String tradeId, String sellerWalletId) {
        return repository.findById(tradeId)
                .switchIfEmpty(Mono.error(new RuntimeException("Solicitud no encontrada")))
                .flatMap(trade -> {
                    if (trade.getStatus() != TradeStatus.PENDING) {
                        return Mono.error(new RuntimeException("La solicitud ya fue aceptada o completada"));
                    }

                    return walletRepository.findById(sellerWalletId)
                            .switchIfEmpty(Mono.error(new RuntimeException("El vendedor no tiene un monedero Bootcoin")))
                            .flatMap(sellerWallet -> {

                                if (trade.getTransferMethod() == TransferMethod.YANKI &&
                                        (sellerWallet.getAssociatedYankiWalletId() == null || sellerWallet.getAssociatedYankiWalletId().isBlank())) {
                                    return Mono.error(new RuntimeException("El vendedor no tiene un monedero Yanki asociado"));
                                }

                                if (trade.getTransferMethod() == TransferMethod.ACCOUNT &&
                                        (sellerWallet.getAssociatedAccountId() == null || sellerWallet.getAssociatedAccountId().isBlank())) {
                                    return Mono.error(new RuntimeException("El vendedor no tiene una cuenta bancaria asociada"));
                                }

                                trade.setStatus(TradeStatus.ACCEPTED);

                                Mono<String> fromMono;
                                Mono<String> toMono;

                                if (trade.getTradeType() == TradeType.BUY) {
                                    // Comprador paga, vendedor recibe
                                    fromMono = walletRepository.findById(trade.getRequesterWalletId())
                                            .flatMap(wallet -> yankiClient.get()
                                                    .uri("/card/{cardNumber}", wallet.getAssociatedYankiWalletId())
                                                    .retrieve()
                                                    .bodyToMono(YankiWalletDto.class)
                                                    .doOnNext(dto -> log.info("Obtenida YankiWalletDto: {}", dto))
                                                    .map(YankiWalletDto::getCardNumber));

                                    toMono = yankiClient.get()
                                            .uri("/card/{cardNumber}", sellerWallet.getAssociatedYankiWalletId())
                                            .retrieve()
                                            .bodyToMono(YankiWalletDto.class)
                                            .doOnNext(dto -> log.info("Obtenida YankiWalletDto: {}", dto))
                                            .map(YankiWalletDto::getCardNumber);
                                } else {
                                    // Vendedor paga, comprador recibe
                                    fromMono = yankiClient.get()
                                            .uri("/card/{cardNumber}", sellerWallet.getAssociatedYankiWalletId())
                                            .retrieve()
                                            .bodyToMono(YankiWalletDto.class)
                                            .map(YankiWalletDto::getCardNumber);

                                    toMono = walletRepository.findById(trade.getRequesterWalletId())
                                            .flatMap(wallet -> yankiClient.get()
                                                    .uri("/card/{cardNumber}", wallet.getAssociatedYankiWalletId())
                                                    .retrieve()
                                                    .bodyToMono(YankiWalletDto.class)
                                                    .doOnNext(dto -> log.info("Obtenida YankiWalletDto: {}", dto))
                                                    .map(YankiWalletDto::getCardNumber));
                                }

                                return Mono.zip(fromMono, toMono)
                                        .flatMap(tuple -> {
                                            String from = tuple.getT1();
                                            String to = tuple.getT2();

                                            log.info("Bootcoin transferencia - from (cardNumber): {}", from);
                                            log.info("Bootcoin transferencia - to (cardNumber): {}", to);
                                            log.info("Bootcoin transferencia - monto: {}", trade.getAmount());
                                            log.info("Bootcoin transferencia - método: {}", trade.getTransferMethod());

                                            BootcoinTransactionEvent event = new BootcoinTransactionEvent(
                                                    UUID.randomUUID().toString(),
                                                    trade.getTradeType() == TradeType.BUY ? from : to,     // quien paga
                                                    trade.getTradeType() == TradeType.BUY ? to : from,     // quien recibe
                                                    trade.getAmount(),
                                                    trade.getTransferMethod()
                                            );

                                            String topic = switch (trade.getTransferMethod()) {
                                                case YANKI -> "bootcoin.yanki.transfer";
                                                case ACCOUNT -> "bootcoin.account.transfer";
                                            };

                                            log.info("Publicando evento a Kafka: {}", event);
                                            kafkaTemplate.send(topic, event);

                                            return repository.save(trade)
                                                    .doOnNext(saved -> kafkaTemplate.send(topic, event))
                                                    .then();
                                        });
                            });
                });
    }
}