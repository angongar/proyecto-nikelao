package com.tonigdev.web.nikelao.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tonigdev.web.nikelao.services.APINikelaoService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class DatesController {
	
	private final APINikelaoService service;
	
	@GetMapping("/dates")
	public String dates(HttpSession session) {
		
		if(session.getAttribute("user") == null) {
			return "redirect:/login";
		}
		return "comun/dates";
	}
	
	@GetMapping("/datestoday")
	public String datestoday(HttpSession session) {
		
		if(session.getAttribute("user") == null) {
			return "redirect:/login";
		}
		return "admin/datestoday";
	}
	

}
