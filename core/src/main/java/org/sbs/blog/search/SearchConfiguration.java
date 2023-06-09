package org.sbs.blog.search;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.sbs.blog.search.kakako.KakaoApis;
import org.sbs.blog.search.naver.NaverApis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class SearchConfiguration {

	Logger logger = LoggerFactory.getLogger(OkHttpClient.class);

	@Value("${sbs.api.kakao.origin}")
	private String kakaoOrigin;

	@Value("${sbs.api.naver.origin}")
	private String naverOrigin;

	@Bean
	HttpLoggingInterceptor httpLoggingInterceptor() {
		return new HttpLoggingInterceptor(message -> {
			if (logger.isDebugEnabled()) {
				logger.debug("{}", message);
			}
		}).setLevel(HttpLoggingInterceptor.Level.BODY);
	}

	@Bean
	OkHttpClient okHttpClient() {
		return new OkHttpClient.Builder()
				.addInterceptor(httpLoggingInterceptor())
				.build();
	}

	@Bean
	KakaoApis kakaoClient() {
		return new Retrofit.Builder()
				.client(okHttpClient())
				.baseUrl(kakaoOrigin)
				.addConverterFactory(JacksonConverterFactory.create())
				.build().create(KakaoApis.class);
	}

	@Bean
	NaverApis naverClient() {
		return new Retrofit.Builder()
				.client(okHttpClient())
				.baseUrl(naverOrigin)
				.addConverterFactory(JacksonConverterFactory.create())
				.build().create(NaverApis.class);
	}
}
