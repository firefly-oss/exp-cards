/*
 * Copyright 2025 Firefly Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package io.fireflysoftware.experience.cards.core.services;

import io.fireflysoftware.experience.cards.interfaces.dtos.CardSummaryResponse;
import io.fireflysoftware.experience.cards.interfaces.dtos.IssueCardRequest;
import io.fireflysoftware.experience.cards.interfaces.dtos.IssueCardResponse;
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
