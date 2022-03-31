package com.mzr.sm.jwt

import com.auth0.jwt.JWT
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.time.Instant
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtHelper(
    @Value("\${app.security.jwt.secret}")
    private val jwtSecret: String
) {
    fun createJwtForClaims(subject: String?, claims: Map<String?, String?>): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = Instant.now().toEpochMilli()
        calendar.add(Calendar.DATE, 1)
        val jwtBuilder = JWT.create().withSubject(subject)

        // Add claims
        claims.forEach { (name: String?, value: String?) -> jwtBuilder.withClaim(name, value) }

        // Add expiredAt and etc
        return jwtBuilder
            .withNotBefore(Date())
            .withExpiresAt(calendar.time)
            .sign(Algorithm.HMAC256(jwtSecret))
    }
}