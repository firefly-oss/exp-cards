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


package io.fireflysoftware.experience.cards.infra.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Connection settings for domain-customer-people.
 * Binds to {@code api-configuration.domain-platform.customer-people}.
 */
@Component
@ConfigurationProperties(prefix = "api-configuration.domain-platform.customer-people")
@Data
public class CustomerPeopleProperties {

    /** Base URL of the customer domain, for example {@code http://localhost:8081}. */
    private String basePath = "http://localhost:8081";
}
