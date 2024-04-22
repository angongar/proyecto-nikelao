package com.tonigdev.web.nikelao.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tonigdev.web.nikelao.dto.LoginDto;
import com.tonigdev.web.nikelao.dto.UsuarioDto;
import com.tonigdev.web.nikelao.jwt.JwtTokenUtil;
import com.tonigdev.web.nikelao.request.APIAuthRequest;
import com.tonigdev.web.nikelao.response.APIAuthResponse;
import com.tonigdev.web.nikelao.services.IAPIAuthService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class LoginController {

	private final IAPIAuthService loginService;
	private final JwtTokenUtil jwtUtil;

	@GetMapping("/login")
	public String login(Model model) {

		model.addAttribute("userLogin", new LoginDto());
		return "login/login";

	}

	@PostMapping("/loginUser")
	public String login_post(@Valid LoginDto userLogin, BindingResult result, Model model, HttpSession session) {

		if (result.hasErrors()) {
			Map<String, String> errores = new HashMap<>();
			result.getFieldErrors().forEach(error -> {
				errores.put(error.getField(), "El campo " + error.getField() + " " + error.getDefaultMessage());
			});

			model.addAttribute("errores", errores);
			model.addAttribute("userLogin", userLogin);
			session.setAttribute("token", null);
			session.setAttribute("user", null);
			return "login/login";
		}

		APIAuthRequest apiRequest = new APIAuthRequest(userLogin.getUsername(), userLogin.getPass());

		// Crear el cuerpo de la solicitud con las credenciales del usuario
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<>(
				"{\"username\": \"" + userLogin.getUsername() + "\", \"pass\": \"" + userLogin.getPass() + "\"}",
				headers);

		ResponseEntity<String> response = loginService.login(request);

		// Verificar si la autenticación fue exitosa
		if (response.getStatusCode() == HttpStatus.OK) {

			try {
				// Almacenar el token JWT en la sesión del usuario
				String json = response.getBody();
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode rootNode;

				rootNode = objectMapper.readTree(json);

				JsonNode resultToken = rootNode.get("token");
				if (resultToken != null) {
					Map<String, Object> userData = jwtUtil.decodeJwt(resultToken.asText());
					UsuarioDto user = new UsuarioDto();
					user.setUsername((String) userData.get("username"));
					user.setId(((Integer)userData.get("id")).longValue());
					
					List<String> authorities = (List<String>) userData.get("authorities");
					user.setAuthorities(authorities);
					
					session.setAttribute("token", resultToken);
					session.setAttribute("user", user);
				}
			} catch (JsonMappingException e) {
				// Si la autenticación falla, mostrar un mensaje de error
				model.addAttribute("error", "Error mapeando el json");
				session.setAttribute("token", null);
				session.setAttribute("user", null);
				return "login/login";
			} catch (JsonProcessingException e) {
				// Si la autenticación falla, mostrar un mensaje de error
				model.addAttribute("error", "Error procesando el json");
				session.setAttribute("token", null);
				session.setAttribute("user", null);
				return "login/login";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Redirigir al usuario a la página de inicio
			return "redirect:/";
		} else {
			// Si la autenticación falla, mostrar un mensaje de error
			model.addAttribute("error", "Credenciales incorrectas");
			session.setAttribute("token", null);
			session.setAttribute("user", null);
			return "login/login";
		}
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {

		session.setAttribute("token", null);
		session.setAttribute("user", null);
		return "redirect:/";

	}
	

}
