package br.com.api_gateway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    private final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final WebClient.Builder webClient;

    public JwtAuthenticationFilter(WebClient.Builder webClient) {
        super(Config.class);
        this.webClient = webClient;
    }

    public  static  class  Config {
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getPath().value();

            if (path.startsWith("/api/auth/login") || path.startsWith("/api/clientes/create")) {
                return chain.filter(exchange);
            }

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                return validateToken(token)
                        .flatMap(username -> proceedWithUsername(username, exchange, chain))
                        .onErrorResume(e -> handleAuthenticationError(exchange, e));
            }

            return handleAuthenticationError(exchange, new RuntimeException("Authorization header is missing or invalid"));
        };
    }

    private Mono<String> validateToken(String token) {
        return webClient.baseUrl("http://ms-clientes:8080")
                .build()
                .post()
                .uri("/auth/validate-token")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(Collections.singletonMap("token", token))
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> response.get("username").toString());
    }

    private Mono<Void> proceedWithUsername(String username, ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(exchange.getRequest().mutate()
                        .header("X-USER", username)
                        .build())
                .build();

        return chain.filter(mutatedExchange);
    }

    public Mono<Void> handleAuthenticationError(ServerWebExchange exchange, Throwable e){
        log.error("Falha na autenticação: {}" , e.getMessage());
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }



}
