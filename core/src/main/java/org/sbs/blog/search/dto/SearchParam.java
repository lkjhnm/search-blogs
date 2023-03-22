package org.sbs.blog.search.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchParam {
	String query;

	@Builder.Default
	int page = 1;

	@Builder.Default
	int size = 10;

	@Builder.Default
	SortType sort = SortType.Accuracy;

	public enum SortType {
		Accuracy, Recency
	}
}
