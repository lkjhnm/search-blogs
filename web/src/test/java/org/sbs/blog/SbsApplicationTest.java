package org.sbs.blog;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.sbs.blog.search.dto.SearchParam;
import org.sbs.blog.search.dto.SearchResult;
import org.sbs.blog.search.kakako.KakaoSearchResult;
import org.sbs.blog.search.kakako.KakaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import retrofit2.Response;

import javax.annotation.PostConstruct;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc
class SbsApplicationTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper mapper;

	@MockBean
	KakaoService kakaoService;

	KakaoSearchResult kakaoSearchResult;

	SearchResult searchResult;

	@PostConstruct
	void init() throws IOException {
		kakaoSearchResult = mapper.readValue(
				new ClassPathResource("kakako-search-res.json").getFile(), KakaoSearchResult.class);
		searchResult = mapper.readValue(
				new ClassPathResource("sbs-search-res.json").getFile(), SearchResult.class);
	}

	@Test
	public void search_blog() throws Exception {
		Mockito.when(kakaoService.search(any(SearchParam.class))).thenReturn(Response.success(kakaoSearchResult));
		var param = new LinkedMultiValueMap<String, String>();
		param.add("query", "test");

		mockMvc.perform(MockMvcRequestBuilders.get("/v1/search/blog")
		                                      .params(param))
		       .andDo(MockMvcResultHandlers.print())
		       .andExpect(MockMvcResultMatchers.status().isOk())
		       .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
		       .andExpect(MockMvcResultMatchers.content()
		                                       .json(mapper.writeValueAsString(searchResult)));
	}

	@Test
	void popular_keyword() throws Exception {
		Mockito.when(kakaoService.search(any(SearchParam.class))).thenReturn(Response.success(kakaoSearchResult));
		var param = new LinkedMultiValueMap<String, String>();
		param.add("query", "test");

		mockMvc.perform(MockMvcRequestBuilders.get("/v1/search/blog")
		                                      .params(param))
		       .andExpect(MockMvcResultMatchers.status().isOk());

		mockMvc.perform(MockMvcRequestBuilders.get("/v1/statistics/keyword/popular"))
		       .andDo(MockMvcResultHandlers.print())
		       .andExpect(MockMvcResultMatchers.status().isOk())
		       .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
		       .andExpect(MockMvcResultMatchers.content()
		                                       .json("[\n" +
				                                       "  {\n" +
				                                       "    \"keyword\": \"test\",\n" +
				                                       "    \"count\": 1\n" +
				                                       "}\n" +
				                                       "]"));

	}
}