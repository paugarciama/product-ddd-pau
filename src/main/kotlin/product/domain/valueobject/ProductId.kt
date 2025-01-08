package product.domain.valueobject

import java.util.*

data class ProductId(
    val value: UUID
) {
    fun from(value: String) = ProductId(UUID.fromString(value))
}