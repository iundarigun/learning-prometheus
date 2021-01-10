package br.com.devcave.prometheus.listener

import org.slf4j.LoggerFactory
import org.springframework.amqp.AmqpRejectAndDontRequeueException
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageListener
import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
class QueueListener : MessageListener {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun onMessage(message: Message?) {
        logger.info("consumer")
        if (Random.nextInt(0, 10) < 1) {
            throw AmqpRejectAndDontRequeueException("Random DLQ")
        }
    }
}