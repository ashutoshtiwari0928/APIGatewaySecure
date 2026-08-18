
package com.chatflow.apigateway.APIGatewayChatFlow.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log =
            LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain) {

        System.out.println("========== GATEWAY FILTER EXECUTED ==========");

        String method = exchange.getRequest()
                .getMethod()
                .name();

        String path = exchange.getRequest()
                .getURI()
                .getPath();

        log.info("Incoming request: {} {}", method, path);

        return chain.filter(exchange)
                .doFinally(signal -> {

                    var status = exchange.getResponse()
                            .getStatusCode();

                    log.info(
                            "Response: {} {} {}",
                            method,
                            path,
                            status != null
                                    ? status.value()
                                    : "UNKNOWN"
                    );
                });
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}

