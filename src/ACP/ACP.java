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
		public static double[][] moyCov(double[][] matrice) {
		    int nblignes = matrice.length;
		    int nbcolonnes = matrice[0].length;
		    
		    double[][] CentreeReduite = new double[nblignes][nbcolonnes];

		    // Calcul des moyennes et écart-types pour chaque colonne
		    double[] moyenne = new double[nbcolonnes];
		    double[] ecart_type = new double[nbcolonnes];

		    // Moyennes
		    for (int j = 0; j < nbcolonnes; j++) {
		        double somme = 0;
		        for (int i = 0; i < nblignes; i++) {
		            somme += matrice[i][j];
		        }
		        moyenne[j] = somme / nblignes;
		    }

		    // Écarts-types
		    for (int j = 0; j < nbcolonnes; j++) {
		        double somme = 0;
		        for (int i = 0; i < nblignes; i++) {
		            double diff = matrice[i][j] - moyenne[j];
		            somme += diff * diff;
		        }
		        ecart_type[j] = Math.sqrt(somme / (nblignes - 1));
		        if (ecart_type[j] == 0) {
		        	ecart_type[j] = 1; // éviter division par zéro
		        }
		    }

		    // On centre et on réduit
		    for (int i = 0; i < nblignes; i++) {
		        for (int j = 0; j < nbcolonnes; j++) {
		        	CentreeReduite[i][j] = (matrice[i][j] - moyenne[j]) / ecart_type[j];
		        }
		    }

		    return CentreeReduite;
		}
	
	// Constructeur
	public ACP(ArrayList<Vecteur> PatchVectorisé) {
		// On transforme la liste de vecteurs en un tableau 2D (matrice)
		double[][] TableauPatchVectorisé = listToMatrice(PatchVectorisé);
		
		// On centre et on réduit les valeurs du tableau 2D (matrice)
		double[][] CentreeReduite = moyCov(TableauPatchVectorisé);
		
		// On transforme en une matrice utilisable par la librairie Apache Commons Math
		RealMatrix matrice = MatrixUtils.createRealMatrix(CentreeReduite);
		
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
