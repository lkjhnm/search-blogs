package org.sbs.blog.search;

import okhttp3.ResponseBody;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.sbs.blog.search.TestSearchConfiguration.MockSearchResult;
import org.sbs.blog.search.dto.SearchParam;
import org.sbs.blog.search.dto.SearchResult;
import org.sbs.blog.search.event.SearchEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import retrofit2.Response;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestSearchConfiguration.class)
@TestPropertySource(locations = "classpath:/application-test.properties")
class SearchServiceTest {

	@Autowired
	SearchService searchService;

	@SpyBean
	SearchEventPublisher eventPublisher;

	@MockBean
	@Qualifier("mockSearchableFirst")
	Searchable mockSearchableFirst;

	@MockBean
	@Qualifier("mockSearchableSecond")
	Searchable mockSearchableSecond;

	@Autowired
	SearchResult expectedResult;

	@Test
	void search() throws IOException {
		Mockito.when(mockSearchableFirst.search(any(SearchParam.class)))
		       .thenReturn(Response.success(new MockSearchResult()));
		SearchParam searchParam = mockParam();

		Assertions.assertEquals(expectedResult, searchService.search(searchParam));
		Mockito.verify(mockSearchableFirst, times(1)).search(eq(searchParam));
		Mockito.verify(mockSearchableSecond, times(0)).search(any());
		Mockito.verify(eventPublisher, times(1)).publish(eq(searchParam));
	}

	@Test
	void search_failover() throws IOException {
		Mockito.when(mockSearchableFirst.search(any(SearchParam.class)))
		       .thenReturn(Response.error(500, ResponseBody.create("error", null)));
		Mockito.when(mockSearchableSecond.search(any(SearchParam.class)))
		       .thenReturn(Response.success(new MockSearchResult()));
		SearchParam searchParam = mockParam();
		Assertions.assertEquals(expectedResult, searchService.search(searchParam));
		Mockito.verify(mockSearchableFirst, times(1)).search(eq(searchParam));
		Mockito.verify(mockSearchableSecond, times(1)).search(eq(searchParam));
	}


	private SearchParam mockParam() {
		return SearchParam.builder().size(5).page(5).query("test")
		                  .build();
	}
}