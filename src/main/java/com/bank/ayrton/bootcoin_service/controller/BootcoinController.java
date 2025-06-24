package com.bank.ayrton.bootcoin_service.controller;

import com.bank.ayrton.bootcoin_service.api.bootcoin.BootcoinWalletService;
import com.bank.ayrton.bootcoin_service.api.bootcoin.ExchangeRateService;
import com.bank.ayrton.bootcoin_service.api.bootcoin.TradeService;
import com.bank.ayrton.bootcoin_service.dto.BootcoinWalletDto;
import com.bank.ayrton.bootcoin_service.dto.ExchangeRateDto;
import com.bank.ayrton.bootcoin_service.dto.TradeRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/bootcoin")
@RequiredArgsConstructor
public class BootcoinController {

    private final BootcoinWalletService walletService;
    private final ExchangeRateService exchangeRateService;
    private final TradeService tradeService;

    @PostMapping("/wallets")
    public Mono<ResponseEntity<BootcoinWalletDto>> createWallet(@RequestBody BootcoinWalletDto dto) {
        return walletService.createWallet(dto)
                .map(ResponseEntity::ok)
                .onErrorResume(error -> Mono.just(ResponseEntity.badRequest().build()));
    }
    @PutMapping("/exchange-rate")
    public Mono<ResponseEntity<ExchangeRateDto>> updateRate(@RequestBody ExchangeRateDto dto) {
        return exchangeRateService.updateRate(dto)
                .map(ResponseEntity::ok)
                .onErrorResume(error -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @GetMapping("/exchange-rate")
    public Mono<ResponseEntity<ExchangeRateDto>> getRate() {
        return exchangeRateService.getRate()
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping("/trades/request")
    public Mono<ResponseEntity<TradeRequestDto>> createTradeRequest(@RequestBody TradeRequestDto dto) {
        return tradeService.createTradeRequest(dto)
                .map(ResponseEntity::ok)
                .onErrorResume(error -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @PostMapping("/trades/accept/{tradeId}")
    public Mono<ResponseEntity<Void>> acceptTrade(@PathVariable String tradeId,
                                                  @RequestParam String sellerWalletId) {
        return tradeService.acceptTrade(tradeId, sellerWalletId)
                .thenReturn(ResponseEntity.ok().build())
                .onErrorResume(error -> Mono.just(ResponseEntity.badRequest().build()));
    }
}
