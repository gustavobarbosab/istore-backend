# bff-checkout

Spring Boot (Kotlin) BFF for the checkout flow. Implements Phase 2 of
the roadmap: synchronous backend, no queue yet, calls `payment-api`
directly over HTTP and returns the (fake) final status right away.

## Endpoints

- `GET /produtos` — static in-memory product catalog.
- `POST /checkout` — creates an order from `{ items, idempotencyKey }`,
  calls `payment-api`, returns `{ orderId, paymentId, status, amount }`.
  Same `idempotencyKey` returns the existing order instead of creating
  a new one / calling payment-api again.
- `GET /pedidos` — lists all orders created so far (in-memory, no
  per-user filtering yet — that needs real auth/user context).

## Configuration

`payment-api.base-url` (env override: `PAYMENT_API_BASE_URL`) — where
to reach the Payment API. Defaults to `http://localhost:8090`, set to
`http://payment-api:8090` in `docker-compose.yml`.

## API docs (Swagger)

Swagger UI: `http://localhost:8081/swagger-ui.html`
OpenAPI JSON: `http://localhost:8081/v3/api-docs`

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

- No auth yet — `/pedidos` returns everyone's orders, not just the
  caller's. Fine for now since the Gateway already gates this endpoint
  behind JWT; per-user filtering comes when there's a real user/session
  concept.
- In-memory storage only, resets on restart.
- No idempotent retry against payment-api beyond the local
  idempotency-key check.
