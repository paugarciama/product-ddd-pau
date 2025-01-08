package product.domain.`interface`

import product.domain.aggregate.Product
import product.domain.valueobject.ProductId
import product.domain.valueobject.ProductName

interface ProductRepository {
    fun save(product: Product)
    fun findById(productId: ProductId): Product?
    fun findByName(productName: ProductName): Product?
    fun findByNameExcludingId(productId: ProductId, productName: ProductName): Product?
}