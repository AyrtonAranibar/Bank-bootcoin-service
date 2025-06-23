package com.bank.ayrton.bootcoin_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateDto {
    private Double buyRate;
    private Double sellRate;
}