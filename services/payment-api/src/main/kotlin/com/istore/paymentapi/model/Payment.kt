package com.istore.paymentapi.model

import java.math.BigDecimal
import java.time.Instant

enum class PaymentStatus {
    APPROVED,
    DECLINED,
}

/**
 * Phase 2 of the roadmap: synchronous fake backend, no queue/worker yet.
 * The decision (approved/declined) is made immediately on creation instead
 * of going through PENDING -> outbox -> queue -> worker like the target
 * architecture. That comes in a later phase.
 */
data class Payment(
    val paymentId: String,
    val orderId: String,
    val amount: BigDecimal,
    val status: PaymentStatus,
    val idempotencyKey: String,
    val createdAt: Instant,
)
