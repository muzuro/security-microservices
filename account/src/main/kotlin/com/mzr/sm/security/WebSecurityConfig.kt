package com.mzr.sm.security

import com.mzr.sm.security.service.ServiceAuthenticationProvider
import com.mzr.sm.security.service.ServiceTokenAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.stereotype.Component


@Component
class WebSecurityConfig(
    private val passwordEncoder: PasswordEncoder,
    private val serviceAuthenticationProvider: ServiceAuthenticationProvider,
) : WebSecurityConfigurerAdapter() {

    override fun configure(builder: AuthenticationManagerBuilder) {
        builder.authenticationProvider(serviceAuthenticationProvider)
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .addFilterAfter(
                ServiceTokenAuthenticationFilter(serviceAuthenticationProvider),
                BasicAuthenticationFilter::class.java)
            .cors()
            .and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests{ configurer ->
                    configurer
                        .antMatchers(
                            "/error",
                            "/login"
                        )
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                }
            .oauth2ResourceServer { obj: OAuth2ResourceServerConfigurer<HttpSecurity?> -> obj.jwt() }
    }

    @Bean
    override fun userDetailsService(): UserDetailsService {
        val user1 = User
            .withUsername("user@mail.com")
            .authorities("USER")
            .passwordEncoder { rawPassword: String? -> passwordEncoder.encode(rawPassword) }
            .password("1234")
            .build()
        val userDetailsManager = InMemoryUserDetailsManager()
        userDetailsManager.createUser(user1)
        return userDetailsManager
    }
}