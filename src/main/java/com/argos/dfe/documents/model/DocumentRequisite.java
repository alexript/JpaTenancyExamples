/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argos.dfe.documents.model;

import com.argos.dfe.documents.PersistedEntity;
import static com.argos.dfe.documents.Tenancy.DISCRIMINATOR_COLUMN_NAME;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "doc_head_req",
        indexes = {
            @Index(name = "IDX_DOCHEAD_MNEMO", columnList = "TENANT,req_mnemo", unique = false)
        })
@Multitenant(MultitenantType.SINGLE_TABLE)
@TenantDiscriminatorColumn(
        name = DISCRIMINATOR_COLUMN_NAME,
        contextProperty = EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT,
        primaryKey = true)
public class DocumentRequisite implements PersistedEntity<String>, Serializable {

    public DocumentRequisite() {
        
    }

    /**
     * @return the document
     */
    public Document getDocument() {
        return document;
    }

    /**
     * @param document the document to set
     */
    public void setDocument(Document document) {
        this.document = document;
    }

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", length = 36, nullable = false, unique = true)
    @GeneratedValue(generator = "ID_GEN")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic(optional = false)
    @Column(name = "req_mnemo", length = 512, nullable = false)
    private String mnemo;

    @Basic(optional = false)
    @Column(name = "req_value", length = 8000, nullable = false)
    private String value;

    @JoinColumns(value = {
        @JoinColumn(name = "document_id", referencedColumnName = "id", nullable = true)
    }, foreignKey = @ForeignKey(
            name = "FK_Document_head_req",
            foreignKeyDefinition = "FOREIGN KEY (document_id) REFERENCES documents (id) ON UPDATE CASCADE ON DELETE CASCADE"))
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Document document;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "requisite", fetch = FetchType.LAZY)
    private Collection<DocumentRequisiteProp> headRequisiteProps;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentRequisite)) {
            return false;
        }
        DocumentRequisite other = (DocumentRequisite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argos.dfe.documents.model.DocumentRequisite[ id=" + id + " ]";
    }

    @Override
    public String getPK() {
        return getId();
    }

    /**
     * @return the mnemo
     */
    public String getMnemo() {
        return mnemo;
    }

    /**
     * @param mnemo the mnemo to set
     */
    public void setMnemo(String mnemo) {
        this.mnemo = mnemo;
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

    /**
     * @return the headRequisiteProps
     */
    public Collection<DocumentRequisiteProp> getHeadRequisiteProps() {
        return headRequisiteProps;
    }

    /**
     * @param headRequisiteProps the headRequisiteProps to set
     */
    public void setHeadRequisiteProps(Collection<DocumentRequisiteProp> headRequisiteProps) {
        this.headRequisiteProps = headRequisiteProps;
        for (DocumentRequisiteProp prop : this.headRequisiteProps) {
            if (!this.equals(prop.getRequisite())) {
                prop.setRequisite(this);
            }
        }
    }

    public void addHeadReqProp(DocumentRequisiteProp req) {
        if (this.headRequisiteProps == null) {
            this.headRequisiteProps = new ArrayList<>();
        }
        if (!this.equals(req.getRequisite())) {
            req.setRequisite(this);
        }
        this.headRequisiteProps.add(req);
    }

}
