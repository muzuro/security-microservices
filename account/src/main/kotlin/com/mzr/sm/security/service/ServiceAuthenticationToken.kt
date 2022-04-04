package com.mzr.sm.security.service

import org.springframework.security.authentication.AbstractAuthenticationToken

class ServiceAuthenticationToken(
    val token: String
): AbstractAuthenticationToken(emptyList()) {
    override fun getCredentials(): Any {
        return token
    }
    override fun getPrincipal(): Any {
        return token
    }
}