package org.sbs.blog.statistics;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "org.sbs.blog.statistics")
@EnableJpaRepositories(basePackages = "org.sbs.blog.statistics")
public class TestKeywordConfiguration {

}
