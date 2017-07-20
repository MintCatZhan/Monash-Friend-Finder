/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MonashFFPkg;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author YuanZhan
 */
@Entity
@Table(name = "STUDENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Student.findAll", query = "SELECT s FROM Student s")
    , @NamedQuery(name = "Student.findByStdId", query = "SELECT s FROM Student s WHERE s.stdId = :stdId")
    , @NamedQuery(name = "Student.findByFname", query = "SELECT s FROM Student s WHERE s.fname = :fname")
    , @NamedQuery(name = "Student.findByLname", query = "SELECT s FROM Student s WHERE s.lname = :lname")
    , @NamedQuery(name = "Student.findByDob", query = "SELECT s FROM Student s WHERE s.dob = :dob")
    , @NamedQuery(name = "Student.findByGender", query = "SELECT s FROM Student s WHERE s.gender = :gender")
    , @NamedQuery(name = "Student.findByCourse", query = "SELECT s FROM Student s WHERE s.course = :course")
    , @NamedQuery(name = "Student.findByMode", query = "SELECT s FROM Student s WHERE s.mode = :mode")
    , @NamedQuery(name = "Student.findByAddress", query = "SELECT s FROM Student s WHERE s.address = :address")
    , @NamedQuery(name = "Student.findBySuburb", query = "SELECT s FROM Student s WHERE s.suburb = :suburb")
    , @NamedQuery(name = "Student.findByNationality", query = "SELECT s FROM Student s WHERE s.nationality = :nationality")
    , @NamedQuery(name = "Student.findByLang", query = "SELECT s FROM Student s WHERE s.lang = :lang")
    , @NamedQuery(name = "Student.findByFavouriteSport", query = "SELECT s FROM Student s WHERE s.favouriteSport = :favouriteSport")
    , @NamedQuery(name = "Student.findByFavouriteMovie", query = "SELECT s FROM Student s WHERE s.favouriteMovie = :favouriteMovie")
    , @NamedQuery(name = "Student.findByFavouriteUnit", query = "SELECT s FROM Student s WHERE s.favouriteUnit = :favouriteUnit")
    , @NamedQuery(name = "Student.findByCurrentJob", query = "SELECT s FROM Student s WHERE s.currentJob = :currentJob")
    , @NamedQuery(name = "Student.findByEmailAddr", query = "SELECT s FROM Student s WHERE s.emailAddr = :emailAddr")
    , @NamedQuery(name = "Student.findByPwd", query = "SELECT s FROM Student s WHERE s.pwd = :pwd")
    , @NamedQuery(name = "Student.findBySubscriptDatetime", query = "SELECT s FROM Student s WHERE s.subscriptDatetime = :subscriptDatetime")})
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "STD_ID")
    private Long stdId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 35)
    @Column(name = "FNAME")
    private String fname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 35)
    @Column(name = "LNAME")
    private String lname;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DOB")
    @Temporal(TemporalType.DATE)
    private Date dob;
    @Basic(optional = false)
    @NotNull
    @Column(name = "GENDER")
    private Boolean gender;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "COURSE")
    private String course;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MODE")
    private Boolean mode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "ADDRESS")
    private String address;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "SUBURB")
    private String suburb;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NATIONALITY")
    private String nationality;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "LANG")
    private String lang;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "FAVOURITE_SPORT")
    private String favouriteSport;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "FAVOURITE_MOVIE")
    private String favouriteMovie;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "FAVOURITE_UNIT")
    private String favouriteUnit;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "CURRENT_JOB")
    private String currentJob;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "EMAIL_ADDR")
    private String emailAddr;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "PWD")
    private String pwd;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SUBSCRIPT_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date subscriptDatetime;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student")
    private Collection<Location> locationCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student")
    private Collection<Friendship> friendshipCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student1")
    private Collection<Friendship> friendshipCollection1;

    public Student() {
    }

    public Student(Long stdId) {
        this.stdId = stdId;
    }

    public Student(Long stdId, String fname, String lname, Date dob, Boolean gender, String course, Boolean mode, String address, String suburb, String nationality, String lang, String favouriteSport, String favouriteMovie, String favouriteUnit, String currentJob, String emailAddr, String pwd, Date subscriptDatetime) {
        this.stdId = stdId;
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
        this.gender = gender;
        this.course = course;
        this.mode = mode;
        this.address = address;
        this.suburb = suburb;
        this.nationality = nationality;
        this.lang = lang;
        this.favouriteSport = favouriteSport;
        this.favouriteMovie = favouriteMovie;
        this.favouriteUnit = favouriteUnit;
        this.currentJob = currentJob;
        this.emailAddr = emailAddr;
        this.pwd = pwd;
        this.subscriptDatetime = subscriptDatetime;
    }

    public Long getStdId() {
        return stdId;
    }

    public void setStdId(Long stdId) {
        this.stdId = stdId;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Boolean getMode() {
        return mode;
    }

    public void setMode(Boolean mode) {
        this.mode = mode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getFavouriteSport() {
        return favouriteSport;
    }

    public void setFavouriteSport(String favouriteSport) {
        this.favouriteSport = favouriteSport;
    }

    public String getFavouriteMovie() {
        return favouriteMovie;
    }

    public void setFavouriteMovie(String favouriteMovie) {
        this.favouriteMovie = favouriteMovie;
    }

    public String getFavouriteUnit() {
        return favouriteUnit;
    }

    public void setFavouriteUnit(String favouriteUnit) {
        this.favouriteUnit = favouriteUnit;
    }

    public String getCurrentJob() {
        return currentJob;
    }

    public void setCurrentJob(String currentJob) {
        this.currentJob = currentJob;
    }

    public String getEmailAddr() {
        return emailAddr;
    }

    public void setEmailAddr(String emailAddr) {
        this.emailAddr = emailAddr;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Date getSubscriptDatetime() {
        return subscriptDatetime;
    }

    public void setSubscriptDatetime(Date subscriptDatetime) {
        this.subscriptDatetime = subscriptDatetime;
    }

    @XmlTransient
    public Collection<Location> getLocationCollection() {
        return locationCollection;
    }

    public void setLocationCollection(Collection<Location> locationCollection) {
        this.locationCollection = locationCollection;
    }

    @XmlTransient
    public Collection<Friendship> getFriendshipCollection() {
        return friendshipCollection;
    }

    public void setFriendshipCollection(Collection<Friendship> friendshipCollection) {
        this.friendshipCollection = friendshipCollection;
    }

    @XmlTransient
    public Collection<Friendship> getFriendshipCollection1() {
        return friendshipCollection1;
    }

    public void setFriendshipCollection1(Collection<Friendship> friendshipCollection1) {
        this.friendshipCollection1 = friendshipCollection1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stdId != null ? stdId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Student)) {
            return false;
        }
        Student other = (Student) object;
        if ((this.stdId == null && other.stdId != null) || (this.stdId != null && !this.stdId.equals(other.stdId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MonashFFPkg.Student[ stdId=" + stdId + " ]";
    }
    
}
