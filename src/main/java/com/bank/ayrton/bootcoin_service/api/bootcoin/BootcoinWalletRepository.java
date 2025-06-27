package com.bank.ayrton.bootcoin_service.api.bootcoin;

import com.bank.ayrton.bootcoin_service.entity.BootcoinWallet;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BootcoinWalletRepository extends ReactiveMongoRepository<BootcoinWallet, String> {

    Mono<BootcoinWallet> findByPhoneNumber(String phoneNumber);

    Mono<BootcoinWallet> findByDocumentNumber(String documentNumber);

    Flux<BootcoinWallet> findByEmail(String email);
}