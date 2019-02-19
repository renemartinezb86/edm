package com.ericsson.dev.service.impl;

import com.ericsson.dev.domain.Discounts;
import com.ericsson.dev.repository.CustomerStateRepository;
import com.ericsson.dev.service.BSCSDataService;
import com.ericsson.dev.service.DiscountProcessService;
import com.ericsson.dev.domain.DiscountProcess;
import com.ericsson.dev.repository.DiscountProcessRepository;
import com.ericsson.dev.repository.search.DiscountProcessSearchRepository;
import com.ericsson.dev.service.PlanDiscountService;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing DiscountProcess.
 */
@Service
public class DiscountProcessServiceImpl implements DiscountProcessService {

    private final Logger log = LoggerFactory.getLogger(DiscountProcessServiceImpl.class);

    private final DiscountProcessRepository discountProcessRepository;

    private final DiscountProcessSearchRepository discountProcessSearchRepository;

    @Autowired
    private CustomerStateRepository customerStateRepository;

    public HashMap<String, List<HashMap>> customerPlans;

    @Autowired
    private BSCSDataService bscsDataService;

    @Autowired
    private PlanDiscountService planDiscountService;

    public DiscountProcessServiceImpl(DiscountProcessRepository discountProcessRepository, DiscountProcessSearchRepository discountProcessSearchRepository) {
        this.discountProcessRepository = discountProcessRepository;
        this.discountProcessSearchRepository = discountProcessSearchRepository;
        customerPlans = bscsDataService.getCustomerPlans();
    }

    /**
     * Save a discountProcess.
     *
     * @param discountProcess the entity to save
     * @return the persisted entity
     */
    @Override
    public DiscountProcess save(DiscountProcess discountProcess) {
        log.debug("Request to save DiscountProcess : {}", discountProcess);
        DiscountProcess result = discountProcessRepository.save(discountProcess);
        discountProcessSearchRepository.save(result);
        return result;
    }

    public List<String> getDiscountsSQL(DiscountProcess discountProcess) {
        List<String> discountSQLs = new ArrayList<>();
        for (String rut : customerPlans.keySet()) {
            List<Discounts> discountsList = getPlansToDiscount(rut);
            discountsList.sort(new Discounts.SortByPrice());
            if (!customerStateRepository.findByRutAndWhiteListTrue(rut).isPresent()) {
                discountsList = discountsList.subList(0, discountProcess.getQuantity());
            }
            for (Discounts discount : discountsList) {
                discount.setPercentage(planDiscountService.getDiscountPercentage(discount.getPlanName(), discountsList.indexOf(discount) + 1));
            }
            discountSQLs.addAll(generateCustomerDiscounts(discountsList));
        }
        return discountSQLs;
    }

    public List<String> generateCustomerDiscounts(List<Discounts> discountsList) {
        List<String> discountSQLs = new ArrayList<>();
        String singleDiscount = "";
        discountSQLs.add(singleDiscount);
        return discountSQLs;
    }

    public List<Discounts> getPlansToDiscount(String rut) {
        List<Discounts> result = new ArrayList<>();
        if (customerStateRepository.findByRutAndBlackListFalse(rut).isPresent()) {
            List<HashMap> plans = customerPlans.get(rut);
            for (HashMap planInfo : plans) {
                Discounts discounts = new Discounts();
                discounts.setRut(rut);
                discounts.setPlanName(planInfo.get("plan").toString());
                discounts.setPrice(Float.parseFloat(planInfo.get("price").toString()));
            }
        }
        return result;
    }

    /**
     * Get all the discountProcesses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<DiscountProcess> findAll(Pageable pageable) {
        log.debug("Request to get all DiscountProcesses");
        return discountProcessRepository.findAll(pageable);
    }


    /**
     * Get one discountProcess by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<DiscountProcess> findOne(String id) {
        log.debug("Request to get DiscountProcess : {}", id);
        return discountProcessRepository.findById(id);
    }

    /**
     * Delete the discountProcess by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete DiscountProcess : {}", id);
        discountProcessRepository.deleteById(id);
        discountProcessSearchRepository.deleteById(id);
    }

    /**
     * Search for the discountProcess corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<DiscountProcess> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DiscountProcesses for query {}", query);
        return discountProcessSearchRepository.search(queryStringQuery(query), pageable);
    }
}
