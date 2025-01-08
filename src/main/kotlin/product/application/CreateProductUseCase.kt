package product.application

import product.application.exception.ProductNameAlreadyExistsException
import product.domain.aggregate.Product
import product.domain.`interface`.ProductRepository
import product.domain.valueobject.ProductName
import product.domain.valueobject.ProductPrice
import product.domain.valueobject.ProductStockQuantity
import product.infrastructure.controller.dto.request.CreateProductRequest
import product.infrastructure.controller.dto.response.ProductResponse
import shared.domain.bus.event.EventBus

class CreateProductUseCase(
    private val productRepository: ProductRepository,
    private val eventBus: EventBus
) {
    fun execute(request: CreateProductRequest): ProductResponse {
        productRepository.findByName(ProductName(request.name)) ?: throw ProductNameAlreadyExistsException(request.name)

        val product = Product.create(
            ProductName(request.name),
            ProductPrice(request.price),
            ProductStockQuantity(request.stockQuantity)
        )

        productRepository.save(product)

        eventBus.publish(product.retrieveAndFlushDomainEvents())

        return ProductResponse.from(product)
    }
}