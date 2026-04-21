# exp-cards

> Experience layer service that provides card management journeys for frontend applications

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Module Structure](#module-structure)
- [API Endpoints](#api-endpoints)
- [Domain SDK Dependencies](#domain-sdk-dependencies)
- [Configuration](#configuration)
- [Running Locally](#running-locally)
- [Testing](#testing)

## Overview

`exp-cards` is the experience-layer (BFF) service that exposes card management capabilities to frontend applications. It provides a simplified API surface for common card operations including issuing cards, viewing card summaries, listing customer cards, activating, blocking, and canceling cards.

Unlike domain services that orchestrate complex multi-step sagas, `exp-cards` focuses on composing domain service calls into frontend-friendly responses. It delegates all business orchestration to `domain-banking-cards` and transforms responses into experience-layer DTOs optimized for UI consumption.

## Architecture

```
Frontend / Mobile App
         |
         v
exp-cards  (port 8101)
         |
         +---> CardExperienceService
         |             |
         |             v
         +---> domain-banking-cards-sdk
                       |
                       +---> CardsApi
                       +---> CardBackofficeApi
```

## Module Structure

| Module | Purpose |
|--------|---------|
| `exp-cards-interfaces` | Experience-layer DTOs: `IssueCardRequest`, `IssueCardResponse`, `CardSummaryResponse` |
| `exp-cards-core` | Service interfaces and implementations: `CardExperienceService`, `CardExperienceServiceImpl` |
| `exp-cards-infra` | `DomainBankingCardsClientFactory` (CardsApi, CardBackofficeApi) and `@ConfigurationProperties` |
| `exp-cards-web` | `CardsController`, Spring Boot application class, `application.yaml` |
| `exp-cards-sdk` | Auto-generated reactive SDK from the OpenAPI spec |

## API Endpoints

Base path: `/api/v1/cards`

| Method | Path | Description | Response |
|--------|------|-------------|----------|
| `POST` | `/api/v1/cards` | Issue a new card | `201 Created` with `IssueCardResponse` |
| `GET` | `/api/v1/cards/{cardId}` | Get card summary | `200 OK` with `CardSummaryResponse` |
| `GET` | `/api/v1/cards/customer/{customerId}` | Get all cards for a customer | `200 OK` with `Flux<CardSummaryResponse>` |
| `POST` | `/api/v1/cards/{cardId}/activate` | Activate a card | `204 No Content` |
| `POST` | `/api/v1/cards/{cardId}/block` | Block a card | `204 No Content` |
| `DELETE` | `/api/v1/cards/{cardId}` | Cancel a card | `204 No Content` |

### Request/Response DTOs

**IssueCardRequest:**
```json
{
  "customerId": "uuid",
  "accountId": "uuid",
  "cardProgramId": "uuid",
  "cardType": "DEBIT"
}
```

**IssueCardResponse:**
```json
{
  "cardId": "uuid",
  "executionId": "uuid",
  "status": "COMPLETED"
}
```

**CardSummaryResponse:**
```json
{
  "cardId": "uuid",
  "maskedCardNumber": "****1234",
  "cardType": "DEBIT",
  "status": "ACTIVE",
  "expiryDate": "2028-12"
}
```

## Domain SDK Dependencies

| SDK | ClientFactory | APIs Used | Purpose |
|-----|--------------|-----------|---------|
| `domain-banking-cards-sdk` | `DomainBankingCardsClientFactory` | `CardsApi`, `CardBackofficeApi` | Issue cards, query summaries, activate, block, cancel |

## Configuration

```yaml
server:
  port: ${SERVER_PORT:8101}

firefly:
  cqrs:
    enabled: true
    command.timeout: 30s
    query:
      timeout: 15s
      caching-enabled: true
      cache-ttl: 5m

api-configuration:
  domain-platform:
    banking-cards:
      base-path: ${BANKING_CARDS_URL:http://localhost:8091}
```

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `SERVER_PORT` | `8101` | HTTP server port |
| `BANKING_CARDS_URL` | `http://localhost:8091` | Base URL for `domain-banking-cards` |

## Running Locally

```bash
# Prerequisites: ensure domain-banking-cards is running
cd exp-cards
mvn spring-boot:run -pl exp-cards-web
```

Server starts on port `8101`. Swagger UI: [http://localhost:8101/swagger-ui.html](http://localhost:8101/swagger-ui.html)

Swagger UI is disabled in the `prod` profile.

## Testing

```bash
mvn clean verify
```

Tests cover `CardExperienceServiceImpl` (unit tests with mocked domain SDK) and `CardsController` (WebTestClient-based integration tests).

---

## Spring Profiles

| Profile | Logging | Swagger | Notes |
|---------|---------|---------|-------|
| `default` | INFO | Enabled | Standard development |
| `dev` | DEBUG | Enabled | Verbose debugging |
| `prod` | INFO | Disabled | Production |
| `openapi` | WARN | Enabled (port 18080) | OpenAPI spec generation |
