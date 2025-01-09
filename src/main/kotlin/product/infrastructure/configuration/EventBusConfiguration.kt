package product.infrastructure.configuration

import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import shared.domain.bus.event.EventBus
import shared.infrastructure.SpringEventBus

@Configuration
class EventBusConfiguration {
    @Bean
    fun eventBus(publisher: ApplicationEventPublisher): EventBus = SpringEventBus(publisher)
}