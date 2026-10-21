/*
 * Copyright 2025 Firefly Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package io.fireflysoftware.experience.cards.core.replacement.services;

import io.fireflysoftware.experience.cards.core.replacement.commands.ReportCardLostCommand;
import io.fireflysoftware.experience.cards.core.replacement.commands.VerifyIdentityCommand;
import io.fireflysoftware.experience.cards.core.replacement.queries.CardReplacementJourneyStatusDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service interface for the card replacement journey (lost/stolen cards).
 * Each method corresponds to an atomic endpoint that starts or advances the workflow.
 */
public interface CardReplacementJourneyService {

    Mono<CardReplacementJourneyStatusDTO> reportCardLost(ReportCardLostCommand command);

    Mono<Void> verifyIdentity(UUID journeyId, VerifyIdentityCommand command);

    Mono<CardReplacementJourneyStatusDTO> getJourneyStatus(UUID journeyId);
}
