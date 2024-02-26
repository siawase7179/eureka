package com.example.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Configuration
public class Config {
    @Value("${auth.token.expiry:3600}")
	private int expiry;
}
