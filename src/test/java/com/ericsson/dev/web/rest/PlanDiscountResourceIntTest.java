package com.ericsson.dev.web.rest;

import com.ericsson.dev.EdmApp;

import com.ericsson.dev.domain.PlanDiscount;
import com.ericsson.dev.repository.PlanDiscountRepository;
import com.ericsson.dev.repository.search.PlanDiscountSearchRepository;
import com.ericsson.dev.service.PlanDiscountService;
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
 * Test class for the PlanDiscountResource REST controller.
 *
 * @see PlanDiscountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EdmApp.class)
public class PlanDiscountResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_POSITION = 1;
    private static final Integer UPDATED_POSITION = 2;

    private static final Double DEFAULT_DISCOUNT_PERCENTAGE = 1D;
    private static final Double UPDATED_DISCOUNT_PERCENTAGE = 2D;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private PlanDiscountRepository planDiscountRepository;

    @Autowired
    private PlanDiscountService planDiscountService;

    /**
     * This repository is mocked in the com.ericsson.dev.repository.search test package.
     *
     * @see com.ericsson.dev.repository.search.PlanDiscountSearchRepositoryMockConfiguration
     */
    @Autowired
    private PlanDiscountSearchRepository mockPlanDiscountSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restPlanDiscountMockMvc;

    private PlanDiscount planDiscount;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlanDiscountResource planDiscountResource = new PlanDiscountResource(planDiscountService);
        this.restPlanDiscountMockMvc = MockMvcBuilders.standaloneSetup(planDiscountResource)
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
    public static PlanDiscount createEntity() {
        PlanDiscount planDiscount = new PlanDiscount()
            .name(DEFAULT_NAME)
            .position(DEFAULT_POSITION)
            .discountPercentage(DEFAULT_DISCOUNT_PERCENTAGE)
            .active(DEFAULT_ACTIVE);
        return planDiscount;
    }

    @Before
    public void initTest() {
        planDiscountRepository.deleteAll();
        planDiscount = createEntity();
    }

    @Test
    public void createPlanDiscount() throws Exception {
        int databaseSizeBeforeCreate = planDiscountRepository.findAll().size();

        // Create the PlanDiscount
        restPlanDiscountMockMvc.perform(post("/api/plan-discounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planDiscount)))
            .andExpect(status().isCreated());

        // Validate the PlanDiscount in the database
        List<PlanDiscount> planDiscountList = planDiscountRepository.findAll();
        assertThat(planDiscountList).hasSize(databaseSizeBeforeCreate + 1);
        PlanDiscount testPlanDiscount = planDiscountList.get(planDiscountList.size() - 1);
        assertThat(testPlanDiscount.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPlanDiscount.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testPlanDiscount.getDiscountPercentage()).isEqualTo(DEFAULT_DISCOUNT_PERCENTAGE);
        assertThat(testPlanDiscount.isActive()).isEqualTo(DEFAULT_ACTIVE);

        // Validate the PlanDiscount in Elasticsearch
        verify(mockPlanDiscountSearchRepository, times(1)).save(testPlanDiscount);
    }

    @Test
    public void createPlanDiscountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = planDiscountRepository.findAll().size();

        // Create the PlanDiscount with an existing ID
        planDiscount.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanDiscountMockMvc.perform(post("/api/plan-discounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planDiscount)))
            .andExpect(status().isBadRequest());

        // Validate the PlanDiscount in the database
        List<PlanDiscount> planDiscountList = planDiscountRepository.findAll();
        assertThat(planDiscountList).hasSize(databaseSizeBeforeCreate);

        // Validate the PlanDiscount in Elasticsearch
        verify(mockPlanDiscountSearchRepository, times(0)).save(planDiscount);
    }

    @Test
    public void getAllPlanDiscounts() throws Exception {
        // Initialize the database
        planDiscountRepository.save(planDiscount);

        // Get all the planDiscountList
        restPlanDiscountMockMvc.perform(get("/api/plan-discounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planDiscount.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].discountPercentage").value(hasItem(DEFAULT_DISCOUNT_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    
    @Test
    public void getPlanDiscount() throws Exception {
        // Initialize the database
        planDiscountRepository.save(planDiscount);

        // Get the planDiscount
        restPlanDiscountMockMvc.perform(get("/api/plan-discounts/{id}", planDiscount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(planDiscount.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION))
            .andExpect(jsonPath("$.discountPercentage").value(DEFAULT_DISCOUNT_PERCENTAGE.doubleValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    public void getNonExistingPlanDiscount() throws Exception {
        // Get the planDiscount
        restPlanDiscountMockMvc.perform(get("/api/plan-discounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updatePlanDiscount() throws Exception {
        // Initialize the database
        planDiscountService.save(planDiscount);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockPlanDiscountSearchRepository);

        int databaseSizeBeforeUpdate = planDiscountRepository.findAll().size();

        // Update the planDiscount
        PlanDiscount updatedPlanDiscount = planDiscountRepository.findById(planDiscount.getId()).get();
        updatedPlanDiscount
            .name(UPDATED_NAME)
            .position(UPDATED_POSITION)
            .discountPercentage(UPDATED_DISCOUNT_PERCENTAGE)
            .active(UPDATED_ACTIVE);

        restPlanDiscountMockMvc.perform(put("/api/plan-discounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPlanDiscount)))
            .andExpect(status().isOk());

        // Validate the PlanDiscount in the database
        List<PlanDiscount> planDiscountList = planDiscountRepository.findAll();
        assertThat(planDiscountList).hasSize(databaseSizeBeforeUpdate);
        PlanDiscount testPlanDiscount = planDiscountList.get(planDiscountList.size() - 1);
        assertThat(testPlanDiscount.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPlanDiscount.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testPlanDiscount.getDiscountPercentage()).isEqualTo(UPDATED_DISCOUNT_PERCENTAGE);
        assertThat(testPlanDiscount.isActive()).isEqualTo(UPDATED_ACTIVE);

        // Validate the PlanDiscount in Elasticsearch
        verify(mockPlanDiscountSearchRepository, times(1)).save(testPlanDiscount);
    }

    @Test
    public void updateNonExistingPlanDiscount() throws Exception {
        int databaseSizeBeforeUpdate = planDiscountRepository.findAll().size();

        // Create the PlanDiscount

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanDiscountMockMvc.perform(put("/api/plan-discounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planDiscount)))
            .andExpect(status().isBadRequest());

        // Validate the PlanDiscount in the database
        List<PlanDiscount> planDiscountList = planDiscountRepository.findAll();
        assertThat(planDiscountList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PlanDiscount in Elasticsearch
        verify(mockPlanDiscountSearchRepository, times(0)).save(planDiscount);
    }

    @Test
    public void deletePlanDiscount() throws Exception {
        // Initialize the database
        planDiscountService.save(planDiscount);

        int databaseSizeBeforeDelete = planDiscountRepository.findAll().size();

        // Delete the planDiscount
        restPlanDiscountMockMvc.perform(delete("/api/plan-discounts/{id}", planDiscount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PlanDiscount> planDiscountList = planDiscountRepository.findAll();
        assertThat(planDiscountList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PlanDiscount in Elasticsearch
        verify(mockPlanDiscountSearchRepository, times(1)).deleteById(planDiscount.getId());
    }

    @Test
    public void searchPlanDiscount() throws Exception {
        // Initialize the database
        planDiscountService.save(planDiscount);
        when(mockPlanDiscountSearchRepository.search(queryStringQuery("id:" + planDiscount.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(planDiscount), PageRequest.of(0, 1), 1));
        // Search the planDiscount
        restPlanDiscountMockMvc.perform(get("/api/_search/plan-discounts?query=id:" + planDiscount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planDiscount.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].discountPercentage").value(hasItem(DEFAULT_DISCOUNT_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlanDiscount.class);
        PlanDiscount planDiscount1 = new PlanDiscount();
        planDiscount1.setId("id1");
        PlanDiscount planDiscount2 = new PlanDiscount();
        planDiscount2.setId(planDiscount1.getId());
        assertThat(planDiscount1).isEqualTo(planDiscount2);
        planDiscount2.setId("id2");
        assertThat(planDiscount1).isNotEqualTo(planDiscount2);
        planDiscount1.setId(null);
        assertThat(planDiscount1).isNotEqualTo(planDiscount2);
    }
}
