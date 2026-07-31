package com.istore.bffcheckout

import com.istore.bffcheckout.model.dto.CheckoutRequest
import com.istore.bffcheckout.model.dto.CheckoutResponse
import com.istore.bffcheckout.model.dto.OrderSummaryResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CheckoutController(private val orderService: OrderService) {

    @PostMapping("/checkout")
    fun checkout(@Valid @RequestBody request: CheckoutRequest): ResponseEntity<CheckoutResponse> {
        val order = orderService.checkout(request)
        return ResponseEntity.ok(
            CheckoutResponse(
                orderId = order.orderId,
                paymentId = order.paymentId,
                status = order.status.name,
                amount = order.amount,
            ),
        )
    }

    @GetMapping("/pedidos")
    fun listOrders(): List<OrderSummaryResponse> =
        orderService.listOrders().map { OrderSummaryResponse.from(it) }
}

@org.springframework.web.bind.annotation.RestControllerAdvice
class CheckoutExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(UnknownProductException::class)
    fun handleUnknownProduct(ex: UnknownProductException): ResponseEntity<Map<String, String>> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to (ex.message ?: "unknown product")))

    @org.springframework.web.bind.annotation.ExceptionHandler(com.istore.bffcheckout.client.PaymentApiUnavailableException::class)
    fun handlePaymentApiDown(ex: com.istore.bffcheckout.client.PaymentApiUnavailableException): ResponseEntity<Map<String, String>> =
        ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(mapOf("error" to "payment-api unavailable"))
}
