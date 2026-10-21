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


package io.fireflysoftware.experience.cards.core.issuance.services.impl;

import io.fireflysoftware.experience.cards.core.issuance.commands.ConfirmAddressCommand;
import io.fireflysoftware.experience.cards.core.issuance.commands.InitiateCardIssuanceCommand;
import io.fireflysoftware.experience.cards.core.issuance.commands.SetupPinCommand;
import io.fireflysoftware.experience.cards.core.issuance.queries.CardIssuanceJourneyStatusDTO;
import io.fireflysoftware.experience.cards.core.issuance.services.CardIssuanceJourneyService;
import io.fireflysoftware.experience.cards.core.issuance.workflows.CardIssuanceJourneyWorkflow;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fireflyframework.orchestration.workflow.engine.WorkflowEngine;
import org.fireflyframework.orchestration.workflow.query.WorkflowQueryService;
import org.fireflyframework.orchestration.workflow.signal.SignalService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

/**
 * Signal-driven workflow implementation of the card issuance journey service.
 * <p>
 * The initiation endpoint starts a long-running workflow (SYNC mode - blocks until the
 * first @WaitForSignal gate). All subsequent endpoints send signals to advance the workflow.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CardIssuanceJourneyServiceImpl implements CardIssuanceJourneyService {

    private final WorkflowEngine workflowEngine;
    private final SignalService signalService;
    private final WorkflowQueryService queryService;

    @Override
    public Mono<CardIssuanceJourneyStatusDTO> initiateCardIssuance(InitiateCardIssuanceCommand command) {
        String correlationId = UUID.randomUUID().toString();
        Map<String, Object> input = Map.of("command", command);

        return workflowEngine.startWorkflow(
                        CardIssuanceJourneyWorkflow.WORKFLOW_ID, input, correlationId, "api", false)
                .flatMap(state -> queryService.executeQuery(
                        correlationId, CardIssuanceJourneyWorkflow.QUERY_JOURNEY_STATUS))
                .cast(CardIssuanceJourneyStatusDTO.class)
                .doOnNext(status -> log.info("Initiated card issuance journey: journeyId={}", correlationId));
    }

    @Override
    public Mono<Void> confirmAddress(UUID journeyId, ConfirmAddressCommand command) {
        return signalService.signal(
                        journeyId.toString(), CardIssuanceJourneyWorkflow.SIGNAL_ADDRESS_CONFIRMED, command)
                .doOnNext(r -> log.info("Signal delivered: address-confirmed for journeyId={}", journeyId))
                .then();
    }

    @Override
    public Mono<Void> setupPin(UUID journeyId, SetupPinCommand command) {
        return signalService.signal(
                        journeyId.toString(), CardIssuanceJourneyWorkflow.SIGNAL_PIN_SET, command)
                .doOnNext(r -> log.info("Signal delivered: pin-set for journeyId={}", journeyId))
                .then();
    }

    @Override
    public Mono<CardIssuanceJourneyStatusDTO> getJourneyStatus(UUID journeyId) {
        return queryService.executeQuery(
                        journeyId.toString(), CardIssuanceJourneyWorkflow.QUERY_JOURNEY_STATUS)
                .cast(CardIssuanceJourneyStatusDTO.class);
    }
}
