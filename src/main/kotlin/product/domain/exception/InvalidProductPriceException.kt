package product.domain.exception

class InvalidProductPriceException : IllegalArgumentException("Price must be greater than 0.")