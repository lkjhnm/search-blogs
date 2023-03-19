package org.sbs.blog.search.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchParam {
	String query;
	int page;
	int size;

	@Builder.Default
	SortType sort = SortType.Accuracy;

	public enum SortType {
		Accuracy, Recency


	}
}
