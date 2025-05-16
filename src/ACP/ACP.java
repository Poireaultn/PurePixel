package ACP;

import java.util.ArrayList;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.EigenDecomposition;

import Vecteur.Vecteur;

public class ACP {
	private RealMatrix covariance;
	private RealMatrix vecteursCentres;
	
	// Fonction qui permet de transformer une liste de vecteurs en matrice
	public static double[][] listToMatrice(ArrayList<Vecteur> ListeVecteurs) {
		int nblignes = ListeVecteurs.size();
		int nbcolonnes = ListeVecteurs.get(0).getTaille();
		
		double[][] matrice = new double[nblignes][nbcolonnes];
		
		for(int i = 0; i < nblignes; i++) {
			double[] vecteur = new double [nbcolonnes];
			vecteur = ListeVecteurs.get(i).getValeur();
			for(int j = 0; j < nbcolonnes; j++) {
				matrice[i][j]= vecteur[j];
			}
				
		}
	    return matrice;
	}
	
	// Fonction qui permet de transformer une matrice en une liste de vecteurs
	public static ArrayList<Vecteur> matriceToList(double[][] matrice) {
		int nblignes = matrice.length;
		ArrayList<Vecteur> listeVecteur = new ArrayList<Vecteur>();
		
		for(int i = 0; i < nblignes; i++) {
			Vecteur newvecteur = new Vecteur(matrice[i].length,matrice[i]);
			listeVecteur.add(newvecteur);
		}
	    return listeVecteur;
	}
	
	// Fonction qui permet de centrer et réduire les variables
	public static ArrayList<Vecteur> moyCov(ArrayList<Vecteur> vecteurs) {
		int nbVecteurs = vecteurs.size(); // nombre de colonnes
		int tailleVecteur = vecteurs.get(0).getValeur().length; // nombre de lignes

		double[][] matrice = listToMatrice(vecteurs);

	    double[] moyenne = new double[tailleVecteur];
	    double[] ecart_type = new double[tailleVecteur];

	    // Calcul des moyennes
	    for (int j = 0; j < tailleVecteur; j++) {
	        double somme = 0;
	        for (int i = 0; i < nbVecteurs; i++) {
	            somme += matrice[i][j];
	        }
	        moyenne[j] = somme / nbVecteurs;
	    }

	    // Calcul des écarts-types
	    for (int j = 0; j < tailleVecteur; j++) {
	        double somme = 0;
	        for (int i = 0; i < nbVecteurs; i++) {
	            double diff = matrice[i][j] - moyenne[j];
	            somme += diff * diff;
	        }
	        ecart_type[j] = Math.sqrt(somme / (nbVecteurs - 1));
	        if (ecart_type[j] == 0) {
	            ecart_type[j] = 1; // éviter division par zéro
	        }
	    }

	    // Construction de la liste centrée-réduite
	    ArrayList<Vecteur> resultat = new ArrayList<Vecteur>();

	    for (int i = 0; i < nbVecteurs; i++) {
	        double[] valeurs = new double[tailleVecteur];
	        for (int j = 0; j < tailleVecteur; j++) {
	            valeurs[j] = (matrice[i][j] - moyenne[j]) / ecart_type[j];
	        }
	        Vecteur nouveau = new Vecteur(vecteurs.get(i).getID(), tailleVecteur, valeurs);
	        resultat.add(nouveau);
	    }

	    return resultat;
	}
	
	// Constructeur
	public ACP(ArrayList<Vecteur> PatchVectorisé) {		
		// On centre et on réduit les vecteurs
		ArrayList<Vecteur> VecteurCentre= moyCov(PatchVectorisé);
		
		// On transforme la liste de vecteurs centrés en un tableau 2D (matrice)
		double[][] MatriceCentree = listToMatrice(VecteurCentre);
		
		// On transforme en une matrice utilisable par la librairie Apache Commons Math
		RealMatrix matrice = MatrixUtils.createRealMatrix(MatriceCentree);
		
		// On calcule la matrice de covariance
		RealMatrix matriceTransposee = matrice.transpose();	// matrice transposée des vecteurs 
		RealMatrix covariance = matrice.multiply(matriceTransposee);	// produit de la matrice des vecteurs avec la matrice transposée des vecteurs 
		
		this.covariance = covariance;
		this.vecteursCentres = matrice;
	}
	
	//Getter et Setter de la Covariance
	public RealMatrix getCovariance() {
		return covariance;
	}

	public void setCovariance(RealMatrix covariance) {
		this.covariance = covariance;
	}
	
	//Getter et Setter des vecteursCentres
	public RealMatrix getVecteursCentres() {
		return vecteursCentres;
	}

	public void setVecteursCentres(RealMatrix vecteursCentres) {
		this.vecteursCentres = vecteursCentres;
	}
	
	// Fonction qui réalise une ACP
	public ArrayList<Vecteur> traitementACP(ACP monACP) {	
		// On décompose la matrice de covariance
		EigenDecomposition eigenDecomposition = new EigenDecomposition(monACP.getCovariance());
		
		ArrayList<Vecteur> BaseOrthonormale = new ArrayList<Vecteur>();
		int nbVecteur = eigenDecomposition.getV().getColumnDimension(); // Nombre de vecteur de la base orthonormale
		
		// On récupérer chaque vecteur de la base orthonormale pour la mettre dans une liste de vecteurs
		for(int i = 0; i < nbVecteur; i++) {
			// Permet de récupérer le ième vecteur de la base
			RealVector vecteur = eigenDecomposition.getEigenvector(i);
			// On le converti en tableau
			double[] valeur = vecteur.toArray();			
			// On crée un nouveau vecteur
			Vecteur newVecteur = new Vecteur(valeur.length,valeur);
			// On l'ajoute à la liste
			BaseOrthonormale.add(newVecteur);
		}
		return BaseOrthonormale;
	}
}
