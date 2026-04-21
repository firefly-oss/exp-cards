package com.firefly.experience.cards.core.replacement.services.impl;

import com.firefly.experience.cards.core.replacement.commands.ReportCardLostCommand;
import com.firefly.experience.cards.core.replacement.commands.VerifyIdentityCommand;
import com.firefly.experience.cards.core.replacement.queries.CardReplacementJourneyStatusDTO;
import com.firefly.experience.cards.core.replacement.services.CardReplacementJourneyService;
import com.firefly.experience.cards.core.replacement.workflows.CardReplacementJourneyWorkflow;
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
