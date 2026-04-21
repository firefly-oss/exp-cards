package com.firefly.experience.cards.core.services;

import com.firefly.experience.cards.interfaces.dtos.IssueCardRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for CardExperienceServiceImpl stub implementation.
 *
 * Note: This service is currently a stub awaiting domain-banking-cards SDK integration.
 * Tests verify the stub behavior (placeholder responses, exceptions for unsupported operations).
 */
class CardExperienceServiceImplTest {

    private CardExperienceServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CardExperienceServiceImpl();
    }

    @Test
    void issueCard_returnsPendingResponse() {
        UUID customerId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        UUID cardProgramId = UUID.randomUUID();

        IssueCardRequest request = IssueCardRequest.builder()
                .customerId(customerId)
                .accountId(accountId)
                .cardProgramId(cardProgramId)
                .build();

        StepVerifier.create(service.issueCard(request))
                .assertNext(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.getCardId()).isNotNull();
                    assertThat(response.getExecutionId()).isNotNull();
                    assertThat(response.getStatus()).isEqualTo("PENDING_SDK_INTEGRATION");
                })
                .verifyComplete();
    }

    @Test
    void getCardSummary_returnsEmpty() {
        UUID cardId = UUID.randomUUID();

        StepVerifier.create(service.getCardSummary(cardId))
                .verifyComplete();
    }

    @Test
    void getCustomerCards_throwsUnsupportedOperationException() {
        UUID customerId = UUID.randomUUID();

        StepVerifier.create(service.getCustomerCards(customerId))
                .expectError(UnsupportedOperationException.class)
                .verify();
    }

    @Test
    void activateCard_completesSuccessfully() {
        UUID cardId = UUID.randomUUID();

        StepVerifier.create(service.activateCard(cardId))
                .verifyComplete();
    }

    @Test
    void blockCard_completesSuccessfully() {
        UUID cardId = UUID.randomUUID();
        String reason = "Lost card";

        StepVerifier.create(service.blockCard(cardId, reason))
                .verifyComplete();
    }

    @Test
    void cancelCard_completesSuccessfully() {
        UUID cardId = UUID.randomUUID();
        String reason = "Customer request";

        StepVerifier.create(service.cancelCard(cardId, reason))
                .verifyComplete();
    }
}
