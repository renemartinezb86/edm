package com.ericsson.dev.service.impl;

import com.ericsson.dev.domain.Discounts;
import com.ericsson.dev.repository.CustomerStateRepository;
import com.ericsson.dev.service.BSCSDataService;
import com.ericsson.dev.service.DiscountProcessService;
import com.ericsson.dev.domain.DiscountProcess;
import com.ericsson.dev.repository.DiscountProcessRepository;
import com.ericsson.dev.repository.search.DiscountProcessSearchRepository;
import com.ericsson.dev.service.OracleConnection;
import com.ericsson.dev.service.PlanDiscountService;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
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

    @Override
    public List<String> getDiscountsSQL(DiscountProcess discountProcess) {
        OracleConnection connection = new OracleConnection(discountProcess.getEnvironment().getUrl(), discountProcess.getEnvironment().getUser(), discountProcess.getEnvironment().getPass());
        if (customerPlans == null) {
            customerPlans = bscsDataService.getCustomerPlans(connection);
        }
        List<String> discountSQLs = new ArrayList<>();
        for (String cuenta : customerPlans.keySet()) {
            List<Discounts> discountsList = getPlansToDiscount(cuenta);
            //Sort by price asc
            discountsList.sort(new Discounts.SortByPrice());
            //Removing from discounts the plan with highest value
            discountsList = discountsList.subList(0, discountsList.size() - 1);
            //If customer is not whilisted (may apply infinites discounts)
            if (!customerStateRepository.findByCuentaAndWhiteListTrue(cuenta).isPresent()) {
                //If max discount applicable is greater than current discountable plans
                if (discountProcess.getQuantity() < discountsList.size()) {
                    discountsList = discountsList.subList(0, discountProcess.getQuantity());
                }
            }
            for (Discounts discount : discountsList) {
                Double discountPercentage = planDiscountService.getDiscountPercentage(discount.getPlanName(), discountsList.indexOf(discount) + 1);
                if (discountPercentage != null) {
                    discount.setPercentage(planDiscountService.getDiscountPercentage(discount.getPlanName(), discountsList.indexOf(discount) + 1));
                    LocalDateTime ldt1 = LocalDateTime.ofInstant(discountProcess.getDateToProcess(), ZoneId.systemDefault());
                    YearMonth yearMonthObject = YearMonth.of(ldt1.getYear(), ldt1.getMonth());
                    discount.setFactor(yearMonthObject.lengthOfMonth());
                    discountSQLs.add(generateCustomerDiscounts(discount, discountProcess.getDateToProcess()));
                }
            }
        }
        try {
            bscsDataService.executeBatch(connection, discountSQLs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discountSQLs;
    }

    @Override
    public String generateCustomerDiscounts(Discounts discount, Instant dateToProcess) {
        float discountAmount = (float) (discount.getPrice() * discount.getPercentage() / 100 / discount.getFactor());
        String singleDiscount = "";
        if (discount.getLastBillDate().after(Date.from(dateToProcess))) {
            singleDiscount = "update fees\n" +
                "set    amount = " + discountAmount + "\n" +
                "where  customer_id = " + discount.getCuenta() + "\n" +
                "and    co_id = " + discount.getContrato() + "\n" +
                "and    seqno = (select MAX_FEES_SEQ.currtval from dual)\n" +
                "and    period = 1;\n";
        } else {
            singleDiscount = "insert into fees \n" +
                "values\n" +
                "( " + discount.getCuenta() + ", (select MAX_FEES_SEQ.nextval from dual), 'R', " + discountAmount + ", pendiente definicion, null, sysdate, 1, 'SYSADM', sysdate, null, null, pendiente definicion\n" +
                ", pendiente definicion, pendiente definicion, v_co_id, null, 44, null, null, null, null, 0, null, null, null, null, 3, (select max(vscode) from rateplan_version where tmcode = 3)\n" +
                ", pendiente definicion, pendiente definicion, pendiente definicion, 3, null, null, null, null, null, null, null, null, null, null, null, null, " + discount.getCuenta() + ", null, 1, null, null\n" +
                ", 0, null, null, null, null, null);\n";
        }
        return singleDiscount;
    }

    @Override
    public List<Discounts> getPlansToDiscount(String cuenta) {
        List<Discounts> result = new ArrayList<>();
        if (!customerStateRepository.findByCuentaAndBlackListTrue(cuenta).isPresent()) {
            List<HashMap> plans = customerPlans.get(cuenta);
            for (HashMap planInfo : plans) {
                Date lastBillDate = (Date) planInfo.get("FECHA_ULTIMO_FACTURAMENTO");
                Discounts discounts = new Discounts();
                discounts.setCuenta(cuenta);
                discounts.setLastBillDate(lastBillDate);
                discounts.setPlanName(planInfo.get("NOMBRE_PLANO").toString());
                discounts.setPrice(Float.parseFloat(planInfo.get("PRECIO_MESUAL").toString()));
                result.add(discounts);
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
