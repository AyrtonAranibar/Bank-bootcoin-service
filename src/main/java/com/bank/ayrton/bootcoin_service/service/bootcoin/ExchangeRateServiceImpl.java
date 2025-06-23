package com.bank.ayrton.bootcoin_service.service.bootcoin;

import com.bank.ayrton.bootcoin_service.api.bootcoin.ExchangeRateRepository;
import com.bank.ayrton.bootcoin_service.api.bootcoin.ExchangeRateService;
import com.bank.ayrton.bootcoin_service.dto.ExchangeRateDto;
import com.bank.ayrton.bootcoin_service.entity.ExchangeRate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ExchangeRateRepository repository;

    private static final String RATE_ID = "BOOTCOIN_RATE"; // for singleton pattern

    @Override
    public Mono<ExchangeRateDto> updateRate(ExchangeRateDto dto) {
        ExchangeRate entity = new ExchangeRate(RATE_ID, dto.getBuyRate(), dto.getSellRate(), LocalDateTime.now());
        return repository.save(entity).map(this::toDto);
    }

    @Override
    public Mono<ExchangeRateDto> getRate() {
        return repository.findById(RATE_ID).map(this::toDto);
    }

    private ExchangeRateDto toDto(ExchangeRate entity) {
        return new ExchangeRateDto(entity.getBuyRate(), entity.getSellRate());
    }
}