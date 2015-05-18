/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.models;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jpa.Announcement;

/**
 *
 * @author Stanslaus Odhiambo
 */
@Stateless
public class AnnouncementFacade extends AbstractFacade<Announcement> {
    @PersistenceContext(unitName = "DocumentArchive")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AnnouncementFacade() {
        super(Announcement.class);
    }
    
}
