package br.com.devcave.prometheus.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

@FeignClient("it-self", url = "http://localhost:1980")
interface ItSelfClient {
    @GetMapping("demo")
    fun get(): String

    @PostMapping("demo/second-endpoint")
    fun post(): String
}