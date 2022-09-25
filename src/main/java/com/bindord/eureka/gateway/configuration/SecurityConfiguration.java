package com.bindord.eureka.gateway.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Profile(value = "!nosec")
public class SecurityConfiguration {

    @Value("${service.ingress.context-path}")
    private String svcContextPath;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri:#{null}}")
    private String jwtIssuerURI;

    @Bean()
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.csrf().disable();
        http
                .authorizeExchange()
                .pathMatchers("/webjars/**").permitAll()
                .pathMatchers("/swagger**").permitAll()
                .pathMatchers("/v3/**").permitAll()
                .pathMatchers("/eureka/gateway/v1/auth**").permitAll()
                .pathMatchers("/eureka/gateway/v1/auth/**").permitAll()
                .pathMatchers("/actuator/**").permitAll()
                .anyExchange().authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(keycloakGrantedAuthoritiesConverter());
        return http.build();
    }

    @Bean
    public KeycloakGrantedAuthoritiesConverter keycloakGrantedAuthoritiesConverter() {
        return new KeycloakGrantedAuthoritiesConverter();
    }

}
