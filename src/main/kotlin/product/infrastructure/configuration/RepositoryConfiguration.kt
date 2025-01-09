package product.infrastructure.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import product.domain.`interface`.ProductRepository
import product.infrastructure.repository.ProductRepositoryInMemory

@Configuration
class RepositoryConfiguration {
    @Bean
    fun productRepository(): ProductRepository = ProductRepositoryInMemory()
}