package product.domain.aggregate

import product.domain.event.ProductArchivedEvent
import product.domain.event.ProductCreatedEvent
import product.domain.event.ProductPublishedEvent
import product.domain.event.ProductUpdatedEvent
import product.domain.valueobject.*
import shared.domain.aggregate.AggregateRoot
import java.time.LocalDateTime
import java.util.*

data class Product(
    val id: ProductId,
    val name: ProductName,
    val price: ProductPrice,
    val stockQuantity: ProductStockQuantity,
    val status: ProductStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) : AggregateRoot() {
    companion object {
        fun create(
            name: ProductName,
            price: ProductPrice,
            stockQuantity: ProductStockQuantity
        ): Product {
            val product = Product(
                ProductId(UUID.randomUUID()),
                name,
                price,
                stockQuantity,
                ProductStatus.CREATED,
                LocalDateTime.now(),
                LocalDateTime.now()
            )

            product.notifyDomainEvent(ProductCreatedEvent(product))

            return product
        }
    }

    fun update(
        name: ProductName,
        price: ProductPrice,
        stockQuantity: ProductStockQuantity
    ): Product {
        val product = this.copy(
            name = name,
            price = price,
            stockQuantity = stockQuantity,
            updatedAt = LocalDateTime.now()
        )

        product.notifyDomainEvent(ProductUpdatedEvent(product))

        return product
    }

    fun reduceStockQuantity(quantity: ProductStockQuantity): Product {
        val product = this.copy(
            stockQuantity = ProductStockQuantity(stockQuantity.value - quantity.value),
            updatedAt = LocalDateTime.now()
        )

        product.notifyDomainEvent(ProductUpdatedEvent(product))

        return product
    }

    fun publish(): Product {
        val product = this.copy(status = ProductStatus.PUBLISHED)

        product.notifyDomainEvent(ProductPublishedEvent(product))

        return product
    }

    fun archive(): Product {
        val product = this.copy(status = ProductStatus.ARCHIVED)

        product.notifyDomainEvent(ProductArchivedEvent(product))

        return product
    }
}