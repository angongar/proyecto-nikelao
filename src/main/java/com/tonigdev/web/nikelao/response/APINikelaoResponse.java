package com.tonigdev.web.nikelao.response;

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter @RequiredArgsConstructor
public class APINikelaoResponse<T> {
	
	private HttpStatus status;
	private String message;
	private List<T> results;

}
