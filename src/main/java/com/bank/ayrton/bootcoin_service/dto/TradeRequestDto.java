package com.bank.ayrton.bootcoin_service.dto;

import com.bank.ayrton.bootcoin_service.entity.TransferMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeRequestDto {
    private String requesterWalletId;
    private Double amount;
    private TransferMethod transferMethod;
}