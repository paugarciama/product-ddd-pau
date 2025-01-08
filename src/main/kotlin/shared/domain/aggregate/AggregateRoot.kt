package shared.domain.aggregate

import shared.domain.bus.event.DomainEvent

abstract class AggregateRoot {
    private val domainEvents: MutableList<DomainEvent> = mutableListOf()

    fun notifyDomainEvent(domainEvent: DomainEvent) = domainEvents.add(domainEvent)

    fun retrieveAndFlushDomainEvents(): List<DomainEvent> {
        val events = domainEvents()
        this.resetDomainEvents()
        return events
    }

    private fun domainEvents(): List<DomainEvent> = domainEvents

    private fun resetDomainEvents() = domainEvents.clear()
}