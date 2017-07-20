/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MonashFFPkg;

import java.util.Date;

/**
 *
 * @author YuanZhan
 */
public class LocationLatestForEach {
    private long stdId;
    private Date dateTime;

    public LocationLatestForEach() {
    }

    public LocationLatestForEach(long stdId, Date dateTime) {
        this.stdId = stdId;
        this.dateTime = dateTime;
    }

    public long getStdId() {
        return stdId;
    }

    public void setStdId(long stdId) {
        this.stdId = stdId;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
    
}
