package product.infrastructure.controller

import product.application.ReduceProductStockQuantityUseCase
import product.infrastructure.controller.dto.request.ReduceStockQuantityRequest
import product.infrastructure.controller.dto.response.ProductResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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
        val product = useCase.execute(id, request)
        return ResponseEntity.status(HttpStatus.OK).body(product)
    }
}