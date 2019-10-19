package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Subject;
import com.mycompany.myapp.repository.SubjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Subject}.
 */
@Service
public class SubjectService {

    private final Logger log = LoggerFactory.getLogger(SubjectService.class);

    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    /**
     * Save a subject.
     *
     * @param subject the entity to save.
     * @return the persisted entity.
     */
    public Subject save(Subject subject) {
        log.debug("Request to save Subject : {}", subject);
        return subjectRepository.save(subject);
    }

    /**
     * Get all the subjects.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<Subject> findAll(Pageable pageable) {
        log.debug("Request to get all Subjects");
        return subjectRepository.findAll(pageable);
    }


    /**
     * Get one subject by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<Subject> findOne(String id) {
        log.debug("Request to get Subject : {}", id);
        return subjectRepository.findById(id);
    }

    /**
     * Delete the subject by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Subject : {}", id);
        subjectRepository.deleteById(id);
    }
}
