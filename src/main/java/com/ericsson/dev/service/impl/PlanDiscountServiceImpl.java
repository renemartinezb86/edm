package com.ericsson.dev.service.impl;

import com.ericsson.dev.service.PlanDiscountService;
import com.ericsson.dev.domain.PlanDiscount;
import com.ericsson.dev.repository.PlanDiscountRepository;
import com.ericsson.dev.repository.search.PlanDiscountSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing PlanDiscount.
 */
@Service
public class PlanDiscountServiceImpl implements PlanDiscountService {

    private final Logger log = LoggerFactory.getLogger(PlanDiscountServiceImpl.class);

    private final PlanDiscountRepository planDiscountRepository;

    private final PlanDiscountSearchRepository planDiscountSearchRepository;

    public PlanDiscountServiceImpl(PlanDiscountRepository planDiscountRepository, PlanDiscountSearchRepository planDiscountSearchRepository) {
        this.planDiscountRepository = planDiscountRepository;
        this.planDiscountSearchRepository = planDiscountSearchRepository;
    }

    /**
     * Save a planDiscount.
     *
     * @param planDiscount the entity to save
     * @return the persisted entity
     */
    @Override
    public PlanDiscount save(PlanDiscount planDiscount) {
        log.debug("Request to save PlanDiscount : {}", planDiscount);
        PlanDiscount result = planDiscountRepository.save(planDiscount);
        planDiscountSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the planDiscounts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<PlanDiscount> findAll(Pageable pageable) {
        log.debug("Request to get all PlanDiscounts");
        return planDiscountRepository.findAll(pageable);
    }


    /**
     * Get one planDiscount by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<PlanDiscount> findOne(String id) {
        log.debug("Request to get PlanDiscount : {}", id);
        return planDiscountRepository.findById(id);
    }

    /**
     * Delete the planDiscount by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete PlanDiscount : {}", id);
        planDiscountRepository.deleteById(id);
        planDiscountSearchRepository.deleteById(id);
    }

    /**
     * Search for the planDiscount corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<PlanDiscount> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PlanDiscounts for query {}", query);
        return planDiscountSearchRepository.search(queryStringQuery(query), pageable);    }
}
