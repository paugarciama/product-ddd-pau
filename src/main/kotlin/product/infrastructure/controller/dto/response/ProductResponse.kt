package product.infrastructure.controller.dto.response

import product.domain.aggregate.Product
import java.time.LocalDateTime
import java.util.*

data class ProductResponse(
    val id: UUID,
    val name: String,
    val price: Double,
    val stockQuantity: Int,
    val status: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(product: Product) = ProductResponse(
            product.id.value,
            product.name.value,
            product.price.value,
            product.stockQuantity.value,
            product.status.name,
            product.createdAt,
            product.updatedAt
        )
    }
}