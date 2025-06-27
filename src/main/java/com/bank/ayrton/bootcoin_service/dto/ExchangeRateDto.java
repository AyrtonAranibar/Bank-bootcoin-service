package com.bank.ayrton.bootcoin_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateDto {
    private Double buyRate;
    private Double sellRate;
    private LocalDateTime updatedAt; // opcional
}