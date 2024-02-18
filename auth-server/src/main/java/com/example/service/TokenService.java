package com.example.service;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.config.Config;
import com.example.model.TokenInfo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Component
public class TokenService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);

	private final Config config;
    
    @Autowired
    public TokenService(Config config){
        this.config = config;
    }
	
    private byte[] keyBytes = "ThisisTokenSecretKey!!!!!!!!!!!!".getBytes();

	public synchronized TokenInfo makeToken(String id, String password){
        Key secretKey = new SecretKeySpec(keyBytes, io.jsonwebtoken.SignatureAlgorithm.HS256.getJcaName());
        
        Date expiration = new Date(System.currentTimeMillis() + 3600000);

        String jwtToken = Jwts.builder()
                .claim("id", id)
                .setExpiration(expiration)
                .signWith(secretKey)
                .compact();

        return TokenInfo.builder().token(jwtToken).expiry(config.getExpiry()).build();
	}
	
	public synchronized boolean checkValidToken(String _token) {
        if (_token.startsWith("Bearer ")) {
            String token = _token.substring(7);
            try {
                Key secretKey = new SecretKeySpec(keyBytes, io.jsonwebtoken.SignatureAlgorithm.HS256.getJcaName());
                Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
                Claims claims = claimsJws.getBody();
                LOGGER.error("Token id:{}, password:{}, expireTime:{} ", claims.get("id", String.class), claims.getExpiration());
            } catch (ExpiredJwtException e) {
                LOGGER.error("Expired Token ", e);
                return false;
            } catch (JwtException e) {
                LOGGER.error("error", e);
                return false;
            }
            return true;
        }else{
            return false;
        }
        
		
	}
}
