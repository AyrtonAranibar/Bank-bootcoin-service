package com.bank.ayrton.bootcoin_service.controller;

import com.bank.ayrton.bootcoin_service.api.bootcoin.BootcoinWalletService;
import com.bank.ayrton.bootcoin_service.api.bootcoin.ExchangeRateService;
import com.bank.ayrton.bootcoin_service.dto.BootcoinWalletDto;
import com.bank.ayrton.bootcoin_service.dto.ExchangeRateDto;
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
}
