/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MonashFFPkg;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author YuanZhan
 */
@Embeddable
public class LocationPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "DATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STD_ID")
    private long stdId;

    public LocationPK() {
    }

    public LocationPK(Date dateTime, long stdId) {
        this.dateTime = dateTime;
        this.stdId = stdId;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public long getStdId() {
        return stdId;
    }

    public void setStdId(long stdId) {
        this.stdId = stdId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dateTime != null ? dateTime.hashCode() : 0);
        hash += (int) stdId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LocationPK)) {
            return false;
        }
        LocationPK other = (LocationPK) object;
        if ((this.dateTime == null && other.dateTime != null) || (this.dateTime != null && !this.dateTime.equals(other.dateTime))) {
            return false;
        }
        if (this.stdId != other.stdId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MonashFFPkg.LocationPK[ dateTime=" + dateTime + ", stdId=" + stdId + " ]";
    }
    
}
