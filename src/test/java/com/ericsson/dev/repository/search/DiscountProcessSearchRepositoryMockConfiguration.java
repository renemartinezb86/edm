package com.ericsson.dev.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of DiscountProcessSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class DiscountProcessSearchRepositoryMockConfiguration {

    @MockBean
    private DiscountProcessSearchRepository mockDiscountProcessSearchRepository;

}
