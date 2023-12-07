package com.example.resource;


import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.AccountInfo;
import com.example.model.ApiResponse;
import com.example.model.TokenInfo;
import com.example.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
public class AuthorizeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizeController.class);    
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private TokenService tokenService;

    @ResponseBody
	@RequestMapping(method=RequestMethod.POST,
					produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
					value="/auth")
    public ResponseEntity<Object> requestToken(HttpServletRequest request, @RequestBody AccountInfo accountInfo) throws Exception{
       
        LOGGER.info("clientId:{}, clientPassword:{}", accountInfo.getClientId(), accountInfo.getClientPassword());

        TokenInfo tokenInfo = tokenService.makeToken(accountInfo.getClientId(), accountInfo.getClientPassword());
        
        return new ResponseEntity<Object>(mapper.writeValueAsString(tokenInfo), HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST,
                    produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
                    value = "/token")
    public ResponseEntity<Object> validateToken(@RequestHeader("Authorization") String token){
        if (tokenService.checkValidToken(token) == false){
            return new ResponseEntity<Object>(ApiResponse.builder()
                                                        .code("90003")
                                                        .message("Token Error")
                                                        .status(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
                                                        .build(), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<Object>(HttpStatus.OK);
    }
}
