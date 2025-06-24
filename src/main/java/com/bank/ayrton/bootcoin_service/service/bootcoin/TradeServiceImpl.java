package com.bank.ayrton.bootcoin_service.service.bootcoin;

import com.bank.ayrton.bootcoin_service.api.bootcoin.TradeRequestRepository;
import com.bank.ayrton.bootcoin_service.api.bootcoin.TradeService;
import com.bank.ayrton.bootcoin_service.dto.BootcoinTradeRequestedEvent;
import com.bank.ayrton.bootcoin_service.dto.BootcoinTransactionEvent;
import com.bank.ayrton.bootcoin_service.dto.TradeRequestDto;
import com.bank.ayrton.bootcoin_service.entity.TradeRequest;
import com.bank.ayrton.bootcoin_service.entity.TradeStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TradeServiceImpl implements TradeService {

    private final TradeRequestRepository repository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public Mono<TradeRequestDto> createTradeRequest(TradeRequestDto dto) {
        TradeRequest entity = new TradeRequest();
        entity.setRequesterWalletId(dto.getRequesterWalletId());
        entity.setAmount(dto.getAmount());
        entity.setTransferMethod(dto.getTransferMethod());
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

                    trade.setStatus(TradeStatus.ACCEPTED);

                    BootcoinTransactionEvent event = new BootcoinTransactionEvent(
                            UUID.randomUUID().toString(),
                            trade.getRequesterWalletId(),
                            sellerWalletId,
                            trade.getAmount(),
                            trade.getTransferMethod()
                    );

                    String topic = switch (trade.getTransferMethod()) {
                        case YANKI -> "yanki-transactions";
                        case ACCOUNT -> "movement-transfers";
                    };

                    return repository.save(trade)
                            .doOnNext(saved -> kafkaTemplate.send(topic, event))
                            .then();
                });
    }
}