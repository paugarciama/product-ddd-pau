package product.infrastructure.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import product.application.BulkArchiveProductsUseCase
import java.util.*

@RestController
@RequestMapping("/api/products")
class BulkArchiveProductsController(
    private val useCase: BulkArchiveProductsUseCase
) {
    @PostMapping("/bulk-archive")
    fun execute(@RequestBody productIds: List<UUID>): ResponseEntity<Void> {
        try {
            useCase.execute(productIds)
            return ResponseEntity.status(HttpStatus.OK).build()
        } catch (ex: Exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
        // more exception detail depending on the exception that comes from the useCase
    }
}