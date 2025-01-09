package shared.domain.aggregate

import shared.domain.bus.event.DomainEvent

abstract class AggregateRoot {
    private val domainEvents: MutableList<DomainEvent> = mutableListOf()

    fun notifyDomainEvent(domainEvent: DomainEvent) = domainEvents.add(domainEvent)

    fun retrieveAndFlushDomainEvents(): List<DomainEvent> {
        val events = domainEvents.toList()
        this.domainEvents.clear()
        return events
    }
}