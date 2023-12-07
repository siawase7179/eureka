package com.example.feign.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class TokenInfo {
    private String token;
    private long expiry;
}
