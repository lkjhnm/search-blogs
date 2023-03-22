package org.sbs.blog;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class SbsControllerAdvice {

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> exceptionHandler(ConstraintViolationException e) {
		return ResponseEntity.badRequest().build();
	}

	// todo: empty list return
}
