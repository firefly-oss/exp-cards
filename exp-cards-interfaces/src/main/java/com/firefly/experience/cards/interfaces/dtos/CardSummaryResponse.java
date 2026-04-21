package com.firefly.experience.cards.interfaces.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Card summary for experience layer")
public class CardSummaryResponse {

    @Schema(description = "Card unique identifier")
    private UUID cardId;

    @Schema(description = "Masked card number")
    private String maskedCardNumber;

    @Schema(description = "Card type (DEBIT, CREDIT, PREPAID)")
    private String cardType;

    @Schema(description = "Card status")
    private String status;

    @Schema(description = "Cardholder name")
    private String cardholderName;

    @Schema(description = "Expiration date")
    private LocalDate expirationDate;

    @Schema(description = "Available credit/balance")
    private BigDecimal availableBalance;

    @Schema(description = "Current balance")
    private BigDecimal currentBalance;

    @Schema(description = "Currency code")
    private String currency;

    @Schema(description = "Whether this is a virtual card")
    private Boolean virtual;
}
