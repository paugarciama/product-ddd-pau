package product.application

import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import product.application.exception.ProductNotFoundException
import product.domain.aggregate.Product
import product.domain.event.ProductPublishedEvent
import product.domain.`interface`.ProductRepository
import product.domain.valueobject.*
import shared.domain.bus.event.EventBus
import java.time.LocalDateTime
import java.util.*

class PublishProductUseCaseTest {
    private lateinit var productRepository: ProductRepository
    private lateinit var eventBus: EventBus
    private lateinit var useCase: PublishProductUseCase

    companion object {
        private val PRODUCT_ID: UUID = UUID.fromString("99f71b77-5c02-4e60-9199-18be088cc0bc")
    }

    @BeforeEach
    fun setUp() {
        productRepository = mockk()
        eventBus = mockk()
        useCase = PublishProductUseCase(productRepository, eventBus)
    }

    @Test
    fun `should update status from product when product exists`() {
        every { productRepository.findById(ProductId(PRODUCT_ID)) } returns
            Product(
                ProductId(PRODUCT_ID),
                ProductName("ExistingProduct"),
                ProductPrice(100.0),
                ProductStockQuantity(5),
                ProductStatus.CREATED,
                LocalDateTime.now(),
                LocalDateTime.now()
            )
        every { productRepository.save(any()) } just Runs
        every { eventBus.publish(*anyVararg()) } just Runs

        val response = useCase.execute(PRODUCT_ID)

        assertNotNull(response)
        assertEquals(ProductStatus.PUBLISHED.name, response.status)

        verify {
            productRepository.findById(ProductId(PRODUCT_ID))
            productRepository.save(any<Product>())
            eventBus.publish(*anyVararg<ProductPublishedEvent>())
        }
    }

    @Test
    fun `should throw exception when product does not exist`() {
        every { productRepository.findById(ProductId(PRODUCT_ID)) } returns null

        assertThrows(ProductNotFoundException::class.java) { useCase.execute(PRODUCT_ID) }

        verify {
            productRepository.findById(ProductId(PRODUCT_ID))
        }
    }
}
