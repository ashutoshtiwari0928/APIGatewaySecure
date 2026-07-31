package com.chatflow.apigateway.APIGatewayChatFlow;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class DebugGatewayFilter implements GlobalFilter{
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("Gateway received: " +
                exchange.getRequest().getMethod()+" "+
                exchange.getRequest().getURI());
        return chain.filter(exchange);
    }
}
