package shared.domain.bus.event

interface DomainEventSubscriber {
    fun subscribedTo(): List<Any>
}