package com.mycompany.myapp.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Subject.
 */
@Document(collection = "subject")
public class Subject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("xnat_id")
    private String xnatId;

    @Field("name")
    private String name;

    @DBRef
    @Field("experiment")
    private Set<Experiment> experiments = new HashSet<>();

    @DBRef
    @Field("project")
    @JsonIgnoreProperties("subjects")
    private Project project;


    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getXnatId() {
        return xnatId;
    }

    public Subject xnatId(String xnatId) {
        this.xnatId = xnatId;
        return this;
    }

    public void setXnatId(String xnatId) {
        this.xnatId = xnatId;
    }

    public String getName() {
        return name;
    }

    public Subject name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Experiment> getExperiments() {
        return experiments;
    }

    public Subject experiments(Set<Experiment> experiments) {
        this.experiments = experiments;
        return this;
    }

    public Subject addExperiment(Experiment experiment) {
        this.experiments.add(experiment);
        experiment.setSubject(this);
        return this;
    }

    public Subject removeExperiment(Experiment experiment) {
        this.experiments.remove(experiment);
        experiment.setSubject(null);
        return this;
    }

    public void setExperiments(Set<Experiment> experiments) {
        this.experiments = experiments;
    }

    public Project getProject() {
        return project;
    }

    public Subject project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Subject)) {
            return false;
        }
        return id != null && id.equals(((Subject) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Subject{" +
            "id=" + getId() +
            ", xnatId='" + getXnatId() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
