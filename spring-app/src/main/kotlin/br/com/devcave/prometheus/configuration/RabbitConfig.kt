package br.com.devcave.prometheus.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.Exchange
import org.springframework.amqp.core.ExchangeBuilder
import org.springframework.amqp.core.QueueBuilder
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
class RabbitConfig(
    private val connectionFactory: ConnectionFactory
) {
    object QueueDefinition {
        const val FIRST_QUEUE = "FIRST-QUEUE-ADVANCED"
        const val DIRECT_EXCHANGE = "DIRECT-EXCHANGE-ADVANCED"
        const val FIRST_BINDING_KEY = "TO-FIRST-QUEUE"
        const val DLQ_EXCHANGE = "DLQ-EXCHANGE"
        const val DLQ_QUEUE = "DLQ-QUEUE"
        const val DLQ_BINDING_KEY = "TO-DLQ"
    }

    @Bean
    fun messageConverter(objectMapper: ObjectMapper): MessageConverter {
        return Jackson2JsonMessageConverter(objectMapper)
    }

    @PostConstruct
    fun createRabbitObjects() {
        RabbitAdmin(connectionFactory).let {
            createExchange(it)
            createFirstQueue(it)
            createDLQ(it)
        }
    }

    private fun createExchange(rabbitAdmin: RabbitAdmin) {
        val exchange = ExchangeBuilder
            .directExchange(QueueDefinition.DIRECT_EXCHANGE)
            .durable(true)
            .build<Exchange>()
        rabbitAdmin.declareExchange(exchange)
    }

    private fun createFirstQueue(rabbitAdmin: RabbitAdmin) {
        val queue = QueueBuilder.durable(QueueDefinition.FIRST_QUEUE)
            .deadLetterExchange(QueueDefinition.DLQ_EXCHANGE)
            .deadLetterRoutingKey(QueueDefinition.DLQ_BINDING_KEY)
            .build()
        val binding = Binding(
            QueueDefinition.FIRST_QUEUE,
            Binding.DestinationType.QUEUE,
            QueueDefinition.DIRECT_EXCHANGE,
            QueueDefinition.FIRST_BINDING_KEY,
            null
        )
        rabbitAdmin.declareQueue(queue)
        rabbitAdmin.declareBinding(binding)
    }

    private fun createDLQ(rabbitAdmin: RabbitAdmin) {
        val queue = QueueBuilder.durable(QueueDefinition.DLQ_QUEUE)
            .build()
        val exchange = ExchangeBuilder
            .directExchange(QueueDefinition.DLQ_EXCHANGE)
            .durable(true)
            .build<Exchange>()
        val binding = Binding(
            QueueDefinition.DLQ_QUEUE,
            Binding.DestinationType.QUEUE,
            QueueDefinition.DLQ_EXCHANGE,
            QueueDefinition.DLQ_BINDING_KEY,
            null
        )
        rabbitAdmin.declareQueue(queue)
        rabbitAdmin.declareExchange(exchange)
        rabbitAdmin.declareBinding(binding)
    }
}