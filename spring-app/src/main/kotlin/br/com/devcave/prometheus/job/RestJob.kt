package br.com.devcave.prometheus.job

import br.com.devcave.prometheus.client.ItSelfClient
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class RestGetJob(
    private val itSelfClient: ItSelfClient
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Scheduled(fixedRate = 1_000L)
    fun getJob() {
        logger.info("getJob, rest")
        itSelfClient.get()
    }
}

@Component
class RestPostJob(
    private val itSelfClient: ItSelfClient
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Scheduled(fixedRate = 1_000L)
    fun postJob() {
        logger.info("postJob, rest")
        Runnable {
            itSelfClient.post()
        }.run()
    }
}