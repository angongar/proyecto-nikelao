package com.tonigdev.web.nikelao.dto;

import java.util.List;

import lombok.Data;

@Data
public class UsuarioDto {
	
	private Long id;
	private String username;
	private List<String> authorities;

}
