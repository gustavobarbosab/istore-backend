package com.istore.bffcheckout

import com.istore.bffcheckout.client.PaymentApiClient
import com.istore.bffcheckout.client.PaymentApiRequest
import com.istore.bffcheckout.model.Order
import com.istore.bffcheckout.model.OrderItem
import com.istore.bffcheckout.model.OrderStatus
import com.istore.bffcheckout.model.ProductCatalog
import com.istore.bffcheckout.model.dto.CheckoutRequest
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class UnknownProductException(productId: String) : RuntimeException("Unknown product: $productId")

@Service
class OrderService(private val paymentApiClient: PaymentApiClient) {

    // In-memory store, same trade-off as payment-api: fine for Phase 2,
    // a real Order table comes with the Payment API / Postgres phase.
    private val ordersById = ConcurrentHashMap<String, Order>()
    private val orderIdByIdempotencyKey = ConcurrentHashMap<String, String>()

    fun checkout(request: CheckoutRequest): Order {
        orderIdByIdempotencyKey[request.idempotencyKey]?.let { existingId ->
            return ordersById.getValue(existingId)
        }

        val items = request.items.map { OrderItem(it.productId, it.quantity) }
        val amount = calculateAmount(items)
        val orderId = UUID.randomUUID().toString()

        val paymentResponse = paymentApiClient.createPayment(
            PaymentApiRequest(
                orderId = orderId,
                amount = amount,
                idempotencyKey = request.idempotencyKey,
            ),
        )

        val order = Order(
            orderId = orderId,
            paymentId = paymentResponse.paymentId,
            items = items,
            amount = amount,
            status = OrderStatus.valueOf(paymentResponse.status),
            idempotencyKey = request.idempotencyKey,
            createdAt = Instant.now(),
        )

        ordersById[orderId] = order
        orderIdByIdempotencyKey[request.idempotencyKey] = orderId
        return order
    }

    fun listOrders(): List<Order> = ordersById.values.sortedByDescending { it.createdAt }

    private fun calculateAmount(items: List<OrderItem>): BigDecimal =
        items.fold(BigDecimal.ZERO) { acc, item ->
            val product = ProductCatalog.find(item.productId) ?: throw UnknownProductException(item.productId)
            acc + product.price.multiply(BigDecimal(item.quantity))
        }
}
