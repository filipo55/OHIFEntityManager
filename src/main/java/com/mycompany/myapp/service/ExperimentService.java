package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Experiment;
import com.mycompany.myapp.domain.Subject;
import com.mycompany.myapp.repository.ExperimentRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Experiment}.
 */
@Service
public class ExperimentService {

    private final Logger log = LoggerFactory.getLogger(ExperimentService.class);

    private final ExperimentRepository experimentRepository;

    @Autowired
    SubjectService subjectService;

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

    public void Sync(JSONArray resultsArray, Subject subject) throws IOException, JSONException, ParseException {

        if (resultsArray != null) {

            //Check if experiments are matching db
            for (int i =0; i< resultsArray.length();i++)
            {

                JSONObject temp = resultsArray.getJSONObject(i);
                String name = temp.getString("label");
                String xnatID = temp.getString("ID");
                DateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss.SSS");
                LocalDate date = format.parse(temp.getString("insert_date")).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                Experiment experiment = findOneByXnatID(xnatID);
                if(experiment != null)
                {
                    if(experiment.getName().matches(name))
                    {
                        log.debug("Found experiment and the name matches as well :) : {}", xnatID);
                    }
                    else
                    {
                        experiment.setName(name);
                    }

                    if(experiment.getDateCreated() == date)
                    {
                        log.debug("Found experiment and the date matches as well :) : {}", xnatID);
                    }
                    else
                    {
                        experiment.setDateCreated(date);
                    }

                    if(experiment.getSubject().getId().matches(subject.getId()))
                    {
                        log.debug("Found experiment and the subject matches as well :) : {}", xnatID);
                    }
                    else
                    {
                        experiment.setSubject(subject);
                    }


                }
                else
                {

                    experiment = new Experiment();
                    experiment.setXnatId(xnatID);
                    experiment.setSubject(subject);
                    experiment.setName(name);
                    experiment.setDateCreated(date);
                    save(experiment);
                }
            }

            //Remove obsolete subjects
            boolean matches;
            List<Experiment> experiments = experimentRepository.findAll();
            for(int i =0; i< experiments.size();i++)
            {
                matches = false;
                for(int j = 0; j < resultsArray.length(); j++)
                {
                    JSONObject temp = resultsArray.getJSONObject(j);
                    String xnatID = temp.getString("ID");
                    if(experiments.get(i).getXnatId().matches(xnatID) && experiments.get(i).getSubject().getId().matches(subject.getId()))
                    {
                        matches = true;
                        break;
                    }
                }
                if(!matches)
                {
//If experiment has the same subject, but was not found then remove
                    if(experiments.get(i).getSubject().getId().matches(subject.getId()))
                        delete(experiments.get(i).getId());
                }
            }
        }
    }

    public Experiment findOneByXnatID(String id)
    {
        log.debug("Request to get Experiment : {}", id);
        List<Experiment> experiments = experimentRepository.findAll();
        for(int i =0; i< experiments.size();i++)
        {
            if(experiments.get(i).getXnatId().matches(id))
            {
                return experiments.get(i);
            }
        }
        return null;
    }

    public List<Experiment> findExperimentWithSubject(String id)
    {
        log.debug("Request to get ExperimentWithSubject : {}", id);
        List<Experiment> experiments = experimentRepository.findAll();
        List<Experiment> result = new ArrayList<>();
        for(int i =0; i< experiments.size();i++)
        {
            if(experiments.get(i).getSubject().getId().matches(id))
            {
                result.add(experiments.get(i));
            }
        }
        return result;
    }
}
