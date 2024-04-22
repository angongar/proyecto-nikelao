package com.tonigdev.web.nikelao.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class APIAuthService implements IAPIAuthService{

	private final RestTemplate restTemplate;
	
	@Override
	public ResponseEntity<String> login(HttpEntity<String> request) {
		return restTemplate.exchange(
                "http://localhost:8008/api-auth/login",
                HttpMethod.POST,
                request,
                String.class
        );
	}

}
