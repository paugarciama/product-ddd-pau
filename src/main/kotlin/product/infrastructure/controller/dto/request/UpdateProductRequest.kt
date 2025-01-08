package product.infrastructure.controller.dto.request

data class UpdateProductRequest(
    val name: String,
    val price: Double,
    val stockQuantity: Int
)