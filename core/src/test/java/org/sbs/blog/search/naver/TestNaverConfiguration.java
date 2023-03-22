package org.sbs.blog.search.naver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.sbs.blog.search.SearchConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Import({SearchConfiguration.class, NaverService.class})
public class TestNaverConfiguration {

	ObjectMapper objectMapper = new ObjectMapper();

	static final String X_NAVER_CLIENT_ID = "X-Naver-Client-Id";

	static final String X_NAVER_CLIENT_SECRET = "X-Naver-Client-Secret";

	@Bean
	String mockResponse() throws IOException {
		return Files.readAllLines(new ClassPathResource("naver-search-res.json").getFile().toPath())
		            .stream().collect(Collectors.joining());
	}

	@Bean
	NaverSearchResult mockSearchResult() throws IOException {
		return objectMapper.readValue(mockResponse(), NaverSearchResult.class);
	}

	@Bean
	NaverSearchResult expected() {
		NaverSearchResult expected = new NaverSearchResult();
		expected.setLastBuildDate("Sun, 19 Mar 2023 14:23:11 +0900");
		expected.setTotal(1);
		expected.setStart(1);
		expected.setDisplay(1);
		expected.setItems(List.of(expectedItem()));
		return expected;
	}

	private NaverSearchResult.Item expectedItem() {
		NaverSearchResult.Item item = new NaverSearchResult.Item();
		item.setTitle("(협찬X) 콜튼 무선 자동차 고압세차기 휴대용 충전 <b>리뷰</b>.");
		item.setDescription("- 자동차 무선 고압 세차기는 처음 구매해서 잘 모르는데 <b>리뷰</b> 협찬받지 않고 <b>리뷰</b>한 괜찮은 고압 자동차 무선 세차기를 찾는 분들 - 내구성 좋고 잔고장이 없는 고압 무선 자동차 세차기를 찾는 분.(이건 좀 더...");
		item.setBloggername("Pet Review");
		item.setLink("https://blog.naver.com/wedcats/223048213107");
		item.setBloggerlink("blog.naver.com/wedcats");
		item.setPostdate("20230318");
		return item;
	}
}
