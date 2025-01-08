package product.domain.exception

class InvalidProductStockQuantityException : IllegalArgumentException("Stock quantity must be a positive value")