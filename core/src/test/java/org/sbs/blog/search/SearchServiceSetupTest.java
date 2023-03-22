package org.sbs.blog.search;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestSearchConfiguration.class)
@TestPropertySource(locations = "classpath:/application-test.properties")
public class SearchServiceSetupTest {

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	List<Searchable> searches;

	@Test
	void setup() {
		Assertions.assertEquals(searches.size(), 2);
		Assertions.assertEquals(searches.get(0), applicationContext.getBean("mockSearchableFirst"));
		Assertions.assertEquals(searches.get(1), applicationContext.getBean("mockSearchableSecond"));
	}
}
