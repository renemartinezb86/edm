package com.ericsson.dev.web.rest;
import com.ericsson.dev.domain.Environment;
import com.ericsson.dev.service.EnvironmentService;
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
 * REST controller for managing Environment.
 */
@RestController
@RequestMapping("/api")
public class EnvironmentResource {

    private final Logger log = LoggerFactory.getLogger(EnvironmentResource.class);

    private static final String ENTITY_NAME = "environment";

    private final EnvironmentService environmentService;

    public EnvironmentResource(EnvironmentService environmentService) {
        this.environmentService = environmentService;
    }

    /**
     * POST  /environments : Create a new environment.
     *
     * @param environment the environment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new environment, or with status 400 (Bad Request) if the environment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/environments")
    public ResponseEntity<Environment> createEnvironment(@RequestBody Environment environment) throws URISyntaxException {
        log.debug("REST request to save Environment : {}", environment);
        if (environment.getId() != null) {
            throw new BadRequestAlertException("A new environment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Environment result = environmentService.save(environment);
        return ResponseEntity.created(new URI("/api/environments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /environments : Updates an existing environment.
     *
     * @param environment the environment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated environment,
     * or with status 400 (Bad Request) if the environment is not valid,
     * or with status 500 (Internal Server Error) if the environment couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/environments")
    public ResponseEntity<Environment> updateEnvironment(@RequestBody Environment environment) throws URISyntaxException {
        log.debug("REST request to update Environment : {}", environment);
        if (environment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Environment result = environmentService.save(environment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, environment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /environments : get all the environments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of environments in body
     */
    @GetMapping("/environments")
    public ResponseEntity<List<Environment>> getAllEnvironments(Pageable pageable) {
        log.debug("REST request to get a page of Environments");
        Page<Environment> page = environmentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/environments");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /environments/:id : get the "id" environment.
     *
     * @param id the id of the environment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the environment, or with status 404 (Not Found)
     */
    @GetMapping("/environments/{id}")
    public ResponseEntity<Environment> getEnvironment(@PathVariable String id) {
        log.debug("REST request to get Environment : {}", id);
        Optional<Environment> environment = environmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(environment);
    }

    /**
     * DELETE  /environments/:id : delete the "id" environment.
     *
     * @param id the id of the environment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/environments/{id}")
    public ResponseEntity<Void> deleteEnvironment(@PathVariable String id) {
        log.debug("REST request to delete Environment : {}", id);
        environmentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/environments?query=:query : search for the environment corresponding
     * to the query.
     *
     * @param query the query of the environment search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/environments")
    public ResponseEntity<List<Environment>> searchEnvironments(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Environments for query {}", query);
        Page<Environment> page = environmentService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/environments");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
