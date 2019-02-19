package com.ericsson.dev.web.rest;

import com.ericsson.dev.EdmApp;

import com.ericsson.dev.domain.CustomerState;
import com.ericsson.dev.repository.CustomerStateRepository;
import com.ericsson.dev.repository.search.CustomerStateSearchRepository;
import com.ericsson.dev.service.CustomerStateService;
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
 * Test class for the CustomerStateResource REST controller.
 *
 * @see CustomerStateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EdmApp.class)
public class CustomerStateResourceIntTest {

    private static final String DEFAULT_RUT = "AAAAAAAAAA";
    private static final String UPDATED_RUT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Boolean DEFAULT_BLACK_LIST = false;
    private static final Boolean UPDATED_BLACK_LIST = true;

    private static final Boolean DEFAULT_WHITE_LIST = false;
    private static final Boolean UPDATED_WHITE_LIST = true;

    @Autowired
    private CustomerStateRepository customerStateRepository;

    @Autowired
    private CustomerStateService customerStateService;

    /**
     * This repository is mocked in the com.ericsson.dev.repository.search test package.
     *
     * @see com.ericsson.dev.repository.search.CustomerStateSearchRepositoryMockConfiguration
     */
    @Autowired
    private CustomerStateSearchRepository mockCustomerStateSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restCustomerStateMockMvc;

    private CustomerState customerState;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CustomerStateResource customerStateResource = new CustomerStateResource(customerStateService);
        this.restCustomerStateMockMvc = MockMvcBuilders.standaloneSetup(customerStateResource)
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
    public static CustomerState createEntity() {
        CustomerState customerState = new CustomerState()
            .rut(DEFAULT_RUT)
            .active(DEFAULT_ACTIVE)
            .blackList(DEFAULT_BLACK_LIST)
            .whiteList(DEFAULT_WHITE_LIST);
        return customerState;
    }

    @Before
    public void initTest() {
        customerStateRepository.deleteAll();
        customerState = createEntity();
    }

    @Test
    public void createCustomerState() throws Exception {
        int databaseSizeBeforeCreate = customerStateRepository.findAll().size();

        // Create the CustomerState
        restCustomerStateMockMvc.perform(post("/api/customer-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerState)))
            .andExpect(status().isCreated());

        // Validate the CustomerState in the database
        List<CustomerState> customerStateList = customerStateRepository.findAll();
        assertThat(customerStateList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerState testCustomerState = customerStateList.get(customerStateList.size() - 1);
        assertThat(testCustomerState.getRut()).isEqualTo(DEFAULT_RUT);
        assertThat(testCustomerState.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testCustomerState.isBlackList()).isEqualTo(DEFAULT_BLACK_LIST);
        assertThat(testCustomerState.isWhiteList()).isEqualTo(DEFAULT_WHITE_LIST);

        // Validate the CustomerState in Elasticsearch
        verify(mockCustomerStateSearchRepository, times(1)).save(testCustomerState);
    }

    @Test
    public void createCustomerStateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customerStateRepository.findAll().size();

        // Create the CustomerState with an existing ID
        customerState.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerStateMockMvc.perform(post("/api/customer-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerState)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerState in the database
        List<CustomerState> customerStateList = customerStateRepository.findAll();
        assertThat(customerStateList).hasSize(databaseSizeBeforeCreate);

        // Validate the CustomerState in Elasticsearch
        verify(mockCustomerStateSearchRepository, times(0)).save(customerState);
    }

    @Test
    public void getAllCustomerStates() throws Exception {
        // Initialize the database
        customerStateRepository.save(customerState);

        // Get all the customerStateList
        restCustomerStateMockMvc.perform(get("/api/customer-states?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerState.getId())))
            .andExpect(jsonPath("$.[*].rut").value(hasItem(DEFAULT_RUT.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].blackList").value(hasItem(DEFAULT_BLACK_LIST.booleanValue())))
            .andExpect(jsonPath("$.[*].whiteList").value(hasItem(DEFAULT_WHITE_LIST.booleanValue())));
    }
    
    @Test
    public void getCustomerState() throws Exception {
        // Initialize the database
        customerStateRepository.save(customerState);

        // Get the customerState
        restCustomerStateMockMvc.perform(get("/api/customer-states/{id}", customerState.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customerState.getId()))
            .andExpect(jsonPath("$.rut").value(DEFAULT_RUT.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.blackList").value(DEFAULT_BLACK_LIST.booleanValue()))
            .andExpect(jsonPath("$.whiteList").value(DEFAULT_WHITE_LIST.booleanValue()));
    }

    @Test
    public void getNonExistingCustomerState() throws Exception {
        // Get the customerState
        restCustomerStateMockMvc.perform(get("/api/customer-states/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateCustomerState() throws Exception {
        // Initialize the database
        customerStateService.save(customerState);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockCustomerStateSearchRepository);

        int databaseSizeBeforeUpdate = customerStateRepository.findAll().size();

        // Update the customerState
        CustomerState updatedCustomerState = customerStateRepository.findById(customerState.getId()).get();
        updatedCustomerState
            .rut(UPDATED_RUT)
            .active(UPDATED_ACTIVE)
            .blackList(UPDATED_BLACK_LIST)
            .whiteList(UPDATED_WHITE_LIST);

        restCustomerStateMockMvc.perform(put("/api/customer-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCustomerState)))
            .andExpect(status().isOk());

        // Validate the CustomerState in the database
        List<CustomerState> customerStateList = customerStateRepository.findAll();
        assertThat(customerStateList).hasSize(databaseSizeBeforeUpdate);
        CustomerState testCustomerState = customerStateList.get(customerStateList.size() - 1);
        assertThat(testCustomerState.getRut()).isEqualTo(UPDATED_RUT);
        assertThat(testCustomerState.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testCustomerState.isBlackList()).isEqualTo(UPDATED_BLACK_LIST);
        assertThat(testCustomerState.isWhiteList()).isEqualTo(UPDATED_WHITE_LIST);

        // Validate the CustomerState in Elasticsearch
        verify(mockCustomerStateSearchRepository, times(1)).save(testCustomerState);
    }

    @Test
    public void updateNonExistingCustomerState() throws Exception {
        int databaseSizeBeforeUpdate = customerStateRepository.findAll().size();

        // Create the CustomerState

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerStateMockMvc.perform(put("/api/customer-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerState)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerState in the database
        List<CustomerState> customerStateList = customerStateRepository.findAll();
        assertThat(customerStateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CustomerState in Elasticsearch
        verify(mockCustomerStateSearchRepository, times(0)).save(customerState);
    }

    @Test
    public void deleteCustomerState() throws Exception {
        // Initialize the database
        customerStateService.save(customerState);

        int databaseSizeBeforeDelete = customerStateRepository.findAll().size();

        // Delete the customerState
        restCustomerStateMockMvc.perform(delete("/api/customer-states/{id}", customerState.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CustomerState> customerStateList = customerStateRepository.findAll();
        assertThat(customerStateList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CustomerState in Elasticsearch
        verify(mockCustomerStateSearchRepository, times(1)).deleteById(customerState.getId());
    }

    @Test
    public void searchCustomerState() throws Exception {
        // Initialize the database
        customerStateService.save(customerState);
        when(mockCustomerStateSearchRepository.search(queryStringQuery("id:" + customerState.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(customerState), PageRequest.of(0, 1), 1));
        // Search the customerState
        restCustomerStateMockMvc.perform(get("/api/_search/customer-states?query=id:" + customerState.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerState.getId())))
            .andExpect(jsonPath("$.[*].rut").value(hasItem(DEFAULT_RUT)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].blackList").value(hasItem(DEFAULT_BLACK_LIST.booleanValue())))
            .andExpect(jsonPath("$.[*].whiteList").value(hasItem(DEFAULT_WHITE_LIST.booleanValue())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerState.class);
        CustomerState customerState1 = new CustomerState();
        customerState1.setId("id1");
        CustomerState customerState2 = new CustomerState();
        customerState2.setId(customerState1.getId());
        assertThat(customerState1).isEqualTo(customerState2);
        customerState2.setId("id2");
        assertThat(customerState1).isNotEqualTo(customerState2);
        customerState1.setId(null);
        assertThat(customerState1).isNotEqualTo(customerState2);
    }
}
