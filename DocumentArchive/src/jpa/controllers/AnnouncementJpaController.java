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
import jpa.Announcement;
import jpa.BioData;
import jpa.controllers.exceptions.NonexistentEntityException;

/**
 *
 * @author Naeima
 */
public class AnnouncementJpaController implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 882592800466042409L;

	public AnnouncementJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Announcement announcement) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BioData postedBy = announcement.getPostedBy();
            if (postedBy != null) {
                postedBy = em.getReference(postedBy.getClass(), postedBy.getId());
                announcement.setPostedBy(postedBy);
            }
            em.persist(announcement);
            if (postedBy != null) {
                postedBy.getAnnouncementList().add(announcement);
                postedBy = em.merge(postedBy);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Announcement announcement) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Announcement persistentAnnouncement = em.find(Announcement.class, announcement.getId());
            BioData postedByOld = persistentAnnouncement.getPostedBy();
            BioData postedByNew = announcement.getPostedBy();
            if (postedByNew != null) {
                postedByNew = em.getReference(postedByNew.getClass(), postedByNew.getId());
                announcement.setPostedBy(postedByNew);
            }
            announcement = em.merge(announcement);
            if (postedByOld != null && !postedByOld.equals(postedByNew)) {
                postedByOld.getAnnouncementList().remove(announcement);
                postedByOld = em.merge(postedByOld);
            }
            if (postedByNew != null && !postedByNew.equals(postedByOld)) {
                postedByNew.getAnnouncementList().add(announcement);
                postedByNew = em.merge(postedByNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = announcement.getId();
                if (findAnnouncement(id) == null) {
                    throw new NonexistentEntityException("The announcement with id " + id + " no longer exists.");
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
            Announcement announcement;
            try {
                announcement = em.getReference(Announcement.class, id);
                announcement.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The announcement with id " + id + " no longer exists.", enfe);
            }
            BioData postedBy = announcement.getPostedBy();
            if (postedBy != null) {
                postedBy.getAnnouncementList().remove(announcement);
                postedBy = em.merge(postedBy);
            }
            em.remove(announcement);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Announcement> findAnnouncementEntities() {
        return findAnnouncementEntities(true, -1, -1);
    }

    public List<Announcement> findAnnouncementEntities(int maxResults, int firstResult) {
        return findAnnouncementEntities(false, maxResults, firstResult);
    }

    private List<Announcement> findAnnouncementEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Announcement.class));
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

    public Announcement findAnnouncement(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Announcement.class, id);
        } finally {
            em.close();
        }
    }

    public int getAnnouncementCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Announcement> rt = cq.from(Announcement.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
