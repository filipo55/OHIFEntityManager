package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Descriptor;
import com.mycompany.myapp.domain.Experiment;
import com.mycompany.myapp.domain.Measurement;
import com.mycompany.myapp.domain.MeasurementType;
import com.mycompany.myapp.service.*;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Descriptor}.
 */
@RestController
@RequestMapping("/api")
public class DescriptorResource {

    private final Logger log = LoggerFactory.getLogger(DescriptorResource.class);

    private static final String ENTITY_NAME = "descriptor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DescriptorService descriptorService;

    @Autowired
    CalculationService calculationService;

    @Autowired
    ExperimentService experimentService;

    @Autowired
    MeasurementService measurementService;

    @Autowired
    SyncService syncService;

    public DescriptorResource(DescriptorService descriptorService) {
        this.descriptorService = descriptorService;
    }

    /**
     * {@code POST  /descriptors} : Create a new descriptor.
     *
     * @param JSON the descriptor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new descriptor, or with status {@code 400 (Bad Request)} if the descriptor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/descriptors")
    public ResponseEntity<Descriptor> createDescriptor(@RequestBody String JSON) throws URISyntaxException, IOException, SAXException, ParserConfigurationException, ParseException, JSONException {
        log.debug("REST request to save Descriptor : {}");

        //Sync with XNAT DB
        syncService.Sync();

        //Parse JSON
        JSONObject obj = new JSONObject(JSON);
        String projectID = obj.getString("projectId");
        String subjectID = obj.getString("subjectId");
        String experimentID = obj.getString("experimentId");
        int height = obj.getInt("voxelZ");
        String xml = obj.getString("xml");

        Experiment experiment = experimentService.findOneByXnatID(experimentID);
        List<String> measurements = calculationService.generateMeasurements(xml);

        for(int i =0; i<measurements.size();i++)
        {
            Measurement measurement = new Measurement();
            measurement.setExperiment(experiment);
            measurement.setName(measurements.get(i));
            if(measurements.get(i).contains("Lesion"))
            {
                measurement.setType(MeasurementType.LESION);
            }
            else if(measurements.get(i).contains("Cancer"))
            {
                measurement.setType(MeasurementType.CANCER);
                measurement = measurementService.save(measurement);
            }
            //Area descriptor
            double area = calculationService.CalculateDataFromFile(xml, measurement.getName());
            Descriptor areaDescriptor = new Descriptor();
            areaDescriptor.setName("Area");
            areaDescriptor.setValue((float) area);
            areaDescriptor.setMeasurement(measurement);
            descriptorService.save(areaDescriptor);

            //Volume descriptor
            Descriptor volumeDescriptor = new Descriptor();
            volumeDescriptor.setName("Volume");
            volumeDescriptor.setValue((float)calculationService.CalculateVolume(area,height));
            volumeDescriptor.setMeasurement(measurement);
            descriptorService.save(volumeDescriptor);
        }



        return (ResponseEntity<Descriptor>) ResponseEntity.ok();

//        return ResponseEntity.created(new URI("/api/descriptors/" + result.getId()))
//            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
//            .body(result);
    }

    /**
     * {@code PUT  /descriptors} : Updates an existing descriptor.
     *
     * @param descriptor the descriptor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated descriptor,
     * or with status {@code 400 (Bad Request)} if the descriptor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the descriptor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/descriptors")
    public ResponseEntity<Descriptor> updateDescriptor(@RequestBody Descriptor descriptor) throws URISyntaxException {
        log.debug("REST request to update Descriptor : {}", descriptor);
        if (descriptor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Descriptor result = descriptorService.save(descriptor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, descriptor.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /descriptors} : get all the descriptors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of descriptors in body.
     */
    @GetMapping("/descriptors")
    public ResponseEntity<List<Descriptor>> getAllDescriptors(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Descriptors");
        Page<Descriptor> page = descriptorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /descriptors/:id} : get the "id" descriptor.
     *
     * @param id the id of the descriptor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the descriptor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/descriptors/{id}")
    public ResponseEntity<Descriptor> getDescriptor(@PathVariable String id) {
        log.debug("REST request to get Descriptor : {}", id);
        Optional<Descriptor> descriptor = descriptorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(descriptor);
    }

    /**
     * {@code DELETE  /descriptors/:id} : delete the "id" descriptor.
     *
     * @param id the id of the descriptor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/descriptors/{id}")
    public ResponseEntity<Void> deleteDescriptor(@PathVariable String id) {
        log.debug("REST request to delete Descriptor : {}", id);
        descriptorService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
