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
public class LocationDistance implements Serializable {

    
    private String stdFname;
    private String stdLname;
    private double locLatitude;
    private double locLongitude;

    public LocationDistance(String stdFname, String stdLname, double locLatitude, double locLongitude) {
        this.stdFname = stdFname;
        this.stdLname = stdLname;
        this.locLatitude = locLatitude;
        this.locLongitude = locLongitude;
    }

    public LocationDistance() {
    }

    public String getStdFname() {
        return stdFname;
    }

    public void setStdFname(String stdFname) {
        this.stdFname = stdFname;
    }

    public String getStdLname() {
        return stdLname;
    }

    public void setStdLname(String stdLname) {
        this.stdLname = stdLname;
    }

    public double getLocLatitude() {
        return locLatitude;
    }

    public void setLocLatitude(double locLatitude) {
        this.locLatitude = locLatitude;
    }

    public double getLocLongitude() {
        return locLongitude;
    }

    public void setLocLongitude(double locLongitude) {
        this.locLongitude = locLongitude;
    }
    
}
