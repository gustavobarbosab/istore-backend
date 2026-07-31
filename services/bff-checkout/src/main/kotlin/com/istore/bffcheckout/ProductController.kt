package com.istore.bffcheckout

import com.istore.bffcheckout.model.Product
import com.istore.bffcheckout.model.ProductCatalog
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductController {

    @GetMapping("/produtos")
    fun list(): List<Product> = ProductCatalog.products
}
