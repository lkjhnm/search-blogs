package org.sbs.blog.search;

import lombok.RequiredArgsConstructor;
import org.sbs.blog.search.dto.SearchParam;
import org.sbs.blog.search.dto.SearchResult;
import org.sbs.blog.search.event.SearchEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SearchService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final List<Searchable> searches;

	private final SearchEventPublisher searchEventPublisher;

	public SearchResult search(SearchParam searchParam) {
		SearchResult result = searches(searchParam).orElseThrow(RuntimeException::new);
		searchEventPublisher.publish(searchParam);
		return result;
	}

	private Optional<SearchResult> searches(SearchParam searchParam) {
		for (Searchable searchable : searches) {
			try {
				Response<SearchResults> response = searchable.search(searchParam);
				if (response.isSuccessful()) {
					return Optional.ofNullable(response.body().result());
				}
			} catch (Exception e) {
				logger.warn("Exception occurred during searching blog", e);
			}
		}
		return Optional.empty();
	}
}
