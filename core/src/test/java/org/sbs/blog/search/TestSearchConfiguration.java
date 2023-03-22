package org.sbs.blog.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Order;
import org.sbs.blog.search.dto.SearchParam;
import org.sbs.blog.search.dto.SearchResult;
import org.sbs.blog.search.event.SearchEventPublisher;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.TestPropertySource;
import retrofit2.Response;

import java.io.IOException;

@TestConfiguration
@Import({SearchService.class, SearchConfiguration.class, SearchEventPublisher.class})
@TestPropertySource(properties = "spring.jackson.serialization.WRITE_DATES_AS_TIMESTAMPS=false")
public class TestSearchConfiguration {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Bean
	@Order(1)
	Searchable mockSearchableFirst() {
		return new Searchable() {
			@Override
			public Response<MockSearchResult> search(SearchParam searchParam) throws IOException {
				return null;
			}
		};
	}

	@Bean
	@Order(2)
	Searchable mockSearchableSecond() {
		return new Searchable() {
			@Override
			public Response<MockSearchResult> search(SearchParam searchParam) throws IOException {
				return null;
			}
		};
	}

	@Bean
	SearchResult expectedResult() throws IOException {
		ClassPathResource resource = new ClassPathResource("sbs-search-res.json");
		return MAPPER.readValue(resource.getFile(), SearchResult.class);
	}

	static class MockSearchResult implements SearchResults {

		@Override
		public SearchResult result() {
			try {
				ClassPathResource resource = new ClassPathResource("sbs-search-res.json");
				return MAPPER.readValue(resource.getFile(), SearchResult.class);
			} catch (IOException e) {
				return null;
			}
		}
	}
}
