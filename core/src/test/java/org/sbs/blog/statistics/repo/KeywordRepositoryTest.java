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
@ContextConfiguration(classes = TestKeywordConfiguration.class)
@AutoConfigureTestDatabase(replace =  AutoConfigureTestDatabase.Replace.NONE)
class KeywordRepositoryTest {

	@Autowired
	KeywordRepository keywordRepository;

	Keyword test;

	@BeforeEach
	void before() {
		test = Keyword.builder().keyword("test").build();
		keywordRepository.save(test);
	}

	@Test
	@Order(1)
	void save() {
		Keyword saved = keywordRepository.save(Keyword.builder().keyword("test2").build());
		Assertions.assertEquals(this.test.getId() + 1, saved.getId());
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