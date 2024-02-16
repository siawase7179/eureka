package com.example.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.feign.model.AccountInfo;
import com.example.feign.model.TokenInfo;
import com.example.vo.ApiResponse;

@FeignClient(value = "auth-server")
public interface AuthorizeService {

	@RequestMapping(path = "/auth/", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public TokenInfo requestToken(@RequestBody AccountInfo accountInfo);

	@RequestMapping(path = "/token", method=RequestMethod.POST)
	public ApiResponse validateToken(@RequestHeader("Authorization") String token);
}
