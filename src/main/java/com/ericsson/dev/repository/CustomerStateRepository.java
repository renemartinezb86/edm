package com.ericsson.dev.repository;

import com.ericsson.dev.domain.CustomerState;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the CustomerState entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerStateRepository extends MongoRepository<CustomerState, String> {

}
