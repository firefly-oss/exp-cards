package com.firefly.experience.cards.core.replacement.queries;

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
public class CardReplacementJourneyStatusDTO {
    private UUID journeyId;
    private UUID customerId;
    private UUID oldCardId;
    private UUID newCardId;
    private String currentPhase;
    private List<String> completedSteps;
    private String nextStep;
    private String oldCardStatus;
    private String newCardStatus;
    private Boolean isPhysicalReplacement;
}
