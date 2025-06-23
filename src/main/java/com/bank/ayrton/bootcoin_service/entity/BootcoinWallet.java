package com.bank.ayrton.bootcoin_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BootcoinWallet {
    @Id
    private String id;

    private String documentType;
    private String documentNumber;
    private String phoneNumber;
    private String email;
}