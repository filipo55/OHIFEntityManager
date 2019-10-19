package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Project;
import com.mycompany.myapp.repository.ProjectRepository;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Service Implementation for managing {@link Project}.
 */
@Service
public class ProjectService {

    private final Logger log = LoggerFactory.getLogger(ProjectService.class);

    private final ProjectRepository projectRepository;


    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;

    }

    /**
     * Save a project.
     *
     * @param project the entity to save.
     * @return the persisted entity.
     */
    public Project save(Project project) {
        log.debug("Request to save Project : {}", project);
        return projectRepository.save(project);
    }

    /**
     * Get all the projects.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<Project> findAll(Pageable pageable) {
        log.debug("Request to get all Projects");
        return projectRepository.findAll(pageable);
    }

    public List<Project> findAll()
    {
        log.debug("Request to get all Projects");
        return  projectRepository.findAll();
    }

    public void Sync() throws IOException, JSONException, ParseException {

        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials
            = new UsernamePasswordCredentials("admin", "admin");
        provider.setCredentials(AuthScope.ANY, credentials);

        HttpClient client = HttpClientBuilder.create()
            .setDefaultCredentialsProvider(provider)
            .build();

        HttpGet request = new HttpGet("http://localhost:/data/archive/projects/");
        HttpResponse response = client.execute(request);


        System.out.println(response.getStatusLine().toString());

        HttpEntity entity = response.getEntity();
        Header headers = entity.getContentType();
        System.out.println(headers);


        List<Project> projects = projectRepository.findAll();
        
        if (entity != null) {
            // return it as a String
            String result = EntityUtils.toString(entity);

            JSONObject obj = new JSONObject(result);
            JSONArray resultsArray = obj.getJSONObject("ResultSet").getJSONArray("Result");

            for (int i =0; i< resultsArray.length();i++)
            {

                JSONObject temp = resultsArray.getJSONObject(i);
                String name = temp.getString("name");
                String xnatID = temp.getString("ID");


            }

            System.out.println(result);
        }


    }

    /**
     * Get one project by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<Project> findOne(String id) {
        log.debug("Request to get Project : {}", id);
        return projectRepository.findById(id);
    }

    /**
     * Delete the project by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Project : {}", id);
        projectRepository.deleteById(id);
    }
}
