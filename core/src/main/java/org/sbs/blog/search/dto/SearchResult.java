package org.sbs.blog.search.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
@Builder
@Jacksonized
public class SearchResult {

	@Builder.Default
	private Meta meta = Meta.builder().build();
	@Builder.Default
	private List<Blog> blogs = Collections.emptyList();

	@Data
	@Builder
	@Jacksonized
	public static class Meta {
		int total;

		@Builder.Default
		boolean end = true;
	}

	@Data
	@Builder
	@Jacksonized
	public static class Blog {
		String title;
		String contents;
		String url;
		String name;
		String postdate;
		Map<String, Object> extras;
	}

}
