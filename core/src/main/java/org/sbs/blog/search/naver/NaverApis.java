package org.sbs.blog.search.naver;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.QueryMap;

import java.util.Map;

public interface NaverApis {

	@GET("/v1/search/blog.json")
	Call<NaverSearchResult> search(@HeaderMap Map<String, String> headers, @QueryMap Map<String, String> params);
}
