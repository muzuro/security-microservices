package com.mzr.sm.job

import mu.KotlinLogging
import org.apache.commons.lang3.time.DateUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class DailyNotificationJob(
    @Value("\${app.security.service.token}")
    private val serviceToken: String,
) {

    private val logger = KotlinLogging.logger {  }

    @Scheduled(fixedDelay = 10 * DateUtils.MILLIS_PER_DAY)
    fun process() {
        val headers = HttpHeaders()
        headers.set("Authorization", "Service $serviceToken")

        val restTemplate = RestTemplate()
        val response = restTemplate.exchange(
            "http://localhost:8087/internal/users",
            HttpMethod.GET,
            HttpEntity<Any>(headers),
            object : ParameterizedTypeReference<List<String>>() {})
        logger.info { "TODO: notify user: ${response.body}" }
    }

}