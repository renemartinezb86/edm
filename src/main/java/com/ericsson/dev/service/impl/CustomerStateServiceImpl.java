package com.ericsson.dev.service.impl;

import com.ericsson.dev.service.CustomerStateService;
import com.ericsson.dev.domain.CustomerState;
import com.ericsson.dev.repository.CustomerStateRepository;
import com.ericsson.dev.repository.search.CustomerStateSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing CustomerState.
 */
@Service
public class CustomerStateServiceImpl implements CustomerStateService {

    private final Logger log = LoggerFactory.getLogger(CustomerStateServiceImpl.class);

    private final CustomerStateRepository customerStateRepository;

    private final CustomerStateSearchRepository customerStateSearchRepository;

    public CustomerStateServiceImpl(CustomerStateRepository customerStateRepository, CustomerStateSearchRepository customerStateSearchRepository) {
        this.customerStateRepository = customerStateRepository;
        this.customerStateSearchRepository = customerStateSearchRepository;
    }

    /**
     * Save a customerState.
     *
     * @param customerState the entity to save
     * @return the persisted entity
     */
    @Override
    public CustomerState save(CustomerState customerState) {
        log.debug("Request to save CustomerState : {}", customerState);
        CustomerState result = customerStateRepository.save(customerState);
        customerStateSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the customerStates.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<CustomerState> findAll(Pageable pageable) {
        log.debug("Request to get all CustomerStates");
        return customerStateRepository.findAll(pageable);
    }


    /**
     * Get one customerState by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<CustomerState> findOne(String id) {
        log.debug("Request to get CustomerState : {}", id);
        return customerStateRepository.findById(id);
    }

    /**
     * Delete the customerState by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete CustomerState : {}", id);
        customerStateRepository.deleteById(id);
        customerStateSearchRepository.deleteById(id);
    }

    /**
     * Search for the customerState corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<CustomerState> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CustomerStates for query {}", query);
        return customerStateSearchRepository.search(queryStringQuery(query), pageable);    }
}
