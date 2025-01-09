package product.infrastructure.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import product.application.CreateProductUseCase
import product.infrastructure.controller.dto.request.CreateProductRequest
import product.infrastructure.controller.dto.response.ProductResponse

@RestController
@RequestMapping("/api/products")
class CreateProductController(
    private val useCase: CreateProductUseCase
) {
    @PostMapping("/create")
    fun execute(@RequestBody request: CreateProductRequest): ResponseEntity<ProductResponse> {
        try {
            val product = useCase.execute(request)
            return ResponseEntity.status(HttpStatus.CREATED).body(product)
        } catch (ex: Exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
        // more exception detail depending on the exception that comes from the useCase
    }
}