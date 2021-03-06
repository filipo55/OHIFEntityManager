package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Descriptor;
import com.mycompany.myapp.repository.DescriptorRepository;
import org.checkerframework.checker.nullness.Opt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Descriptor}.
 */
@Service
public class DescriptorService {

    private final Logger log = LoggerFactory.getLogger(DescriptorService.class);

    private final DescriptorRepository descriptorRepository;

    public DescriptorService(DescriptorRepository descriptorRepository) {
        this.descriptorRepository = descriptorRepository;
    }

    /**
     * Save a descriptor.
     *
     * @param descriptor the entity to save.
     * @return the persisted entity.
     */
    public Descriptor save(Descriptor descriptor) {
        log.debug("Request to save Descriptor : {}", descriptor);
        return descriptorRepository.save(descriptor);
    }

    /**
     * Get all the descriptors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<Descriptor> findAll(Pageable pageable) {
        log.debug("Request to get all Descriptors");
        return descriptorRepository.findAll(pageable);
    }


    /**
     * Get one descriptor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<Descriptor> findOne(String id) {
        log.debug("Request to get Descriptor : {}", id);
        return descriptorRepository.findById(id);
    }

    /**
     * Delete the descriptor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Descriptor : {}", id);
        descriptorRepository.deleteById(id);
    }

    public List<Descriptor> findDescriptorsWithMeasurement(String id)
    {
        log.debug("Request to get Measurement : {}", id);
        List<Descriptor> descriptors = descriptorRepository.findAll();
        List<Descriptor> result = new ArrayList<>();
        for(int i =0; i< descriptors.size();i++)
        {
            if(descriptors.get(i).getMeasurement().getId().matches(id))
            {
                result.add(descriptors.get(i));
            }
        }
        return result;
    }

    public void SyncDescriptors()
    {
        List<Descriptor> descriptors = descriptorRepository.findAll();
        for(int i =0; i< descriptors.size();i++)
        {
            if(descriptors.get(i).getMeasurement() == null)
                delete(descriptors.get(i).getId());
        }
    }
}
