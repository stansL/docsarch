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
import jpa.Schedule;
import jpa.controllers.exceptions.NonexistentEntityException;

/**
 *
 * @author Stanslaus Odhiambo
 */
public class ScheduleJpaController implements Serializable {

    public ScheduleJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Schedule schedule) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BioData postedBy = schedule.getPostedBy();
            if (postedBy != null) {
                postedBy = em.getReference(postedBy.getClass(), postedBy.getId());
                schedule.setPostedBy(postedBy);
            }
            em.persist(schedule);
            if (postedBy != null) {
                postedBy.getScheduleList().add(schedule);
                postedBy = em.merge(postedBy);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Schedule schedule) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Schedule persistentSchedule = em.find(Schedule.class, schedule.getId());
            BioData postedByOld = persistentSchedule.getPostedBy();
            BioData postedByNew = schedule.getPostedBy();
            if (postedByNew != null) {
                postedByNew = em.getReference(postedByNew.getClass(), postedByNew.getId());
                schedule.setPostedBy(postedByNew);
            }
            schedule = em.merge(schedule);
            if (postedByOld != null && !postedByOld.equals(postedByNew)) {
                postedByOld.getScheduleList().remove(schedule);
                postedByOld = em.merge(postedByOld);
            }
            if (postedByNew != null && !postedByNew.equals(postedByOld)) {
                postedByNew.getScheduleList().add(schedule);
                postedByNew = em.merge(postedByNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = schedule.getId();
                if (findSchedule(id) == null) {
                    throw new NonexistentEntityException("The schedule with id " + id + " no longer exists.");
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
            Schedule schedule;
            try {
                schedule = em.getReference(Schedule.class, id);
                schedule.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The schedule with id " + id + " no longer exists.", enfe);
            }
            BioData postedBy = schedule.getPostedBy();
            if (postedBy != null) {
                postedBy.getScheduleList().remove(schedule);
                postedBy = em.merge(postedBy);
            }
            em.remove(schedule);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Schedule> findScheduleEntities() {
        return findScheduleEntities(true, -1, -1);
    }

    public List<Schedule> findScheduleEntities(int maxResults, int firstResult) {
        return findScheduleEntities(false, maxResults, firstResult);
    }

    private List<Schedule> findScheduleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Schedule.class));
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

    public Schedule findSchedule(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Schedule.class, id);
        } finally {
            em.close();
        }
    }

    public int getScheduleCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Schedule> rt = cq.from(Schedule.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
