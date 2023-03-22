package org.sbs.blog.search.naver;

import lombok.Data;
import org.sbs.blog.search.SearchResults;
import org.sbs.blog.search.dto.SearchResult;
import org.sbs.blog.search.dto.SearchResult.Blog;
import org.sbs.blog.search.dto.SearchResult.Meta;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class NaverSearchResult implements SearchResults {

	String lastBuildDate;
	int total;
	int start;
	int display;
	List<Item> items;

	@Data
	static class Item {
		String title;
		String link;
		String description;
		String bloggername;
		String bloggerlink;
		String postdate;
	}

	@Override
	public SearchResult result() {
		return SearchResult.builder()
		                   .meta(toMeta())
		                   .blogs(toBlogs())
		                   .build();
	}

	private Meta toMeta() {
		return Meta.builder()
		           .total(getTotal())
		           .end(getStart() * getDisplay() >= getTotal())
		           .build();
	}

	private List<Blog> toBlogs() {
		return items.stream()
		            .map(this::toBlog)
		            .collect(Collectors.toList());
	}

	private Blog toBlog(Item item) {
		return Blog.builder()
		           .title(item.getTitle())
		           .url(item.getLink())
		           .contents(item.getDescription())
		           .postdate(item.getPostdate())
		           .name(item.getBloggername())
		           .extras(extra(item))
		           .build();
	}

	private Map<String, Object> extra(Item item) {
		return Map.of("bloggerlink", item.bloggerlink);
	}
}
