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
import java.util.*

class UpdateProductUseCase(
    private val productRepository: ProductRepository
) {
    fun execute(productId: UUID, request: UpdateProductRequest): ProductResponse {
        productRepository.findByNameExcludingId(ProductId(productId), ProductName(request.name))
            ?: throw ProductNameAlreadyExistsException(request.name)

        val product = productRepository.findById(ProductId(productId)) ?: throw ProductNotFoundException(productId)

        val updatedProduct = product.update(
            ProductName(request.name),
            ProductPrice(request.price),
            ProductStockQuantity(request.stockQuantity)
        )

        productRepository.save(updatedProduct)

        return ProductResponse.from(updatedProduct)
    }
}