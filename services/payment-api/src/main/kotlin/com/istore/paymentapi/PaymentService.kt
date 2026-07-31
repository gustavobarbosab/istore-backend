package com.istore.paymentapi

import com.istore.paymentapi.model.Payment
import com.istore.paymentapi.model.PaymentStatus
import com.istore.paymentapi.model.dto.CreatePaymentRequest
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random

class PaymentNotFoundException(paymentId: String) : RuntimeException("Payment not found: $paymentId")

@Service
class PaymentService {

    // In-memory store, good enough for Phase 2. A real Postgres table +
    // outbox pattern comes in a later phase (see "3. Architecture
    // Decisions" in the Notion doc).
    private val paymentsById = ConcurrentHashMap<String, Payment>()
    private val paymentIdByIdempotencyKey = ConcurrentHashMap<String, String>()

    fun create(request: CreatePaymentRequest): Payment {
        paymentIdByIdempotencyKey[request.idempotencyKey]?.let { existingId ->
            return paymentsById.getValue(existingId)
        }

        val payment = Payment(
            paymentId = UUID.randomUUID().toString(),
            orderId = request.orderId,
            amount = request.amount,
            status = decide(),
            idempotencyKey = request.idempotencyKey,
            createdAt = Instant.now(),
        )

        paymentsById[payment.paymentId] = payment
        paymentIdByIdempotencyKey[request.idempotencyKey] = payment.paymentId
        return payment
    }

    fun get(paymentId: String): Payment =
        paymentsById[paymentId] ?: throw PaymentNotFoundException(paymentId)

    /**
     * Fake decision logic for Phase 2 (no real payment processor yet).
     * ~85% approval rate, just so the BFF has both outcomes to handle.
     */
    private fun decide(): PaymentStatus =
        if (Random.nextInt(100) < 85) PaymentStatus.APPROVED else PaymentStatus.DECLINED
}
