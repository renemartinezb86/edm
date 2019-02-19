package com.ericsson.dev.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DiscountProcess.
 */
@Document(collection = "discount_process")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "discountprocess")
public class DiscountProcess implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;

    @Field("quantity")
    private Integer quantity;

    @Field("date_to_process")
    private Instant dateToProcess;

    @Field("created_date")
    private Instant createdDate;

    @Field("sql_file_path")
    private String sqlFilePath;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public DiscountProcess quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Instant getDateToProcess() {
        return dateToProcess;
    }

    public DiscountProcess dateToProcess(Instant dateToProcess) {
        this.dateToProcess = dateToProcess;
        return this;
    }

    public void setDateToProcess(Instant dateToProcess) {
        this.dateToProcess = dateToProcess;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public DiscountProcess createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getSqlFilePath() {
        return sqlFilePath;
    }

    public DiscountProcess sqlFilePath(String sqlFilePath) {
        this.sqlFilePath = sqlFilePath;
        return this;
    }

    public void setSqlFilePath(String sqlFilePath) {
        this.sqlFilePath = sqlFilePath;
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
        DiscountProcess discountProcess = (DiscountProcess) o;
        if (discountProcess.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), discountProcess.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DiscountProcess{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", dateToProcess='" + getDateToProcess() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", sqlFilePath='" + getSqlFilePath() + "'" +
            "}";
    }
}
