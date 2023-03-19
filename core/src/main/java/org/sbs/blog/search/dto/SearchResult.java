package org.sbs.blog.search.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.Map;

@Data
@Builder
@Jacksonized
public class SearchResult {

	private Meta meta;
	private List<Blog> blogs;

	@Data
	@Builder
	@Jacksonized
	public static class Meta {
		int total;
		boolean end;
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
