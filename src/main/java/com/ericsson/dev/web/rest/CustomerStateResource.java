package com.ericsson.dev.web.rest;
import com.ericsson.dev.domain.CustomerState;
import com.ericsson.dev.service.CustomerStateService;
import com.ericsson.dev.web.rest.errors.BadRequestAlertException;
import com.ericsson.dev.web.rest.util.HeaderUtil;
import com.ericsson.dev.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing CustomerState.
 */
@RestController
@RequestMapping("/api")
public class CustomerStateResource {

    private final Logger log = LoggerFactory.getLogger(CustomerStateResource.class);

    private static final String ENTITY_NAME = "customerState";

    private final CustomerStateService customerStateService;

    public CustomerStateResource(CustomerStateService customerStateService) {
        this.customerStateService = customerStateService;
    }

    /**
     * POST  /customer-states : Create a new customerState.
     *
     * @param customerState the customerState to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customerState, or with status 400 (Bad Request) if the customerState has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/customer-states")
    public ResponseEntity<CustomerState> createCustomerState(@RequestBody CustomerState customerState) throws URISyntaxException {
        log.debug("REST request to save CustomerState : {}", customerState);
        if (customerState.getId() != null) {
            throw new BadRequestAlertException("A new customerState cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomerState result = customerStateService.save(customerState);
        return ResponseEntity.created(new URI("/api/customer-states/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /customer-states : Updates an existing customerState.
     *
     * @param customerState the customerState to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated customerState,
     * or with status 400 (Bad Request) if the customerState is not valid,
     * or with status 500 (Internal Server Error) if the customerState couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/customer-states")
    public ResponseEntity<CustomerState> updateCustomerState(@RequestBody CustomerState customerState) throws URISyntaxException {
        log.debug("REST request to update CustomerState : {}", customerState);
        if (customerState.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CustomerState result = customerStateService.save(customerState);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, customerState.getId().toString()))
            .body(result);
    }

    /**
     * GET  /customer-states : get all the customerStates.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of customerStates in body
     */
    @GetMapping("/customer-states")
    public ResponseEntity<List<CustomerState>> getAllCustomerStates(Pageable pageable) {
        log.debug("REST request to get a page of CustomerStates");
        Page<CustomerState> page = customerStateService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/customer-states");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /customer-states/:id : get the "id" customerState.
     *
     * @param id the id of the customerState to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customerState, or with status 404 (Not Found)
     */
    @GetMapping("/customer-states/{id}")
    public ResponseEntity<CustomerState> getCustomerState(@PathVariable String id) {
        log.debug("REST request to get CustomerState : {}", id);
        Optional<CustomerState> customerState = customerStateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerState);
    }

    /**
     * DELETE  /customer-states/:id : delete the "id" customerState.
     *
     * @param id the id of the customerState to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/customer-states/{id}")
    public ResponseEntity<Void> deleteCustomerState(@PathVariable String id) {
        log.debug("REST request to delete CustomerState : {}", id);
        customerStateService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/customer-states?query=:query : search for the customerState corresponding
     * to the query.
     *
     * @param query the query of the customerState search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/customer-states")
    public ResponseEntity<List<CustomerState>> searchCustomerStates(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CustomerStates for query {}", query);
        Page<CustomerState> page = customerStateService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/customer-states");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
