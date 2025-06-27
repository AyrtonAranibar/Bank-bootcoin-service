package com.bank.ayrton.bootcoin_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeRequest {

    @Id
    private String id;

    @Field("requesterWalletId")
    private String requesterWalletId;  // ID del monedero del comprador

    private Double amount;             // bootcoins
    private TradeStatus status;        // PENDING, ACCEPTED, COMPLETED, FAILED
    private TransferMethod transferMethod; // YANKI o ACCOUNT
    private TradeType tradeType;       // BUY o SELL

    private LocalDateTime requestedAt;
}