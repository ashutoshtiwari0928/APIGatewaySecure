package com.chatflow.apigateway.APIGatewayChatFlow.Config;

import com.chatflow.apigateway.APIGatewayChatFlow.Utility.RsaKeyLoader;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.BCRSAPublicKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.ProviderManagerBuilder;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtReactiveAuthenticationManager;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) throws Exception {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(
                        exchange-> exchange
                                .pathMatchers("/api/secure/auth/**").permitAll()
                                .pathMatchers("/api/secure/swagger-ui.html").permitAll()
                                .pathMatchers("/acutator/info").permitAll()
                                .anyExchange().authenticated()
                )
                .oauth2ResourceServer(
                        oAuth2ResourceServerSpec ->
                                oAuth2ResourceServerSpec
                                        .jwt(
                                                jwtSpec ->
                                                {
                                                    try {
                                                        jwtSpec.authenticationManager(jwtReactiveAuthenticationManager());
                                                    } catch (Exception e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                }
                                        )
                )
                .build();
    }

    @Bean
    public JwtReactiveAuthenticationManager jwtReactiveAuthenticationManager() throws Exception {
        return new  JwtReactiveAuthenticationManager(jwtDecoder());
    }
    @Bean
    public NimbusReactiveJwtDecoder jwtDecoder() throws Exception {
        return NimbusReactiveJwtDecoder
                .withPublicKey((RSAPublicKey) RsaKeyLoader
                        .loadPublicKey("jwt-keys/public.pem"))
                .build();
    }


}
