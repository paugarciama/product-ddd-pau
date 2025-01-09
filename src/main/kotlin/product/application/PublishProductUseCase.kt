package product.application

import product.application.exception.ProductNotFoundException
import product.domain.`interface`.ProductRepository
import product.domain.valueobject.ProductId
import product.infrastructure.controller.dto.response.ProductResponse
import shared.domain.bus.event.EventBus
import java.util.*

class PublishProductUseCase(
    private val productRepository: ProductRepository,
    private val eventBus: EventBus
) {
    fun execute(productId: UUID): ProductResponse {
        val product = productRepository.findById(ProductId(productId)) ?: throw ProductNotFoundException(productId)

        val publishedProduct = product.publish()

        productRepository.save(product)

        eventBus.publish(*product.retrieveAndFlushDomainEvents().toTypedArray())

        return ProductResponse.from(publishedProduct)
    }
}