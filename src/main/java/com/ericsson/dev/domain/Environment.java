package com.ericsson.dev.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Environment.
 */
@Document(collection = "environment")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "environment")
public class Environment implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("url")
    private String url;

    @Field("user")
    private String user;

    @Field("pass")
    private String pass;

    @DBRef
    @Field("discountProcess")
    private Set<DiscountProcess> discountProcesses = new HashSet<>();
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

    public Environment name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public Environment url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public Environment user(String user) {
        this.user = user;
        return this;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public Environment pass(String pass) {
        this.pass = pass;
        return this;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Set<DiscountProcess> getDiscountProcesses() {
        return discountProcesses;
    }

    public Environment discountProcesses(Set<DiscountProcess> discountProcesses) {
        this.discountProcesses = discountProcesses;
        return this;
    }

    public Environment addDiscountProcess(DiscountProcess discountProcess) {
        this.discountProcesses.add(discountProcess);
        discountProcess.setEnvironment(this);
        return this;
    }

    public Environment removeDiscountProcess(DiscountProcess discountProcess) {
        this.discountProcesses.remove(discountProcess);
        discountProcess.setEnvironment(null);
        return this;
    }

    public void setDiscountProcesses(Set<DiscountProcess> discountProcesses) {
        this.discountProcesses = discountProcesses;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Environment environment = (Environment) o;
        if (environment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), environment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Environment{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", url='" + getUrl() + "'" +
            ", user='" + getUser() + "'" +
            ", pass='" + getPass() + "'" +
            "}";
    }
}
