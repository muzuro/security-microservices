package com.mzr.sm.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.apache.commons.lang3.time.DateUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtHelper(
    @Value("\${app.security.jwt.secret}")
    private val jwtSecret: String
) {
    fun createJwtForClaims(subject: String, claims: Map<String, String>): String {
        val jwtBuilder = JWT.create().withSubject(subject)
        claims.forEach { (name: String, value: String) -> jwtBuilder.withClaim(name, value) }
        return jwtBuilder
            .withNotBefore(Date())
            .withExpiresAt(DateUtils.addDays(Date(), 1))
            .sign(Algorithm.HMAC256(jwtSecret))
    }
}