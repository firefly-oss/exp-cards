package com.firefly.experience.cards.core.replacement.services;

import com.firefly.experience.cards.core.replacement.commands.ReportCardLostCommand;
import com.firefly.experience.cards.core.replacement.commands.VerifyIdentityCommand;
import com.firefly.experience.cards.core.replacement.queries.CardReplacementJourneyStatusDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service interface for the card replacement journey (lost/stolen cards).
 * Each method corresponds to an atomic endpoint that starts or advances the workflow.
 */
public interface CardReplacementJourneyService {

    Mono<CardReplacementJourneyStatusDTO> reportCardLost(ReportCardLostCommand command);

    Mono<Void> verifyIdentity(UUID journeyId, VerifyIdentityCommand command);

    Mono<CardReplacementJourneyStatusDTO> getJourneyStatus(UUID journeyId);
}
