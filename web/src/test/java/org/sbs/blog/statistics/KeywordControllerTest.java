package org.sbs.blog.statistics;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.sbs.blog.statistics.entity.Keyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@WebMvcTest(value = KeywordController.class)
class KeywordControllerTest {

	@Autowired
	ObjectMapper MAPPER;

	@Autowired
	MockMvc mockMvc;

	@MockBean
	KeywordService keywordService;

	List<Keyword> mockResult;

	@PostConstruct
	void init() throws IOException {
		mockResult = MAPPER.readValue(new ClassPathResource("/popular-keyword-res.json").getFile(),
				new TypeReference<List<Keyword>>() {
				});
	}

	@Test
	void popular_keyword() throws Exception {
		Mockito.when(keywordService.getPopularKeywords(anyInt())).thenReturn(mockResult);

		mockMvc.perform(MockMvcRequestBuilders.get("/v1/statistics/keyword/popular"))
		       .andDo(MockMvcResultHandlers.print())
		       .andExpect(MockMvcResultMatchers.status().isOk())
		       .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
		       .andExpect(MockMvcResultMatchers.content()
		                                       .json(MAPPER.writeValueAsString(mockResult)));

		Mockito.verify(keywordService, times(1)).getPopularKeywords(eq(10));
	}

	@Test
	void popular_keyword_specify() throws Exception {
		Mockito.when(keywordService.getPopularKeywords(anyInt())).thenReturn(mockResult);

		var param = new LinkedMultiValueMap<String, String>();
		param.add("size", "5");

		mockMvc.perform(MockMvcRequestBuilders.get("/v1/statistics/keyword/popular")
		                                      .params(param))
		       .andDo(MockMvcResultHandlers.print())
		       .andExpect(MockMvcResultMatchers.status().isOk())
		       .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
		       .andExpect(MockMvcResultMatchers.content()
		                                       .json(MAPPER.writeValueAsString(mockResult)));

		Mockito.verify(keywordService, times(1)).getPopularKeywords(eq(5));
	}

	@Test
	void bad_request() throws Exception {
		var param = new LinkedMultiValueMap<String, String>();
		param.add("size", "50");
		mockMvc.perform(MockMvcRequestBuilders.get("/v1/statistics/keyword/popular")
		                                      .params(param))
		       .andDo(MockMvcResultHandlers.print())
		       .andExpect(MockMvcResultMatchers.status().isBadRequest());

		Mockito.verify(keywordService, never()).getPopularKeywords(anyInt());
	}
}