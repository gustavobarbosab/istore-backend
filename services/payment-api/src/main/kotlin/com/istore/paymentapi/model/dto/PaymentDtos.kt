package com.istore.paymentapi.model.dto

import com.istore.paymentapi.model.Payment
import com.istore.paymentapi.model.PaymentStatus
import java.math.BigDecimal
import java.time.Instant
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank

data class CreatePaymentRequest(
    @field:NotBlank
    val orderId: String,
    @field:DecimalMin(value = "0.01")
    val amount: BigDecimal,
    @field:NotBlank
    val idempotencyKey: String,
)

data class PaymentResponse(
    val paymentId: String,
    val orderId: String,
    val amount: BigDecimal,
    val status: PaymentStatus,
    val createdAt: Instant,
) {
    companion object {
        fun from(payment: Payment) = PaymentResponse(
            paymentId = payment.paymentId,
            orderId = payment.orderId,
            amount = payment.amount,
            status = payment.status,
            createdAt = payment.createdAt,
        )
    }
}
