package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Descriptor;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Descriptor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DescriptorRepository extends MongoRepository<Descriptor, String> {

}
