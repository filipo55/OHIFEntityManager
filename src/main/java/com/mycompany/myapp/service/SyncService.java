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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;

@Component
public class SyncService {

    private final Logger log = LoggerFactory.getLogger(ProjectService.class);

    private final HttpClient client = createHttpClient();

    @Autowired
    ProjectService projectService;

    @Autowired
    SubjectService subjectService;

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


    public void Sync() throws ParseException, IOException, JSONException
    {
        SyncProjects();
        SyncSubjects();

    }


    public void SyncProjects() throws JSONException, ParseException, IOException
    {
        projectService.Sync(findXnatData("projects/"));

    }

    public void SyncSubjects() throws IOException, JSONException, ParseException {

        subjectService.Sync(findXnatData("subjects/"));

    }

    private JSONArray findXnatData(String data) throws IOException {
        //Send http request
        HttpGet request = new HttpGet("http://localhost:/data/archive/" + data);
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

}
