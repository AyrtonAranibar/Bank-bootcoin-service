package com.bank.ayrton.bootcoin_service.api.bootcoin;

import com.bank.ayrton.bootcoin_service.entity.TradeRequest;
import com.bank.ayrton.bootcoin_service.entity.TradeStatus;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface TradeRequestRepository extends ReactiveMongoRepository<TradeRequest, String> {
    Flux<TradeRequest> findByStatus(TradeStatus status);
}