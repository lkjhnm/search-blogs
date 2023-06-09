package org.sbs.blog.search.kakako;

import lombok.RequiredArgsConstructor;
import org.sbs.blog.search.Searchable;
import org.sbs.blog.search.dto.SearchParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Map;

@Service
@Order(1)
@RequiredArgsConstructor
public class KakaoService implements Searchable {

	private final KakaoApis kakaoApis;

	@Value("#{'KakaoAK ${sbs.api.kakao.key}'}")
	private String apiKey;

	@Override
	public Response<KakaoSearchResult> search(SearchParam searchParam) throws IOException {
		Call<KakaoSearchResult> search = kakaoApis.search(apiKey, queryMap(searchParam));
		return search.execute();
	}

	private Map<String, String> queryMap(SearchParam searchParam) {
		String sort = searchParam.getSort().equals(SearchParam.SortType.accuracy) ? "accuracy" : "recency";
		return Map.of("query", searchParam.getQuery(),
				"sort", sort,
				"page", String.valueOf(searchParam.getPage()),
				"size", String.valueOf(searchParam.getSize()));
	}
}
