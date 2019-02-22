package com.ericsson.dev.service;

import com.ericsson.dev.domain.DiscountProcess;

import com.ericsson.dev.domain.Discounts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing DiscountProcess.
 */
public interface DiscountProcessService {

    /**
     * Save a discountProcess.
     *
     * @param discountProcess the entity to save
     * @return the persisted entity
     */
    DiscountProcess save(DiscountProcess discountProcess);

    /**
     * Get all the discountProcesses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DiscountProcess> findAll(Pageable pageable);


    /**
     * Get the "id" discountProcess.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<DiscountProcess> findOne(String id);

    List<String> getDiscountsSQL(DiscountProcess discountProcess);

    String generateCustomerDiscounts(Discounts discount, Instant dateToProcess);

    List<Discounts> getPlansToDiscount(String cuenta);

    /**
     * Delete the "id" discountProcess.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the discountProcess corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DiscountProcess> search(String query, Pageable pageable);
}
