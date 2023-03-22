package org.sbs.blog.search.event;

import lombok.RequiredArgsConstructor;
import org.sbs.blog.search.dto.SearchParam;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchEventPublisher {

	private final ApplicationEventPublisher applicationEventPublisher;

	public void publish(SearchParam searchParam) {
		applicationEventPublisher.publishEvent(new SearchEvent(this, searchParam));
	}
}
