package com.firefly.experience.cards.core.services;

import com.firefly.experience.cards.interfaces.dtos.CardSummaryResponse;
import com.firefly.experience.cards.interfaces.dtos.IssueCardRequest;
import com.firefly.experience.cards.interfaces.dtos.IssueCardResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Experience layer service for card operations.
 *
 * Note: The domain-banking-cards SDK needs to be regenerated with Cards-specific APIs
 * (CardsApi, CardBackofficeApi). Once available, these methods will delegate to the
 * domain service. Currently returns placeholders to enable service startup.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CardExperienceServiceImpl implements CardExperienceService {

    @Override
    public Mono<IssueCardResponse> issueCard(IssueCardRequest request) {
        log.info("Issuing card for customer: {}", request.getCustomerId());

        // TODO: Call domain-banking-cards CardsApi.issueCard() once SDK is regenerated
        return Mono.just(IssueCardResponse.builder()
                .cardId(UUID.randomUUID())
                .executionId(UUID.randomUUID().toString())
                .status("PENDING_SDK_INTEGRATION")
                .build());
    }

    @Override
    public Mono<CardSummaryResponse> getCardSummary(UUID cardId) {
        log.debug("Fetching card summary for: {}", cardId);

        // TODO: Call domain-banking-cards CardBackofficeApi.getCard() once SDK is regenerated
        return Mono.empty();
    }

    @Override
    public Flux<CardSummaryResponse> getCustomerCards(UUID customerId) {
        log.debug("Fetching cards for customer: {}", customerId);

        // TODO: Call domain-banking-cards CardBackofficeApi.getCustomerCards() once SDK is regenerated
        return Flux.error(new UnsupportedOperationException(
                "domain-banking-cards SDK does not currently expose a getCustomerCards endpoint. " +
                "Regenerate the SDK after adding this endpoint to the domain service."
        ));
    }

    @Override
    public Mono<Void> activateCard(UUID cardId) {
        log.info("Activating card: {}", cardId);
        // TODO: Call domain-banking-cards CardsApi.activateCard() once SDK is regenerated
        return Mono.empty();
    }

    @Override
    public Mono<Void> blockCard(UUID cardId, String reason) {
        log.info("Blocking card: {} with reason: {}", cardId, reason);
        // TODO: Call domain-banking-cards CardsApi.blockCard() once SDK is regenerated
        return Mono.empty();
    }

    @Override
    public Mono<Void> cancelCard(UUID cardId, String reason) {
        log.info("Cancelling card: {} with reason: {}", cardId, reason);
        // TODO: Call domain-banking-cards CardsApi.cancelCard() once SDK is regenerated
        return Mono.empty();
    }
}
