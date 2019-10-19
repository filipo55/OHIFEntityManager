package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Experiment;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Experiment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExperimentRepository extends MongoRepository<Experiment, String> {

}
