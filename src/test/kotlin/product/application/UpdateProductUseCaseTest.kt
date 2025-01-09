package product.application

import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import product.application.exception.ProductNameAlreadyExistsException
import product.application.exception.ProductNotFoundException
import product.domain.aggregate.Product
import product.domain.event.ProductUpdatedEvent
import product.domain.`interface`.ProductRepository
import product.domain.valueobject.*
import product.infrastructure.controller.dto.request.UpdateProductRequest
import shared.domain.bus.event.EventBus
import java.time.LocalDateTime
import java.util.*

class UpdateProductUseCaseTest {
    private lateinit var productRepository: ProductRepository
    private lateinit var eventBus: EventBus
    private lateinit var useCase: UpdateProductUseCase

    companion object {
        private val PRODUCT_ID: UUID = UUID.fromString("99f71b77-5c02-4e60-9199-18be088cc0bc")
    }

    @BeforeEach
    fun setUp() {
        productRepository = mockk()
        eventBus = mockk()
        useCase = UpdateProductUseCase(productRepository, eventBus)
    }

    @Test
    fun `should update a product when product exists`() {
        val request = UpdateProductRequest("ExistingProduct", 200.0, 5)
        every { productRepository.findByNameExcludingId(ProductId(PRODUCT_ID), ProductName(request.name)) } returns null
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

        val response = useCase.execute(PRODUCT_ID, request)

        assertNotNull(response)
        assertEquals(request.name, response.name)
        assertEquals(request.price, response.price)
        assertEquals(request.stockQuantity, response.stockQuantity)

        verify {
            productRepository.findByNameExcludingId(ProductId(PRODUCT_ID), ProductName(request.name))
            productRepository.findById(ProductId(PRODUCT_ID))
            productRepository.save(any<Product>())
            eventBus.publish(*anyVararg<ProductUpdatedEvent>())
        }
    }

    @Test
    fun `should throw exception when product name already exists`() {
        val request = UpdateProductRequest("ExistingProduct", 100.0, 10)
        every { productRepository.findByNameExcludingId(ProductId(PRODUCT_ID), ProductName(request.name)) } returns
            Product(
                ProductId(UUID.randomUUID()),
                ProductName("ExistingProduct"),
                ProductPrice(100.0),
                ProductStockQuantity(5),
                ProductStatus.CREATED,
                LocalDateTime.now(),
                LocalDateTime.now()
            )

        assertThrows(ProductNameAlreadyExistsException::class.java) { useCase.execute(PRODUCT_ID, request) }

        verify { productRepository.findByNameExcludingId(ProductId(PRODUCT_ID), ProductName(request.name)) }
    }

    @Test
    fun `should throw exception when product does not exist`() {
        val request = UpdateProductRequest("ExistingProduct", 100.0, 10)
        every { productRepository.findByNameExcludingId(ProductId(PRODUCT_ID), ProductName(request.name)) } returns null
        every { productRepository.findById(ProductId(PRODUCT_ID)) } returns null

        assertThrows(ProductNotFoundException::class.java) { useCase.execute(PRODUCT_ID, request) }

        verify {
            productRepository.findByNameExcludingId(ProductId(PRODUCT_ID), ProductName(request.name))
            productRepository.findById(ProductId(PRODUCT_ID))
        }
    }
}
