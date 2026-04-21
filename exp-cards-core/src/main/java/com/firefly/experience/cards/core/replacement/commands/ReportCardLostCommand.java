package com.firefly.experience.cards.core.replacement.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportCardLostCommand {
    private UUID cardId;
    private UUID customerId;
    private String reason;
    private String email;
    private String phone;
    private Boolean requestPhysicalReplacement;
}
