package com.firefly.experience.cards.core.issuance.services;

import com.firefly.experience.cards.core.issuance.commands.ConfirmAddressCommand;
import com.firefly.experience.cards.core.issuance.commands.InitiateCardIssuanceCommand;
import com.firefly.experience.cards.core.issuance.commands.SetupPinCommand;
import com.firefly.experience.cards.core.issuance.queries.CardIssuanceJourneyStatusDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service interface for the card issuance journey.
 * Each method corresponds to an atomic endpoint that starts or advances the workflow.
 */
public interface CardIssuanceJourneyService {

    Mono<CardIssuanceJourneyStatusDTO> initiateCardIssuance(InitiateCardIssuanceCommand command);

    Mono<Void> confirmAddress(UUID journeyId, ConfirmAddressCommand command);

    Mono<Void> setupPin(UUID journeyId, SetupPinCommand command);

    Mono<CardIssuanceJourneyStatusDTO> getJourneyStatus(UUID journeyId);
}
