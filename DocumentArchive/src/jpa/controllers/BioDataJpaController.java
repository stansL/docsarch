/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.controllers;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import jpa.Schedule;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpa.Announcement;
import jpa.BioData;
import jpa.Documents;
import jpa.Messages;
import jpa.controllers.exceptions.IllegalOrphanException;
import jpa.controllers.exceptions.NonexistentEntityException;

/**
 *
 * @author Naeima
 */
public class BioDataJpaController implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5343074995811092435L;

	public BioDataJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BioData bioData) {
        if (bioData.getScheduleList() == null) {
            bioData.setScheduleList(new ArrayList<Schedule>());
        }
        if (bioData.getAnnouncementList() == null) {
            bioData.setAnnouncementList(new ArrayList<Announcement>());
        }
        if (bioData.getDocumentsList() == null) {
            bioData.setDocumentsList(new ArrayList<Documents>());
        }
        if (bioData.getMessagesList() == null) {
            bioData.setMessagesList(new ArrayList<Messages>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Schedule> attachedScheduleList = new ArrayList<Schedule>();
            for (Schedule scheduleListScheduleToAttach : bioData.getScheduleList()) {
                scheduleListScheduleToAttach = em.getReference(scheduleListScheduleToAttach.getClass(), scheduleListScheduleToAttach.getId());
                attachedScheduleList.add(scheduleListScheduleToAttach);
            }
            bioData.setScheduleList(attachedScheduleList);
            List<Announcement> attachedAnnouncementList = new ArrayList<Announcement>();
            for (Announcement announcementListAnnouncementToAttach : bioData.getAnnouncementList()) {
                announcementListAnnouncementToAttach = em.getReference(announcementListAnnouncementToAttach.getClass(), announcementListAnnouncementToAttach.getId());
                attachedAnnouncementList.add(announcementListAnnouncementToAttach);
            }
            bioData.setAnnouncementList(attachedAnnouncementList);
            List<Documents> attachedDocumentsList = new ArrayList<Documents>();
            for (Documents documentsListDocumentsToAttach : bioData.getDocumentsList()) {
                documentsListDocumentsToAttach = em.getReference(documentsListDocumentsToAttach.getClass(), documentsListDocumentsToAttach.getIddocuments());
                attachedDocumentsList.add(documentsListDocumentsToAttach);
            }
            bioData.setDocumentsList(attachedDocumentsList);
            List<Messages> attachedMessagesList = new ArrayList<Messages>();
            for (Messages messagesListMessagesToAttach : bioData.getMessagesList()) {
                messagesListMessagesToAttach = em.getReference(messagesListMessagesToAttach.getClass(), messagesListMessagesToAttach.getId());
                attachedMessagesList.add(messagesListMessagesToAttach);
            }
            bioData.setMessagesList(attachedMessagesList);
            em.persist(bioData);
            for (Schedule scheduleListSchedule : bioData.getScheduleList()) {
                BioData oldPostedByOfScheduleListSchedule = scheduleListSchedule.getPostedBy();
                scheduleListSchedule.setPostedBy(bioData);
                scheduleListSchedule = em.merge(scheduleListSchedule);
                if (oldPostedByOfScheduleListSchedule != null) {
                    oldPostedByOfScheduleListSchedule.getScheduleList().remove(scheduleListSchedule);
                    oldPostedByOfScheduleListSchedule = em.merge(oldPostedByOfScheduleListSchedule);
                }
            }
            for (Announcement announcementListAnnouncement : bioData.getAnnouncementList()) {
                BioData oldPostedByOfAnnouncementListAnnouncement = announcementListAnnouncement.getPostedBy();
                announcementListAnnouncement.setPostedBy(bioData);
                announcementListAnnouncement = em.merge(announcementListAnnouncement);
                if (oldPostedByOfAnnouncementListAnnouncement != null) {
                    oldPostedByOfAnnouncementListAnnouncement.getAnnouncementList().remove(announcementListAnnouncement);
                    oldPostedByOfAnnouncementListAnnouncement = em.merge(oldPostedByOfAnnouncementListAnnouncement);
                }
            }
            for (Documents documentsListDocuments : bioData.getDocumentsList()) {
                BioData oldCreatedByOfDocumentsListDocuments = documentsListDocuments.getCreatedBy();
                documentsListDocuments.setCreatedBy(bioData);
                documentsListDocuments = em.merge(documentsListDocuments);
                if (oldCreatedByOfDocumentsListDocuments != null) {
                    oldCreatedByOfDocumentsListDocuments.getDocumentsList().remove(documentsListDocuments);
                    oldCreatedByOfDocumentsListDocuments = em.merge(oldCreatedByOfDocumentsListDocuments);
                }
            }
            for (Messages messagesListMessages : bioData.getMessagesList()) {
                BioData oldSenderOfMessagesListMessages = messagesListMessages.getSender();
                messagesListMessages.setSender(bioData);
                messagesListMessages = em.merge(messagesListMessages);
                if (oldSenderOfMessagesListMessages != null) {
                    oldSenderOfMessagesListMessages.getMessagesList().remove(messagesListMessages);
                    oldSenderOfMessagesListMessages = em.merge(oldSenderOfMessagesListMessages);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BioData bioData) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BioData persistentBioData = em.find(BioData.class, bioData.getId());
            List<Schedule> scheduleListOld = persistentBioData.getScheduleList();
            List<Schedule> scheduleListNew = bioData.getScheduleList();
            List<Announcement> announcementListOld = persistentBioData.getAnnouncementList();
            List<Announcement> announcementListNew = bioData.getAnnouncementList();
            List<Documents> documentsListOld = persistentBioData.getDocumentsList();
            List<Documents> documentsListNew = bioData.getDocumentsList();
            List<Messages> messagesListOld = persistentBioData.getMessagesList();
            List<Messages> messagesListNew = bioData.getMessagesList();
            List<String> illegalOrphanMessages = null;
            for (Schedule scheduleListOldSchedule : scheduleListOld) {
                if (!scheduleListNew.contains(scheduleListOldSchedule)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Schedule " + scheduleListOldSchedule + " since its postedBy field is not nullable.");
                }
            }
            for (Announcement announcementListOldAnnouncement : announcementListOld) {
                if (!announcementListNew.contains(announcementListOldAnnouncement)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Announcement " + announcementListOldAnnouncement + " since its postedBy field is not nullable.");
                }
            }
            for (Documents documentsListOldDocuments : documentsListOld) {
                if (!documentsListNew.contains(documentsListOldDocuments)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Documents " + documentsListOldDocuments + " since its createdBy field is not nullable.");
                }
            }
            for (Messages messagesListOldMessages : messagesListOld) {
                if (!messagesListNew.contains(messagesListOldMessages)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Messages " + messagesListOldMessages + " since its sender field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Schedule> attachedScheduleListNew = new ArrayList<Schedule>();
            for (Schedule scheduleListNewScheduleToAttach : scheduleListNew) {
                scheduleListNewScheduleToAttach = em.getReference(scheduleListNewScheduleToAttach.getClass(), scheduleListNewScheduleToAttach.getId());
                attachedScheduleListNew.add(scheduleListNewScheduleToAttach);
            }
            scheduleListNew = attachedScheduleListNew;
            bioData.setScheduleList(scheduleListNew);
            List<Announcement> attachedAnnouncementListNew = new ArrayList<Announcement>();
            for (Announcement announcementListNewAnnouncementToAttach : announcementListNew) {
                announcementListNewAnnouncementToAttach = em.getReference(announcementListNewAnnouncementToAttach.getClass(), announcementListNewAnnouncementToAttach.getId());
                attachedAnnouncementListNew.add(announcementListNewAnnouncementToAttach);
            }
            announcementListNew = attachedAnnouncementListNew;
            bioData.setAnnouncementList(announcementListNew);
            List<Documents> attachedDocumentsListNew = new ArrayList<Documents>();
            for (Documents documentsListNewDocumentsToAttach : documentsListNew) {
                documentsListNewDocumentsToAttach = em.getReference(documentsListNewDocumentsToAttach.getClass(), documentsListNewDocumentsToAttach.getIddocuments());
                attachedDocumentsListNew.add(documentsListNewDocumentsToAttach);
            }
            documentsListNew = attachedDocumentsListNew;
            bioData.setDocumentsList(documentsListNew);
            List<Messages> attachedMessagesListNew = new ArrayList<Messages>();
            for (Messages messagesListNewMessagesToAttach : messagesListNew) {
                messagesListNewMessagesToAttach = em.getReference(messagesListNewMessagesToAttach.getClass(), messagesListNewMessagesToAttach.getId());
                attachedMessagesListNew.add(messagesListNewMessagesToAttach);
            }
            messagesListNew = attachedMessagesListNew;
            bioData.setMessagesList(messagesListNew);
            bioData = em.merge(bioData);
            for (Schedule scheduleListNewSchedule : scheduleListNew) {
                if (!scheduleListOld.contains(scheduleListNewSchedule)) {
                    BioData oldPostedByOfScheduleListNewSchedule = scheduleListNewSchedule.getPostedBy();
                    scheduleListNewSchedule.setPostedBy(bioData);
                    scheduleListNewSchedule = em.merge(scheduleListNewSchedule);
                    if (oldPostedByOfScheduleListNewSchedule != null && !oldPostedByOfScheduleListNewSchedule.equals(bioData)) {
                        oldPostedByOfScheduleListNewSchedule.getScheduleList().remove(scheduleListNewSchedule);
                        oldPostedByOfScheduleListNewSchedule = em.merge(oldPostedByOfScheduleListNewSchedule);
                    }
                }
            }
            for (Announcement announcementListNewAnnouncement : announcementListNew) {
                if (!announcementListOld.contains(announcementListNewAnnouncement)) {
                    BioData oldPostedByOfAnnouncementListNewAnnouncement = announcementListNewAnnouncement.getPostedBy();
                    announcementListNewAnnouncement.setPostedBy(bioData);
                    announcementListNewAnnouncement = em.merge(announcementListNewAnnouncement);
                    if (oldPostedByOfAnnouncementListNewAnnouncement != null && !oldPostedByOfAnnouncementListNewAnnouncement.equals(bioData)) {
                        oldPostedByOfAnnouncementListNewAnnouncement.getAnnouncementList().remove(announcementListNewAnnouncement);
                        oldPostedByOfAnnouncementListNewAnnouncement = em.merge(oldPostedByOfAnnouncementListNewAnnouncement);
                    }
                }
            }
            for (Documents documentsListNewDocuments : documentsListNew) {
                if (!documentsListOld.contains(documentsListNewDocuments)) {
                    BioData oldCreatedByOfDocumentsListNewDocuments = documentsListNewDocuments.getCreatedBy();
                    documentsListNewDocuments.setCreatedBy(bioData);
                    documentsListNewDocuments = em.merge(documentsListNewDocuments);
                    if (oldCreatedByOfDocumentsListNewDocuments != null && !oldCreatedByOfDocumentsListNewDocuments.equals(bioData)) {
                        oldCreatedByOfDocumentsListNewDocuments.getDocumentsList().remove(documentsListNewDocuments);
                        oldCreatedByOfDocumentsListNewDocuments = em.merge(oldCreatedByOfDocumentsListNewDocuments);
                    }
                }
            }
            for (Messages messagesListNewMessages : messagesListNew) {
                if (!messagesListOld.contains(messagesListNewMessages)) {
                    BioData oldSenderOfMessagesListNewMessages = messagesListNewMessages.getSender();
                    messagesListNewMessages.setSender(bioData);
                    messagesListNewMessages = em.merge(messagesListNewMessages);
                    if (oldSenderOfMessagesListNewMessages != null && !oldSenderOfMessagesListNewMessages.equals(bioData)) {
                        oldSenderOfMessagesListNewMessages.getMessagesList().remove(messagesListNewMessages);
                        oldSenderOfMessagesListNewMessages = em.merge(oldSenderOfMessagesListNewMessages);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = bioData.getId();
                if (findBioData(id) == null) {
                    throw new NonexistentEntityException("The bioData with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BioData bioData;
            try {
                bioData = em.getReference(BioData.class, id);
                bioData.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The bioData with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Schedule> scheduleListOrphanCheck = bioData.getScheduleList();
            for (Schedule scheduleListOrphanCheckSchedule : scheduleListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This BioData (" + bioData + ") cannot be destroyed since the Schedule " + scheduleListOrphanCheckSchedule + " in its scheduleList field has a non-nullable postedBy field.");
            }
            List<Announcement> announcementListOrphanCheck = bioData.getAnnouncementList();
            for (Announcement announcementListOrphanCheckAnnouncement : announcementListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This BioData (" + bioData + ") cannot be destroyed since the Announcement " + announcementListOrphanCheckAnnouncement + " in its announcementList field has a non-nullable postedBy field.");
            }
            List<Documents> documentsListOrphanCheck = bioData.getDocumentsList();
            for (Documents documentsListOrphanCheckDocuments : documentsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This BioData (" + bioData + ") cannot be destroyed since the Documents " + documentsListOrphanCheckDocuments + " in its documentsList field has a non-nullable createdBy field.");
            }
            List<Messages> messagesListOrphanCheck = bioData.getMessagesList();
            for (Messages messagesListOrphanCheckMessages : messagesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This BioData (" + bioData + ") cannot be destroyed since the Messages " + messagesListOrphanCheckMessages + " in its messagesList field has a non-nullable sender field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(bioData);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BioData> findBioDataEntities() {
        return findBioDataEntities(true, -1, -1);
    }

    public List<BioData> findBioDataEntities(int maxResults, int firstResult) {
        return findBioDataEntities(false, maxResults, firstResult);
    }

    private List<BioData> findBioDataEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BioData.class));
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

    public BioData findBioData(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BioData.class, id);
        } finally {
            em.close();
        }
    }

    public int getBioDataCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BioData> rt = cq.from(BioData.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
