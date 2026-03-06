package org.revature.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class RequestLoggingFilter
            extends AbstractGatewayFilterFactory<RequestLoggingFilter.Config> {

    public RequestLoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            long startTime = System.currentTimeMillis();

            log.info("GATEWAY -> [{} {}] from IP: {} user: {}",
                    request.getMethod(),
                    request.getPath(),
                    request.getRemoteAddress(),
                    request.getHeaders().getFirst("X-User-Email"));

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
               long duration = System.currentTimeMillis() - startTime;
               log.info("GATEWAY <- [{} {}] status: {} took: {}ms",
                       request.getMethod(),
                       request.getPath(),
                       exchange.getResponse().getStatusCode(),
                       duration);
            }));
        });
    }

    public static class Config{}
}
