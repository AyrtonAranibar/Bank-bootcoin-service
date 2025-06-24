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
public class TradeRequest {

    @Id
    private String id;

    private String requesterWalletId;  // ID del monedero del comprador

    private Double amount;             // bootcoins
    private TradeStatus status;        // PENDING, ACCEPTED, COMPLETED, FAILED
    private TransferMethod transferMethod; // YANKI o ACCOUNT

    private LocalDateTime requestedAt;
}