package com.firefly.experience.cards.interfaces.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to issue a new card")
public class IssueCardRequest {

    @NotNull
    @Schema(description = "Customer identifier")
    private UUID customerId;

    @NotNull
    @Schema(description = "Account identifier to link the card")
    private UUID accountId;

    @NotNull
    @Schema(description = "Card program identifier")
    private UUID cardProgramId;

    @Schema(description = "Cardholder name to emboss on card")
    private String cardholderName;

    @Schema(description = "Whether to issue as virtual card")
    private Boolean virtual;
}
