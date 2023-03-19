package org.sbs.blog.search.kakako;

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
@Import({SearchConfiguration.class, KakaoService.class})
public class TestKakaoConfiguration {

	ObjectMapper objectMapper = new ObjectMapper();

	@Bean
	String mockResponse() throws IOException {
		return Files.readAllLines(new ClassPathResource("kakako-search-res.json").getFile().toPath())
		            .stream().collect(Collectors.joining());
	}

	@Bean
	KakaoSearchResult mockSearchResult() throws IOException {
		return objectMapper.readValue(mockResponse(), KakaoSearchResult.class);
	}

	@Bean
	KakaoSearchResult expected() {
		KakaoSearchResult expected = new KakaoSearchResult();
		expected.setMeta(expectedMeta());
		expected.setDocuments(List.of(expectedDocument()));
		return expected;
	}

	private KakaoSearchResult.Document expectedDocument() {
		KakaoSearchResult.Document document = new KakaoSearchResult.Document();
		document.setTitle("작은 <b>집</b> <b>짓기</b> 기본컨셉 - <b>집</b><b>짓기</b> 초기구상하기");
		document.setContents("이 점은 <b>집</b>을 지으면서 고민해보아야 한다. 하지만, 금액에 대한 가성비 대비 크게 문제되지 않을 부분이라 생각하여 설계로 극복하자고 생각했다. 전체 <b>집</b><b>짓기</b>의 기본방향은 크게 세 가지이다. 우선은 여가의 영역 증대이다. 현대 시대 일도 중요하지만, 여가시간 <b>집</b>에서 어떻게 보내느냐가 중요하니깐 이를 기본적...");
		document.setBlogname("정란수의 브런치");
		document.setUrl("https://brunch.co.kr/@tourism/91");
		document.setThumbnail("http://search3.kakaocdn.net/argon/130x130_85_c/7r6ygzbvBDc");
		document.setDatetime("2017-05-07T18:50:07.000+09:00");
		return document;
	}

	private KakaoSearchResult.Meta expectedMeta() {
		KakaoSearchResult.Meta meta = new KakaoSearchResult.Meta();
		meta.setEnd(true);
		meta.setPageableCount(1);
		meta.setTotalCount(1);
		return meta;
	}
}
