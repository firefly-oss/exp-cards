package com.firefly.experience.cards.web.controllers;

import com.firefly.experience.cards.core.services.CardExperienceService;
import com.firefly.experience.cards.interfaces.dtos.CardSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
@Tag(name = "Cards", description = "Card experience operations")
public class CardsController {

    private final CardExperienceService cardService;

    @GetMapping("/{cardId}")
    @Operation(
            summary = "Get card summary",
            description = "Retrieves the card summary including balances and status"
    )
    @ApiResponse(responseCode = "200", description = "Card found")
    @ApiResponse(responseCode = "404", description = "Card not found")
    public Mono<ResponseEntity<CardSummaryResponse>> getCardSummary(
            @Parameter(description = "Card identifier")
            @PathVariable UUID cardId) {
        return cardService.getCardSummary(cardId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    @Operation(
            summary = "Get customer cards",
            description = "Retrieves all cards for a specific customer"
    )
    @ApiResponse(responseCode = "200", description = "Cards retrieved")
    public Flux<CardSummaryResponse> getCustomerCards(
            @Parameter(description = "Customer identifier")
            @PathVariable UUID customerId) {
        return cardService.getCustomerCards(customerId);
    }

    @PostMapping("/{cardId}/activate")
    @Operation(
            summary = "Activate a card",
            description = "Activates a card that is in pending activation status"
    )
    @ApiResponse(responseCode = "204", description = "Card activated successfully")
    @ApiResponse(responseCode = "404", description = "Card not found")
    public Mono<ResponseEntity<Void>> activateCard(
            @Parameter(description = "Card identifier")
            @PathVariable UUID cardId) {
        return cardService.activateCard(cardId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }

    @PostMapping("/{cardId}/block")
    @Operation(
            summary = "Block a card",
            description = "Blocks a card temporarily"
    )
    @ApiResponse(responseCode = "204", description = "Card blocked successfully")
    @ApiResponse(responseCode = "404", description = "Card not found")
    public Mono<ResponseEntity<Void>> blockCard(
            @Parameter(description = "Card identifier")
            @PathVariable UUID cardId,
            @Parameter(description = "Reason for blocking")
            @RequestParam(required = false) String reason) {
        return cardService.blockCard(cardId, reason)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }

    @DeleteMapping("/{cardId}")
    @Operation(
            summary = "Cancel a card",
            description = "Cancels a card permanently"
    )
    @ApiResponse(responseCode = "204", description = "Card cancelled successfully")
    @ApiResponse(responseCode = "404", description = "Card not found")
    public Mono<ResponseEntity<Void>> cancelCard(
            @Parameter(description = "Card identifier")
            @PathVariable UUID cardId,
            @Parameter(description = "Reason for cancellation")
            @RequestParam(required = false) String reason) {
        return cardService.cancelCard(cardId, reason)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }
}
