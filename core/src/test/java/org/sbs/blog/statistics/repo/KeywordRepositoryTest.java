package org.sbs.blog.statistics.repo;

import org.junit.jupiter.api.*;
import org.sbs.blog.statistics.TestKeywordConfiguration;
import org.sbs.blog.statistics.entity.Keyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace =  AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = TestKeywordConfiguration.class)
class KeywordRepositoryTest {

	@Autowired
	KeywordRepository keywordRepository;

	@BeforeEach
	void before() {
		Keyword test = Keyword.builder().keyword("test").count(1l).build();
		keywordRepository.save(test);
	}

	@Test
	@Order(1)
	void save() {
		Keyword test = Keyword.builder().keyword("test2").count(1l).build();
		Keyword saved = keywordRepository.save(test);
		Assertions.assertEquals(2, saved.getId());
	}

	@Test
	void find_by_id() {
		Assertions.assertNotNull(keywordRepository.findById(1l));
	}

	@Test
	void find_by_keyword() {
		Assertions.assertNotNull(keywordRepository.findByKeyword("test"));
	}
}