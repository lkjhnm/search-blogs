package org.sbs.blog.search.naver;

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

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.sbs.blog.search.naver.TestNaverConfiguration.X_NAVER_CLIENT_ID;
import static org.sbs.blog.search.naver.TestNaverConfiguration.X_NAVER_CLIENT_SECRET;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestNaverConfiguration.class)
@TestPropertySource(locations = "classpath:/application-test.properties")
class NaverServiceTest {

	@Autowired
	NaverService naverService;

	@MockBean
	NaverApis naverApis;

	@Autowired
	NaverSearchResult mockSearchResult;

	@Autowired
	NaverSearchResult expected;

	@Test
	void search() throws IOException {
		Mockito.when(naverApis.search(anyMap(), anyMap()))
		       .thenReturn(Calls.response(Response.success(mockSearchResult)));

		Response<NaverSearchResult> response = naverService.search(
				SearchParam.builder().query("test").page(1).size(10).build());

		Assertions.assertTrue(response.isSuccessful());
		Assertions.assertEquals(expected, response.body());

		var expectedParam =
				Map.of("query", "test",
						"start", "1",
						"display", "10",
						"sort", "sim");

		Mockito.verify(naverApis, times(1))
		       .search(eq(Map.of(X_NAVER_CLIENT_ID, "test-id", X_NAVER_CLIENT_SECRET, "test-secret")),
				       eq(expectedParam));
	}
}