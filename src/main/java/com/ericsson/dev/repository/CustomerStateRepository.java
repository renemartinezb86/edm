package com.ericsson.dev.repository;

import com.ericsson.dev.domain.CustomerState;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data MongoDB repository for the CustomerState entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerStateRepository extends MongoRepository<CustomerState, String> {
    Optional<CustomerState> findByCuentaAndBlackListFalse(String cuenta);

    Optional<CustomerState> findByCuentaAndWhiteListTrue(String cuenta);
}
