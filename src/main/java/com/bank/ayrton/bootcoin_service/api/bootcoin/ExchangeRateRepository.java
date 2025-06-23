package com.bank.ayrton.bootcoin_service.api.bootcoin;

import com.bank.ayrton.bootcoin_service.entity.ExchangeRate;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ExchangeRateRepository extends ReactiveMongoRepository<ExchangeRate, String> {
}