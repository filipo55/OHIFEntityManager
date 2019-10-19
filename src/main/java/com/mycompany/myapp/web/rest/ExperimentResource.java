package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Experiment;
import com.mycompany.myapp.service.ExperimentService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Experiment}.
 */
@RestController
@RequestMapping("/api")
public class ExperimentResource {

    private final Logger log = LoggerFactory.getLogger(ExperimentResource.class);

    private static final String ENTITY_NAME = "experiment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExperimentService experimentService;

    public ExperimentResource(ExperimentService experimentService) {
        this.experimentService = experimentService;
    }

    /**
     * {@code POST  /experiments} : Create a new experiment.
     *
     * @param experiment the experiment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new experiment, or with status {@code 400 (Bad Request)} if the experiment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/experiments")
    public ResponseEntity<Experiment> createExperiment(@RequestBody Experiment experiment) throws URISyntaxException {
        log.debug("REST request to save Experiment : {}", experiment);
        if (experiment.getId() != null) {
            throw new BadRequestAlertException("A new experiment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Experiment result = experimentService.save(experiment);
        return ResponseEntity.created(new URI("/api/experiments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /experiments} : Updates an existing experiment.
     *
     * @param experiment the experiment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated experiment,
     * or with status {@code 400 (Bad Request)} if the experiment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the experiment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/experiments")
    public ResponseEntity<Experiment> updateExperiment(@RequestBody Experiment experiment) throws URISyntaxException {
        log.debug("REST request to update Experiment : {}", experiment);
        if (experiment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Experiment result = experimentService.save(experiment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, experiment.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /experiments} : get all the experiments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of experiments in body.
     */
    @GetMapping("/experiments")
    public ResponseEntity<List<Experiment>> getAllExperiments(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Experiments");
        Page<Experiment> page = experimentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /experiments/:id} : get the "id" experiment.
     *
     * @param id the id of the experiment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the experiment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/experiments/{id}")
    public ResponseEntity<Experiment> getExperiment(@PathVariable String id) {
        log.debug("REST request to get Experiment : {}", id);
        Optional<Experiment> experiment = experimentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(experiment);
    }

    /**
     * {@code DELETE  /experiments/:id} : delete the "id" experiment.
     *
     * @param id the id of the experiment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/experiments/{id}")
    public ResponseEntity<Void> deleteExperiment(@PathVariable String id) {
        log.debug("REST request to delete Experiment : {}", id);
        experimentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
