package com.ericsson.dev.service.impl;

import com.ericsson.dev.service.EnvironmentService;
import com.ericsson.dev.domain.Environment;
import com.ericsson.dev.repository.EnvironmentRepository;
import com.ericsson.dev.repository.search.EnvironmentSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Environment.
 */
@Service
public class EnvironmentServiceImpl implements EnvironmentService {

    private final Logger log = LoggerFactory.getLogger(EnvironmentServiceImpl.class);

    private final EnvironmentRepository environmentRepository;

    private final EnvironmentSearchRepository environmentSearchRepository;

    public EnvironmentServiceImpl(EnvironmentRepository environmentRepository, EnvironmentSearchRepository environmentSearchRepository) {
        this.environmentRepository = environmentRepository;
        this.environmentSearchRepository = environmentSearchRepository;
    }

    /**
     * Save a environment.
     *
     * @param environment the entity to save
     * @return the persisted entity
     */
    @Override
    public Environment save(Environment environment) {
        log.debug("Request to save Environment : {}", environment);
        Environment result = environmentRepository.save(environment);
        environmentSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the environments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<Environment> findAll(Pageable pageable) {
        log.debug("Request to get all Environments");
        return environmentRepository.findAll(pageable);
    }


    /**
     * Get one environment by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<Environment> findOne(String id) {
        log.debug("Request to get Environment : {}", id);
        return environmentRepository.findById(id);
    }

    /**
     * Delete the environment by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Environment : {}", id);
        environmentRepository.deleteById(id);
        environmentSearchRepository.deleteById(id);
    }

    /**
     * Search for the environment corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<Environment> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Environments for query {}", query);
        return environmentSearchRepository.search(queryStringQuery(query), pageable);    }
}
