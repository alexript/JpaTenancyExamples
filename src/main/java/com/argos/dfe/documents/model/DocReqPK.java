/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argos.dfe.documents.model;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author malyshev
 */
public class DocReqPK implements Serializable {

    private static final long serialVersionUID = -8536372378150076890L;
    private String document;
    private String mnemo;

    public DocReqPK() {

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DocReqPK other = (DocReqPK) obj;
        return Objects.equals(document, other.document) && Objects.equals(mnemo, other.mnemo);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.document);
        hash = 29 * hash + Objects.hashCode(this.mnemo);
        return hash;
    }



    /**
     * @return the document
     */
    public String getDocument() {
        return document;
    }

    /**
     * @param document the document to set
     */
    public void setDocument(String document) {
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
}
