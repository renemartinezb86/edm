package com.ericsson.dev.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.util.Objects;

/**
 * A CustomerState.
 */
@Document(collection = "customer_state")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "customerstate")
public class CustomerState implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;

    @Field("rut")
    private String rut;

    @Field("active")
    private Boolean active;

    @Field("black_list")
    private Boolean blackList;

    @Field("white_list")
    private Boolean whiteList;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRut() {
        return rut;
    }

    public CustomerState rut(String rut) {
        this.rut = rut;
        return this;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public Boolean isActive() {
        return active;
    }

    public CustomerState active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean isBlackList() {
        return blackList;
    }

    public CustomerState blackList(Boolean blackList) {
        this.blackList = blackList;
        return this;
    }

    public void setBlackList(Boolean blackList) {
        this.blackList = blackList;
    }

    public Boolean isWhiteList() {
        return whiteList;
    }

    public CustomerState whiteList(Boolean whiteList) {
        this.whiteList = whiteList;
        return this;
    }

    public void setWhiteList(Boolean whiteList) {
        this.whiteList = whiteList;
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
        CustomerState customerState = (CustomerState) o;
        if (customerState.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customerState.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CustomerState{" +
            "id=" + getId() +
            ", rut='" + getRut() + "'" +
            ", active='" + isActive() + "'" +
            ", blackList='" + isBlackList() + "'" +
            ", whiteList='" + isWhiteList() + "'" +
            "}";
    }
}
