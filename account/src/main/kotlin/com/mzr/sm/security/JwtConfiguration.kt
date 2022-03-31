package com.mzr.sm.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


@Configuration
class JwtConfiguration(
    @Value("\${app.security.jwt.secret}")
    private val jwtSecret: String
) {

    @Bean
    fun jwtDecoder(): JwtDecoder {
        val key = jwtSecret.toByteArray()
        val originalKey: SecretKey = SecretKeySpec(key, 0, key.size, "AES")
        return NimbusJwtDecoder.withSecretKey(originalKey).build()
    }


}