package com.ericsson.dev.web.rest;
import com.ericsson.dev.domain.PlanDiscount;
import com.ericsson.dev.service.PlanDiscountService;
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
 * REST controller for managing PlanDiscount.
 */
@RestController
@RequestMapping("/api")
public class PlanDiscountResource {

    private final Logger log = LoggerFactory.getLogger(PlanDiscountResource.class);

    private static final String ENTITY_NAME = "planDiscount";

    private final PlanDiscountService planDiscountService;

    public PlanDiscountResource(PlanDiscountService planDiscountService) {
        this.planDiscountService = planDiscountService;
    }

    /**
     * POST  /plan-discounts : Create a new planDiscount.
     *
     * @param planDiscount the planDiscount to create
     * @return the ResponseEntity with status 201 (Created) and with body the new planDiscount, or with status 400 (Bad Request) if the planDiscount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/plan-discounts")
    public ResponseEntity<PlanDiscount> createPlanDiscount(@RequestBody PlanDiscount planDiscount) throws URISyntaxException {
        log.debug("REST request to save PlanDiscount : {}", planDiscount);
        if (planDiscount.getId() != null) {
            throw new BadRequestAlertException("A new planDiscount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlanDiscount result = planDiscountService.save(planDiscount);
        return ResponseEntity.created(new URI("/api/plan-discounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /plan-discounts : Updates an existing planDiscount.
     *
     * @param planDiscount the planDiscount to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated planDiscount,
     * or with status 400 (Bad Request) if the planDiscount is not valid,
     * or with status 500 (Internal Server Error) if the planDiscount couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/plan-discounts")
    public ResponseEntity<PlanDiscount> updatePlanDiscount(@RequestBody PlanDiscount planDiscount) throws URISyntaxException {
        log.debug("REST request to update PlanDiscount : {}", planDiscount);
        if (planDiscount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PlanDiscount result = planDiscountService.save(planDiscount);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, planDiscount.getId().toString()))
            .body(result);
    }

    /**
     * GET  /plan-discounts : get all the planDiscounts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of planDiscounts in body
     */
    @GetMapping("/plan-discounts")
    public ResponseEntity<List<PlanDiscount>> getAllPlanDiscounts(Pageable pageable) {
        log.debug("REST request to get a page of PlanDiscounts");
        Page<PlanDiscount> page = planDiscountService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/plan-discounts");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /plan-discounts/:id : get the "id" planDiscount.
     *
     * @param id the id of the planDiscount to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the planDiscount, or with status 404 (Not Found)
     */
    @GetMapping("/plan-discounts/{id}")
    public ResponseEntity<PlanDiscount> getPlanDiscount(@PathVariable String id) {
        log.debug("REST request to get PlanDiscount : {}", id);
        Optional<PlanDiscount> planDiscount = planDiscountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(planDiscount);
    }

    /**
     * DELETE  /plan-discounts/:id : delete the "id" planDiscount.
     *
     * @param id the id of the planDiscount to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/plan-discounts/{id}")
    public ResponseEntity<Void> deletePlanDiscount(@PathVariable String id) {
        log.debug("REST request to delete PlanDiscount : {}", id);
        planDiscountService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/plan-discounts?query=:query : search for the planDiscount corresponding
     * to the query.
     *
     * @param query the query of the planDiscount search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/plan-discounts")
    public ResponseEntity<List<PlanDiscount>> searchPlanDiscounts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PlanDiscounts for query {}", query);
        Page<PlanDiscount> page = planDiscountService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/plan-discounts");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
