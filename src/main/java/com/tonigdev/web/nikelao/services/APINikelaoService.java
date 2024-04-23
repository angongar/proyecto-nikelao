package com.tonigdev.web.nikelao.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tonigdev.web.nikelao.dto.NewsDto;
import com.tonigdev.web.nikelao.response.APINikelaoResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class APINikelaoService implements IAPINikelaoService {

	private final RestTemplate restTemplate;

	@Override
	public APINikelaoResponse<NewsDto> getNews() {
		APINikelaoResponse<NewsDto> result = new APINikelaoResponse<>();
		List<NewsDto> listNews;
		ResponseEntity<String> response = restTemplate
				.getForEntity("http://localhost:8010/api-nikelao/news", String.class);
		
		
		try {
			String json = response.getBody();
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(json);
			
			JsonNode statusNode = rootNode.get("status");
			if(statusNode != null && statusNode.isTextual()) {
				//result.setStatus());
			}
			
			JsonNode messageNode = rootNode.get("message");
			if(messageNode != null && messageNode.isTextual()) {
				result.setMessage(messageNode.asText());
			}
			
			
			JsonNode resultsNode = rootNode.get("results");
			if(resultsNode != null && resultsNode.isArray()) {
				listNews = new ArrayList<>();
				for(JsonNode node: resultsNode) {
					NewsDto object = objectMapper.treeToValue(node, NewsDto.class);
					listNews.add(object);
				}
				result.setResults(listNews);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

}
