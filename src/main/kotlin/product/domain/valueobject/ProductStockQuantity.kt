package product.domain.valueobject

import product.domain.exception.InvalidProductStockQuantityException

data class ProductStockQuantity(
    val value: Int
) {
    init {
        require(value >= 0) { throw InvalidProductStockQuantityException() }
    }
}