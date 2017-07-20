/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MonashFFPkg;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author YuanZhan
 */
@Embeddable
public class FriendshipPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "STD_ID")
    private long stdId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FRIEND_ID")
    private long friendId;

    public FriendshipPK() {
    }

    public FriendshipPK(long stdId, long friendId) {
        this.stdId = stdId;
        this.friendId = friendId;
    }

    public long getStdId() {
        return stdId;
    }

    public void setStdId(long stdId) {
        this.stdId = stdId;
    }

    public long getFriendId() {
        return friendId;
    }

    public void setFriendId(long friendId) {
        this.friendId = friendId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) stdId;
        hash += (int) friendId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FriendshipPK)) {
            return false;
        }
        FriendshipPK other = (FriendshipPK) object;
        if (this.stdId != other.stdId) {
            return false;
        }
        if (this.friendId != other.friendId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MonashFFPkg.FriendshipPK[ stdId=" + stdId + ", friendId=" + friendId + " ]";
    }
    
}
