package product.application.exception

import java.util.*

class ProductNotFoundException(productId: UUID) : Exception("Product with id $productId not found")