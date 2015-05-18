/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.models;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jpa.Documents;

/**
 *
 * @author Stanslaus Odhiambo
 */
@Stateless
public class DocumentsFacade extends AbstractFacade<Documents> {
    @PersistenceContext(unitName = "DocumentArchive")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DocumentsFacade() {
        super(Documents.class);
    }
    
}
