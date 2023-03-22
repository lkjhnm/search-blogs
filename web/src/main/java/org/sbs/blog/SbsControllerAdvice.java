package org.sbs.blog;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.sbs.blog.search.NoSuchBlogException;
import org.sbs.blog.search.dto.SearchResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class SbsControllerAdvice {

	@ExceptionHandler({ConstraintViolationException.class, UnrecognizedPropertyException.class})
	public ResponseEntity<Void> exceptionHandler(Exception e) {
		return ResponseEntity.badRequest().build();
	}

	@ExceptionHandler(NoSuchBlogException.class)
	public ResponseEntity<SearchResult> searchHandler() {
		return ResponseEntity.ok(SearchResult.builder().build());
	}
}
