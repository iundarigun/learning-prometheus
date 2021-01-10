package br.com.devcave.prometheus.controller

import io.micrometer.core.annotation.Timed
import io.micrometer.core.instrument.Metrics
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.random.Random

@RestController
@RequestMapping("demo")
class PrometheusMetricsDemoController {
    private val getCounter = Metrics.counter("callToGet")
    private val postCounter = Metrics.counter("callToPost")

    @Timed
    @GetMapping
    fun get(): String {
        getCounter.increment()
        return "get demo"
    }

    @Timed(histogram = true)
    @PostMapping("second-endpoint")
    fun post(): String {
        postCounter.increment()
        val delay = Random.nextLong(0, 5)
        Thread.sleep(delay * 1000)
        return "post demo"
    }
}