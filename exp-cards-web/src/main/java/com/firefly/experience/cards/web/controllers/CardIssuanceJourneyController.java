package com.firefly.experience.cards.web.controllers;

import com.firefly.experience.cards.core.issuance.commands.ConfirmAddressCommand;
import com.firefly.experience.cards.core.issuance.commands.InitiateCardIssuanceCommand;
import com.firefly.experience.cards.core.issuance.commands.SetupPinCommand;
import com.firefly.experience.cards.core.issuance.queries.CardIssuanceJourneyStatusDTO;
import com.firefly.experience.cards.core.issuance.services.CardIssuanceJourneyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

/**
 * REST controller for the card issuance journey.
 * Each endpoint is atomic: it either starts the workflow or sends a signal to advance it.
 */
@RestController
@RequestMapping("/api/v1/journeys/card-issuance")
@RequiredArgsConstructor
@Tag(name = "Card Issuance Journey",
     description = "Endpoints for the card issuance journey workflow")
public class CardIssuanceJourneyController {

    private static final String KEY_JOURNEY_ID = "journeyId";
    private static final String KEY_STATUS = "status";

    private static final String STATUS_ADDRESS_CONFIRMED = "ADDRESS_CONFIRMED";
    private static final String STATUS_PIN_SET = "PIN_SET";

    private final CardIssuanceJourneyService journeyService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Initiate Card Issuance",
               description = "Start a new card issuance journey - verifies eligibility, "
                   + "sends welcome notification, and waits for address confirmation")
    public Mono<ResponseEntity<CardIssuanceJourneyStatusDTO>> initiateCardIssuance(
            @Valid @RequestBody InitiateCardIssuanceCommand command) {
        return journeyService.initiateCardIssuance(command)
                .map(status -> ResponseEntity.status(HttpStatus.CREATED).body(status));
    }

    @GetMapping(value = "/{journeyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Journey Status",
               description = "Retrieve the current state of a card issuance journey. "
                   + "Returns completed steps, current phase, and next expected action.")
    public Mono<ResponseEntity<CardIssuanceJourneyStatusDTO>> getJourneyStatus(
            @PathVariable UUID journeyId) {
        return journeyService.getJourneyStatus(journeyId)
                .map(ResponseEntity::ok);
    }

    @PostMapping(value = "/{journeyId}/address",
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Confirm Delivery Address",
               description = "Submit the delivery address for the card. "
                   + "Advances the journey past the address confirmation gate.")
    public Mono<ResponseEntity<Map<String, Object>>> confirmAddress(
            @PathVariable UUID journeyId,
            @Valid @RequestBody ConfirmAddressCommand command) {
        return journeyService.confirmAddress(journeyId, command)
                .thenReturn(ResponseEntity.ok(Map.of(
                        KEY_JOURNEY_ID, (Object) journeyId,
                        KEY_STATUS, STATUS_ADDRESS_CONFIRMED)));
    }

    @PostMapping(value = "/{journeyId}/pin",
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Setup PIN",
               description = "Setup the PIN for the card. "
                   + "Advances the journey past the PIN setup gate and triggers card issuance.")
    public Mono<ResponseEntity<Map<String, Object>>> setupPin(
            @PathVariable UUID journeyId,
            @Valid @RequestBody SetupPinCommand command) {
        return journeyService.setupPin(journeyId, command)
                .thenReturn(ResponseEntity.ok(Map.of(
                        KEY_JOURNEY_ID, (Object) journeyId,
                        KEY_STATUS, STATUS_PIN_SET)));
    }
}
