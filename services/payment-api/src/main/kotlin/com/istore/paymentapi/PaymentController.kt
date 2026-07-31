package com.istore.paymentapi

import com.istore.paymentapi.model.dto.CreatePaymentRequest
import com.istore.paymentapi.model.dto.PaymentResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PaymentController(private val paymentService: PaymentService) {

    /**
     * Phase 2: synchronous fake response (no queue/worker yet), so this
     * returns 200 with the final status instead of 202 + PENDING.
     */
    @PostMapping("/payments")
    fun create(@Valid @RequestBody request: CreatePaymentRequest): ResponseEntity<PaymentResponse> {
        val payment = paymentService.create(request)
        return ResponseEntity.ok(PaymentResponse.from(payment))
    }

    @GetMapping("/payments/{paymentId}")
    fun get(@PathVariable paymentId: String): ResponseEntity<PaymentResponse> {
        val payment = paymentService.get(paymentId)
        return ResponseEntity.ok(PaymentResponse.from(payment))
    }
}

@org.springframework.web.bind.annotation.RestControllerAdvice
class PaymentExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(PaymentNotFoundException::class)
    fun handleNotFound(ex: PaymentNotFoundException): ResponseEntity<Map<String, String>> =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to (ex.message ?: "not found")))
}
