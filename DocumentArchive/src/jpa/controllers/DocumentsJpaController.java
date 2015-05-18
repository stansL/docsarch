/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.controllers;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import jpa.BioData;
import jpa.Documents;
import jpa.controllers.exceptions.NonexistentEntityException;

/**
 *
 * @author Stanslaus Odhiambo
 */
public class DocumentsJpaController implements Serializable {

    public DocumentsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Documents documents) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BioData createdBy = documents.getCreatedBy();
            if (createdBy != null) {
                createdBy = em.getReference(createdBy.getClass(), createdBy.getId());
                documents.setCreatedBy(createdBy);
            }
            em.persist(documents);
            if (createdBy != null) {
                createdBy.getDocumentsList().add(documents);
                createdBy = em.merge(createdBy);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Documents documents) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Documents persistentDocuments = em.find(Documents.class, documents.getIddocuments());
            BioData createdByOld = persistentDocuments.getCreatedBy();
            BioData createdByNew = documents.getCreatedBy();
            if (createdByNew != null) {
                createdByNew = em.getReference(createdByNew.getClass(), createdByNew.getId());
                documents.setCreatedBy(createdByNew);
            }
            documents = em.merge(documents);
            if (createdByOld != null && !createdByOld.equals(createdByNew)) {
                createdByOld.getDocumentsList().remove(documents);
                createdByOld = em.merge(createdByOld);
            }
            if (createdByNew != null && !createdByNew.equals(createdByOld)) {
                createdByNew.getDocumentsList().add(documents);
                createdByNew = em.merge(createdByNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = documents.getIddocuments();
                if (findDocuments(id) == null) {
                    throw new NonexistentEntityException("The documents with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Documents documents;
            try {
                documents = em.getReference(Documents.class, id);
                documents.getIddocuments();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The documents with id " + id + " no longer exists.", enfe);
            }
            BioData createdBy = documents.getCreatedBy();
            if (createdBy != null) {
                createdBy.getDocumentsList().remove(documents);
                createdBy = em.merge(createdBy);
            }
            em.remove(documents);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Documents> findDocumentsEntities() {
        return findDocumentsEntities(true, -1, -1);
    }

    public List<Documents> findDocumentsEntities(int maxResults, int firstResult) {
        return findDocumentsEntities(false, maxResults, firstResult);
    }

    private List<Documents> findDocumentsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Documents.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Documents findDocuments(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Documents.class, id);
        } finally {
            em.close();
        }
    }

    public int getDocumentsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Documents> rt = cq.from(Documents.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
