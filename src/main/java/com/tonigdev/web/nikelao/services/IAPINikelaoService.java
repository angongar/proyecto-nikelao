package com.tonigdev.web.nikelao.services;

import com.tonigdev.web.nikelao.dto.NewsDto;
import com.tonigdev.web.nikelao.response.APINikelaoResponse;

public interface IAPINikelaoService {
	
	public APINikelaoResponse<NewsDto> getNews();

}
