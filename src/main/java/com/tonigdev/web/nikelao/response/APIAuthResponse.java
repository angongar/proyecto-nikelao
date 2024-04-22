package com.tonigdev.web.nikelao.response;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter @RequiredArgsConstructor
public class APIAuthResponse {
	
	private HttpStatus status;
	private String message;
	private String token;

}
