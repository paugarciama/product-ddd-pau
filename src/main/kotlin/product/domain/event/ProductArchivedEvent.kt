package product.domain.event

import product.domain.aggregate.Product
import shared.domain.bus.event.DomainEvent

class ProductArchivedEvent(val product: Product) : DomainEvent() {
    companion object {
        const val EVENT_NAME = "product.archived"
    }
}