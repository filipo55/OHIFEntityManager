package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.OhifEntityManagerApp;
import com.mycompany.myapp.config.TestSecurityConfiguration;
import com.mycompany.myapp.domain.Measurement;
import com.mycompany.myapp.repository.MeasurementRepository;
import com.mycompany.myapp.service.MeasurementService;
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


import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link MeasurementResource} REST controller.
 */
@SpringBootTest(classes = {OhifEntityManagerApp.class, TestSecurityConfiguration.class})
public class MeasurementResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private MeasurementService measurementService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restMeasurementMockMvc;

    private Measurement measurement;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MeasurementResource measurementResource = new MeasurementResource(measurementService);
        this.restMeasurementMockMvc = MockMvcBuilders.standaloneSetup(measurementResource)
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
    public static Measurement createEntity() {
        Measurement measurement = new Measurement()
            .name(DEFAULT_NAME);
        return measurement;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Measurement createUpdatedEntity() {
        Measurement measurement = new Measurement()
            .name(UPDATED_NAME);
        return measurement;
    }

    @BeforeEach
    public void initTest() {
        measurementRepository.deleteAll();
        measurement = createEntity();
    }

    @Test
    public void createMeasurement() throws Exception {
        int databaseSizeBeforeCreate = measurementRepository.findAll().size();

        // Create the Measurement
        restMeasurementMockMvc.perform(post("/api/measurements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(measurement)))
            .andExpect(status().isCreated());

        // Validate the Measurement in the database
        List<Measurement> measurementList = measurementRepository.findAll();
        assertThat(measurementList).hasSize(databaseSizeBeforeCreate + 1);
        Measurement testMeasurement = measurementList.get(measurementList.size() - 1);
        assertThat(testMeasurement.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void createMeasurementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = measurementRepository.findAll().size();

        // Create the Measurement with an existing ID
        measurement.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restMeasurementMockMvc.perform(post("/api/measurements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(measurement)))
            .andExpect(status().isBadRequest());

        // Validate the Measurement in the database
        List<Measurement> measurementList = measurementRepository.findAll();
        assertThat(measurementList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllMeasurements() throws Exception {
        // Initialize the database
        measurementRepository.save(measurement);

        // Get all the measurementList
        restMeasurementMockMvc.perform(get("/api/measurements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(measurement.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    public void getMeasurement() throws Exception {
        // Initialize the database
        measurementRepository.save(measurement);

        // Get the measurement
        restMeasurementMockMvc.perform(get("/api/measurements/{id}", measurement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(measurement.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    public void getNonExistingMeasurement() throws Exception {
        // Get the measurement
        restMeasurementMockMvc.perform(get("/api/measurements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateMeasurement() throws Exception {
        // Initialize the database
        measurementService.save(measurement);

        int databaseSizeBeforeUpdate = measurementRepository.findAll().size();

        // Update the measurement
        Measurement updatedMeasurement = measurementRepository.findById(measurement.getId()).get();
        updatedMeasurement
            .name(UPDATED_NAME);

        restMeasurementMockMvc.perform(put("/api/measurements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMeasurement)))
            .andExpect(status().isOk());

        // Validate the Measurement in the database
        List<Measurement> measurementList = measurementRepository.findAll();
        assertThat(measurementList).hasSize(databaseSizeBeforeUpdate);
        Measurement testMeasurement = measurementList.get(measurementList.size() - 1);
        assertThat(testMeasurement.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    public void updateNonExistingMeasurement() throws Exception {
        int databaseSizeBeforeUpdate = measurementRepository.findAll().size();

        // Create the Measurement

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeasurementMockMvc.perform(put("/api/measurements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(measurement)))
            .andExpect(status().isBadRequest());

        // Validate the Measurement in the database
        List<Measurement> measurementList = measurementRepository.findAll();
        assertThat(measurementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteMeasurement() throws Exception {
        // Initialize the database
        measurementService.save(measurement);

        int databaseSizeBeforeDelete = measurementRepository.findAll().size();

        // Delete the measurement
        restMeasurementMockMvc.perform(delete("/api/measurements/{id}", measurement.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Measurement> measurementList = measurementRepository.findAll();
        assertThat(measurementList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Measurement.class);
        Measurement measurement1 = new Measurement();
        measurement1.setId("id1");
        Measurement measurement2 = new Measurement();
        measurement2.setId(measurement1.getId());
        assertThat(measurement1).isEqualTo(measurement2);
        measurement2.setId("id2");
        assertThat(measurement1).isNotEqualTo(measurement2);
        measurement1.setId(null);
        assertThat(measurement1).isNotEqualTo(measurement2);
    }
}
