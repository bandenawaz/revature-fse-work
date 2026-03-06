package org.revature.apigateway.filter;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
@Component
public class JwtAuthGatewayFilter extends AbstractGatewayFilterFactory<JwtAuthGatewayFilter.Config>{

    @Value("${jwt.secret}")
    private String secretKey;

    //Public paths that don't need JWT
    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/actuator/health",
            "/actuator/info"
    );

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getPath().value();

            //SKIP JWT check for public paths
            if (PUBLIC_PATHS.stream().anyMatch(path::startsWith)) {
                return chain.filter(exchange);
            }

            //Check Authorization Header exists
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return unauthorizedResponse(exchange,"Missing Authorixation Header");

            }

            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return unauthorizedResponse(exchange,"Invalid Authorization Format");
            }

            String token = authHeader.substring(7);
            try{
                //Validate the token
                Claims claims = Jwts.parser()
                        .verifyWith(getSigningKey())
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();
                //Extract user info from claims
                String userEmail = claims.getSubject();
                String role = claims.get("role", String.class);

                /**
                 * Mutate the request - add user info as header
                 * So downstream services know WHO is making the request
                 * Without parsing the JWT
                 */
                ServerHttpRequest mutatedRequest = request.mutate()
                        .header("X-User-Email", userEmail)
                        .header("X-User-Role", role).build();

                return chain.filter(exchange.mutate().request(mutatedRequest).build());
            }catch (ExpiredJwtException e){
                return unauthorizedResponse(exchange,"Expired JWT Token");
            }catch (JwtException jwtException){
                return unauthorizedResponse(exchange,"invalid JWT Token");
            }
        };
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json");
        byte[] bytes = ("{\"error\":\"" + message + "\"}").getBytes();
        return response.writeWith(
                Mono.just(response.bufferFactory().wrap(bytes))
        );
    }


    public static class Config{
        //Placeholder- add filter-leve; config here if needed
    }
}
