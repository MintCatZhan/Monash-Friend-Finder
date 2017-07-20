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
public class FavUnitFrequency implements Serializable {

    private String unit;
    
    private int fre;
    
    public FavUnitFrequency(){
    }

    public FavUnitFrequency(String unit, int fre) {
        this.unit = unit;
        this.fre = fre;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getFre() {
        return fre;
    }

    public void setFre(int fre) {
        this.fre = fre;
    }
}
