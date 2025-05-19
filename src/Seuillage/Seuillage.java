package Seuillage;

import java.util.List;
import java.util.ArrayList;

import Vecteur.Vecteur;

public class Seuillage {
    
    private double seuil;
    
    private List<Vecteur> listV;  // Liste des vecteurs à seuiller

    // Constructeur
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

    // Méthode pour calculer le seuil VisuShrink
    public static double seuilV(double sigma, int tailleImage) {
        return sigma * Math.sqrt(2 * Math.log(tailleImage));
    }
    
    // Méthode pour calculer le seuil BayesShrink
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


    // Méthode de seuillage dur
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

    // Méthode de seuillage doux
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
