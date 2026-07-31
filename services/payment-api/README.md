# payment-api

Spring Boot (Kotlin) Payment API. Implements Phase 2 of the roadmap:
synchronous fake backend, no queue/worker/Postgres yet — the
approve/decline decision happens immediately on creation.

## Endpoints

- `POST /payments` — body `{ orderId, amount, idempotencyKey }`, returns
  `{ paymentId, orderId, amount, status, createdAt }` with `status` in
  `APPROVED` / `DECLINED` (~85% approval rate, randomized). Same
  `idempotencyKey` returns the existing payment instead of creating a
  new one.
- `GET /payments/{paymentId}` — returns the stored payment, `404` if
  unknown.

## API docs (Swagger)

Swagger UI: `http://localhost:8090/swagger-ui.html`
OpenAPI JSON: `http://localhost:8090/v3/api-docs`

Not exposed through the Gateway (it only proxies the specific business
endpoints), so it's only reachable by hitting the service directly —
fine for local/dev.

**On by default, off in prod.** Set `SPRING_PROFILES_ACTIVE=prod` to
disable both (see `application-prod.yml`) — don't ship API docs / a
try-it-out UI to a production environment.

## Run locally (without Docker)

Needs JDK 21 and Gradle installed locally (there's no wrapper committed
here — see the repo root README for why). Then:

```bash
gradle bootRun
```

## Known simplifications (Phase 2)

- In-memory storage only, resets on restart — Phase 4 of the roadmap
  introduces Postgres as the source of truth plus the outbox pattern.
- No queue/worker — the decision is synchronous and immediate instead
  of `PENDING` → async processing.
- No dead-letter queue yet (there's no queue at all yet).
