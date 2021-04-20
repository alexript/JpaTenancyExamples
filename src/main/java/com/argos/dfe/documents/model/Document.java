/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argos.dfe.documents.model;

import com.argos.dfe.documents.DateTimeConverter;
import com.argos.dfe.documents.PersistedEntity;
import static com.argos.dfe.documents.Tenancy.DISCRIMINATOR_COLUMN_NAME;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
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
import javax.persistence.Temporal;
import org.eclipse.persistence.annotations.CascadeOnDelete;
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
@Table(name = "documents",
        indexes = {
            @Index(name = "IDX_DOCUMENT_MNEMO_NUMBER_DATE", columnList = "TENANT,doc_mnemo,number,d", unique = false)
        })
@Multitenant(MultitenantType.SINGLE_TABLE)
@TenantDiscriminatorColumn(
        name = DISCRIMINATOR_COLUMN_NAME,
        contextProperty = EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT,
        primaryKey = true)
public class Document implements PersistedEntity<String>, Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", length = 36, nullable = false, unique = true)
    @GeneratedValue(generator = "ID_GEN")
    private String id;

    @Basic(optional = false)
    @Column(name = "doc_mnemo", length = 512, nullable = false, updatable = false)
    private String mnemo;

    @Basic(optional = false)
    @Column(name = "number", length = 64, nullable = false)
    private String number;

    @Basic(optional = false)
    @Column(name = "d", nullable = false)
    @Convert(converter = DateTimeConverter.class)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date date;

    @JoinColumns(value = {
        @JoinColumn(name = "parent_id", referencedColumnName = "id", nullable = true)
    }, foreignKey = @ForeignKey(
            name = "FK_Document_Parent",
            foreignKeyDefinition = "FOREIGN KEY (parent_id) REFERENCES documents (id) ON DELETE SET NULL"))
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Document parent;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "parent", fetch = FetchType.LAZY)
    private Collection<Document> children;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "document", fetch = FetchType.LAZY, orphanRemoval = true)
    @CascadeOnDelete
    private Collection<DocumentRequisite> headRequisites;

    public Document() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Document)) {
            return false;
        }
        Document other = (Document) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argos.dfe.documents.Document[ id=" + id + " ]";
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
     * @return the number
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String getPK() {
        return getId();
    }

    /**
     * @return the parent
     */
    public Document getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Document parent) {
        this.parent = parent;
    }

    /**
     * @return the children
     */
    public Collection<Document> getChildren() {
        return children;
    }

    /**
     * @param children the children to set
     */
    public void setChildren(Collection<Document> children) {
        this.children = children;
        for (Document child : this.children) {
            if (!this.equals(child.getParent())) {
                child.setParent(this);
            }
        }
    }

    public void addChild(Document child) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        if (!this.equals(child.getParent())) {
            child.setParent(this);
        }
        this.children.add(child);
    }

    /**
     * @return the headRequisites
     */
    public Collection<DocumentRequisite> getHeadRequisites() {
        return headRequisites;
    }

    /**
     * @param headRequisites the headRequisites to set
     */
    public void setHeadRequisites(Collection<DocumentRequisite> headRequisites) {
        this.headRequisites = headRequisites;

        for (DocumentRequisite req : this.headRequisites) {
            if (!this.equals(req.getDocument())) {
                req.setDocument(this);
            }
        }
    }

    public void addHeadReq(DocumentRequisite req) {
        if (this.headRequisites == null) {
            this.headRequisites = new ArrayList<>();
        }
        if (!this.equals(req.getDocument())) {
            req.setDocument(this);
        }
        this.headRequisites.add(req);
    }
}
