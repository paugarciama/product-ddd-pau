package product.infrastructure.controller.dto.request

data class CreateProductRequest(
    val name: String,
    val price: Double,
    val stockQuantity: Int
)