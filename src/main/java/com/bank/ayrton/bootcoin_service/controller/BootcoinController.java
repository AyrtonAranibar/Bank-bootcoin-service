package com.bank.ayrton.bootcoin_service.controller;

import com.bank.ayrton.bootcoin_service.api.bootcoin.BootcoinWalletService;
import com.bank.ayrton.bootcoin_service.dto.BootcoinWalletDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/bootcoin")
@RequiredArgsConstructor
public class BootcoinController {

    private final BootcoinWalletService walletService;

    @PostMapping("/wallets")
    public Mono<ResponseEntity<BootcoinWalletDto>> createWallet(@RequestBody BootcoinWalletDto dto) {
        return walletService.createWallet(dto)
                .map(ResponseEntity::ok)
                .onErrorResume(error -> Mono.just(ResponseEntity.badRequest().build()));
    }
}
