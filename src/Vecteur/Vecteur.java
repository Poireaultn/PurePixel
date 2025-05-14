package Vecteur;

import java.util.ArrayList;
import java.util.List;

public class Vecteur {
		
	    private int taille;
	    private double[] valeur;
	    
	    
	    //Constructeur par défaut
	    public Vecteur() {
	        this.taille = 0;
	        this.valeur = new double[0];
	    }
	    
	    //Constructeur avec paramètres
	    public Vecteur(int taille, double[] valeur) {
	       
	        this.taille = taille;
	        this.valeur = valeur;
	    }
	    
	
	    //Getter et Setter taille
	    public int getTaille() {
	        return taille;
	    }

	    public void setTaille(int taille) {
	        this.taille = taille;
	    }

	    //Getter et Setter Valeur
	    public double[] getValeur() {
	        return valeur;
	    }

	    public void setValeur(double[] valeur) {
	        this.valeur = valeur;
	    }
	    
	    
	    @Override
	    public String toString() {
	        StringBuilder sb = new StringBuilder();
	        sb.append("Vecteur ID: ").append(", Taille: ").append(taille).append(", Valeurs: [");
	        for (int i = 0; i < valeur.length; i++) {
	            sb.append(valeur[i]);
	            if (i < valeur.length - 1) {
	                sb.append(", ");
	            }
	        }
	        sb.append("]");
	        return sb.toString();
	    }
	    
	    public static double produitScalaire(double[] a, double[] b) {
	    	double sum=0.0;
	    	for (int i=0;i<a.length;i++) {
	    		sum= sum + a[i] * b[i];
	    	}
	    	return sum;
	    }
	    
	    //Fait le projeté des vecteurs centrés sur la base orthonormale
	    public static List<Vecteur> Proj(List<Vecteur> bo, List<Vecteur> listv) {
	        List<Vecteur> Vcont = new ArrayList<>();

	        for (int k = 0; k < listv.size(); k++) {
	            Vecteur v = listv.get(k);
	            double[] alpha = new double[bo.size()];

	            for (int i = 0; i < bo.size(); i++) {
	                Vecteur u = bo.get(i);
	                alpha[i] = produitScalaire(u.getValeur(), v.getValeur());
	            }

	            Vecteur contribution = new Vecteur(alpha.length, alpha);
	            Vcont.add(contribution);
	        }

	        return Vcont;
	    }


}
