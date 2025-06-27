package com.bank.ayrton.bootcoin_service.service.bootcoin;

import lombok.Data;

@Data
public class YankiWalletDto {
    private String cardNumber;
    private String documentType;
    private String documentNumber;
    private String phoneNumber;
    private String imei;
    private String email;
}