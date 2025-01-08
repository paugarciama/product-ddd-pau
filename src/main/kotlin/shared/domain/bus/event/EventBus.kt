package shared.domain.bus.event

interface EventBus {
    fun publish(vararg domainEvents: DomainEvent)
}