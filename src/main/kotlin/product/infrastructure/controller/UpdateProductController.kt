package product.infrastructure.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import product.application.UpdateProductUseCase
import product.infrastructure.controller.dto.request.UpdateProductRequest
import product.infrastructure.controller.dto.response.ProductResponse
import java.util.*

@RestController
@RequestMapping("/api/products")
class UpdateProductController(
    private val useCase: UpdateProductUseCase
) {
    @PutMapping("/{id}/update")
    fun execute(
        @PathVariable id: UUID,
        @RequestBody request: UpdateProductRequest
    ): ResponseEntity<ProductResponse> {
        try {
            val product = useCase.execute(id, request)
            return ResponseEntity.status(HttpStatus.OK).body(product)
        } catch (ex: Exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }
}