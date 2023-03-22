package org.sbs.blog.search.naver;

import lombok.RequiredArgsConstructor;
import org.sbs.blog.search.Searchable;
import org.sbs.blog.search.dto.SearchParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NaverService implements Searchable {

	private final NaverApis naverApis;

	@Value("${sbs.api.naver.key.id}")
	private String keyId;

	@Value("${sbs.api.naver.key.secret}")
	private String keySecret;

	private Map<String, String> headers;

	@PostConstruct
	void init() {
		headers = Map.of(
				"X-Naver-Client-Id", keyId,
				"X-Naver-Client-Secret", keySecret
		);
	}

	@Override
	public Response<NaverSearchResult> search(SearchParam searchParam) throws IOException {
		return naverApis.search(headers, queryMap(searchParam)).execute();
	}

	private Map<String, String> queryMap(SearchParam searchParam) {
		String sort = searchParam.getSort().equals(SearchParam.SortType.accuracy) ? "sim" : "date";
		return Map.of(
				"query", searchParam.getQuery(),
				"display", String.valueOf(searchParam.getSize()),
				"start", String.valueOf(searchParam.getPage()),
				"sort", sort
		);
	}
}
