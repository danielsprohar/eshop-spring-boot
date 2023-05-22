package dev.sprohar.eshopgateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
// Spring Cloud Gateway is a Flux project
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity
                .csrf()
                .disable()
                .authorizeExchange(exchange ->
                        exchange.pathMatchers( "/eureka/**")
                                .permitAll()
                                .anyExchange()
                                .authenticated()
                )
                .oauth2ResourceServer(
                        ServerHttpSecurity.OAuth2ResourceServerSpec::jwt
                );

        return serverHttpSecurity.build();
    }
}
