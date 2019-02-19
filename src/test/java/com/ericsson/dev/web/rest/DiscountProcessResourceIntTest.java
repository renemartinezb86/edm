package com.ericsson.dev.web.rest;

import com.ericsson.dev.EdmApp;

import com.ericsson.dev.domain.DiscountProcess;
import com.ericsson.dev.repository.DiscountProcessRepository;
import com.ericsson.dev.repository.search.DiscountProcessSearchRepository;
import com.ericsson.dev.service.DiscountProcessService;
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

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Test class for the DiscountProcessResource REST controller.
 *
 * @see DiscountProcessResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EdmApp.class)
public class DiscountProcessResourceIntTest {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Instant DEFAULT_DATE_TO_PROCESS = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_TO_PROCESS = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_SQL_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_SQL_FILE_PATH = "BBBBBBBBBB";

    @Autowired
    private DiscountProcessRepository discountProcessRepository;

    @Autowired
    private DiscountProcessService discountProcessService;

    /**
     * This repository is mocked in the com.ericsson.dev.repository.search test package.
     *
     * @see com.ericsson.dev.repository.search.DiscountProcessSearchRepositoryMockConfiguration
     */
    @Autowired
    private DiscountProcessSearchRepository mockDiscountProcessSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restDiscountProcessMockMvc;

    private DiscountProcess discountProcess;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DiscountProcessResource discountProcessResource = new DiscountProcessResource(discountProcessService);
        this.restDiscountProcessMockMvc = MockMvcBuilders.standaloneSetup(discountProcessResource)
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
    public static DiscountProcess createEntity() {
        DiscountProcess discountProcess = new DiscountProcess()
            .quantity(DEFAULT_QUANTITY)
            .dateToProcess(DEFAULT_DATE_TO_PROCESS)
            .createdDate(DEFAULT_CREATED_DATE)
            .sqlFilePath(DEFAULT_SQL_FILE_PATH);
        return discountProcess;
    }

    @Before
    public void initTest() {
        discountProcessRepository.deleteAll();
        discountProcess = createEntity();
    }

    @Test
    public void createDiscountProcess() throws Exception {
        int databaseSizeBeforeCreate = discountProcessRepository.findAll().size();

        // Create the DiscountProcess
        restDiscountProcessMockMvc.perform(post("/api/discount-processes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discountProcess)))
            .andExpect(status().isCreated());

        // Validate the DiscountProcess in the database
        List<DiscountProcess> discountProcessList = discountProcessRepository.findAll();
        assertThat(discountProcessList).hasSize(databaseSizeBeforeCreate + 1);
        DiscountProcess testDiscountProcess = discountProcessList.get(discountProcessList.size() - 1);
        assertThat(testDiscountProcess.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testDiscountProcess.getDateToProcess()).isEqualTo(DEFAULT_DATE_TO_PROCESS);
        assertThat(testDiscountProcess.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testDiscountProcess.getSqlFilePath()).isEqualTo(DEFAULT_SQL_FILE_PATH);

        // Validate the DiscountProcess in Elasticsearch
        verify(mockDiscountProcessSearchRepository, times(1)).save(testDiscountProcess);
    }

    @Test
    public void createDiscountProcessWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = discountProcessRepository.findAll().size();

        // Create the DiscountProcess with an existing ID
        discountProcess.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restDiscountProcessMockMvc.perform(post("/api/discount-processes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discountProcess)))
            .andExpect(status().isBadRequest());

        // Validate the DiscountProcess in the database
        List<DiscountProcess> discountProcessList = discountProcessRepository.findAll();
        assertThat(discountProcessList).hasSize(databaseSizeBeforeCreate);

        // Validate the DiscountProcess in Elasticsearch
        verify(mockDiscountProcessSearchRepository, times(0)).save(discountProcess);
    }

    @Test
    public void getAllDiscountProcesses() throws Exception {
        // Initialize the database
        discountProcessRepository.save(discountProcess);

        // Get all the discountProcessList
        restDiscountProcessMockMvc.perform(get("/api/discount-processes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discountProcess.getId())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].dateToProcess").value(hasItem(DEFAULT_DATE_TO_PROCESS.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].sqlFilePath").value(hasItem(DEFAULT_SQL_FILE_PATH.toString())));
    }
    
    @Test
    public void getDiscountProcess() throws Exception {
        // Initialize the database
        discountProcessRepository.save(discountProcess);

        // Get the discountProcess
        restDiscountProcessMockMvc.perform(get("/api/discount-processes/{id}", discountProcess.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(discountProcess.getId()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.dateToProcess").value(DEFAULT_DATE_TO_PROCESS.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.sqlFilePath").value(DEFAULT_SQL_FILE_PATH.toString()));
    }

    @Test
    public void getNonExistingDiscountProcess() throws Exception {
        // Get the discountProcess
        restDiscountProcessMockMvc.perform(get("/api/discount-processes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateDiscountProcess() throws Exception {
        // Initialize the database
        discountProcessService.save(discountProcess);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockDiscountProcessSearchRepository);

        int databaseSizeBeforeUpdate = discountProcessRepository.findAll().size();

        // Update the discountProcess
        DiscountProcess updatedDiscountProcess = discountProcessRepository.findById(discountProcess.getId()).get();
        updatedDiscountProcess
            .quantity(UPDATED_QUANTITY)
            .dateToProcess(UPDATED_DATE_TO_PROCESS)
            .createdDate(UPDATED_CREATED_DATE)
            .sqlFilePath(UPDATED_SQL_FILE_PATH);

        restDiscountProcessMockMvc.perform(put("/api/discount-processes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDiscountProcess)))
            .andExpect(status().isOk());

        // Validate the DiscountProcess in the database
        List<DiscountProcess> discountProcessList = discountProcessRepository.findAll();
        assertThat(discountProcessList).hasSize(databaseSizeBeforeUpdate);
        DiscountProcess testDiscountProcess = discountProcessList.get(discountProcessList.size() - 1);
        assertThat(testDiscountProcess.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testDiscountProcess.getDateToProcess()).isEqualTo(UPDATED_DATE_TO_PROCESS);
        assertThat(testDiscountProcess.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testDiscountProcess.getSqlFilePath()).isEqualTo(UPDATED_SQL_FILE_PATH);

        // Validate the DiscountProcess in Elasticsearch
        verify(mockDiscountProcessSearchRepository, times(1)).save(testDiscountProcess);
    }

    @Test
    public void updateNonExistingDiscountProcess() throws Exception {
        int databaseSizeBeforeUpdate = discountProcessRepository.findAll().size();

        // Create the DiscountProcess

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiscountProcessMockMvc.perform(put("/api/discount-processes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discountProcess)))
            .andExpect(status().isBadRequest());

        // Validate the DiscountProcess in the database
        List<DiscountProcess> discountProcessList = discountProcessRepository.findAll();
        assertThat(discountProcessList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DiscountProcess in Elasticsearch
        verify(mockDiscountProcessSearchRepository, times(0)).save(discountProcess);
    }

    @Test
    public void deleteDiscountProcess() throws Exception {
        // Initialize the database
        discountProcessService.save(discountProcess);

        int databaseSizeBeforeDelete = discountProcessRepository.findAll().size();

        // Delete the discountProcess
        restDiscountProcessMockMvc.perform(delete("/api/discount-processes/{id}", discountProcess.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DiscountProcess> discountProcessList = discountProcessRepository.findAll();
        assertThat(discountProcessList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DiscountProcess in Elasticsearch
        verify(mockDiscountProcessSearchRepository, times(1)).deleteById(discountProcess.getId());
    }

    @Test
    public void searchDiscountProcess() throws Exception {
        // Initialize the database
        discountProcessService.save(discountProcess);
        when(mockDiscountProcessSearchRepository.search(queryStringQuery("id:" + discountProcess.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(discountProcess), PageRequest.of(0, 1), 1));
        // Search the discountProcess
        restDiscountProcessMockMvc.perform(get("/api/_search/discount-processes?query=id:" + discountProcess.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discountProcess.getId())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].dateToProcess").value(hasItem(DEFAULT_DATE_TO_PROCESS.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].sqlFilePath").value(hasItem(DEFAULT_SQL_FILE_PATH)));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DiscountProcess.class);
        DiscountProcess discountProcess1 = new DiscountProcess();
        discountProcess1.setId("id1");
        DiscountProcess discountProcess2 = new DiscountProcess();
        discountProcess2.setId(discountProcess1.getId());
        assertThat(discountProcess1).isEqualTo(discountProcess2);
        discountProcess2.setId("id2");
        assertThat(discountProcess1).isNotEqualTo(discountProcess2);
        discountProcess1.setId(null);
        assertThat(discountProcess1).isNotEqualTo(discountProcess2);
    }
}
