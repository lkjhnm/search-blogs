package org.sbs.blog;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.sbs.blog.search.dto.SearchParam;
import org.sbs.blog.search.dto.SearchResult;
import org.sbs.blog.search.kakako.KakaoSearchResult;
import org.sbs.blog.search.kakako.KakaoService;
import org.sbs.blog.search.naver.NaverSearchResult;
import org.sbs.blog.search.naver.NaverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
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

	@MockBean
	NaverService naverService;

	KakaoSearchResult kakaoSearchResult;

	SearchResult searchResultFromKakao;

	NaverSearchResult naverSearchResult;

	SearchResult searchResultFromNaver;

	@PostConstruct
	void init() throws IOException {
		kakaoSearchResult = mapper.readValue(
				new ClassPathResource("kakako-search-res.json").getFile(), KakaoSearchResult.class);
		searchResultFromKakao = mapper.readValue(
				new ClassPathResource("sbs-search-kakao-res.json").getFile(), SearchResult.class);
		naverSearchResult = mapper.readValue(
				new ClassPathResource("naver-search-res.json").getFile(), NaverSearchResult.class);
		searchResultFromNaver = mapper.readValue(
				new ClassPathResource("sbs-search-naver-res.json").getFile(), SearchResult.class);
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
		                                       .json(mapper.writeValueAsString(searchResultFromKakao)));
	}

	@Test
	public void search_blog_failover() throws Exception {
		Mockito.when(kakaoService.search(any(SearchParam.class)))
		       .thenReturn(Response.error(500, ResponseBody.create("error", null)));
		Mockito.when(naverService.search(any(SearchParam.class)))
				.thenReturn(Response.success(naverSearchResult));

		var param = new LinkedMultiValueMap<String, String>();
		param.add("query", "test");

		mockMvc.perform(MockMvcRequestBuilders.get("/v1/search/blog")
		                                      .params(param))
		       .andDo(MockMvcResultHandlers.print())
		       .andExpect(MockMvcResultMatchers.status().isOk())
		       .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
		       .andExpect(MockMvcResultMatchers.content()
		                                       .json(mapper.writeValueAsString(searchResultFromNaver)));
	}

	@Test
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
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