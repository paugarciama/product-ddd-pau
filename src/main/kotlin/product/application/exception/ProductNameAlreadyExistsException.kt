package product.application.exception

class ProductNameAlreadyExistsException(productName: String) : Exception("Product with name $productName already exists.")