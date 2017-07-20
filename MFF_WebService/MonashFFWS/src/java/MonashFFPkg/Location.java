/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MonashFFPkg;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author YuanZhan
 */
@Entity
@Table(name = "LOCATION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Location.findAll", query = "SELECT l FROM Location l")
    , @NamedQuery(name = "Location.findByLatitude", query = "SELECT l FROM Location l WHERE l.latitude = :latitude")
    , @NamedQuery(name = "Location.findByLongitude", query = "SELECT l FROM Location l WHERE l.longitude = :longitude")
    , @NamedQuery(name = "Location.findByDateTime", query = "SELECT l FROM Location l WHERE l.locationPK.dateTime = :dateTime")
    , @NamedQuery(name = "Location.findByLocName", query = "SELECT l FROM Location l WHERE l.locName = :locName")
    , @NamedQuery(name = "Location.findByStdId", query = "SELECT l FROM Location l WHERE l.locationPK.stdId = :stdId")})
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected LocationPK locationPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "LATITUDE")
    private BigDecimal latitude;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LONGITUDE")
    private BigDecimal longitude;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "LOC_NAME")
    private String locName;
    @JoinColumn(name = "STD_ID", referencedColumnName = "STD_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Student student;

    public Location() {
    }

    public Location(LocationPK locationPK) {
        this.locationPK = locationPK;
    }

    public Location(LocationPK locationPK, BigDecimal latitude, BigDecimal longitude, String locName) {
        this.locationPK = locationPK;
        this.latitude = latitude;
        this.longitude = longitude;
        this.locName = locName;
    }

    public Location(Date dateTime, long stdId) {
        this.locationPK = new LocationPK(dateTime, stdId);
    }

    public LocationPK getLocationPK() {
        return locationPK;
    }

    public void setLocationPK(LocationPK locationPK) {
        this.locationPK = locationPK;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getLocName() {
        return locName;
    }

    public void setLocName(String locName) {
        this.locName = locName;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (locationPK != null ? locationPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Location)) {
            return false;
        }
        Location other = (Location) object;
        if ((this.locationPK == null && other.locationPK != null) || (this.locationPK != null && !this.locationPK.equals(other.locationPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MonashFFPkg.Location[ locationPK=" + locationPK + " ]";
    }
    
}
