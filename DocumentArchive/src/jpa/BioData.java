/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Naeima
 */
@Entity
@Table(name = "bio_data")
@NamedQueries({
    @NamedQuery(name = "BioData.findAll", query = "SELECT b FROM BioData b"),
    @NamedQuery(name = "BioData.findById", query = "SELECT b FROM BioData b WHERE b.id = :id"),
    @NamedQuery(name = "BioData.findByFullNames", query = "SELECT b FROM BioData b WHERE b.fullNames = :fullNames"),
    @NamedQuery(name = "BioData.findByEmail", query = "SELECT b FROM BioData b WHERE b.email = :email"),
    @NamedQuery(name = "BioData.findByPhone", query = "SELECT b FROM BioData b WHERE b.phone = :phone"),
    @NamedQuery(name = "BioData.findByNationality", query = "SELECT b FROM BioData b WHERE b.nationality = :nationality"),
    @NamedQuery(name = "BioData.findBySex", query = "SELECT b FROM BioData b WHERE b.sex = :sex"),
    @NamedQuery(name = "BioData.findByDateOfBirth", query = "SELECT b FROM BioData b WHERE b.dateOfBirth = :dateOfBirth"),
    @NamedQuery(name = "BioData.findByAddress", query = "SELECT b FROM BioData b WHERE b.address = :address")})
public class BioData implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "full_names")
    private String fullNames;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phone;
    @Column(name = "nationality")
    private String nationality;
    @Column(name = "sex")
    private String sex;
    @Column(name = "date_of_birth")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfBirth;
    @Column(name = "address")
    private String address;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "postedBy", fetch = FetchType.LAZY)
    private List<Schedule> scheduleList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "postedBy", fetch = FetchType.LAZY)
    private List<Announcement> announcementList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "createdBy", fetch = FetchType.LAZY)
    private List<Documents> documentsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sender", fetch = FetchType.LAZY)
    private List<Messages> messagesList;

    public BioData() {
    }

    public BioData(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullNames() {
        return fullNames;
    }

    public void setFullNames(String fullNames) {
        this.fullNames = fullNames;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Schedule> getScheduleList() {
        return scheduleList;
    }

    public void setScheduleList(List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
    }

    public List<Announcement> getAnnouncementList() {
        return announcementList;
    }

    public void setAnnouncementList(List<Announcement> announcementList) {
        this.announcementList = announcementList;
    }

    public List<Documents> getDocumentsList() {
        return documentsList;
    }

    public void setDocumentsList(List<Documents> documentsList) {
        this.documentsList = documentsList;
    }

    public List<Messages> getMessagesList() {
        return messagesList;
    }

    public void setMessagesList(List<Messages> messagesList) {
        this.messagesList = messagesList;
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
        if (!(object instanceof BioData)) {
            return false;
        }
        BioData other = (BioData) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.BioData[ id=" + id + " ]";
    }
    
}
