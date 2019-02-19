package com.ericsson.dev.web.rest;

import com.ericsson.dev.EdmApp;

import com.ericsson.dev.domain.Environment;
import com.ericsson.dev.repository.EnvironmentRepository;
import com.ericsson.dev.repository.search.EnvironmentSearchRepository;
import com.ericsson.dev.service.EnvironmentService;
import com.ericsson.dev.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;

import java.util.Collections;
import java.util.List;


import static com.ericsson.dev.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EnvironmentResource REST controller.
 *
 * @see EnvironmentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EdmApp.class)
public class EnvironmentResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_USER = "AAAAAAAAAA";
    private static final String UPDATED_USER = "BBBBBBBBBB";

    private static final String DEFAULT_PASS = "AAAAAAAAAA";
    private static final String UPDATED_PASS = "BBBBBBBBBB";

    @Autowired
    private EnvironmentRepository environmentRepository;

    @Autowired
    private EnvironmentService environmentService;

    /**
     * This repository is mocked in the com.ericsson.dev.repository.search test package.
     *
     * @see com.ericsson.dev.repository.search.EnvironmentSearchRepositoryMockConfiguration
     */
    @Autowired
    private EnvironmentSearchRepository mockEnvironmentSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restEnvironmentMockMvc;

    private Environment environment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EnvironmentResource environmentResource = new EnvironmentResource(environmentService);
        this.restEnvironmentMockMvc = MockMvcBuilders.standaloneSetup(environmentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Environment createEntity() {
        Environment environment = new Environment()
            .name(DEFAULT_NAME)
            .url(DEFAULT_URL)
            .user(DEFAULT_USER)
            .pass(DEFAULT_PASS);
        return environment;
    }

    @Before
    public void initTest() {
        environmentRepository.deleteAll();
        environment = createEntity();
    }

    @Test
    public void createEnvironment() throws Exception {
        int databaseSizeBeforeCreate = environmentRepository.findAll().size();

        // Create the Environment
        restEnvironmentMockMvc.perform(post("/api/environments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(environment)))
            .andExpect(status().isCreated());

        // Validate the Environment in the database
        List<Environment> environmentList = environmentRepository.findAll();
        assertThat(environmentList).hasSize(databaseSizeBeforeCreate + 1);
        Environment testEnvironment = environmentList.get(environmentList.size() - 1);
        assertThat(testEnvironment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEnvironment.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testEnvironment.getUser()).isEqualTo(DEFAULT_USER);
        assertThat(testEnvironment.getPass()).isEqualTo(DEFAULT_PASS);

        // Validate the Environment in Elasticsearch
        verify(mockEnvironmentSearchRepository, times(1)).save(testEnvironment);
    }

    @Test
    public void createEnvironmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = environmentRepository.findAll().size();

        // Create the Environment with an existing ID
        environment.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnvironmentMockMvc.perform(post("/api/environments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(environment)))
            .andExpect(status().isBadRequest());

        // Validate the Environment in the database
        List<Environment> environmentList = environmentRepository.findAll();
        assertThat(environmentList).hasSize(databaseSizeBeforeCreate);

        // Validate the Environment in Elasticsearch
        verify(mockEnvironmentSearchRepository, times(0)).save(environment);
    }

    @Test
    public void getAllEnvironments() throws Exception {
        // Initialize the database
        environmentRepository.save(environment);

        // Get all the environmentList
        restEnvironmentMockMvc.perform(get("/api/environments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(environment.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].user").value(hasItem(DEFAULT_USER.toString())))
            .andExpect(jsonPath("$.[*].pass").value(hasItem(DEFAULT_PASS.toString())));
    }
    
    @Test
    public void getEnvironment() throws Exception {
        // Initialize the database
        environmentRepository.save(environment);

        // Get the environment
        restEnvironmentMockMvc.perform(get("/api/environments/{id}", environment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(environment.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.user").value(DEFAULT_USER.toString()))
            .andExpect(jsonPath("$.pass").value(DEFAULT_PASS.toString()));
    }

    @Test
    public void getNonExistingEnvironment() throws Exception {
        // Get the environment
        restEnvironmentMockMvc.perform(get("/api/environments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateEnvironment() throws Exception {
        // Initialize the database
        environmentService.save(environment);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockEnvironmentSearchRepository);

        int databaseSizeBeforeUpdate = environmentRepository.findAll().size();

        // Update the environment
        Environment updatedEnvironment = environmentRepository.findById(environment.getId()).get();
        updatedEnvironment
            .name(UPDATED_NAME)
            .url(UPDATED_URL)
            .user(UPDATED_USER)
            .pass(UPDATED_PASS);

        restEnvironmentMockMvc.perform(put("/api/environments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEnvironment)))
            .andExpect(status().isOk());

        // Validate the Environment in the database
        List<Environment> environmentList = environmentRepository.findAll();
        assertThat(environmentList).hasSize(databaseSizeBeforeUpdate);
        Environment testEnvironment = environmentList.get(environmentList.size() - 1);
        assertThat(testEnvironment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEnvironment.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testEnvironment.getUser()).isEqualTo(UPDATED_USER);
        assertThat(testEnvironment.getPass()).isEqualTo(UPDATED_PASS);

        // Validate the Environment in Elasticsearch
        verify(mockEnvironmentSearchRepository, times(1)).save(testEnvironment);
    }

    @Test
    public void updateNonExistingEnvironment() throws Exception {
        int databaseSizeBeforeUpdate = environmentRepository.findAll().size();

        // Create the Environment

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnvironmentMockMvc.perform(put("/api/environments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(environment)))
            .andExpect(status().isBadRequest());

        // Validate the Environment in the database
        List<Environment> environmentList = environmentRepository.findAll();
        assertThat(environmentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Environment in Elasticsearch
        verify(mockEnvironmentSearchRepository, times(0)).save(environment);
    }

    @Test
    public void deleteEnvironment() throws Exception {
        // Initialize the database
        environmentService.save(environment);

        int databaseSizeBeforeDelete = environmentRepository.findAll().size();

        // Delete the environment
        restEnvironmentMockMvc.perform(delete("/api/environments/{id}", environment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Environment> environmentList = environmentRepository.findAll();
        assertThat(environmentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Environment in Elasticsearch
        verify(mockEnvironmentSearchRepository, times(1)).deleteById(environment.getId());
    }

    @Test
    public void searchEnvironment() throws Exception {
        // Initialize the database
        environmentService.save(environment);
        when(mockEnvironmentSearchRepository.search(queryStringQuery("id:" + environment.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(environment), PageRequest.of(0, 1), 1));
        // Search the environment
        restEnvironmentMockMvc.perform(get("/api/_search/environments?query=id:" + environment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(environment.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].user").value(hasItem(DEFAULT_USER)))
            .andExpect(jsonPath("$.[*].pass").value(hasItem(DEFAULT_PASS)));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Environment.class);
        Environment environment1 = new Environment();
        environment1.setId("id1");
        Environment environment2 = new Environment();
        environment2.setId(environment1.getId());
        assertThat(environment1).isEqualTo(environment2);
        environment2.setId("id2");
        assertThat(environment1).isNotEqualTo(environment2);
        environment1.setId(null);
        assertThat(environment1).isNotEqualTo(environment2);
    }
}
