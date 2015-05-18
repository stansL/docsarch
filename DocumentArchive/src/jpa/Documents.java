/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Stanslaus Odhiambo
 */
@Entity
@Table(name = "documents")
@NamedQueries({
    @NamedQuery(name = "Documents.findAll", query = "SELECT d FROM Documents d"),
    @NamedQuery(name = "Documents.findByIddocuments", query = "SELECT d FROM Documents d WHERE d.iddocuments = :iddocuments"),
    @NamedQuery(name = "Documents.findByTitle", query = "SELECT d FROM Documents d WHERE d.title = :title")})
public class Documents implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "iddocuments")
    private Integer iddocuments;
    @Column(name = "title")
    private String title;
    @Lob
    @Column(name = "content")
    private byte[] content;
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private BioData createdBy;

    public Documents() {
    }

    public Documents(Integer iddocuments) {
        this.iddocuments = iddocuments;
    }

    public Integer getIddocuments() {
        return iddocuments;
    }

    public void setIddocuments(Integer iddocuments) {
        this.iddocuments = iddocuments;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public BioData getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(BioData createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iddocuments != null ? iddocuments.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Documents)) {
            return false;
        }
        Documents other = (Documents) object;
        if ((this.iddocuments == null && other.iddocuments != null) || (this.iddocuments != null && !this.iddocuments.equals(other.iddocuments))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.Documents[ iddocuments=" + iddocuments + " ]";
    }
    
}
