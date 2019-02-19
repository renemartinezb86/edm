package com.ericsson.dev.repository.search;

import com.ericsson.dev.domain.PlanDiscount;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PlanDiscount entity.
 */
public interface PlanDiscountSearchRepository extends ElasticsearchRepository<PlanDiscount, String> {
}
