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


package io.fireflysoftware.experience.cards.web.controllers;

import io.fireflysoftware.experience.cards.core.services.CardExperienceService;
import io.fireflysoftware.experience.cards.interfaces.dtos.CardSummaryResponse;
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
