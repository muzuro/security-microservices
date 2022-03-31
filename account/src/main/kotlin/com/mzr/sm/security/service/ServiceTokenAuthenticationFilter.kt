package com.mzr.sm.security.service

import mu.KotlinLogging
import org.springframework.core.log.LogMessage
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.util.regex.Pattern
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class ServiceTokenAuthenticationFilter(
    val authenticationManager: ServiceAuthenticationProvider,
) : OncePerRequestFilter() {

    private val logger = KotlinLogging.logger {  }

    private val serviceTokenHeaderName = HttpHeaders.AUTHORIZATION

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader(this.serviceTokenHeaderName)
        if (!StringUtils.startsWithIgnoreCase(authorizationHeader, "service")) {
            //no
            filterChain.doFilter(request, response)
            return
        }
        val matcher = authorizationPattern.matcher(authorizationHeader)
        if (!matcher.matches()) {
            throw AuthenticationServiceException("Service token is malformed")
        }
        val token = matcher.group("token")

        try {
//            val authenticationManager = authenticationManagerResolver.resolve(request)
            val authenticationResult = authenticationManager.authenticate(ServiceAuthenticationToken(token))
            val context = SecurityContextHolder.createEmptyContext()
            context.authentication = authenticationResult
            SecurityContextHolder.setContext(context)
            if (logger.isDebugEnabled) {
                this.logger.debug(LogMessage.format("Set SecurityContextHolder to %s", authenticationResult))
            }
            filterChain.doFilter(request, response)
        } catch (failed: AuthenticationException) {
            SecurityContextHolder.clearContext()
            this.logger.trace("Failed to process authentication request", failed)
            authenticationEntryPoint.commence(request, response, failed)
        }
    }

    companion object {
        val authorizationPattern = Pattern.compile(
            "^Service (?<token>[a-zA-Z0-9-._~+/]+=*)$",
            Pattern.CASE_INSENSITIVE
        )
        val authenticationEntryPoint = AuthenticationEntryPoint {
                request, response, authException -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
        }
    }
}