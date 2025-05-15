package Seuillage;

import java.util.List;

public class Seuillage {
    
    private double seuil;
    
    List<double[]> ListV;

    public Seuillage(double seuil, List<double[]> ListV) {
        this.seuil = seuil;
        this.ListV = ListV;
    }
    public double getSeuil() {
        return seuil;
    }

    public void setSeuil(double seuil) {
        this.seuil = seuil;
    }

    public List<double[]> getListV() {
        return ListV;
    }

    public void setListV(List<double[]> listV) {
        ListV = listV;
    }

    


}
