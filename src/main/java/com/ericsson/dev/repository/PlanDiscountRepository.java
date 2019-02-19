package com.ericsson.dev.repository;

import com.ericsson.dev.domain.PlanDiscount;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the PlanDiscount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlanDiscountRepository extends MongoRepository<PlanDiscount, String> {

}
