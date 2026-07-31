package com.istore.paymentapi

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun paymentApiOpenApi(): OpenAPI = OpenAPI()
        .info(
            Info()
                .title("Payment API")
                .description(
                    "Fake synchronous payment backend (roadmap Phase 2 — no queue/worker/Postgres yet). " +
                        "Not exposed via the Gateway; called internally by bff-checkout.",
                )
                .version("0.1.0"),
        )
}
