package org.sbs.blog.statistics;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.sbs.blog.search.dto.SearchParam;
import org.sbs.blog.search.event.SearchEvent;
import org.sbs.blog.statistics.entity.Keyword;
import org.sbs.blog.statistics.repo.KeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;

@DataJpaTest
@ContextConfiguration(classes = TestKeywordConfiguration.class)
@AutoConfigureTestDatabase(replace =  AutoConfigureTestDatabase.Replace.NONE)
class KeywordServiceTest {

	@SpyBean
	KeywordService keywordService;

	@SpyBean
	KeywordRepository keywordRepository;

	@Autowired
	ApplicationEventPublisher eventPublisher;

	@Test
	void subscribe() {
		SearchEvent event = new SearchEvent(this, mockParam("test"));
		eventPublisher.publishEvent(event);

		Mockito.verify(keywordService, times(1)).subscribe(event);
		Mockito.verify(keywordRepository, times(1))
		       .save(argThat(arg -> arg.getCount() == 1l && "test".equals(arg.getKeyword())));
	}

	@Test
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
	void get_top_keywords() {
		Stream.of(mockParams("first", 10),
				      mockParams("third", 5),
				      mockParams("second", 9))
		      .flatMap(List::stream)
		      .parallel()
		      .map(v -> new SearchEvent(this, v))
		      .forEach(this.eventPublisher::publishEvent);

		List<Keyword> topKeywords = keywordService.getPopularKeywords(10);
		Assertions.assertEquals(3, topKeywords.size());
		Keyword first = topKeywords.get(0);
		Keyword second = topKeywords.get(1);
		Keyword third = topKeywords.get(2);
		Assertions.assertEquals("first", first.getKeyword());
		Assertions.assertEquals(10, first.getCount());
		Assertions.assertEquals("second", second.getKeyword());
		Assertions.assertEquals(9, second.getCount());
		Assertions.assertEquals("third", third.getKeyword());
		Assertions.assertEquals(5, third.getCount());
	}

	private List<SearchParam> mockParams(String query, int size) {
		return IntStream.range(0, size)
		                .mapToObj(unused -> mockParam(query))
		                .collect(Collectors.toList());
	}

	private SearchParam mockParam(String query) {
		return SearchParam.builder()
		                  .query(query)
		                  .build();
	}

}