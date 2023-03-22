package org.sbs.blog.search;

import lombok.RequiredArgsConstructor;
import org.sbs.blog.search.dto.SearchParam;
import org.sbs.blog.search.dto.SearchResult;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/search")
public class SearchController {

	private final SearchService searchService;

	@RequestMapping(value = "/blog", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SearchResult> search(@Valid SearchParam searchParam) {
		return ResponseEntity.of(Optional.ofNullable(searchService.search(searchParam)));
	}
}
