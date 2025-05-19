package Vecteur;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un vecteur numérique avec identifiant, taille et contenu.
 * Utilisé pour la manipulation de données sous forme de vecteurs, notamment dans les algorithmes d'ACP.
 * 
 * Cette classe permet de créer des vecteurs, d'accéder à leurs valeurs et de calculer le produit scalaire entre deux vecteurs.
 * 
 * @author Alaia
 * 
 * @version 1.0
 */
public class Vecteur {
		private int id;
	    private int taille;
	    private double[] valeur;
	    
	    
	    /**
	     * Constructeur par défaut (vecteur vide).
	     */
	    public Vecteur() {
	    	this.id = 0;
	        this.taille = 0;
	        this.valeur = new double[0];
	    }
	    
	    /**
	     * Constructeur avec identifiant.
	     *
	     * @param id     Identifiant du vecteur.
	     * @param taille Taille du vecteur.
	     * @param valeur Tableau des valeurs du vecteur.
	     */
	    public Vecteur(int id, int taille, double[] valeur) {
	    	this.id = id;
	        this.taille = taille;
	        this.valeur = valeur;
	    }
	    
	    /**
	     * Constructeur sans identifiant explicite (id = -1).
	     */
	    public Vecteur(int taille, double[] valeur) {
	    	this.id = -1;
	        this.taille = taille;
	        this.valeur = valeur;
	    }
	    
	    //Getter et Setter ID
	    public int getID() {
	        return id;
	    }

	    public void setID(int id) {
	        this.id = id;
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
	    
	    /**
	     * Affiche une représentation textuelle du vecteur.
	     */
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
	    
	    /**
	     * Calcule le produit scalaire entre deux tableaux.
	     *
	     * @param a Premier vecteur.
	     * @param b Deuxième vecteur.
	     * @return Produit scalaire des deux vecteurs.
	     */
	    public static double produitScalaire(double[] a, double[] b) {
	    	double sum=0.0;
	    	for (int i=0;i<a.length;i++) {
	    		sum= sum + a[i] * b[i];
	    	}
	    	return sum;
	    }
	    
	    
	    
	    /**
	     * Projette une liste de vecteurs sur une base orthonormale.
	     *
	     * @param bo    Base orthonormale.
	     * @param listv Liste de vecteurs à projeter.
	     * @return Liste de vecteurs projetés (contributions).
	     */
	    public static List<Vecteur> proj(List<Vecteur> bo, List<Vecteur> listv) {
	        List<Vecteur> Vcontrib = new ArrayList<>();

	        for (int k = 0; k < listv.size(); k++) {
	            Vecteur v = listv.get(k);
	            double[] alpha = new double[bo.size()];

	            for (int i = 0; i < bo.size(); i++) {
	                Vecteur u = bo.get(i);
	                alpha[i] = produitScalaire(u.getValeur(), v.getValeur());
	            }

	            Vecteur contribution = new Vecteur(v.getID(),alpha.length, alpha);
	            Vcontrib.add(contribution);
	        }

	        return Vcontrib;
	    }
	    
	     


}
