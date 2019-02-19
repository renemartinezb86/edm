package com.ericsson.dev.repository;

import com.ericsson.dev.domain.DiscountProcess;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the DiscountProcess entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DiscountProcessRepository extends MongoRepository<DiscountProcess, String> {

}
