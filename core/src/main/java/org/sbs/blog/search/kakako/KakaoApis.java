package org.sbs.blog.search.kakako;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.QueryMap;

import java.util.Map;

public interface KakaoApis {

	@GET("/v2/search/blog")
	Call<KakaoSearchResult> search(@Header("Authorization") String authorization, @QueryMap Map<String, String> params);
}
