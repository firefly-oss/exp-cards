package com.firefly.experience.cards.core.issuance.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InitiateCardIssuanceCommand {
    private UUID customerId;
    private UUID accountId;
    private UUID cardProgramId;
    private String cardholderName;
    private String email;
    private String phone;
    private Boolean virtual;
}
