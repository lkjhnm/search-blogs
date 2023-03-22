package org.sbs.blog.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.sbs.blog.search.dto.SearchParam;
import org.sbs.blog.search.dto.SearchResult;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@WebMvcTest(value = SearchController.class)
class SearchControllerTest {

	@Autowired
	ObjectMapper MAPPER;

	@Autowired
	MockMvc mockMvc;

	@MockBean
	SearchService searchService;

	SearchResult mockResult;

	@PostConstruct
	void init() throws IOException {
		mockResult = MAPPER.readValue(new ClassPathResource("/sbs-search-kakao-res.json").getFile(), SearchResult.class);
	}

	@Test
	void search_default() throws Exception {
		Mockito.when(searchService.search(any(SearchParam.class))).thenReturn(mockResult);
		var param = new LinkedMultiValueMap<String, String>();
		param.add("query", "test");

		mockMvc.perform(MockMvcRequestBuilders.get("/v1/search/blog")
		                                      .params(param))
		       .andDo(MockMvcResultHandlers.print())
		       .andExpect(MockMvcResultMatchers.status().isOk())
		       .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
		       .andExpect(MockMvcResultMatchers.content()
		                                       .json(MAPPER.writeValueAsString(mockResult)));

		Mockito.verify(searchService, times(1))
		       .search(
				       argThat(arg -> arg.getQuery().equals("test") &&
						       arg.getPage() == 1 && arg.getSize() == 10 &&
						       arg.getSort().equals(SearchParam.SortType.accuracy))
		       );
	}

	@Test
	void search_specify() throws Exception {
		Mockito.when(searchService.search(any(SearchParam.class))).thenReturn(mockResult);
		var param = new LinkedMultiValueMap<String, String>();
		param.add("query", "test");
		param.add("page", "10");
		param.add("size", "50");
		param.add("sort", "recency");

		mockMvc.perform(MockMvcRequestBuilders.get("/v1/search/blog")
		                                      .params(param))
		       .andDo(MockMvcResultHandlers.print())
		       .andExpect(MockMvcResultMatchers.status().isOk())
		       .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
		       .andExpect(MockMvcResultMatchers.content()
		                                       .json(MAPPER.writeValueAsString(mockResult)));

		Mockito.verify(searchService, times(1))
		       .search(
				       argThat(arg -> arg.getQuery().equals("test") &&
						       arg.getPage() == 10 && arg.getSize() == 50 &&
						       arg.getSort().equals(SearchParam.SortType.recency))
		       );
	}

	@Test
	void bad_request() throws Exception {
		var param = new LinkedMultiValueMap<String, String>();
		param.add("query", "test");
		param.add("page", "100");
		param.add("size", "500");
		param.add("sort", "recency");

		mockMvc.perform(MockMvcRequestBuilders.get("/v1/search/blog")
		                                      .params(param))
		       .andDo(MockMvcResultHandlers.print())
		       .andExpect(MockMvcResultMatchers.status().isBadRequest());

		Mockito.verify(searchService, never()).search(any());
	}

	@Test
	void no_such_blog() throws Exception {
		Mockito.when(searchService.search(any(SearchParam.class))).thenThrow(NoSuchBlogException.class);
		var param = new LinkedMultiValueMap<String, String>();
		param.add("query", "NO SUCH BLOG EXCEPTION");

		mockMvc.perform(MockMvcRequestBuilders.get("/v1/search/blog")
		                                      .params(param))
		       .andDo(MockMvcResultHandlers.print())
		       .andExpect(MockMvcResultMatchers.status().isOk())
		       .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
		       .andExpect(MockMvcResultMatchers.content()
		                                       .json(MAPPER.writeValueAsString(SearchResult.builder().build())));
	}

}