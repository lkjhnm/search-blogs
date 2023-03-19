package org.sbs.blog.search.kakako;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sbs.blog.search.dto.SearchResult;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

class KakaoSearchResultTest {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Test
	public void result() throws IOException {
		KakaoSearchResult kakaoSearchResult =
				MAPPER.readValue(new ClassPathResource("kakako-search-res.json").getFile(), KakaoSearchResult.class);
		SearchResult expected =
				MAPPER.readValue(new ClassPathResource("sbs-search-res.json").getFile(), SearchResult.class);
		Assertions.assertEquals(expected, kakaoSearchResult.result());
	}
}