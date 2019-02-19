package com.ericsson.dev.service;

import com.ericsson.dev.domain.PlanDiscount;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing PlanDiscount.
 */
public interface PlanDiscountService {

    /**
     * Save a planDiscount.
     *
     * @param planDiscount the entity to save
     * @return the persisted entity
     */
    PlanDiscount save(PlanDiscount planDiscount);

    /**
     * Get all the planDiscounts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PlanDiscount> findAll(Pageable pageable);

    Double getDiscountPercentage (String name, Integer position);
    /**
     * Get the "id" planDiscount.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PlanDiscount> findOne(String id);

    /**
     * Delete the "id" planDiscount.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the planDiscount corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PlanDiscount> search(String query, Pageable pageable);
}
