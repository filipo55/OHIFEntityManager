package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Experiment;
import com.mycompany.myapp.repository.ExperimentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Experiment}.
 */
@Service
public class ExperimentService {

    private final Logger log = LoggerFactory.getLogger(ExperimentService.class);

    private final ExperimentRepository experimentRepository;

    public ExperimentService(ExperimentRepository experimentRepository) {
        this.experimentRepository = experimentRepository;
    }

    /**
     * Save a experiment.
     *
     * @param experiment the entity to save.
     * @return the persisted entity.
     */
    public Experiment save(Experiment experiment) {
        log.debug("Request to save Experiment : {}", experiment);
        return experimentRepository.save(experiment);
    }

    /**
     * Get all the experiments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<Experiment> findAll(Pageable pageable) {
        log.debug("Request to get all Experiments");
        return experimentRepository.findAll(pageable);
    }


    /**
     * Get one experiment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<Experiment> findOne(String id) {
        log.debug("Request to get Experiment : {}", id);
        return experimentRepository.findById(id);
    }

    /**
     * Delete the experiment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Experiment : {}", id);
        experimentRepository.deleteById(id);
    }
}
