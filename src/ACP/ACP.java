package ACP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import Seuillage.Seuillage;

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
		RealMatrix covariance = matrice.transpose().multiply(matrice).scalarMultiply(1.0 / matrice.getRowDimension());

		
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
	
	
	public static ArrayList<Vecteur> denoisingACP(ArrayList<Vecteur> vecteurs, String typeSeuillage, String methodeSeuil, double sigma) {
	    // 1. Centrage / réduction
	    ArrayList<Vecteur> vecteursCentres = moyCov(vecteurs);

	    // 2. Passage en matrice et transpose (colonnes = vecteurs)
	    double[][] data = listToMatrice(vecteursCentres);
	    RealMatrix X = MatrixUtils.createRealMatrix(data);
	    System.out.println("X dimensions: " + X.getRowDimension() + " x " + X.getColumnDimension());

	    // 3. ACP
	    ACP acp = new ACP(vecteurs);
	    ArrayList<Vecteur> base = acp.traitementACP(acp);
	    RealMatrix B = MatrixUtils.createRealMatrix(listToMatrice(base)).transpose();
	    System.out.println("B dimensions: " + B.getRowDimension() + " x " + B.getColumnDimension());

	    // 5. Projection dans la base ACP : coeffs = B^T * X
	    System.out.println("Multiplying B^T (" + B.transpose().getRowDimension() + " x " + B.transpose().getColumnDimension() + ") with X (" + X.getRowDimension() + " x " + X.getColumnDimension() + ")");
	    if (X.getColumnDimension() != B.transpose().getRowDimension()) {
	        System.err.println("Dimension mismatch for multiplication X * B^T: " + X.getColumnDimension() + " != " + B.transpose().getRowDimension());
	        throw new IllegalArgumentException("Dimension mismatch for multiplication X * B^T");
	    }

	    
	    RealMatrix coeffs = X.multiply(B.transpose());



	    // 6. Récupération des coefficients pour seuillage
	    List<double[]> listCoeff = new ArrayList<>();
	    for (int i = 0; i < coeffs.getColumnDimension(); i++) {
	        double[] colonne = coeffs.getColumn(i);
	        listCoeff.add(colonne);
	    }

	    // 🔍 Stats avant seuillage
	    if (!listCoeff.isEmpty()) {
	        printStats("Avant seuillage (1er vecteur)", listCoeff.get(0));
	    }

	    // 7. Calcul du seuil
	    double seuil = 0;
	    if (methodeSeuil.equalsIgnoreCase("VisuShrink")) {
	        int tailleImage = vecteurs.size() * vecteurs.get(0).getTaille();
	        seuil = Seuillage.seuilV(sigma, tailleImage);
	    } else if (methodeSeuil.equalsIgnoreCase("BayesShrink")) {
	        seuil = Seuillage.seuilB(sigma, listCoeff);
	    }
	    System.out.println("Seuil utilisé : " + seuil);
	    seuil *= 0.1;

	    // 8. Application du seuillage
	    Seuillage seuillage = new Seuillage(seuil, listCoeff);
	    List<double[]> coeffsSeuillees;
	    if (typeSeuillage.equalsIgnoreCase("dur")) {
	        coeffsSeuillees = seuillage.seuillageDur();
	    } else {
	        coeffsSeuillees = seuillage.seuillageDoux();
	    }

	    // 🔍 Stats après seuillage
	    if (!coeffsSeuillees.isEmpty()) {
	        printStats("Après seuillage (1er vecteur)", coeffsSeuillees.get(0));
	    }

	    // 9. Reconstruction de la matrice coeffs seuillees
	    double[][] coeffsMatrix = new double[coeffsSeuillees.get(0).length][coeffsSeuillees.size()];
	    for (int i = 0; i < coeffsSeuillees.size(); i++) {
	        double[] col = coeffsSeuillees.get(i);
	        for (int j = 0; j < col.length; j++) {
	            coeffsMatrix[j][i] = col[j];
	        }
	    }
	    RealMatrix coeffsSeuilleesMatrix = MatrixUtils.createRealMatrix(coeffsMatrix);

	    // 10. Reconstruction de X débruité
	    System.out.println("Multiplying B (" + B.getRowDimension() + " x " + B.getColumnDimension() + ") with coeffsSeuilleesMatrix (" + coeffsSeuilleesMatrix.getRowDimension() + " x " + coeffsSeuilleesMatrix.getColumnDimension() + ")");
	    if (B.getColumnDimension() != coeffsSeuilleesMatrix.getRowDimension()) {
	        System.err.println("Dimension mismatch pour multiplication B * coeffsSeuilleesMatrix: " + B.getColumnDimension() + " != " + coeffsSeuilleesMatrix.getRowDimension());
	        throw new IllegalArgumentException("Dimension mismatch pour multiplication B * coeffsSeuilleesMatrix");
	    }
	    RealMatrix X_denoised = B.multiply(coeffsSeuilleesMatrix);
	    System.out.println(Arrays.toString(X_denoised.getRow(0)));

	    // 🔍 Stats après reconstruction
	    double[] allValues = Arrays.stream(X_denoised.getData())
	            .flatMapToDouble(Arrays::stream)
	            .toArray();
	    printStats("Image reconstruite (X_denoised)", allValues);

	    // 11. Transpose et conversion en liste de vecteurs
	    double[][] dataDenoised = X_denoised.transpose().getData();
	    ArrayList<Vecteur> resultat = matriceToList(dataDenoised);

	    return resultat;
	}


	
	public static void printStats(String label, double[] data) {
	    double min = Double.MAX_VALUE;
	    double max = -Double.MAX_VALUE;
	    double sum = 0;
	    for (double v : data) {
	        if (v < min) min = v;
	        if (v > max) max = v;
	        sum += v;
	    }
	    double mean = sum / data.length;
	    double variance = 0;
	    for (double v : data) variance += (v - mean) * (v - mean);
	    variance /= data.length;
	    double std = Math.sqrt(variance);
	    System.out.printf("[%s] Min: %.4f, Max: %.4f, Mean: %.4f, Std: %.4f\n", label, min, max, mean, std);
	}



}
