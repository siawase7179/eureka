package com.example.feign.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class TokenInfo {
    private String token;
    private long expiry;
}
