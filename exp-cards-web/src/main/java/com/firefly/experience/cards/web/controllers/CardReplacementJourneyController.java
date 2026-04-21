package com.firefly.experience.cards.web.controllers;

import com.firefly.experience.cards.core.replacement.commands.ReportCardLostCommand;
import com.firefly.experience.cards.core.replacement.commands.VerifyIdentityCommand;
import com.firefly.experience.cards.core.replacement.queries.CardReplacementJourneyStatusDTO;
import com.firefly.experience.cards.core.replacement.services.CardReplacementJourneyService;
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
 * REST controller for the card replacement journey (lost/stolen cards).
 * Each endpoint is atomic: it either starts the workflow or sends a signal to advance it.
 */
@RestController
@RequestMapping("/api/v1/journeys/card-replacement")
@RequiredArgsConstructor
@Tag(name = "Card Replacement Journey",
     description = "Endpoints for the card replacement journey workflow (lost/stolen)")
public class CardReplacementJourneyController {

    private static final String KEY_JOURNEY_ID = "journeyId";
    private static final String KEY_STATUS = "status";

    private static final String STATUS_IDENTITY_VERIFIED = "IDENTITY_VERIFIED";

    private final CardReplacementJourneyService journeyService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Report Card Lost/Stolen",
               description = "Start a card replacement journey - blocks the old card, "
                   + "sends security alert, and waits for identity verification")
    public Mono<ResponseEntity<CardReplacementJourneyStatusDTO>> reportCardLost(
            @Valid @RequestBody ReportCardLostCommand command) {
        return journeyService.reportCardLost(command)
                .map(status -> ResponseEntity.status(HttpStatus.CREATED).body(status));
    }

    @GetMapping(value = "/{journeyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Journey Status",
               description = "Retrieve the current state of a card replacement journey. "
                   + "Returns completed steps, current phase, and card statuses.")
    public Mono<ResponseEntity<CardReplacementJourneyStatusDTO>> getJourneyStatus(
            @PathVariable UUID journeyId) {
        return journeyService.getJourneyStatus(journeyId)
                .map(ResponseEntity::ok);
    }

    @PostMapping(value = "/{journeyId}/identity",
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Verify Identity",
               description = "Submit identity verification for card replacement. "
                   + "Advances the journey past the identity verification gate and triggers replacement card creation.")
    public Mono<ResponseEntity<Map<String, Object>>> verifyIdentity(
            @PathVariable UUID journeyId,
            @Valid @RequestBody VerifyIdentityCommand command) {
        return journeyService.verifyIdentity(journeyId, command)
                .thenReturn(ResponseEntity.ok(Map.of(
                        KEY_JOURNEY_ID, (Object) journeyId,
                        KEY_STATUS, STATUS_IDENTITY_VERIFIED)));
    }
}
