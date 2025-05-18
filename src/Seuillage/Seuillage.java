package Seuillage;

import java.util.List;
import java.util.ArrayList;


public class Seuillage {
    
    private double seuil;
    
    // liste de tableau de double qui vont être appliqués à seuillage
    private List<double[]> ListV;

    //Constructeur
    public Seuillage(double seuil, List<double[]> ListV) {
        this.seuil = seuil;
        this.ListV = ListV;
    }
    
    //Getter et Setter 
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

    // Méthode pour calculer le seuil VisuShrink
 
    public static double seuilV(double sigma, int tailleImage) {
        return sigma * Math.sqrt(2 * Math.log(tailleImage));
    }
    
    //Méthode pour calculer le seuil BayesShrink
    
    public static double seuilB(double sigma, List<double[]> listeVecteurs) {
    	double somme = 0.0; // somme des carrés des pixels
    	int nbVal = 0;	    // Nombre total de valeurs
    	
    	
    	for(double[] vecteur : listeVecteurs) {
    		for(double pixel : vecteur) {
    			somme += Math.pow(pixel, 2); //Ajout du carré de la valeur au total
    			nbVal++;
    		}
    	}
    	
    	// Variance moyenne de l'image
    	double varIm= somme/nbVal;
    	
    	//Ecart-type estimé
    	double sigmaX = Math.sqrt(Math.max(varIm - Math.pow(sigma, 2), 0));
    	
    	//Si l'écart-type est nul on retourne un maximum, Sinon on calcul le sueil 
    	if(sigmaX == 0) {
    		return Double.MAX_VALUE;
    	}
    	return Math.pow(sigma, 2) / sigmaX;
    }
 
    // Méthode de seuillage dur
    public List<double[]> seuillageDur() {
    List<double[]> list = new ArrayList<>(); // Tableau qui stockera les valeurs après seuil

    for (double[] vecteur : this.ListV) {
        double[] modifie = new double[vecteur.length];

        for (int i = 0; i < vecteur.length; i++) {
            if (Math.abs(vecteur[i]) > this.seuil) {
                modifie[i] = vecteur[i];
            } else {
                modifie[i] = 0.0;
            }
        }

        list.add(modifie);
    }

    return list;
    }

    
    
    //Méthode de seuillage doux
    
    public List<double[]> seuillageDoux(){
    
    	List<double[]> res = new ArrayList<>(); // Tableau qui stockera les valeurs après seuil
    	
    	//Application du seuil Doux à chaque élément du vecteur
    	for(double[] vecteur : this.ListV) {
    		double[] vecteurSeuil = new double[vecteur.length];
    		
    		for(int i=0; i < vecteur.length; i++) {
    			if(Math.abs(vecteur[i])<= this.seuil) {
    				vecteurSeuil[i] = 0.0; // Si la valeur est inférieur au seuil, elle devient 0
    			}else {
    				vecteurSeuil[i]= Math.signum(vecteur[i])*(Math.abs(vecteur[i]) - this.seuil);	//Sinon, on la réduit (en gardant le même signe)		
    		}
    	}
    	//Ajout du vecteur après seuil à la liste de vecteurs "seuillés"	
    	res.add(vecteurSeuil);
    }
    	//On retourne une liste contenant tous les vecteurs après le sueil doux
    	return res;

    }
    


}