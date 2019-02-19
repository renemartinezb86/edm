package com.ericsson.dev.service;

import com.ericsson.dev.domain.Environment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Environment.
 */
public interface EnvironmentService {

    /**
     * Save a environment.
     *
     * @param environment the entity to save
     * @return the persisted entity
     */
    Environment save(Environment environment);

    /**
     * Get all the environments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Environment> findAll(Pageable pageable);


    /**
     * Get the "id" environment.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Environment> findOne(String id);

    /**
     * Delete the "id" environment.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the environment corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Environment> search(String query, Pageable pageable);
}
