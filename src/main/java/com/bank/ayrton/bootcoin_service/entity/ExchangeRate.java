package com.bank.ayrton.bootcoin_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRate {
    @Id
    private String id;

    private Double buyRate;
    private Double sellRate;
    private LocalDateTime updatedAt;
}