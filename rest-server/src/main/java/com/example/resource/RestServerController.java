package com.example.resource;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.exception.RestException;
import com.example.feign.AuthorizeService;
import com.example.feign.model.AccountInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class RestServerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestServerController.class);
    @Autowired
    private AuthorizeService authorizeService;

    @ResponseBody
	@RequestMapping(
        method = RequestMethod.POST,
        value="/**"
    )
	public ResponseEntity<HttpStatus> requsetErrorPath(HttpServletRequest request) {
		LOGGER.error("{}, No handler found for {} {}", request.getRemoteHost(), request.getMethod(), request.getRequestURI());
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
    
    @ResponseBody
	@RequestMapping(method=RequestMethod.POST,
					produces = MediaType.APPLICATION_JSON_VALUE,
                    value="/token"
					)
    public ResponseEntity<Object> request(HttpServletRequest request) throws Exception{
        String clientId = request.getHeader("Client-Id");
        String clientPassword = request.getHeader("Client-Password");

        if (clientId == null || clientId.length() <= 0){
            throw new RestException(HttpStatus.BAD_REQUEST, "90001", "Client-Id not set");
        }
        if (clientPassword == null || clientPassword.length() <= 0){
            throw new RestException(HttpStatus.BAD_REQUEST,"90002", "Client-Password not set");
        }

        AccountInfo accountInfo = getAccountInfo(clientId, clientPassword);
                
        ObjectMapper objectMapper = new ObjectMapper();
        String response = objectMapper.writeValueAsString(accountInfo);

        LOGGER.info("remote:{}:{} clientId:{}, clientPassword:{}", request.getRemoteAddr(), request.getRemotePort(), clientId, clientPassword);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    public AccountInfo getAccountInfo(String clientId, String clientPassword){
        return authorizeService.getAccount(clientId, clientPassword);
    }
}
