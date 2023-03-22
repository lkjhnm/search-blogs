package org.sbs.blog.search.naver;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sbs.blog.search.AbstractApiTestBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.Map;

import static org.sbs.blog.search.naver.TestNaverConfiguration.X_NAVER_CLIENT_ID;
import static org.sbs.blog.search.naver.TestNaverConfiguration.X_NAVER_CLIENT_SECRET;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestNaverConfiguration.class)
@TestPropertySource(locations = "classpath:/application-test.properties")
class NaverApisTest extends AbstractApiTestBase {

	NaverApis naverApis;

	@Autowired
	String mockResponse;

	@Value("${sbs.api.naver.key.id}")
	String keyId;

	@Value("${sbs.api.naver.key.secret}")
	String keySecret;

	@Autowired
	NaverSearchResult expected;

	@Override
	protected void init() {
		naverApis = new Retrofit.Builder()
				.client(okHttpClient)
				.baseUrl(String.format("http://%s:%d", mockWebServer.getHostName(), mockWebServer.getPort()))
				.addConverterFactory(JacksonConverterFactory.create())
				.build().create(NaverApis.class);
	}

	@Test
	void search() throws IOException, InterruptedException {
		setupMockResponse(new MockResponse().addHeader("Content-Type", "application/json")
		                                    .setBody(mockResponse));

		mockWebServer.url("/v1/search/blog.json");
		Map<String, String> headers = Map.of(
				X_NAVER_CLIENT_ID, keyId,
				X_NAVER_CLIENT_SECRET, keySecret
		);

		Response<NaverSearchResult> response = naverApis.search(headers, Map.of("query", "test",
				                                                "start", "1",
				                                                "display", "10"))
		                                                .execute();
		NaverSearchResult searchResult = response.body();
		Assertions.assertEquals(expected, searchResult);
		RecordedRequest request = mockWebServer.takeRequest();
		Assertions.assertEquals("test-id", request.getHeader(X_NAVER_CLIENT_ID));
		Assertions.assertEquals("test-secret", request.getHeader(X_NAVER_CLIENT_SECRET));
	}
}