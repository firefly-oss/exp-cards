package com.firefly.experience.cards.interfaces.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response after issuing a card")
public class IssueCardResponse {

    @Schema(description = "Newly created card identifier")
    private UUID cardId;

    @Schema(description = "Masked card number")
    private String maskedCardNumber;

    @Schema(description = "Execution identifier for tracking")
    private String executionId;

    @Schema(description = "Operation status")
    private String status;
}
