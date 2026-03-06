package org.revature.apigateway.config;

import lombok.RequiredArgsConstructor;
import org.revature.apigateway.filter.JwtAuthGatewayFilter;
import org.revature.apigateway.filter.RequestLoggingFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ApiGatewayRoutesConfig {

    private final JwtAuthGatewayFilter jwtAuthGatewayFilter;
    private final RequestLoggingFilter requestLoggingFilter;

    @Bean
    public RouteLocator universityRoutes(RouteLocatorBuilder builder) {
      return builder.routes()
      // ----AUTH ROUTES (PUBLIC - no JWT needed -------
              .route("student-auth", r -> r
                      .path("/api/v1/auth/**")
              //lb: load balanced - discovers from Eureka by name
                      //"student-service must match apring.application.name
                              .uri("lb://student-service")
              )
              // ---STUDENT SERVICE ROUTES ------
              .route("student-service",r -> r
                      .path("/api/v1/students/**")
                      .filters(f -> f
                      //Apply Jwt validation filter
                                      .filter(jwtAuthGatewayFilter.apply(new JwtAuthGatewayFilter.Config()))
                              //Apply logging filter
                                      .filter(requestLoggingFilter.apply(new RequestLoggingFilter.Config()))
                              //Strip first path segment? No - keep /api/v1/stiudents as-is
                              //Add request header so student-service knows it came via gayeway
                                      .addRequestHeader("X-Gateway-Source", "university-gateway")
                              //Add response header
                                      .addResponseHeader("X-Service", "student-service")
                      )
                      .uri("lb://student-service")
              )
              // ---COURSE SERVICE ROUTES ------
              .route("course-service",r -> r
                      .path("/api/v1/courses/**")
                      .filters(f -> f
                              //Apply Jwt validation filter
                              .filter(jwtAuthGatewayFilter.apply(new JwtAuthGatewayFilter.Config()))
                              //Apply logging filter
                              .filter(requestLoggingFilter.apply(new RequestLoggingFilter.Config()))
                              //Strip first path segment? No - keep /api/v1/courses as-is
                              //Add request header so course-service knows it came via gateway
                              .addRequestHeader("X-Gateway-Source", "university-gateway")
                              //Add response header
                              .addResponseHeader("X-Service", "course-service")
                      )
                      .uri("lb://course-service")
              )
              .build();

    }

}
