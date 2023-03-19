package org.sbs.blog.search;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.sbs.blog.YamlPropertyLoader;
import org.sbs.blog.search.kakako.KakaoApis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
@PropertySource(value = "classpath:application.yml", factory = YamlPropertyLoader.class)
public class SearchConfiguration {

	Logger logger = LoggerFactory.getLogger(OkHttpClient.class);

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
				.baseUrl("https://dapi.kakao.com/")  // todo: extract to yml
				.addConverterFactory(JacksonConverterFactory.create())
				.build().create(KakaoApis.class);
	}
}
