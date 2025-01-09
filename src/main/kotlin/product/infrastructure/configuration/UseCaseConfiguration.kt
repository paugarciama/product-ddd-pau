package product.infrastructure.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import product.application.*
import product.domain.`interface`.ProductRepository
import shared.domain.bus.event.EventBus

@Configuration
class UseCaseConfiguration {
    @Bean
    fun createProductUseCase(
        productRepository: ProductRepository,
        eventBus: EventBus
    ): CreateProductUseCase = CreateProductUseCase(
        productRepository,
        eventBus
    )

    @Bean
    fun updateProductUseCase(
        productRepository: ProductRepository,
        eventBus: EventBus
    ): UpdateProductUseCase = UpdateProductUseCase(
        productRepository,
        eventBus
    )

    @Bean
    fun publishProductUseCase(
        productRepository: ProductRepository,
        eventBus: EventBus
    ): PublishProductUseCase = PublishProductUseCase(
        productRepository,
        eventBus
    )

    @Bean
    fun reduceProductStockQuantityUseCase(
        productRepository: ProductRepository,
        eventBus: EventBus
    ): ReduceProductStockQuantityUseCase = ReduceProductStockQuantityUseCase(
        productRepository,
        eventBus
    )

    @Bean
    fun bulkArchiveProductsUseCase(
        productRepository: ProductRepository,
        eventBus: EventBus
    ): BulkArchiveProductsUseCase = BulkArchiveProductsUseCase(
        productRepository,
        eventBus
    )
}