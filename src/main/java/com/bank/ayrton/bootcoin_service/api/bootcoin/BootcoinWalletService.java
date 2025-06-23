package com.bank.ayrton.bootcoin_service.api.bootcoin;

import com.bank.ayrton.bootcoin_service.dto.BootcoinWalletDto;
import reactor.core.publisher.Mono;

public interface BootcoinWalletService {
    Mono<BootcoinWalletDto> createWallet(BootcoinWalletDto dto);
}