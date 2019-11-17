package com.mycompany.myapp.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;




/**
 * A Measurement.
 */
@Document(collection = "measurement")
public class Measurement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    //@GeneratedValue(strategy= GenerationType.AUTO)
    private String id;

    @Field("name")
    private String name;

    @DBRef
    @Field("descriptor")
    private Set<Descriptor> descriptors = new HashSet<>();

    @DBRef
    @Field("experiment")
    @JsonIgnoreProperties("measurements")
    private Experiment experiment;

    @DBRef
    @Field("type")
    private MeasurementType type;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Measurement name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(MeasurementType type)
    {
        this.type = type;
    }

    public MeasurementType getType()
    {
        return this.type;
    }

    public Measurement measurementType(MeasurementType type)
    {
        this.type = type;
        return this;
    }

    public Set<Descriptor> getDescriptors() {
        return descriptors;
    }

    public Measurement descriptors(Set<Descriptor> descriptors) {
        this.descriptors = descriptors;
        return this;
    }

    public Measurement addDescriptor(Descriptor descriptor) {
        this.descriptors.add(descriptor);
        descriptor.setMeasurement(this);
        return this;
    }

    public Measurement removeDescriptor(Descriptor descriptor) {
        this.descriptors.remove(descriptor);
        descriptor.setMeasurement(null);
        return this;
    }

    public void setDescriptors(Set<Descriptor> descriptors) {
        this.descriptors = descriptors;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public Measurement experiment(Experiment experiment) {
        this.experiment = experiment;
        return this;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Measurement)) {
            return false;
        }
        return id != null && id.equals(((Measurement) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Measurement{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
