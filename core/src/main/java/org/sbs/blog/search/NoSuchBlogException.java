package org.sbs.blog.search;

import java.util.NoSuchElementException;

public class NoSuchBlogException extends NoSuchElementException {

	public NoSuchBlogException() {
	}

	public NoSuchBlogException(String s) {
		super(s);
	}
}
