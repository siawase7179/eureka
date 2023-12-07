package com.example.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.config.Config;
import com.example.model.TokenInfo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Repository
public class TokenService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);

    @Autowired
	private Config config;
	
    private String secretKey = "EurekaTokenServer!!!!!";

	public synchronized TokenInfo makeToken(String id, String password){
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + 1000 * config.getExpiry()); // 1시간
        
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        
        Map<String, Object> headerMap = new HashMap<String, Object>();

        headerMap.put("typ","JWT");
        headerMap.put("alg","HS256");

        Map<String, Object> map= new HashMap<String, Object>();
        map.put("id", id);
        map.put("password", password);

        JwtBuilder builder = Jwts.builder().setHeader(headerMap)
                .setClaims(map)
                .setExpiration(expirationDate)
                .signWith(signatureAlgorithm, signingKey);

        return TokenInfo.builder().token(builder.compact()).expiry(config.getExpiry()).build();
	}
	
	public synchronized boolean checkValidToken(String _token) {
        if (_token.startsWith("Bearer ")) {
            String token = _token.substring(7);
            Claims claims =null;
            try {
                claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey)).parseClaimsJws(token).getBody();
                LOGGER.error("Token id:{}, password:{}, expireTime:{} ", claims.get("id"), claims.get("password"), claims.getExpiration());
            } catch (ExpiredJwtException e) {
                LOGGER.error("Expired Token id:{}, password:{}, expireTime:{} ", claims.get("id"), claims.get("password"), claims.getExpiration(), e);
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
