package product.domain.aggregate

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import product.domain.event.ProductArchivedEvent
import product.domain.event.ProductCreatedEvent
import product.domain.event.ProductPublishedEvent
import product.domain.event.ProductUpdatedEvent
import product.domain.valueobject.ProductName
import product.domain.valueobject.ProductPrice
import product.domain.valueobject.ProductStatus
import product.domain.valueobject.ProductStockQuantity

class ProductTest {

    @Test
    fun `should create a product and raise event`() {
        val name = ProductName("Test Product")
        val price = ProductPrice(100.0)
        val stockQuantity = ProductStockQuantity(10)

        val product = Product.create(name, price, stockQuantity)

        val eventsBeforeFlush = product.retrieveAndFlushDomainEvents()

        assertNotNull(product.id)
        assertEquals(name, product.name)
        assertEquals(price, product.price)
        assertEquals(stockQuantity, product.stockQuantity)
        assertEquals(ProductStatus.CREATED, product.status)
        assertEquals(1, eventsBeforeFlush.size)

        val event = eventsBeforeFlush.first()
        assertTrue(event is ProductCreatedEvent)
        assertEquals(product, (event as ProductCreatedEvent).product)
    }

    @Test
    fun `should update a product and raise ProductUpdatedEvent`() {
        val product = Product.create(
            ProductName("Original Product"),
            ProductPrice(50.0),
            ProductStockQuantity(5)
        )

        val newName = ProductName("Updated Product")
        val newPrice = ProductPrice(75.0)
        val newStockQuantity = ProductStockQuantity(15)

        val updatedProduct = product.update(newName, newPrice, newStockQuantity)

        val eventsBeforeFlush = updatedProduct.retrieveAndFlushDomainEvents()

        assertEquals(newName, updatedProduct.name)
        assertEquals(newPrice, updatedProduct.price)
        assertEquals(newStockQuantity, updatedProduct.stockQuantity)
        assertEquals(1, eventsBeforeFlush.size)

        val event = eventsBeforeFlush.first()
        assertTrue(event is ProductUpdatedEvent)
        assertEquals(updatedProduct, (event as ProductUpdatedEvent).product)
    }

    @Test
    fun `should reduce stock quantity and raise ProductUpdatedEvent`() {
        val product = Product.create(
            ProductName("Test Product"),
            ProductPrice(20.0),
            ProductStockQuantity(10)
        )

        val reductionQuantity = ProductStockQuantity(3)

        val updatedProduct = product.reduceStockQuantity(reductionQuantity)

        val eventsBeforeFlush = updatedProduct.retrieveAndFlushDomainEvents()

        assertEquals(ProductStockQuantity(7), updatedProduct.stockQuantity)
        assertEquals(1, eventsBeforeFlush.size)

        val event = eventsBeforeFlush.first()
        assertTrue(event is ProductUpdatedEvent)
        assertEquals(updatedProduct, (event as ProductUpdatedEvent).product)
    }

    @Test
    fun `should publish product and raise ProductPublishedEvent`() {
        val product = Product.create(
            ProductName("Test Product"),
            ProductPrice(20.0),
            ProductStockQuantity(10)
        )

        val publishedProduct = product.publish()

        val eventsBeforeFlush = publishedProduct.retrieveAndFlushDomainEvents()

        assertEquals(ProductStatus.PUBLISHED, publishedProduct.status)
        assertEquals(1, eventsBeforeFlush.size)

        val event = eventsBeforeFlush.first()
        assertTrue(event is ProductPublishedEvent)
        assertEquals(publishedProduct, (event as ProductPublishedEvent).product)
    }

    @Test
    fun `should archive product and raise ProductArchivedEvent`() {
        val product = Product.create(
            ProductName("Test Product"),
            ProductPrice(20.0),
            ProductStockQuantity(10)
        )

        val archivedProduct = product.archive()

        val eventsBeforeFlush = archivedProduct.retrieveAndFlushDomainEvents()

        assertEquals(ProductStatus.ARCHIVED, archivedProduct.status)
        assertEquals(1, eventsBeforeFlush.size)

        val event = eventsBeforeFlush.first()
        assertTrue(event is ProductArchivedEvent)
        assertEquals(archivedProduct, (event as ProductArchivedEvent).product)
    }

    @Test
    fun `should not mutate the original product`() {
        val product = Product.create(
            ProductName("Original Product"),
            ProductPrice(50.0),
            ProductStockQuantity(5)
        )

        val updatedProduct = product.update(
            ProductName("Updated Product"),
            ProductPrice(75.0),
            ProductStockQuantity(15)
        )

        assertNotSame(product, updatedProduct)
        assertEquals("Original Product", product.name.value)
        assertEquals(50.0, product.price.value)
        assertEquals(5, product.stockQuantity.value)
    }
}
