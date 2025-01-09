package product.application

import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import product.domain.aggregate.Product
import product.domain.event.ProductArchivedEvent
import product.domain.`interface`.ProductRepository
import product.domain.valueobject.ProductId
import shared.domain.bus.event.EventBus
import java.util.*
import kotlin.test.Test

class BulkArchiveProductsUseCaseTest {
    private lateinit var productRepository: ProductRepository
    private lateinit var eventBus: EventBus
    private lateinit var useCase: BulkArchiveProductsUseCase

    @BeforeEach
    fun setUp() {
        productRepository = mockk()
        eventBus = mockk()
        useCase = BulkArchiveProductsUseCase(productRepository, eventBus)
    }

    @Test
    fun `should archive products and publish events`() = runBlocking {
        val productIds = listOf(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
        val products = productIds.map {
            mockk<Product>(relaxed = true).apply {
                every { archive() } returns this
                every { retrieveAndFlushDomainEvents() } returns listOf(ProductArchivedEvent(this))
            }
        }

        productIds.forEachIndexed { index, id ->
            every { productRepository.findById(ProductId(id)) } returns products[index]
            every { productRepository.save(products[index]) } just Runs
        }
        every { eventBus.publish(*anyVararg()) } just Runs

        useCase.execute(productIds)

        productIds.forEachIndexed { index, id ->
            verify {
                productRepository.findById(ProductId(id))
                products[index].archive()
                productRepository.save(products[index])
            }
        }
    }

    @Test
    fun `should continue processing even if one product fails`() = runBlocking {
        val productIds = listOf(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
        val product = mockk<Product>(relaxed = true).apply {
            every { archive() } returns this
            every { retrieveAndFlushDomainEvents() } returns listOf(ProductArchivedEvent(this))
        }

        every { productRepository.findById(ProductId(productIds[0])) } returns product
        every { productRepository.save(product) } just Runs
        every { eventBus.publish(*anyVararg()) } just Runs

        every { productRepository.findById(ProductId(productIds[1])) } throws RuntimeException("DB error")
        every { productRepository.findById(ProductId(productIds[2])) } returns null

        useCase.execute(productIds)

        verify {
            productRepository.findById(ProductId(productIds[0]))
            product.archive()
            productRepository.save(product)
        }
        verify { productRepository.findById(ProductId(productIds[1])) }
        verify { productRepository.findById(ProductId(productIds[2])) }
    }
}
