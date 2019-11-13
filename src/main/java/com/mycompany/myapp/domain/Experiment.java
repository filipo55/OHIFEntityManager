package com.mycompany.myapp.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Experiment.
 */
@Document(collection = "experiment")
public class Experiment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("xnat_id")
    private String xnatId;

    @Field("name")
    private String name;

    @Field("date_created")
    private LocalDate dateCreated;

    @DBRef
    @Field("subject")
    @JsonIgnoreProperties("experiments")
    private Subject subject;

    @DBRef
    @Field("measurement")
    private Set<Measurement> measurements = new HashSet<>();

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

    public Experiment xnatId(String xnatId) {
        this.xnatId = xnatId;
        return this;
    }

    public void setXnatId(String xnatId) {
        this.xnatId = xnatId;
    }

    public String getName() {
        return name;
    }

    public Experiment name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public Experiment dateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Subject getSubject() {
        return subject;
    }

    public Experiment subject(Subject subject) {
        this.subject = subject;
        return this;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }


    public Set<Measurement> getMeasurements() {
        return measurements;
    }

    public Experiment measurements(Set<Measurement> measurements) {
        this.measurements = measurements;
        return this;
    }

    public Experiment addMeasurement(Measurement measurement) {
        this.measurements.add(measurement);
        measurement.setExperiment(this);
        return this;
    }

    public Experiment removeMeasurement(Measurement measurement) {
        this.measurements.remove(measurement);
        measurement.setExperiment(null);
        return this;
    }

    public void setMeasurements(Set<Measurement> measurements) {
        this.measurements = measurements;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Experiment)) {
            return false;
        }
        return id != null && id.equals(((Experiment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Experiment{" +
            "id=" + getId() +
            ", xnatId='" + getXnatId() + "'" +
            ", name='" + getName() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            "}";
    }
}
