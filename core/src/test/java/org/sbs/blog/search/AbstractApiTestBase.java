package org.sbs.blog.search;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public abstract class AbstractApiTestBase {

	@Autowired
	protected OkHttpClient okHttpClient;

	protected MockWebServer mockWebServer;

	@BeforeEach
	void before() throws IOException {
		mockWebServer = new MockWebServer();
		mockWebServer.start();
		init();
	}

	protected abstract void init();

	@AfterEach
	void after() throws IOException {
		mockWebServer.shutdown();
	}

	protected void setupMockResponse(MockResponse mockResponse) {
		mockWebServer.enqueue(mockResponse);
	}
}
