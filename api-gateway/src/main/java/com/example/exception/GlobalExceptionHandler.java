package com.example.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import com.example.vo.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Order(-1)
@RequiredArgsConstructor
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final ObjectMapper objectMapper;
    
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        // TODO Auto-generated method stub

        LOGGER.error("error", ex);

        ApiResponse apiResponse = null;
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        
        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        try{
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            if (ex instanceof AuthenticateException){
                response.setStatusCode(((AuthenticateException)ex).getStatusCode());

                apiResponse = ApiResponse.builder().status(((AuthenticateException)ex).getStatusCode().value())
                            .code(((AuthenticateException)ex).getCode())
                            .message(((AuthenticateException)ex).getMessage())
                            .build();
            }else if (ex instanceof ResponseStatusException){
                response.setStatusCode(((ResponseStatusException)ex).getStatusCode());

                apiResponse = ApiResponse.builder()
                        .status(((ResponseStatusException)ex).getStatusCode().value())
                        .code("90001")
                        .message("No handler found for " + exchange.getRequest().getMethod().toString() + " " + exchange.getRequest().getPath().toString())
                        .build();
            }else if (ex instanceof NotFoundException){
                apiResponse = ApiResponse.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .code("99999")
                        .message("server error")
                        .build();
            }
            
            return write(response, apiResponse);
            
        }catch (Exception e){
            LOGGER.error("error", e);
        }
        
        return response.writeWith(Flux.empty());
    }

    private Mono<Void> write(ServerHttpResponse response, ApiResponse apiResponse) throws JsonProcessingException{
        if (apiResponse == null){
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return response.writeWith(Flux.empty());
        }
        
        byte[] bytes = objectMapper.writeValueAsBytes(apiResponse);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);

        return response.writeWith(Flux.just(buffer)).then(Mono.fromRunnable(() -> {
            LOGGER.error("response: {}", new String(bytes));
        }));
    }
}
