/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MonashFFPkg;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author YuanZhan
 */

@XmlRootElement
public class LocationFrequency implements Serializable {

    private String locName;
    
    
    private int times;
    
    
    public LocationFrequency(){
    }
    
    public LocationFrequency(String locName, int times){
        this.locName = locName;
        this.times = times;
    }

    public String getLocName() {
        return locName;
    }

    public void setLocName(String locName) {
        this.locName = locName;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }
}
