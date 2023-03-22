package org.sbs.blog.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.sbs.blog.search.dto.SearchParam;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
public class SearchArgumentResolver implements HandlerMethodArgumentResolver {

	private final ObjectMapper mapper;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(SearchParam.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,
	                              ModelAndViewContainer mavContainer,
	                              NativeWebRequest webRequest,
	                              WebDataBinderFactory binderFactory) throws Exception {
		Iterable<String> iterable = () -> webRequest.getParameterNames();
		Map<String, String> params = StreamSupport
				.stream(iterable.spliterator(), false)
				.collect(Collectors.toMap(key -> key, key -> webRequest.getParameter(key)));
		return mapper.convertValue(params, SearchParam.class);
	}
}
