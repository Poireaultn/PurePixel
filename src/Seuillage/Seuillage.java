package Seuillage;

import java.util.List;
import java.util.ArrayList;

import Vecteur.Vecteur;

/**
 * Classe permettant d'appliquer différents types de seuillages (dur et doux)
 * sur des vecteurs de coefficients (par exemple issus d'une décomposition en ACP).
 * Fournit également les méthodes de calcul de seuils VisuShrink et BayesShrink.
 * 
 * Cette classe est utilisée pour le débruitage d'images en utilisant des techniques
 * 
 * 
 * @author Alaia, Hichem
 * @version 1.0
 * 
 */
public class Seuillage {
    
    private double seuil;
    
    private List<Vecteur> listV;  // Liste des vecteurs à seuiller

    /**
     * Constructeur de la classe Seuillage.
     *
     * @param seuil Seuil à appliquer aux coefficients.
     * @param coeffs Liste de vecteurs de coefficients.
     */
    public Seuillage(double seuil, List<Vecteur> coeffs) {
        this.seuil = seuil;
        this.listV = coeffs;
    }
    
    // Getter et Setter
    public double getSeuil() {
        return seuil;
    }

    public void setSeuil(double seuil) {
        this.seuil = seuil;
    }

    public List<Vecteur> getListV() {
        return listV;
    }

    public void setListV(List<Vecteur> listV) {
        this.listV = listV;
    }

    /**
     * Calcule le seuil VisuShrink.
     *
     * @param sigma       Bruit estimé (sigma).
     * @param tailleImage Nombre total de pixels ou de coefficients.
     * @return Seuil calculé.
     */
    public static double seuilV(double sigma, int tailleImage) {
        return sigma * Math.sqrt(2 * Math.log(tailleImage));
    }
    
    /**
     * Calcule le seuil BayesShrink.
     *
     * @param sigma          Bruit estimé (sigma).
     * @param listeVecteurs  Liste des vecteurs de coefficients.
     * @return Seuil adaptatif selon la variance de l'image.
     */
    public static double seuilB(double sigma, List<Vecteur> listeVecteurs) {
        double somme = 0.0;
        int nbVal = 0;

        for (Vecteur vecteur : listeVecteurs) {
            double[] vals = vecteur.getValeur();
            for (int i = 0; i < vecteur.getTaille(); i++) {
                double val = vals[i];
                somme += val * val;
                nbVal++;
            }
        }

        double varIm = somme / nbVal;
        double sigmaX = Math.sqrt(Math.max(varIm - sigma * sigma, 0));

        if (sigmaX == 0) {
            return Double.MAX_VALUE;
        }
        return (sigma * sigma) / sigmaX;
    }


    /**
     * Applique un seuillage dur à tous les vecteurs : les valeurs en-dessous du seuil deviennent 0.
     *
     * @return Liste des vecteurs après seuillage dur.
     */
    public List<Vecteur> seuillageDur() {
        List<Vecteur> resultat = new ArrayList<>();

        for (Vecteur vecteur : this.listV) {
            double[] vals = vecteur.getValeur();
            double[] modifieVals = new double[vecteur.getTaille()];

            for (int i = 0; i < vecteur.getTaille(); i++) {
                double val = vals[i];
                if (Math.abs(val) > this.seuil) {
                    modifieVals[i] = val;
                } else {
                    modifieVals[i] = 0.0;
                }
            }

            Vecteur modifie = new Vecteur(vecteur.getID(), vecteur.getTaille(), modifieVals);
            resultat.add(modifie);
        }

        return resultat;
    }

    /**
     * Applique un seuillage doux à tous les vecteurs :
     * les valeurs en-dessous du seuil deviennent 0, les autres sont réduites du seuil.
     *
     * @return Liste des vecteurs après seuillage doux.
     */
    public List<Vecteur> seuillageDoux() {
        List<Vecteur> resultat = new ArrayList<>();

        for (Vecteur vecteur : this.listV) {
            double[] vals = vecteur.getValeur();
            double[] modifieVals = new double[vecteur.getTaille()];

            for (int i = 0; i < vecteur.getTaille(); i++) {
                double val = vals[i];
                if (Math.abs(val) <= this.seuil) {
                    modifieVals[i] = 0.0;
                } else {
                    double signe = Math.signum(val);
                    modifieVals[i] = signe * (Math.abs(val) - this.seuil);
                }
            }

            Vecteur modifie = new Vecteur(vecteur.getID(), vecteur.getTaille(), modifieVals);
            resultat.add(modifie);
        }

        return resultat;
    }

}
