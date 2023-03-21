package org.sbs.blog.statistics.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Keyword {

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true, nullable = false)
	private String keyword; //todo: 길이 제한 확인

	@Column
	private Long count;


	@Builder
	public Keyword(String keyword, Long count) {
		this.keyword = keyword;
		this.count = count;
	}
}
