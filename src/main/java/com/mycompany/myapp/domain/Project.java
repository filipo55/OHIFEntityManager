package com.mycompany.myapp.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Project.
 */
@Document(collection = "project")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("xnat_id")
    private String xnatId;

    @Field("name")
    private String name;

    @DBRef
    @Field("subject")
    private Set<Subject> subjects = new HashSet<>();

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

    public Project xnatId(String xnatId) {
        this.xnatId = xnatId;
        return this;
    }

    public void setXnatId(String xnatId) {
        this.xnatId = xnatId;
    }

    public String getName() {
        return name;
    }

    public Project name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public Project subjects(Set<Subject> subjects) {
        this.subjects = subjects;
        return this;
    }

    public Project addSubject(Subject subject) {
        this.subjects.add(subject);
        subject.setProject(this);
        return this;
    }

    public Project removeSubject(Subject subject) {
        this.subjects.remove(subject);
        subject.setProject(null);
        return this;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Project)) {
            return false;
        }
        return id != null && id.equals(((Project) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Project{" +
            "id=" + getId() +
            ", xnatId='" + getXnatId() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
