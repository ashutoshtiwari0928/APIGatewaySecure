package com.chatflow.apigateway.APIGatewayChatFlow.Filter;

import jakarta.ws.rs.core.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthenticationLoggingFilter implements WebFilter {

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            WebFilterChain chain) {

        String requestId = UUID.randomUUID().toString();

        String path = exchange.getRequest()
                .getURI()
                .getPath();

        String method = exchange.getRequest()
                .getMethod()
                .name();

        String authorization = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        log.info(
                "[{}] [REQUEST] {} {}",
                requestId,
                method,
                path
        );

        log.info(
                "[{}] [AUTH] Authorization header: {}",
                requestId,
                authorization == null ? "MISSING" : "PRESENT"
        );

        return chain.filter(exchange)
                .doOnSuccess(v -> {

                    log.info(
                            "[{}] [RESPONSE] status={}",
                            requestId,
                            exchange.getResponse()
                                    .getStatusCode()
                    );

                })
                .doOnError(error -> {

                    log.error(
                            "[{}] [ERROR] {}: {}",
                            requestId,
                            error.getClass().getSimpleName(),
                            error.getMessage(),
                            error
                    );

                });
    }
}