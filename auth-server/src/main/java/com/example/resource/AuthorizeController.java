package com.example.resource;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.AccountInfo;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class AuthorizeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizeController.class);    

    @ResponseBody
	@RequestMapping(method=RequestMethod.POST,
					produces = MediaType.APPLICATION_JSON_VALUE,
					value="/auth")
    public ResponseEntity<Object> requestToken(HttpServletRequest request, @RequestBody AccountInfo accountInfo) throws Exception{

        LOGGER.info("clientId:{}, clientPassword:{}", accountInfo.getClientId(), accountInfo.getClientPassword());
        
        return new ResponseEntity<Object>(HttpStatus.OK);
    }
}
