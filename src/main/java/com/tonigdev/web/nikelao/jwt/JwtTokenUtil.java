package com.tonigdev.web.nikelao.jwt;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class JwtTokenUtil {
	
	@Value("${auth.secret}")
	private String tokenSecret;
	
	@Value("${auth.expires}")
	private Long tokenExpiration;
	
	public Claims parseClaims(String token) {
		return Jwts.parser()
				.setSigningKey(tokenSecret)
				.parseClaimsJws(token)
				.getBody();
	}
	
	public Map<String, Object> decodeJwt(String jwtToken) throws IOException {
        String[] jwtParts = jwtToken.split("\\.");
        String jwtPayload = new String(Base64.getUrlDecoder().decode(jwtParts[1]));
        
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jwtPayload, new TypeReference<Map<String, Object>>() {});
    }

}
