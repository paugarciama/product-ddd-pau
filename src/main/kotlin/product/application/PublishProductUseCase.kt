package product.application

import product.application.exception.ProductNotFoundException
import product.domain.`interface`.ProductRepository
import product.domain.valueobject.ProductId
import product.infrastructure.controller.dto.response.ProductResponse
import java.util.*

class PublishProductUseCase(
    private val productRepository: ProductRepository
) {
    fun execute(productId: UUID): ProductResponse {
        val product = productRepository.findById(ProductId(productId)) ?: throw ProductNotFoundException(productId)

        val publishedProduct = product.publish()

        productRepository.save(product)

        return ProductResponse.from(publishedProduct)
    }
}