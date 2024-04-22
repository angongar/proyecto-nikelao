package com.tonigdev.web.nikelao.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginDto {
	
	@NotEmpty(message = "está vacío.")
	@Size(min = 5, max = 15, message = "debe tener entre 5 y 15 caracteres.")
	private String username;
	@NotEmpty(message = "está vacío.")
	@Size(min = 5, max = 20, message = "debe tener entre 6 y 20 caracteres.")
	private String pass;

}
