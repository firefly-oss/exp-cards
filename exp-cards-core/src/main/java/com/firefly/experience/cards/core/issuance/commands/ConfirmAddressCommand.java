package com.firefly.experience.cards.core.issuance.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmAddressCommand {
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String postalCode;
    private String country;
}
