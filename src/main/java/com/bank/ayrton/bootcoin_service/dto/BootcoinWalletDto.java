package com.bank.ayrton.bootcoin_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BootcoinWalletDto {
    private String documentType;
    private String documentNumber;
    private String phoneNumber;
    private String email;

    private String associatedYankiWalletId;
    private String associatedAccountId;
}