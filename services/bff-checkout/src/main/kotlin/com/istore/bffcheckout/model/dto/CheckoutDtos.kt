package com.istore.bffcheckout.model.dto

import com.istore.bffcheckout.model.Order
import com.istore.bffcheckout.model.OrderItem
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.Valid
import java.math.BigDecimal
import java.time.Instant

data class CheckoutItemRequest(
    @field:NotBlank
    val productId: String,
    @field:Min(1)
    val quantity: Int,
)

data class CheckoutRequest(
    @field:NotEmpty
    val items: List<@Valid CheckoutItemRequest>,
    @field:NotBlank
    val idempotencyKey: String,
)

data class CheckoutResponse(
    val orderId: String,
    val paymentId: String,
    val status: String,
    val amount: BigDecimal,
)

data class OrderSummaryResponse(
    val orderId: String,
    val paymentId: String,
    val status: String,
    val amount: BigDecimal,
    val items: List<OrderItem>,
    val createdAt: Instant,
) {
    companion object {
        fun from(order: Order) = OrderSummaryResponse(
            orderId = order.orderId,
            paymentId = order.paymentId,
            status = order.status.name,
            amount = order.amount,
            items = order.items,
            createdAt = order.createdAt,
        )
    }
}
