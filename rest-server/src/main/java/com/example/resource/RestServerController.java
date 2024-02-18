package com.example.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.exception.RestException;
import com.example.feign.AuthorizeService;
import com.example.feign.model.AccountInfo;
import com.example.feign.model.TokenInfo;
import com.example.vo.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/v1")
public class RestServerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestServerController.class);
    private final AuthorizeService authorizeService;
    private ObjectMapper mapper = new ObjectMapper();
    
    @Autowired
    public RestServerController(AuthorizeService authorizeService){
        this.authorizeService = authorizeService;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST,
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    value = "/request"
                    )
    public ResponseEntity<Object> requstMessage(@RequestHeader("Authorization")String token) throws JsonProcessingException{
        ApiResponse response = authorizeService.validateToken(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @ResponseBody
	@RequestMapping(method=RequestMethod.POST,
					produces = MediaType.APPLICATION_JSON_VALUE,
                    value="/token"
					)
    public ResponseEntity<Object> requestToken(HttpServletRequest request, @RequestHeader("X-Client-Id") String clientId, @RequestHeader("X-Client-Password")String clientPassword) throws Exception{
        if (clientId == null || clientId.length() <= 0){
            throw new RestException(HttpStatus.BAD_REQUEST, "90003", "X-Client-Id not set");
        }
        if (clientPassword == null || clientPassword.length() <= 0){
            throw new RestException(HttpStatus.BAD_REQUEST,"90004", "Client-Password not set");
        }

        TokenInfo tokenInfo = authorizeService.requestToken(AccountInfo.builder()
                                                                            .clientId(clientId)
                                                                            .clientPassword(clientPassword)
                                                                            .build()
                                                                );

        LOGGER.info("remote:{} clientId:{}, clientPassword:{}", request.getRemoteHost(), clientId, clientPassword);
        return new ResponseEntity<>(mapper.writeValueAsString(tokenInfo), HttpStatus.OK);
    }
}
