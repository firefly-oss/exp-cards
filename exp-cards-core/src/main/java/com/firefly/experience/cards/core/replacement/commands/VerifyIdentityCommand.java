package com.firefly.experience.cards.core.replacement.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyIdentityCommand {
    private String verificationCode;
    private String documentType;
    private String documentNumber;
}
