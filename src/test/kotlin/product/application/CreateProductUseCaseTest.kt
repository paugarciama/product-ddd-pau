package product.application

import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import product.application.exception.ProductNameAlreadyExistsException
import product.domain.aggregate.Product
import product.domain.event.ProductCreatedEvent
import product.domain.`interface`.ProductRepository
import product.domain.valueobject.*
import product.infrastructure.controller.dto.request.CreateProductRequest
import shared.domain.bus.event.EventBus
import java.time.LocalDateTime
import java.util.*

class CreateProductUseCaseTest {
    private lateinit var productRepository: ProductRepository
    private lateinit var eventBus: EventBus
    private lateinit var useCase: CreateProductUseCase

    @BeforeEach
    fun setUp() {
        productRepository = mockk()
        eventBus = mockk()
        useCase = CreateProductUseCase(productRepository, eventBus)
    }

    @Test
    fun `should create a new product when name is unique`() {
        val request = CreateProductRequest("NewProduct", 200.0, 5)
        every { productRepository.findByName(ProductName(request.name)) } returns null
        every { productRepository.save(any()) } just Runs
        every { eventBus.publish(*anyVararg()) } just Runs

        val response = useCase.execute(request)

        assertNotNull(response)
        assertEquals(request.name, response.name)
        assertEquals(request.price, response.price)
        assertEquals(request.stockQuantity, response.stockQuantity)

        verify {
            productRepository.findByName(ProductName(request.name))
            productRepository.save(any<Product>())
            eventBus.publish(*anyVararg<ProductCreatedEvent>())
        }
    }

    @Test
    fun `should throw exception when product name already exists`() {
        val request = CreateProductRequest("ExistingProduct", 100.0, 10)
        every { productRepository.findByName(ProductName(request.name)) } returns
            Product(
                ProductId(UUID.randomUUID()),
                ProductName(request.name),
                ProductPrice(100.0),
                ProductStockQuantity(10),
                ProductStatus.CREATED,
                LocalDateTime.now(),
                LocalDateTime.now()
            )

        assertThrows(ProductNameAlreadyExistsException::class.java) { useCase.execute(request) }

        verify { productRepository.findByName(ProductName(request.name)) }
    }
}
