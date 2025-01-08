package shared.domain.bus.event

import java.util.*

abstract class DomainEvent(
    val eventId: UUID = UUID.randomUUID(),
    val occurredOn: Long = System.currentTimeMillis(),
)