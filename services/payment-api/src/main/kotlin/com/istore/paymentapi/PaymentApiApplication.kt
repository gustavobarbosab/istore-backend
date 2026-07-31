package com.istore.paymentapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PaymentApiApplication

fun main(args: Array<String>) {
    runApplication<PaymentApiApplication>(*args)
}
