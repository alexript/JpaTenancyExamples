/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argos.dfe.documents.controller;

import com.argos.dfe.documents.model.DocPk;
import com.argos.dfe.documents.model.Document;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author malyshev
 */
public class DocumentController extends JpaController<Document, String> {

    public DocumentController(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    protected Class<Document> getEntityClass() {
        return Document.class;
    }

}
