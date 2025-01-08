package product.domain.valueobject

import product.domain.exception.InvalidProductStockQuantityException

data class ProductPrice(
    val value: Double
) {
    init {
        require(value > 0) { throw InvalidProductStockQuantityException() }
    }
}