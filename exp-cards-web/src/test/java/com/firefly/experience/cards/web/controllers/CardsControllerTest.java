package com.firefly.experience.cards.web.controllers;

import com.firefly.experience.cards.core.services.CardExperienceService;
import com.firefly.experience.cards.interfaces.dtos.CardSummaryResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardsControllerTest {

    @Mock
    private CardExperienceService cardService;

    @InjectMocks
    private CardsController controller;

    @Test
    void getCardSummary_returnsOkWithCardSummary() {
        UUID cardId = UUID.randomUUID();

        CardSummaryResponse summaryResponse = CardSummaryResponse.builder()
                .cardId(cardId)
                .maskedCardNumber("**** **** **** 1234")
                .cardType("DEBIT")
                .status("ACTIVE")
                .cardholderName("John Doe")
                .expirationDate(LocalDate.of(2028, 12, 31))
                .availableBalance(BigDecimal.valueOf(1000.00))
                .currentBalance(BigDecimal.valueOf(500.00))
                .currency("EUR")
                .virtual(false)
                .build();

        when(cardService.getCardSummary(cardId))
                .thenReturn(Mono.just(summaryResponse));

        StepVerifier.create(controller.getCardSummary(cardId))
                .assertNext(response -> {
                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                    CardSummaryResponse body = response.getBody();
                    assertThat(body).isNotNull();
                    assertThat(body.getCardId()).isEqualTo(cardId);
                    assertThat(body.getMaskedCardNumber()).isEqualTo("**** **** **** 1234");
                    assertThat(body.getCardType()).isEqualTo("DEBIT");
                    assertThat(body.getStatus()).isEqualTo("ACTIVE");
                })
                .verifyComplete();
    }

    @Test
    void getCardSummary_returnsNotFoundWhenEmpty() {
        UUID cardId = UUID.randomUUID();

        when(cardService.getCardSummary(cardId))
                .thenReturn(Mono.empty());

        StepVerifier.create(controller.getCardSummary(cardId))
                .assertNext(response -> {
                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                    assertThat(response.getBody()).isNull();
                })
                .verifyComplete();
    }

    @Test
    void getCustomerCards_returnsListOfCards() {
        UUID customerId = UUID.randomUUID();
        UUID cardId1 = UUID.randomUUID();
        UUID cardId2 = UUID.randomUUID();

        CardSummaryResponse card1 = CardSummaryResponse.builder()
                .cardId(cardId1)
                .maskedCardNumber("**** **** **** 1234")
                .cardType("DEBIT")
                .status("ACTIVE")
                .cardholderName("John Doe")
                .currency("EUR")
                .virtual(false)
                .build();

        CardSummaryResponse card2 = CardSummaryResponse.builder()
                .cardId(cardId2)
                .maskedCardNumber("**** **** **** 5678")
                .cardType("CREDIT")
                .status("ACTIVE")
                .cardholderName("John Doe")
                .currency("EUR")
                .virtual(true)
                .build();

        when(cardService.getCustomerCards(customerId))
                .thenReturn(Flux.just(card1, card2));

        StepVerifier.create(controller.getCustomerCards(customerId))
                .assertNext(response -> {
                    assertThat(response.getCardId()).isEqualTo(cardId1);
                    assertThat(response.getCardType()).isEqualTo("DEBIT");
                    assertThat(response.getVirtual()).isFalse();
                })
                .assertNext(response -> {
                    assertThat(response.getCardId()).isEqualTo(cardId2);
                    assertThat(response.getCardType()).isEqualTo("CREDIT");
                    assertThat(response.getVirtual()).isTrue();
                })
                .verifyComplete();
    }

    @Test
    void activateCard_returnsNoContent() {
        UUID cardId = UUID.randomUUID();

        when(cardService.activateCard(cardId))
                .thenReturn(Mono.empty());

        StepVerifier.create(controller.activateCard(cardId))
                .assertNext(response -> {
                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
                })
                .verifyComplete();
    }

    @Test
    void blockCard_returnsNoContent() {
        UUID cardId = UUID.randomUUID();
        String reason = "Lost card";

        when(cardService.blockCard(cardId, reason))
                .thenReturn(Mono.empty());

        StepVerifier.create(controller.blockCard(cardId, reason))
                .assertNext(response -> {
                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
                })
                .verifyComplete();
    }

    @Test
    void cancelCard_returnsNoContent() {
        UUID cardId = UUID.randomUUID();
        String reason = "Customer request";

        when(cardService.cancelCard(cardId, reason))
                .thenReturn(Mono.empty());

        StepVerifier.create(controller.cancelCard(cardId, reason))
                .assertNext(response -> {
                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
                })
                .verifyComplete();
    }
}
