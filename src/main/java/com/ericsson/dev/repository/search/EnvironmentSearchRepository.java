package com.ericsson.dev.repository.search;

import com.ericsson.dev.domain.Environment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Environment entity.
 */
public interface EnvironmentSearchRepository extends ElasticsearchRepository<Environment, String> {
}
