package com.istore.bffcheckout

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun bffCheckoutOpenApi(): OpenAPI = OpenAPI()
        .info(
            Info()
                .title("BFF Checkout")
                .description(
                    "Checkout flow BFF (roadmap Phase 2 — synchronous, in-memory, calls payment-api directly). " +
                        "Reached through the Gateway under the /checkout prefix.",
                )
                .version("0.1.0"),
        )
}
