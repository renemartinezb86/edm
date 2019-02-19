package com.ericsson.dev.repository.search;

import com.ericsson.dev.domain.CustomerState;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CustomerState entity.
 */
public interface CustomerStateSearchRepository extends ElasticsearchRepository<CustomerState, String> {
}
