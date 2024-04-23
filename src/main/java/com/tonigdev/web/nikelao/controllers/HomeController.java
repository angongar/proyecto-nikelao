package com.tonigdev.web.nikelao.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tonigdev.web.nikelao.dto.NewsDto;
import com.tonigdev.web.nikelao.response.APINikelaoResponse;
import com.tonigdev.web.nikelao.services.IAPINikelaoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class HomeController {
	
	private final IAPINikelaoService service;

	@GetMapping("/")
	public String home(Model model) {
		APINikelaoResponse<NewsDto> apiResponse;
		
		apiResponse = service.getNews();
		
		if(apiResponse != null && apiResponse.getResults() != null) {
			model.addAttribute("news", apiResponse.getResults());
		}
		

		return "comun/home";
	}
	

}
