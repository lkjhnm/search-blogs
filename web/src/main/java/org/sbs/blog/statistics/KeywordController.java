package org.sbs.blog.statistics;

import lombok.RequiredArgsConstructor;
import org.sbs.blog.statistics.entity.Keyword;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/statistics")
public class KeywordController {

	private static final String DEFAULT_SIZE = "10";

	private final KeywordService keywordService;

	@GetMapping(value = "/keyword/popular", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Keyword>> popularKeyword(@RequestParam(defaultValue = DEFAULT_SIZE)
	                                                    @Min(1) @Max(10) int size) {
		return ResponseEntity.of(Optional.ofNullable(keywordService.getPopularKeywords(size)));
	}
}
