package product.application

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import product.domain.`interface`.ProductRepository
import product.infrastructure.controller.dto.request.CreateProductRequest
import kotlinx.coroutines.runBlocking
import product.domain.valueobject.ProductId
import java.util.UUID

class BulkArchiveProductsUseCase(
    private val productRepository: ProductRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun execute(productIds: List<UUID>) {
        runBlocking {
            productIds.asFlow()
                .map { flowOf(it) }
                .flattenMerge(concurrency = 5)
                .collect {
                    try {
                        val product = productRepository.findById(ProductId(it))
                        product?.archive()?.let { p -> productRepository.save(p) }
                    } catch (e: Exception) {
                        println("Error archiving product $it: ${e.message}")
                    }
                }
        }
    }
}