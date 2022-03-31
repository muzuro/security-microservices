package com.mzr.sm.controller

import com.mzr.sm.domain.dto.LoginResult
import org.springframework.web.bind.annotation.RestController
import com.mzr.sm.jwt.JwtHelper
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.server.ResponseStatusException
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import java.util.HashMap
import org.springframework.security.core.GrantedAuthority
import java.util.stream.Collectors

@RestController
class AuthController(
    private val jwtHelper: JwtHelper,
    private val userDetailsService: UserDetailsService,
    private val passwordEncoder: PasswordEncoder
) {
    @PostMapping(path = ["login"], consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun login(
        @RequestParam username: String,
        @RequestParam password: String
    ): LoginResult {
        val userDetails: UserDetails
        userDetails = try {
            userDetailsService.loadUserByUsername(username)
        } catch (e: UsernameNotFoundException) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found")
        }
        if (passwordEncoder.matches(password, userDetails.password)) {
            val claims: MutableMap<String?, String?> = HashMap()
            claims["username"] = username
            val authorities = userDetails.authorities.stream()
                .map { obj: GrantedAuthority -> obj.authority }
                .collect(Collectors.joining(","))
            claims["authorities"] = authorities
            claims["userId"] = 1.toString()
            val jwt = jwtHelper.createJwtForClaims(username, claims)
            return LoginResult(jwt)
        }
        throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated")
    }
}