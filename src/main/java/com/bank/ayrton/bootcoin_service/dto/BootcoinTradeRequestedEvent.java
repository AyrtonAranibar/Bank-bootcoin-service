package com.bank.ayrton.bootcoin_service.dto;

import com.bank.ayrton.bootcoin_service.entity.TransferMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BootcoinTradeRequestedEvent {
    private String tradeId;
    private String requesterWalletId;
    private Double amount;
    private TransferMethod transferMethod;
    private LocalDateTime requestedAt;
}