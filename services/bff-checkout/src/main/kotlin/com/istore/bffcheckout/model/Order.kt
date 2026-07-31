package com.istore.bffcheckout.model

import java.math.BigDecimal
import java.time.Instant

enum class OrderStatus {
    APPROVED,
    DECLINED,
}

data class OrderItem(
    val productId: String,
    val quantity: Int,
)

data class Order(
    val orderId: String,
    val paymentId: String,
    val items: List<OrderItem>,
    val amount: BigDecimal,
    val status: OrderStatus,
    val idempotencyKey: String,
    val createdAt: Instant,
)
