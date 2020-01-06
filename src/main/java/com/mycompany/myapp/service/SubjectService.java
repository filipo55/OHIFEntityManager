package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Project;
import com.mycompany.myapp.domain.Subject;
import com.mycompany.myapp.repository.SubjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.Optional;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Service Implementation for managing {@link Subject}.
 */
@Service
public class SubjectService {

    private final Logger log = LoggerFactory.getLogger(SubjectService.class);

    private final SubjectRepository subjectRepository;

    @Autowired
    ProjectService projectService;

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

    public List<Subject> findAll()
    {
        log.debug("Request to get all Subjects");
        return subjectRepository.findAll();
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

    public void Sync(JSONArray resultsArray) throws IOException, JSONException, ParseException {

        if (resultsArray != null) {

            //Check if subjects are matching db
            for (int i =0; i< resultsArray.length();i++)
            {

                JSONObject temp = resultsArray.getJSONObject(i);
                String name = temp.getString("label");
                String projectID = temp.getString("project");
                String xnatID = temp.getString("ID");
                Subject subject = findOneByXnatID(xnatID);
                if(subject != null)
                {
                    if(subject.getName().matches(name))
                    {
                        log.debug("Found subject and the name matches as well :) : {}", xnatID);
                    }
                    else
                    {
                        subject.setName(name);
                    }
                    if(subject.getProject() == null)
                    {
                        delete(subject.getId());
                    }
                    else
                    {
                        if(subject.getProject().getXnatId().matches(projectID))
                        {
                            log.debug("Found subject and the project matches as well :) : {}", xnatID);
                        }
                        else
                        {
                            subject.setProject(projectService.findOneByXnatID(projectID));
                        }
                    }

                }
                else
                {

                    subject = new Subject();
                    subject.setName(name);
                    subject.setXnatId(xnatID);
                    subject.setProject(projectService.findOneByXnatID(projectID));
                    save(subject);
                }
            }

            //Remove obsolete subjects
            boolean matches;
            List<Subject> subjects = subjectRepository.findAll();
            for(int i =0; i< subjects.size();i++)
            {
                matches = false;
                for(int j = 0; j < resultsArray.length(); j++)
                {
                    JSONObject temp = resultsArray.getJSONObject(j);
                    String xnatID = temp.getString("ID");
                    if(subjects.get(i).getXnatId().matches(xnatID))
                    {
                        matches = true;
                        break;
                    }
                }
                if(!matches)
                {

                    delete(subjects.get(i).getId());
                }
            }
        }
    }

    public Subject findOneByXnatID(String id)
    {
        log.debug("Request to get Subject : {}", id);
        List<Subject> subjects = subjectRepository.findAll();
        for(int i =0; i< subjects.size();i++)
        {
            if(subjects.get(i).getXnatId().matches(id))
            {
                return subjects.get(i);
            }
        }
        return null;
    }

    public List<Subject> findSubjectsWithProject (String id)
    {
        log.debug("Request to get SubjectsWithProject : {}", id);
        List<Subject> subjects = subjectRepository.findAll();
        List<Subject> result = new ArrayList<>();
        for(int i =0; i< subjects.size();i++)
        {
            if(subjects.get(i).getProject().getId().matches(id))
            {
                result.add(subjects.get(i));
            }
        }
        return result;
    }
}
