/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argos.dfe.documents.model;

import com.argos.dfe.documents.PersistedEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.CascadeOnDelete;

/**
 *
 * @author malyshev
 */
@Entity
@Table(name = "doc_head_req")
@IdClass(DocReqPK.class)
public class DocumentRequisite implements PersistedEntity<Document>, Serializable {

    public DocumentRequisite() {

    }

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "req_mnemo", length = 512, nullable = false)
    private String mnemo;

    @Basic(optional = false)
    @Column(name = "req_value", length = 8000, nullable = false)
    private String value;

    @Id
    @JoinColumns(value = {
        @JoinColumn(name = "document_id", referencedColumnName = "id"),
    }
    )
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Document document;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "id", fetch = FetchType.LAZY, orphanRemoval = true)
    @CascadeOnDelete
    private Collection<DocumentRequisiteProp> headRequisiteProps;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (document != null ? document.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentRequisite)) {
            return false;
        }
        DocumentRequisite other = (DocumentRequisite) object;
        if ((this.document == null && other.document != null) || (this.document != null && !this.document.equals(other.document))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.argos.dfe.documents.model.DocumentRequisite[ id= ]";
    }

    @Override
    public Document getPK() {
        return getDocument();
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

    public void addHeadReqProp(DocumentRequisiteProp prop) {
        if (this.headRequisiteProps == null) {
            this.headRequisiteProps = new ArrayList<>();
        }
        if (!this.equals(prop.getRequisite())) {
            prop.setRequisite(this);
        }
        this.headRequisiteProps.add(prop);
    }

}
