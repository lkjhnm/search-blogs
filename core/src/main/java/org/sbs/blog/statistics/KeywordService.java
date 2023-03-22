package org.sbs.blog.statistics;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.sbs.blog.search.event.SearchEvent;
import org.sbs.blog.statistics.entity.Keyword;
import org.sbs.blog.statistics.repo.KeywordRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeywordService {

	private final KeywordRepository repository;

	private final Map<String, Keyword> keywordStatistics = new ConcurrentHashMap<>();

	//todo: need to change asynchronous?
	@EventListener
	void subscribe(SearchEvent searchEvent) {
		Keyword keyword = saveKeyword(searchEvent.getMessage().getQuery());
		keywordStatistics.put(keyword.getKeyword(), keyword);
	}

	@NotNull
	private Keyword saveKeyword(String query) {
		Keyword keyword = repository.findByKeyword(query).orElse(Keyword.builder().keyword(query).build());
		return repository.save(keyword.increaseCount());
	}

	public List<Keyword> getPopularKeywords(int size) {
		return keywordStatistics.values().stream()
		                        .sorted(Comparator.comparingLong(Keyword::getCount).reversed())
		                        .limit(size)
		                        .collect(Collectors.toList());
	}
}
