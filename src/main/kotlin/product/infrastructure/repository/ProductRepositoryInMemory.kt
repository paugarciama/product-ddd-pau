package product.infrastructure.repository

import product.domain.aggregate.Product
import product.domain.`interface`.ProductRepository
import product.domain.valueobject.ProductId
import product.domain.valueobject.ProductName

class ProductRepositoryInMemory : ProductRepository {
    private val products: MutableList<Product> = mutableListOf()

    override fun save(product: Product) { products.add(product) }

    override fun findById(productId: ProductId): Product? = products.find { it.id == productId }?.let { return it }

    override fun findByName(productName: ProductName): Product? = products.find { it.name == productName }

    override fun findByNameExcludingId(productId: ProductId, productName: ProductName): Product? =
        products.find { it.id != productId && it.name == productName }
}