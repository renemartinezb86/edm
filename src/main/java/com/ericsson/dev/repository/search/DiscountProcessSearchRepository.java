package com.ericsson.dev.repository.search;

import com.ericsson.dev.domain.DiscountProcess;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the DiscountProcess entity.
 */
public interface DiscountProcessSearchRepository extends ElasticsearchRepository<DiscountProcess, String> {
}
