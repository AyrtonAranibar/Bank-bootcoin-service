package com.bank.ayrton.bootcoin_service.service.bootcoin;

import com.bank.ayrton.bootcoin_service.api.bootcoin.BootcoinWalletRepository;
import com.bank.ayrton.bootcoin_service.api.bootcoin.BootcoinWalletService;
import com.bank.ayrton.bootcoin_service.dto.BootcoinWalletDto;
import com.bank.ayrton.bootcoin_service.entity.BootcoinWallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BootcoinWalletServiceImpl implements BootcoinWalletService {

    private final BootcoinWalletRepository repository;

    @Override
    public Mono<BootcoinWalletDto> createWallet(BootcoinWalletDto dto) {
        return repository.findByPhoneNumber(dto.getPhoneNumber())
                .flatMap(existing -> Mono.<BootcoinWalletDto>error(new RuntimeException("El número ya está registrado")))
                .switchIfEmpty(Mono.defer(() -> {
                    BootcoinWallet entity = toEntity(dto);
                    return repository.save(entity).map(this::toDto);
                }));
    }

    private BootcoinWallet toEntity(BootcoinWalletDto dto) {
        return new BootcoinWallet(null, dto.getDocumentType(), dto.getDocumentNumber(), dto.getPhoneNumber(), dto.getEmail());
    }

    private BootcoinWalletDto toDto(BootcoinWallet entity) {
        return new BootcoinWalletDto(entity.getDocumentType(), entity.getDocumentNumber(), entity.getPhoneNumber(), entity.getEmail());
    }
}