package com.mzr.sm.controller

import mu.KotlinLogging
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/internal")
class InternalController(
    val userDetailsService: UserDetailsService,
) {

    private val logger = KotlinLogging.logger {  }

    @GetMapping("/userDetails")
    fun getUser(authentication: Authentication): String {
        logger.info { "TODO: obtain user name for user ${authentication.name}" }
        return "John Doe"
    }

    @GetMapping("/users")
    fun getUsers(): List<String> {
        return listOf("user@mail.com")
    }

}