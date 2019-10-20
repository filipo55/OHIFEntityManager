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
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Service Implementation for managing {@link Project}.
 */
@Service
public class ProjectService {

    private final Logger log = LoggerFactory.getLogger(ProjectService.class);

    private final ProjectRepository projectRepository;


    private final HttpClient client = createHttpClient();

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

    private HttpClient createHttpClient()
    {
        //Create http client
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials
            = new UsernamePasswordCredentials("admin", "admin");
        provider.setCredentials(AuthScope.ANY, credentials);

        return HttpClientBuilder.create()
            .setDefaultCredentialsProvider(provider)
            .build();
    }

    private JSONArray findXnatProjects() throws IOException {
        //Send http request
        HttpGet request = new HttpGet("http://localhost:/data/archive/projects/");
        HttpResponse response = client.execute(request);


        System.out.println(response.getStatusLine().toString());

        //Parse
        HttpEntity entity = response.getEntity();
        Header headers = entity.getContentType();
        System.out.println(headers);
        if (entity != null) {
            // return it as a String
            String result = EntityUtils.toString(entity);

            JSONObject obj = new JSONObject(result);
            JSONArray resultsArray = obj.getJSONObject("ResultSet").getJSONArray("Result");
            return resultsArray;
        }

        return null;
    }

    public void Sync() throws IOException, JSONException, ParseException {


        JSONArray resultsArray = findXnatProjects();

        if (resultsArray != null) {

            //Check if projects are matching db
            for (int i =0; i< resultsArray.length();i++)
            {

                JSONObject temp = resultsArray.getJSONObject(i);
                String name = temp.getString("name");
                String xnatID = temp.getString("ID");
                Project project = findOneByXnatID(xnatID);
                if(project != null)
                {
                    if(project.getName().matches(name))
                    {
                        log.debug("Found project and the name matches as well :) : {}", xnatID);
                    }
                    else
                    {
                        project.setName(name);
                    }
                }
                else
                {
                    project = new Project();
                    project.setName(name);
                    project.setXnatId(xnatID);
                    save(project);
                }
            }

            //Remove obsolete projects
            boolean matches;
            List<Project> projects = projectRepository.findAll();
            for(int i =0; i< projects.size();i++)
            {
                matches = false;
                for(int j = 0; j < resultsArray.length(); j++)
                {
                    JSONObject temp = resultsArray.getJSONObject(j);
                    String xnatID = temp.getString("ID");
                    if(projects.get(i).getXnatId().matches(xnatID))
                    {
                        matches = true;
                        break;
                    }
                }
                if(!matches)
                {

                    delete(projects.get(i).getId());
                }
            }
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

    public Project findOneByXnatID(String id)
    {
        log.debug("Request to get Project : {}", id);
        List<Project> projects = projectRepository.findAll();
        for(int i =0; i< projects.size();i++)
        {
            if(projects.get(i).getXnatId().matches(id))
            {
                return projects.get(i);
            }
        }
        return null;
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
