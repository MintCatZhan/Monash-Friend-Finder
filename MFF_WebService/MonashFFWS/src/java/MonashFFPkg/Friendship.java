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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author YuanZhan
 */
@Entity
@Table(name = "FRIENDSHIP")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Friendship.findAll", query = "SELECT f FROM Friendship f")
    , @NamedQuery(name = "Friendship.findByStdId", query = "SELECT f FROM Friendship f WHERE f.friendshipPK.stdId = :stdId")
    , @NamedQuery(name = "Friendship.findByFriendId", query = "SELECT f FROM Friendship f WHERE f.friendshipPK.friendId = :friendId")
    , @NamedQuery(name = "Friendship.findByStratingDate", query = "SELECT f FROM Friendship f WHERE f.stratingDate = :stratingDate")
    , @NamedQuery(name = "Friendship.findByEndingDate", query = "SELECT f FROM Friendship f WHERE f.endingDate = :endingDate")})
public class Friendship implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FriendshipPK friendshipPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STRATING_DATE")
    @Temporal(TemporalType.DATE)
    private Date stratingDate;
    @Column(name = "ENDING_DATE")
    @Temporal(TemporalType.DATE)
    private Date endingDate;
    @JoinColumn(name = "FRIEND_ID", referencedColumnName = "STD_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Student student;
    @JoinColumn(name = "STD_ID", referencedColumnName = "STD_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Student student1;

    public Friendship() {
    }

    public Friendship(FriendshipPK friendshipPK) {
        this.friendshipPK = friendshipPK;
    }

    public Friendship(FriendshipPK friendshipPK, Date stratingDate) {
        this.friendshipPK = friendshipPK;
        this.stratingDate = stratingDate;
    }

    public Friendship(long stdId, long friendId) {
        this.friendshipPK = new FriendshipPK(stdId, friendId);
    }

    public FriendshipPK getFriendshipPK() {
        return friendshipPK;
    }

    public void setFriendshipPK(FriendshipPK friendshipPK) {
        this.friendshipPK = friendshipPK;
    }

    public Date getStratingDate() {
        return stratingDate;
    }

    public void setStratingDate(Date stratingDate) {
        this.stratingDate = stratingDate;
    }

    public Date getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(Date endingDate) {
        this.endingDate = endingDate;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Student getStudent1() {
        return student1;
    }

    public void setStudent1(Student student1) {
        this.student1 = student1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (friendshipPK != null ? friendshipPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Friendship)) {
            return false;
        }
        Friendship other = (Friendship) object;
        if ((this.friendshipPK == null && other.friendshipPK != null) || (this.friendshipPK != null && !this.friendshipPK.equals(other.friendshipPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MonashFFPkg.Friendship[ friendshipPK=" + friendshipPK + " ]";
    }
    
}
