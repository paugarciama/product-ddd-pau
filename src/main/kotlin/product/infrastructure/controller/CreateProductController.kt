package product.infrastructure.controller

import product.infrastructure.controller.dto.request.CreateProductRequest
import product.infrastructure.controller.dto.response.ProductResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import product.application.CreateProductUseCase

@RestController
@RequestMapping("/api/products")
class CreateProductController(
    private val useCase: CreateProductUseCase
) {
    @PostMapping("/create")
    fun execute(@RequestBody request: CreateProductRequest): ResponseEntity<ProductResponse> {
        val product = useCase.execute(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(product)
    }
}