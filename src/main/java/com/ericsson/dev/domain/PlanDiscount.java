package com.ericsson.dev.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.util.Objects;

/**
 * A PlanDiscount.
 */
@Document(collection = "plan_discount")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "plandiscount")
public class PlanDiscount implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("position")
    private Integer position;

    @Field("discount_percentage")
    private Double discountPercentage;

    @Field("active")
    private Boolean active;

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

    public PlanDiscount name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPosition() {
        return position;
    }

    public PlanDiscount position(Integer position) {
        this.position = position;
        return this;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public PlanDiscount discountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
        return this;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Boolean isActive() {
        return active;
    }

    public PlanDiscount active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
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
        PlanDiscount planDiscount = (PlanDiscount) o;
        if (planDiscount.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), planDiscount.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PlanDiscount{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", position=" + getPosition() +
            ", discountPercentage=" + getDiscountPercentage() +
            ", active='" + isActive() + "'" +
            "}";
    }
}
