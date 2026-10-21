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


package io.fireflysoftware.experience.cards.core.replacement.services.impl;

import io.fireflysoftware.experience.cards.core.replacement.commands.ReportCardLostCommand;
import io.fireflysoftware.experience.cards.core.replacement.commands.VerifyIdentityCommand;
import io.fireflysoftware.experience.cards.core.replacement.queries.CardReplacementJourneyStatusDTO;
import io.fireflysoftware.experience.cards.core.replacement.services.CardReplacementJourneyService;
import io.fireflysoftware.experience.cards.core.replacement.workflows.CardReplacementJourneyWorkflow;
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
 * Signal-driven workflow implementation of the card replacement journey service.
 * <p>
 * The report endpoint starts a long-running workflow (SYNC mode - blocks until the
 * first @WaitForSignal gate). All subsequent endpoints send signals to advance the workflow.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CardReplacementJourneyServiceImpl implements CardReplacementJourneyService {

    private final WorkflowEngine workflowEngine;
    private final SignalService signalService;
    private final WorkflowQueryService queryService;

    @Override
    public Mono<CardReplacementJourneyStatusDTO> reportCardLost(ReportCardLostCommand command) {
        String correlationId = UUID.randomUUID().toString();
        Map<String, Object> input = Map.of("command", command);

        return workflowEngine.startWorkflow(
                        CardReplacementJourneyWorkflow.WORKFLOW_ID, input, correlationId, "api", false)
                .flatMap(state -> queryService.executeQuery(
                        correlationId, CardReplacementJourneyWorkflow.QUERY_JOURNEY_STATUS))
                .cast(CardReplacementJourneyStatusDTO.class)
                .doOnNext(status -> log.info("Initiated card replacement journey: journeyId={}", correlationId));
    }

    @Override
    public Mono<Void> verifyIdentity(UUID journeyId, VerifyIdentityCommand command) {
        return signalService.signal(
                        journeyId.toString(), CardReplacementJourneyWorkflow.SIGNAL_IDENTITY_VERIFIED, command)
                .doOnNext(r -> log.info("Signal delivered: identity-verified for journeyId={}", journeyId))
                .then();
    }

    @Override
    public Mono<CardReplacementJourneyStatusDTO> getJourneyStatus(UUID journeyId) {
        return queryService.executeQuery(
                        journeyId.toString(), CardReplacementJourneyWorkflow.QUERY_JOURNEY_STATUS)
                .cast(CardReplacementJourneyStatusDTO.class);
    }
}
