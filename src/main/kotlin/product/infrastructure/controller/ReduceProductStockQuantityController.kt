package product.infrastructure.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import product.application.ReduceProductStockQuantityUseCase
import product.infrastructure.controller.dto.request.ReduceStockQuantityRequest
import product.infrastructure.controller.dto.response.ProductResponse
import java.util.*

@RestController
@RequestMapping("/api/products")
class ReduceProductStockQuantityController(
    private val useCase: ReduceProductStockQuantityUseCase
) {
    @PutMapping("/{id}/reduce-stock")
    fun execute(
        @PathVariable id: UUID,
        @RequestBody request: ReduceStockQuantityRequest
    ): ResponseEntity<ProductResponse> {
        try {
            val product = useCase.execute(id, request)
            return ResponseEntity.status(HttpStatus.OK).body(product)
        } catch (ex: Exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }
}