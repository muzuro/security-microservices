package com.mzr.sm.security.service

import org.springframework.security.authentication.AuthenticationProvider
import kotlin.Throws
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Component
import java.util.ArrayList

@Component
class ServiceAuthenticationProvider(
    @Value("\${app.security.service.token}")
    private val serviceToken: String,
) : AuthenticationProvider {

    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication {
        val name = authentication.name
        val password = authentication.credentials.toString()
        return if (isServiceTokenValid(authentication as ServiceAuthenticationToken)) {
            // use the credentials
            // and authenticate against the third-party system
            UsernamePasswordAuthenticationToken(name, password, ArrayList())
        } else {
            throw AuthenticationServiceException("Unknown service ${authentication.name}")
        }
    }

    private fun isServiceTokenValid(authentication: ServiceAuthenticationToken) = authentication.token == serviceToken

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == ServiceAuthenticationToken::class.java
    }
}