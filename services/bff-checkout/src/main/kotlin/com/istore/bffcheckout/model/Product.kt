package com.istore.bffcheckout.model

import java.math.BigDecimal

data class Product(
    val id: String,
    val name: String,
    val price: BigDecimal,
)

/**
 * Static in-memory catalog. Fine for Phase 2 — a real Catalog service is
 * out of scope for this exercise (see bff-catalogo placeholder).
 */
object ProductCatalog {
    val products: List<Product> = listOf(
        Product("prod-1", "Wireless Mouse", BigDecimal("29.90")),
        Product("prod-2", "Mechanical Keyboard", BigDecimal("249.90")),
        Product("prod-3", "USB-C Hub", BigDecimal("89.90")),
        Product("prod-4", "Noise-Cancelling Headset", BigDecimal("399.90")),
    )

    fun find(productId: String): Product? = products.find { it.id == productId }
}
