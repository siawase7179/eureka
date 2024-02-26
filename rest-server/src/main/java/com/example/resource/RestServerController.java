package com.example.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.feign.AuthorizeService;
import com.example.feign.model.AccountInfo;
import com.example.service.TokenService;
import com.example.vo.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class RestServerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestServerController.class);
    private ObjectMapper mapper = new ObjectMapper();

    private final AuthorizeService authorizeService;
    private final TokenService tokenService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST,
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    value = "/request"
                    )
    public ResponseEntity<Object> requstMessage(@RequestHeader("Authorization")String token) throws Exception{
        tokenService.checkValidToken(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @ResponseBody
	@RequestMapping(method=RequestMethod.POST,
					produces = MediaType.APPLICATION_JSON_VALUE,
                    value="/token"
					)
    public ResponseEntity<Object> requestToken(HttpServletRequest request,
                                               @RequestHeader("X-Client-Id") String clientId,
                                               @RequestHeader("X-Client-Password")String clientPassword) throws Exception{
        authorizeService.auth(
            AccountInfo.builder()
                .clientId(clientId)
                .clientPassword(clientPassword)
                .build()
        );

        LOGGER.info("remote:{} clientId:{}, clientPassword:{}", request.getRemoteHost(), clientId, clientPassword);
        return ResponseEntity.ok()
            .body(
                ApiResponse.builder()
                                .status(HttpStatus.OK.value())
                                .code("0000")
                                .message("success")
                                .data(
                                    tokenService.makeToken(clientId, clientPassword)
                                )
                                .build()
        ); 
    }
}
