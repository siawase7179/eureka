package com.example.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.feign.model.AccountInfo;

@FeignClient(value = "auth-server", url = "${feign.auth-server.url:}")
public interface AuthorizeService {

	@RequestMapping(path = "/auth", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public AccountInfo auth(@RequestBody AccountInfo accountInfo);
}
