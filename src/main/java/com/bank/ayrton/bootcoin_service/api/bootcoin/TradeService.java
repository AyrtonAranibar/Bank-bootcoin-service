package com.bank.ayrton.bootcoin_service.api.bootcoin;

import com.bank.ayrton.bootcoin_service.dto.TradeRequestDto;
import reactor.core.publisher.Mono;

public interface TradeService {
    Mono<TradeRequestDto> createTradeRequest(TradeRequestDto dto);
    Mono<Void> acceptTrade(String tradeId, String sellerWalletId);
}