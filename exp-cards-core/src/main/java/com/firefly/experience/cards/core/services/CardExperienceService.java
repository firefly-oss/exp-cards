package com.firefly.experience.cards.core.services;

import com.firefly.experience.cards.interfaces.dtos.CardSummaryResponse;
import com.firefly.experience.cards.interfaces.dtos.IssueCardRequest;
import com.firefly.experience.cards.interfaces.dtos.IssueCardResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CardExperienceService {

    Mono<IssueCardResponse> issueCard(IssueCardRequest request);

    Mono<CardSummaryResponse> getCardSummary(UUID cardId);

    Flux<CardSummaryResponse> getCustomerCards(UUID customerId);

    Mono<Void> activateCard(UUID cardId);

    Mono<Void> blockCard(UUID cardId, String reason);

    Mono<Void> cancelCard(UUID cardId, String reason);
}
