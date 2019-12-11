package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Measurement;
import com.mycompany.myapp.repository.MeasurementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Measurement}.
 */
@Service
public class MeasurementService {

    private final Logger log = LoggerFactory.getLogger(MeasurementService.class);

    private final MeasurementRepository measurementRepository;

    public MeasurementService(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    /**
     * Save a measurement.
     *
     * @param measurement the entity to save.
     * @return the persisted entity.
     */
    public Measurement save(Measurement measurement) {
        log.debug("Request to save Measurement : {}", measurement);
        return measurementRepository.save(measurement);
    }

    /**
     * Get all the measurements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<Measurement> findAll(Pageable pageable) {
        log.debug("Request to get all Measurements");
        return measurementRepository.findAll(pageable);
    }


    /**
     * Get one measurement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<Measurement> findOne(String id) {
        log.debug("Request to get Measurement : {}", id);
        return measurementRepository.findById(id);
    }

    /**
     * Delete the measurement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Measurement : {}", id);
        measurementRepository.deleteById(id);
    }

    public List<Measurement> findMeasurementsWithExperiment(String id)
    {
        log.debug("Request to get Experiment : {}", id);
        List<Measurement> measurements = measurementRepository.findAll();
        List<Measurement> result = new ArrayList<>();
        for(int i =0; i< measurements.size();i++)
        {
            if(measurements.get(i).getExperiment().getId().matches(id))
            {
                result.add(measurements.get(i));
            }
        }
        return result;
    }
}
