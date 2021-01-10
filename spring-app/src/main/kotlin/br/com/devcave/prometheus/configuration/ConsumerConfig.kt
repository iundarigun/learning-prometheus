package br.com.devcave.prometheus.configuration

import br.com.devcave.prometheus.listener.QueueListener
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.listener.MessageListenerContainer
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ConsumerConfig(
    private val connectionFactory: ConnectionFactory,
    private val queueListener: QueueListener
) {
    @Bean
    fun listenerContainer(): MessageListenerContainer {
        val container = SimpleMessageListenerContainer()
        container.connectionFactory = connectionFactory
        container.setQueueNames(RabbitConfig.QueueDefinition.FIRST_QUEUE)
        container.setMessageListener(queueListener)
        return container
    }
}