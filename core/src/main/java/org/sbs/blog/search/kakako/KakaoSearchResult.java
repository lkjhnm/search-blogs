package org.sbs.blog.search.kakako;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.sbs.blog.search.SearchResults;
import org.sbs.blog.search.dto.SearchResult;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class KakaoSearchResult implements SearchResults {

	private Meta meta;

	private List<Document> documents;

	@Data
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	static class Meta {
		int totalCount;
		int pageableCount;

		@JsonProperty("is_end")
		boolean end;
	}

	@Data
	static class Document {
		String title;
		String contents;
		String url;
		String blogname;
		String thumbnail;
		String datetime;
	}

	@Override
	public SearchResult result() {
		return SearchResult.builder()
		                   .meta(toMeta())
		                   .blogs(toBlogs())
		                   .build();
	}

	private SearchResult.Meta toMeta() {
		return SearchResult.Meta.builder()
		                        .total(this.meta.getPageableCount())
		                        .end(this.meta.isEnd())
		                        .build();
	}

	private List<SearchResult.Blog> toBlogs() {
		return this.documents.stream()
		              .map(this::toBlog)
		              .collect(Collectors.toList());
	}

	private SearchResult.Blog toBlog(Document document) {
		return SearchResult.Blog.builder()
		                        .title(document.getTitle())
		                        .name(document.getBlogname())
		                        .contents(document.getContents())
		                        .url(document.getUrl())
		                        .postdate(document.getDatetime())
								.extras(extra(document))
		                        .build();
	}

	private Map<String, Object> extra(Document document) {
		return Map.of("thumbnail", document.getThumbnail());
	}
}
