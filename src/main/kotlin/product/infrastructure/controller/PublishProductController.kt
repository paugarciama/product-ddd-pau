package product.infrastructure.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import product.application.PublishProductUseCase
import product.infrastructure.controller.dto.response.ProductResponse
import java.util.*

@RestController
@RequestMapping("/api/products")
class PublishProductController(
    private val useCase: PublishProductUseCase
) {
    @PutMapping("/{id}/publish")
    fun execute(@PathVariable id: UUID): ResponseEntity<ProductResponse> {
        try {
            val product = useCase.execute(id)
            return ResponseEntity.status(HttpStatus.OK).body(product)
        } catch (ex: Exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }
}