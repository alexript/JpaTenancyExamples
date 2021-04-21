/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argos.dfe.documents.model;

import com.argos.dfe.documents.PersistedEntity;
import static com.argos.dfe.documents.Tenancy.DISCRIMINATOR_COLUMN_NAME;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import org.eclipse.persistence.annotations.UuidGenerator;
import org.eclipse.persistence.config.EntityManagerProperties;

/**
 *
 * @author malyshev
 */
@Entity
@UuidGenerator(name = "ID_GEN")
@Table(name = "doc_head_req_prop")
@Multitenant(MultitenantType.SINGLE_TABLE)
@TenantDiscriminatorColumn(
        name = DISCRIMINATOR_COLUMN_NAME,
        contextProperty = EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT,
        primaryKey = true)
@IdClass(DocReqPropPK.class)
public class DocumentRequisiteProp implements PersistedEntity<DocumentRequisite>, Serializable {

    public DocumentRequisiteProp() {

    }

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "reqprop_name", length = 256)//, nullable = false, updatable = false)
    private String name;

    @Id
    @PrimaryKeyJoinColumns(value = {
        @PrimaryKeyJoinColumn(name = "requisite_id", referencedColumnName = "id")},
            foreignKey = @ForeignKey(
                    name = "FK_Document_head_req_prop",
                    foreignKeyDefinition = "FOREIGN KEY (requisite_id) REFERENCES doc_head_req (id) ON UPDATE CASCADE ON DELETE CASCADE"))
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    private DocumentRequisite id;

    @Basic(optional = false)
    @Column(name = "reqprop_val", length = 256, nullable = false)
    private String value;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentRequisiteProp)) {
            return false;
        }
        DocumentRequisiteProp other = (DocumentRequisiteProp) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argos.dfe.documents.model.DocumentRequisiteProp[ id=" + id + " ]";
    }

    @Override
    public DocumentRequisite getPK() {
        return getRequisite();
    }

    /**
     * @return the requisite
     */
    public DocumentRequisite getRequisite() {
        return id;
    }

    /**
     * @param requisite the requisite to set
     */
    public void setRequisite(DocumentRequisite requisite) {
        this.id = requisite;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

}
