package org.sbs.blog.search.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Builder
@Jacksonized
public class SearchParam {

	String query;

	@Min(1)
	@Max(50)
	@Builder.Default
	int page = 1;

	@Min(1)
	@Max(50)
	@Builder.Default
	int size = 10;

	@Builder.Default
	SortType sort = SortType.accuracy;

	public enum SortType {
		accuracy, recency
	}
}
