package product.application

import product.application.exception.ProductNotFoundException
import product.domain.`interface`.ProductRepository
import product.domain.valueobject.ProductId
import product.domain.valueobject.ProductStockQuantity
import product.infrastructure.controller.dto.request.ReduceStockQuantityRequest
import product.infrastructure.controller.dto.response.ProductResponse
import java.util.*

class ReduceProductStockQuantityUseCase(
    private val productRepository: ProductRepository
) {
    fun execute(productId: UUID, request: ReduceStockQuantityRequest): ProductResponse {
        val product = productRepository.findById(ProductId(productId)) ?: throw ProductNotFoundException(productId)

        val updatedProduct = product.reduceStockQuantity(ProductStockQuantity(request.quantity))

        productRepository.save(updatedProduct)

        return ProductResponse.from(updatedProduct)
    }
}