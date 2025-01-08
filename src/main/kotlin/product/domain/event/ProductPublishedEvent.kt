package product.domain.event

import product.domain.aggregate.Product
import shared.domain.bus.event.DomainEvent

class ProductPublishedEvent(val product: Product) : DomainEvent() {
    companion object {
        const val EVENT_NAME = "product.published"
    }
}