/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Stanslaus Odhiambo
 */
@Entity
@Table(name = "announcement")
@NamedQueries({
    @NamedQuery(name = "Announcement.findAll", query = "SELECT a FROM Announcement a"),
    @NamedQuery(name = "Announcement.findById", query = "SELECT a FROM Announcement a WHERE a.id = :id"),
    @NamedQuery(name = "Announcement.findByTitle", query = "SELECT a FROM Announcement a WHERE a.title = :title"),
    @NamedQuery(name = "Announcement.findByDateOfAnnouncemnt", query = "SELECT a FROM Announcement a WHERE a.dateOfAnnouncemnt = :dateOfAnnouncemnt"),
    @NamedQuery(name = "Announcement.findByContent", query = "SELECT a FROM Announcement a WHERE a.content = :content")})
public class Announcement implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "title")
    private String title;
    @Column(name = "date_of_announcemnt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfAnnouncemnt;
    @Column(name = "content")
    private String content;
    @JoinColumn(name = "posted_by", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private BioData postedBy;

    public Announcement() {
    }

    public Announcement(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDateOfAnnouncemnt() {
        return dateOfAnnouncemnt;
    }

    public void setDateOfAnnouncemnt(Date dateOfAnnouncemnt) {
        this.dateOfAnnouncemnt = dateOfAnnouncemnt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BioData getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(BioData postedBy) {
        this.postedBy = postedBy;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Announcement)) {
            return false;
        }
        Announcement other = (Announcement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.Announcement[ id=" + id + " ]";
    }
    
}
