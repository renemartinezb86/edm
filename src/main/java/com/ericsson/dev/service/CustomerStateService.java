package com.ericsson.dev.service;

import com.ericsson.dev.domain.CustomerState;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing CustomerState.
 */
public interface CustomerStateService {

    /**
     * Save a customerState.
     *
     * @param customerState the entity to save
     * @return the persisted entity
     */
    CustomerState save(CustomerState customerState);

    /**
     * Get all the customerStates.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CustomerState> findAll(Pageable pageable);


    /**
     * Get the "id" customerState.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CustomerState> findOne(String id);

    /**
     * Delete the "id" customerState.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the customerState corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CustomerState> search(String query, Pageable pageable);
}
