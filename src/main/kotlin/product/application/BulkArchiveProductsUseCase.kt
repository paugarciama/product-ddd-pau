package product.application

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import product.domain.`interface`.ProductRepository
import product.domain.valueobject.ProductId
import shared.domain.bus.event.EventBus
import java.util.*

class BulkArchiveProductsUseCase(
    private val productRepository: ProductRepository,
    private val eventBus: EventBus
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
                        product?.archive()?.let { p ->
                            productRepository.save(p)
                            eventBus.publish(*product.retrieveAndFlushDomainEvents().toTypedArray())
                        }
                    } catch (e: Exception) {
                        println("Error archiving product $it: ${e.message}")
                    }
                }

        }
    }
}