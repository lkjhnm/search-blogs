package org.sbs.blog.search.kakako;

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


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestKakaoConfiguration.class)
@TestPropertySource(locations = "classpath:/application-test.properties")
class KakaoApisTest extends AbstractApiTestBase {

	@Autowired
	String mockResponse;

	@Value("#{'KakaoAK ${sbs.api.kakao.key}'}")
	String restKey;

	@Autowired
	KakaoSearchResult expected;

	KakaoApis kakaoApis;

	@Override
	protected void init() {
		kakaoApis = new Retrofit.Builder()
				.client(okHttpClient)
				.baseUrl(String.format("http://%s:%d", mockWebServer.getHostName(), mockWebServer.getPort()))
				.addConverterFactory(JacksonConverterFactory.create())
				.build().create(KakaoApis.class);
	}

	@Test
	void search() throws IOException, InterruptedException {
		setupMockResponse(new MockResponse().addHeader("Content-Type", "application/json")
		                                    .setBody(mockResponse));
		mockWebServer.url("/v2/search/blog");
		Response<KakaoSearchResult> response = kakaoApis.search(restKey, Map.of("query", "test",
						                                                "page", "1",
						                                                "size", "10"))
		                                                .execute();
		KakaoSearchResult searchResult = response.body();
		Assertions.assertEquals(expected, searchResult);
		RecordedRequest request = mockWebServer.takeRequest();
		Assertions.assertEquals("KakaoAK test-kakao-rest-key", request.getHeader("Authorization"));
	}

	@Test
	void search_fail() throws IOException {
		setupMockResponse(new MockResponse().setResponseCode(500));
		mockWebServer.url("/v2/search/blog");
		Response<KakaoSearchResult> response = kakaoApis.search(String.format("KakaoAK %s", restKey),
				                                                Map.of("query", "test",
						                                                "page", "1",
						                                                "size", "10"))
		                                                .execute();
		Assertions.assertFalse(response.isSuccessful());
	}

}