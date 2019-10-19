package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.OhifEntityManagerApp;
import com.mycompany.myapp.config.TestSecurityConfiguration;
import com.mycompany.myapp.domain.Experiment;
import com.mycompany.myapp.repository.ExperimentRepository;
import com.mycompany.myapp.service.ExperimentService;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link ExperimentResource} REST controller.
 */
@SpringBootTest(classes = {OhifEntityManagerApp.class, TestSecurityConfiguration.class})
public class ExperimentResourceIT {

    private static final String DEFAULT_XNAT_ID = "AAAAAAAAAA";
    private static final String UPDATED_XNAT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_CREATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATED = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private ExperimentRepository experimentRepository;

    @Autowired
    private ExperimentService experimentService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restExperimentMockMvc;

    private Experiment experiment;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExperimentResource experimentResource = new ExperimentResource(experimentService);
        this.restExperimentMockMvc = MockMvcBuilders.standaloneSetup(experimentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Experiment createEntity() {
        Experiment experiment = new Experiment()
            .xnatId(DEFAULT_XNAT_ID)
            .name(DEFAULT_NAME)
            .dateCreated(DEFAULT_DATE_CREATED);
        return experiment;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Experiment createUpdatedEntity() {
        Experiment experiment = new Experiment()
            .xnatId(UPDATED_XNAT_ID)
            .name(UPDATED_NAME)
            .dateCreated(UPDATED_DATE_CREATED);
        return experiment;
    }

    @BeforeEach
    public void initTest() {
        experimentRepository.deleteAll();
        experiment = createEntity();
    }

    @Test
    public void createExperiment() throws Exception {
        int databaseSizeBeforeCreate = experimentRepository.findAll().size();

        // Create the Experiment
        restExperimentMockMvc.perform(post("/api/experiments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(experiment)))
            .andExpect(status().isCreated());

        // Validate the Experiment in the database
        List<Experiment> experimentList = experimentRepository.findAll();
        assertThat(experimentList).hasSize(databaseSizeBeforeCreate + 1);
        Experiment testExperiment = experimentList.get(experimentList.size() - 1);
        assertThat(testExperiment.getXnatId()).isEqualTo(DEFAULT_XNAT_ID);
        assertThat(testExperiment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testExperiment.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
    }

    @Test
    public void createExperimentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = experimentRepository.findAll().size();

        // Create the Experiment with an existing ID
        experiment.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restExperimentMockMvc.perform(post("/api/experiments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(experiment)))
            .andExpect(status().isBadRequest());

        // Validate the Experiment in the database
        List<Experiment> experimentList = experimentRepository.findAll();
        assertThat(experimentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllExperiments() throws Exception {
        // Initialize the database
        experimentRepository.save(experiment);

        // Get all the experimentList
        restExperimentMockMvc.perform(get("/api/experiments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(experiment.getId())))
            .andExpect(jsonPath("$.[*].xnatId").value(hasItem(DEFAULT_XNAT_ID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())));
    }
    
    @Test
    public void getExperiment() throws Exception {
        // Initialize the database
        experimentRepository.save(experiment);

        // Get the experiment
        restExperimentMockMvc.perform(get("/api/experiments/{id}", experiment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(experiment.getId()))
            .andExpect(jsonPath("$.xnatId").value(DEFAULT_XNAT_ID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()));
    }

    @Test
    public void getNonExistingExperiment() throws Exception {
        // Get the experiment
        restExperimentMockMvc.perform(get("/api/experiments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateExperiment() throws Exception {
        // Initialize the database
        experimentService.save(experiment);

        int databaseSizeBeforeUpdate = experimentRepository.findAll().size();

        // Update the experiment
        Experiment updatedExperiment = experimentRepository.findById(experiment.getId()).get();
        updatedExperiment
            .xnatId(UPDATED_XNAT_ID)
            .name(UPDATED_NAME)
            .dateCreated(UPDATED_DATE_CREATED);

        restExperimentMockMvc.perform(put("/api/experiments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedExperiment)))
            .andExpect(status().isOk());

        // Validate the Experiment in the database
        List<Experiment> experimentList = experimentRepository.findAll();
        assertThat(experimentList).hasSize(databaseSizeBeforeUpdate);
        Experiment testExperiment = experimentList.get(experimentList.size() - 1);
        assertThat(testExperiment.getXnatId()).isEqualTo(UPDATED_XNAT_ID);
        assertThat(testExperiment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExperiment.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
    }

    @Test
    public void updateNonExistingExperiment() throws Exception {
        int databaseSizeBeforeUpdate = experimentRepository.findAll().size();

        // Create the Experiment

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExperimentMockMvc.perform(put("/api/experiments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(experiment)))
            .andExpect(status().isBadRequest());

        // Validate the Experiment in the database
        List<Experiment> experimentList = experimentRepository.findAll();
        assertThat(experimentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteExperiment() throws Exception {
        // Initialize the database
        experimentService.save(experiment);

        int databaseSizeBeforeDelete = experimentRepository.findAll().size();

        // Delete the experiment
        restExperimentMockMvc.perform(delete("/api/experiments/{id}", experiment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Experiment> experimentList = experimentRepository.findAll();
        assertThat(experimentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Experiment.class);
        Experiment experiment1 = new Experiment();
        experiment1.setId("id1");
        Experiment experiment2 = new Experiment();
        experiment2.setId(experiment1.getId());
        assertThat(experiment1).isEqualTo(experiment2);
        experiment2.setId("id2");
        assertThat(experiment1).isNotEqualTo(experiment2);
        experiment1.setId(null);
        assertThat(experiment1).isNotEqualTo(experiment2);
    }
}
