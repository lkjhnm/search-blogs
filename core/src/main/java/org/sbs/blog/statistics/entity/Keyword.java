package org.sbs.blog.statistics.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
	@JsonIgnore
	private Long id;

	@Column(unique = true, nullable = false, columnDefinition = "TEXT")
	private String keyword;

	@Column
	private Long count;

	public Keyword increaseCount() {
		this.count++;
		return this;
	}

	@Builder
	public Keyword(String keyword) {
		this.keyword = keyword;
		this.count = 0l;
	}
}
