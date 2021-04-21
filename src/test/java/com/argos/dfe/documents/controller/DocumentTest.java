/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argos.dfe.documents.controller;

import com.argos.dfe.documents.model.Document;
import com.argos.dfe.documents.model.DocumentRequisite;
import com.argos.dfe.documents.model.DocumentRequisiteProp;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author malyshev
 */
public class DocumentTest {

    private static EntityManagerFactory emf;

    @BeforeAll
    public static void setUpClass() {
        emf = Persistence.createEntityManagerFactory("documents.PU");
    }

    @AfterAll
    public static void tearDownClass() {
        emf.close();
    }

    @Test
    public void testCreateDocument() {
        DocumentController c = new DocumentController(emf);
        Document document = new Document();
        document.setMnemo("test.create");
        document.setDate(new Date());
        document.setNumber("1");
        boolean result = c.create(document);
        assertTrue(result);
    }

    @Test
    public void testUpdateDocument() {
        DocumentController c = new DocumentController(emf);
        Document document = new Document();
        document.setMnemo("test.update");
        document.setDate(new Date());
        document.setNumber("1");
        c.create(document);

        String pk = document.getPK();

        document.setNumber("2");
        c.update(document);

        Document read = c.read(pk);
        assertNotNull(read);
        String result = read.getNumber();
        assertEquals("2", result);
    }

    @Test
    public void testDeleteDocument() {
        DocumentController c = new DocumentController(emf);
        Document document = new Document();
        document.setMnemo("test.delete");
        document.setDate(new Date());
        document.setNumber("1");
        c.create(document);
        String pk = document.getPK();

        boolean result = c.delete(pk);
        assertTrue(result);

        Document read = c.read(pk);
        assertNull(read);
    }

    @Test
    public void testCreateChilds() {
        DocumentController c = new DocumentController(emf);

        Document child1 = new Document();
        child1.setMnemo("test.child1");
        child1.setDate(new Date());
        child1.setNumber("1");

        Document child2 = new Document();
        child2.setMnemo("test.child2");
        child2.setDate(new Date());
        child2.setNumber("2");

        List<Document> children = new ArrayList<>();
        children.add(child1);
        children.add(child2);
        Document parent = new Document();
        parent.setMnemo("test.parent");
        parent.setDate(new Date());
        parent.setNumber("1");
        parent.setChildren(children);
        c.create(parent);

        String pk = parent.getPK();
        Document read = c.read(pk);
        Collection<Document> result = read.getChildren();
        assertEquals(2, result.size());

    }

    @Test
    public void testAddChild() {
        DocumentController c = new DocumentController(emf);

        Document parent = new Document();
        parent.setMnemo("test.parent.add");
        parent.setDate(new Date());
        parent.setNumber("1");
        c.create(parent);

        Document child = new Document();
        child.setMnemo("test.child.add");
        child.setDate(new Date());
        child.setNumber("1");
        c.create(child);

        parent.addChild(child);
        boolean result = c.update(parent);
        assertTrue(result);
    }

    @Test
    public void testDeleteParent() {
        DocumentController c = new DocumentController(emf);

        Document child = new Document();
        child.setMnemo("test.child.delete");
        child.setDate(new Date());
        child.setNumber("1");

        Document parent = new Document();
        parent.setMnemo("test.parent.delete");
        parent.setDate(new Date());
        parent.setNumber("1");
        parent.addChild(child);

        c.create(parent);

        String parentpk = parent.getPK();
        String childpk = child.getPK();
        boolean result = c.delete(parentpk);
        assertTrue(result);

        Document read = c.read(childpk);
        assertNotNull(read);
        Document parent1 = read.getParent();
        assertNull(parent1);
    }

    @Test
    public void testSetHeadRequisites() {
        Document doc = new Document();
        doc.setMnemo("test.document.req");
        doc.setDate(new Date());
        doc.setNumber("1");

        DocumentRequisite r = new DocumentRequisite();
        r.setMnemo("r1");
        r.setValue("1");

        DocumentRequisiteProp p = new DocumentRequisiteProp();
        p.setName("p1");
        p.setValue("r1p1");
        r.addHeadReqProp(p);

        p = new DocumentRequisiteProp();
        p.setName("p2");
        p.setValue("r1p2");
        r.addHeadReqProp(p);
        doc.addHeadReq(r);

        r = new DocumentRequisite();
        r.setMnemo("r2");
        r.setValue("2");

        p = new DocumentRequisiteProp();
        p.setName("p1");
        p.setValue("r2p1");
        r.addHeadReqProp(p);

        p = new DocumentRequisiteProp();
        p.setName("p2");
        p.setValue("r2p2");
        r.addHeadReqProp(p);

        doc.addHeadReq(r);
        DocumentController c = new DocumentController(emf);
        c.create(doc);
        String pk = doc.getPK();

        Document read = c.read(pk);
        assertEquals(2, read.getHeadRequisites().size());
    }
}
