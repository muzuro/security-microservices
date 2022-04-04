package com.mzr.sm.controller

import mu.KotlinLogging
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
class NotificationController() {

    private val logger = KotlinLogging.logger {  }

    @PostMapping("/verifyEmail")
    fun verifyEmail(@RequestHeader("Authorization") authHeader: String) {
        val headers = HttpHeaders()
        headers.set("Authorization", authHeader)

        val restTemplate = RestTemplate()
        val response = restTemplate.exchange(
            "http://localhost:8087/internal/userDetails",
            HttpMethod.GET,
            HttpEntity<Any>(headers),
            object : ParameterizedTypeReference<String>() {})
        logger.info { "TODO: sent verify email to ${response.body}" }
    }
}