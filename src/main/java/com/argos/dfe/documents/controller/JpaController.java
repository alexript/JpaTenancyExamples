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
import org.eclipse.persistence.config.EntityManagerProperties;

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
        return create(entity, null);
    }

    public boolean create(EntityClass entity, String tenant) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            if (tenant != null) {
                em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, tenant);
            }
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
        return read(id, null);
    }

    public EntityClass read(PKClass id, String tenant) {
        EntityManager em = emf.createEntityManager();
        try {
            if (tenant != null) {
                em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, tenant);
            }
            return em.find(getEntityClass(), id);
        } catch (Exception x) {
            return null;
        } finally {
            em.close();
        }
    }

    public boolean update(EntityClass entity) {
        return update(entity, null);
    }

    public boolean update(EntityClass entity, String tenant) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            if (tenant != null) {
                em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, tenant);
            }
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
        return delete(id, null);
    }

    public boolean delete(PKClass id, String tenant) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tr = em.getTransaction();
        if (tenant != null) {
            em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, tenant);
        }
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
        return createOrUpdate(entity, null);
    }

    public boolean createOrUpdate(EntityClass entity, String tenant) {
        PKClass pk = entity.getPK();
        if (pk == null) {
            return create(entity, tenant);
        } else {
            return update(entity, tenant);
        }
    }
}
