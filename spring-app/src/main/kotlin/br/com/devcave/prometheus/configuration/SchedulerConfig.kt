package br.com.devcave.prometheus.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.scheduling.config.ScheduledTaskRegistrar

@Configuration
class SchedulerConfig: SchedulingConfigurer {

    override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) {
        val scheduler = ThreadPoolTaskScheduler().also {
            it.poolSize = 10
            it.setThreadNamePrefix("scheduled-thread-")
            it.initialize()
        }
        taskRegistrar.setTaskScheduler(scheduler)
    }
}