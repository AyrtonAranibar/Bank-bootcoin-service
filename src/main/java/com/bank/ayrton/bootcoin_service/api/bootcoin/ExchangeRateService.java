package com.bank.ayrton.bootcoin_service.api.bootcoin;

import com.bank.ayrton.bootcoin_service.dto.ExchangeRateDto;
import reactor.core.publisher.Mono;

public interface ExchangeRateService {
    Mono<ExchangeRateDto> updateRate(ExchangeRateDto dto);
    Mono<ExchangeRateDto> getRate();
}