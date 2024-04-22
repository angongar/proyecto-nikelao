package com.tonigdev.web.nikelao.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import com.tonigdev.web.nikelao.response.APIAuthResponse;

public interface IAPIAuthService {
	
	public ResponseEntity<String> login(HttpEntity<String> request);

}
