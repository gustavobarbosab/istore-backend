package com.istore.bffcheckout.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
class PaymentApiConfig {

    @Bean
    fun paymentApiRestClient(@Value("\${payment-api.base-url}") baseUrl: String): RestClient =
        RestClient.builder()
            .baseUrl(baseUrl)
            .build()
}
