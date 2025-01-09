package product.application

import product.application.exception.ProductNameAlreadyExistsException
import product.application.exception.ProductNotFoundException
import product.domain.`interface`.ProductRepository
import product.domain.valueobject.ProductId
import product.domain.valueobject.ProductName
import product.domain.valueobject.ProductPrice
import product.domain.valueobject.ProductStockQuantity
import product.infrastructure.controller.dto.request.UpdateProductRequest
import product.infrastructure.controller.dto.response.ProductResponse
import shared.domain.bus.event.EventBus
import java.util.*

class UpdateProductUseCase(
    private val productRepository: ProductRepository,
    private val eventBus: EventBus
) {
    fun execute(productId: UUID, request: UpdateProductRequest): ProductResponse {
        assertProductNameDoesNotExist(productId, request.name)

        val product = productRepository.findById(ProductId(productId)) ?: throw ProductNotFoundException(productId)

        val updatedProduct = product.update(
            ProductName(request.name),
            ProductPrice(request.price),
            ProductStockQuantity(request.stockQuantity)
        )

        productRepository.save(updatedProduct)

        eventBus.publish(*product.retrieveAndFlushDomainEvents().toTypedArray())

        return ProductResponse.from(updatedProduct)
    }

    private fun assertProductNameDoesNotExist(
        productId: UUID,
        productName: String
    ) {
        if (productRepository.findByNameExcludingId(ProductId(productId), ProductName(productName)) != null) {
            throw ProductNameAlreadyExistsException(productName)
        }
    }
}