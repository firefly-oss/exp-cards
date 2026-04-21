package com.firefly.experience.cards.core.issuance.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetupPinCommand {
    private String encryptedPin;
    private String pinConfirmation;
}
