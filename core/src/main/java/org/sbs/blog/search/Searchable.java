package org.sbs.blog.search;

import org.sbs.blog.search.dto.SearchParam;
import retrofit2.Response;

import java.io.IOException;

public interface Searchable {

	<T extends SearchResults> Response<T> search(SearchParam searchParam) throws IOException;
}
