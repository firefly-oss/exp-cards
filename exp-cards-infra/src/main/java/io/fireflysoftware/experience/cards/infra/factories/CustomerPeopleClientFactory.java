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


package io.fireflysoftware.experience.cards.infra.factories;

import io.fireflysoftware.domain.people.sdk.api.CustomersApi;
import io.fireflysoftware.domain.people.sdk.api.PartyContactQueriesApi;
import io.fireflysoftware.domain.people.sdk.invoker.ApiClient;
import io.fireflysoftware.experience.cards.infra.properties.CustomerPeopleProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Generated clients for the customer domain. Card journeys read the party and identity documents there.
 */
@Component
public class CustomerPeopleClientFactory {

    private final ApiClient apiClient;

    public CustomerPeopleClientFactory(CustomerPeopleProperties properties) {
        this.apiClient = new ApiClient();
        String basePath = properties.getBasePath();
        this.apiClient.setBasePath(basePath.endsWith("/") ? basePath.substring(0, basePath.length() - 1) : basePath);
    }

    @Bean
    public CustomersApi customersApi() {
        return new CustomersApi(apiClient);
    }

    @Bean
    public PartyContactQueriesApi partyContactQueriesApi() {
        return new PartyContactQueriesApi(apiClient);
    }
}
