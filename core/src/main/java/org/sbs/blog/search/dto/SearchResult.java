package org.sbs.blog.search.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
		LocalDateTime postdate;
		Map<String, Object> extras;

		public String getPostdate() {
			return postdate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		}

		public static class BlogBuilder {
			public BlogBuilder postdate(String postdate) {
				return postdate(postdate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
			}

			public BlogBuilder postdate(String postdate, DateTimeFormatter formatter) {
				this.postdate = LocalDateTime.parse(postdate, formatter);
				return this;
			}
		}
	}

}
