package org.sbs.blog.search.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class SearchParam {
	String query;

	@Builder.Default
	int page = 1;

	@Builder.Default
	int size = 10;

	@Builder.Default
	SortType sort = SortType.accuracy;

	public enum SortType {
		accuracy, recency
	}
}
