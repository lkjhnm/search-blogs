package org.sbs.blog.statistics.repo;

import org.sbs.blog.statistics.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {

	Optional<Keyword> findByKeyword(String keyword);
}
