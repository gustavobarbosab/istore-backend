package com.istore.bffcheckout.client

import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestClientException
import java.math.BigDecimal

class PaymentApiUnavailableException(cause: Throwable) :
    RuntimeException("Payment API call failed", cause)

data class PaymentApiRequest(
    val orderId: String,
    val amount: BigDecimal,
    val idempotencyKey: String,
)

data class PaymentApiResponse(
    val paymentId: String,
    val orderId: String,
    val amount: BigDecimal,
    val status: String,
)

@Component
class PaymentApiClient(private val paymentApiRestClient: RestClient) {

    fun createPayment(request: PaymentApiRequest): PaymentApiResponse {
        try {
            return paymentApiRestClient.post()
                .uri("/payments")
                .body(request)
                .retrieve()
                .body(PaymentApiResponse::class.java)
                ?: throw PaymentApiUnavailableException(IllegalStateException("empty response body"))
        } catch (ex: RestClientException) {
            throw PaymentApiUnavailableException(ex)
        }
    }
}
