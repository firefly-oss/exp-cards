package com.firefly.experience.cards.core.issuance.queries;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardIssuanceJourneyStatusDTO {
    private UUID journeyId;
    private UUID customerId;
    private UUID cardId;
    private String currentPhase;
    private List<String> completedSteps;
    private String nextStep;
    private String cardStatus;
    private Boolean isVirtual;
}
