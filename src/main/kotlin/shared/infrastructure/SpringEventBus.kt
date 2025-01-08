package shared.infrastructure

import org.springframework.context.ApplicationEventPublisher
import shared.domain.bus.event.DomainEvent
import shared.domain.bus.event.EventBus

class SpringEventBus(
    private val publisher: ApplicationEventPublisher
) : EventBus {
    override fun publish(vararg domainEvents: DomainEvent) {
        domainEvents.forEach {
            try {
                publisher.publishEvent(it)
            } catch (ex: Exception) {
                println(ex.message)
            }
        }
    }

}