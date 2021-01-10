package br.com.devcave.prometheus.job

import br.com.devcave.prometheus.configuration.RabbitConfig
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.listener.MessageListenerContainer
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class QueueProducerJob(
    private val rabbitTemplate: RabbitTemplate
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Scheduled(fixedRate = 2_000L)
    fun postQueue() {
        logger.info("postQueue, send message")
        rabbitTemplate.convertAndSend(
            RabbitConfig.QueueDefinition.DIRECT_EXCHANGE,
            RabbitConfig.QueueDefinition.FIRST_BINDING_KEY,
            UUID.randomUUID().toString()
        )
    }
}

@Component
class QueueConsumerJob(
    private val listenerContainer: MessageListenerContainer
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Scheduled(initialDelay = 300_000L, fixedRate = 3_000_000L)
    fun stopConsumer() {
        logger.info("stopConsumer, stopping")
        listenerContainer.stop()
        Thread.sleep(300_000L)
        listenerContainer.start()
        logger.info("stopConsumer, starting")
    }
}

@Component
class CleanDLQJob(private val rabbitTemplate: RabbitTemplate) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Scheduled(fixedRate = 3_000_000L)
    fun stopConsumer() {
        logger.info("Clean DLQ")
        while (true) {
            rabbitTemplate.receive(RabbitConfig.QueueDefinition.DLQ_QUEUE) ?: break
        }
    }
}