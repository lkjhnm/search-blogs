package org.sbs.blog.search.kakako;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.sbs.blog.search.dto.SearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import retrofit2.Response;
import retrofit2.mock.Calls;

import java.io.IOException;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestKakaoConfiguration.class)
@TestPropertySource(properties = {"sbs.key.kakao=test-kakao-rest-key"})
class KakaoServiceTest {

	@Autowired
	KakaoService kakaoService;

	@MockBean
	KakaoApis kakaoApis;

	@Autowired
	KakaoSearchResult mockSearchResult;

	@Autowired
	KakaoSearchResult expected;

	@Test
	public void search() throws IOException {
		Mockito.when(kakaoApis.search(anyString(), anyMap()))
		       .thenReturn(Calls.response(Response.success(mockSearchResult)));

		Response<KakaoSearchResult> response = kakaoService.search(mockParam());
		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertEquals(expected, response.body());

		var expectedParam =
				Map.of("query", "test",
						"page", "1",
						"size", "10",
						"sort", "accuracy");

		Mockito.verify(kakaoApis, times(1))
		       .search(eq("test-kakao-rest-key"), eq(expectedParam));
	}

	private SearchParam mockParam() {
		return SearchParam.builder()
		                  .query("test").page(1).size(10)
		                  .build();
	}
}