package com.example.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import com.example.exception.AuthenticateException;



@Component
public class AuthenticateFilter extends AbstractGatewayFilterFactory<AuthenticateFilter.Config> {    

    public AuthenticateFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if(!request.getHeaders().containsKey("X-Client-id")){
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                throw new AuthenticateException(HttpStatus.BAD_REQUEST, "90003", "X-Client-Id not set");
            }

            if(!request.getHeaders().containsKey("X-Client-Password")){
                throw new AuthenticateException(HttpStatus.BAD_REQUEST,"90004", "Client-Password not set");
            }

            return chain.filter(exchange) ;
        });
    }

    public static class Config {

    }
}