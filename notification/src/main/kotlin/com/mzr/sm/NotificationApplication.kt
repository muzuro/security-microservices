package com.mzr.sm

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class NotificationApplication

fun main(args: Array<String>) {
    SpringApplication.run(NotificationApplication::class.java, *args)
}