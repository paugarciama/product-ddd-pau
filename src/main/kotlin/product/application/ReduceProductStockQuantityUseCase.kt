package product.application

import product.application.exception.ProductNotFoundException
import product.domain.`interface`.ProductRepository
import product.domain.valueobject.ProductId
import product.domain.valueobject.ProductStockQuantity
import product.infrastructure.controller.dto.request.ReduceStockQuantityRequest
import product.infrastructure.controller.dto.response.ProductResponse
import shared.domain.bus.event.EventBus
import java.util.*

class ReduceProductStockQuantityUseCase(
    private val productRepository: ProductRepository,
    private val eventBus: EventBus
) {
    fun execute(productId: UUID, request: ReduceStockQuantityRequest): ProductResponse {
        val product = productRepository.findById(ProductId(productId)) ?: throw ProductNotFoundException(productId)

        val updatedProduct = product.reduceStockQuantity(ProductStockQuantity(request.quantity))

        productRepository.save(updatedProduct)

        eventBus.publish(*product.retrieveAndFlushDomainEvents().toTypedArray())

        return ProductResponse.from(updatedProduct)
    }
}