package org.sbs.blog.search.event;

import org.sbs.blog.search.dto.SearchParam;
import org.springframework.context.ApplicationEvent;

public class SearchEvent extends ApplicationEvent {

	SearchParam message;

	public SearchEvent(Object source, SearchParam message) {
		super(source);
		this.message = message;
	}

	public SearchParam getMessage() {
		return message;
	}
}
