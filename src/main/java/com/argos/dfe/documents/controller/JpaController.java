/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argos.dfe.documents.controller;

import com.argos.dfe.documents.PersistedEntity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

/**
 *
 * @author malyshev
 * @param <EntityClass>
 * @param <PKClass>
 */
public abstract class JpaController<EntityClass extends PersistedEntity<PKClass>, PKClass> {

    private EntityManagerFactory emf = null;

    public JpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public boolean create(EntityClass entity) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.persist(entity);
            tr.commit();
            return true;
        } catch (Exception x) {
            tr.rollback();
            return false;
        } finally {
            em.close();
        }
    }

    abstract protected Class<EntityClass> getEntityClass();

    public EntityClass read(PKClass id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(getEntityClass(), id);
        } catch (Exception x) {
            return null;
        } finally {
            em.close();
        }
    }

    public boolean update(EntityClass entity) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.merge(entity);
            tr.commit();
            return true;
        } catch (Exception x) {
            tr.rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public boolean delete(PKClass id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tr = em.getTransaction();
        try {
            EntityClass entity = em.getReference(getEntityClass(), id);
            if (entity == null) {
                return false;
            }
            tr.begin();
            em.remove(entity);
            tr.commit();
            return true;
        } catch (Exception x) {
            tr.rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public boolean createOrUpdate(EntityClass entity) {
        PKClass pk = entity.getPK();
        if (pk == null) {
            return create(entity);
        } else {
            return update(entity);
        }
    }
}
