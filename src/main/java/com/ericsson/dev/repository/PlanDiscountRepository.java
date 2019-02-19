package com.ericsson.dev.repository;

import com.ericsson.dev.domain.PlanDiscount;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data MongoDB repository for the PlanDiscount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlanDiscountRepository extends MongoRepository<PlanDiscount, String> {

    Optional<PlanDiscount> findByNameAndPositionAndActiveTrue(String name, Integer position);

    List<PlanDiscount> findAllByName(String name);
}
