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
import jpa.Messages;
import jpa.controllers.exceptions.NonexistentEntityException;

/**
 *
 * @author Stanslaus Odhiambo
 */
public class MessagesJpaController implements Serializable {

    public MessagesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Messages messages) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BioData sender = messages.getSender();
            if (sender != null) {
                sender = em.getReference(sender.getClass(), sender.getId());
                messages.setSender(sender);
            }
            em.persist(messages);
            if (sender != null) {
                sender.getMessagesList().add(messages);
                sender = em.merge(sender);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Messages messages) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Messages persistentMessages = em.find(Messages.class, messages.getId());
            BioData senderOld = persistentMessages.getSender();
            BioData senderNew = messages.getSender();
            if (senderNew != null) {
                senderNew = em.getReference(senderNew.getClass(), senderNew.getId());
                messages.setSender(senderNew);
            }
            messages = em.merge(messages);
            if (senderOld != null && !senderOld.equals(senderNew)) {
                senderOld.getMessagesList().remove(messages);
                senderOld = em.merge(senderOld);
            }
            if (senderNew != null && !senderNew.equals(senderOld)) {
                senderNew.getMessagesList().add(messages);
                senderNew = em.merge(senderNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = messages.getId();
                if (findMessages(id) == null) {
                    throw new NonexistentEntityException("The messages with id " + id + " no longer exists.");
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
            Messages messages;
            try {
                messages = em.getReference(Messages.class, id);
                messages.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The messages with id " + id + " no longer exists.", enfe);
            }
            BioData sender = messages.getSender();
            if (sender != null) {
                sender.getMessagesList().remove(messages);
                sender = em.merge(sender);
            }
            em.remove(messages);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Messages> findMessagesEntities() {
        return findMessagesEntities(true, -1, -1);
    }

    public List<Messages> findMessagesEntities(int maxResults, int firstResult) {
        return findMessagesEntities(false, maxResults, firstResult);
    }

    private List<Messages> findMessagesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Messages.class));
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

    public Messages findMessages(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Messages.class, id);
        } finally {
            em.close();
        }
    }

    public int getMessagesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Messages> rt = cq.from(Messages.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
