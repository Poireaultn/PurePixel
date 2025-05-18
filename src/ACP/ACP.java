/**
 * Classe permettant de réaliser une Analyse en Composantes Principales (ACP) sur un ensemble de vecteurs.
 * Elle propose également des méthodes pour le traitement de données telles que la conversion de listes en matrices, le centrage-réduction, et le débruitage par seuillage.
 * @author Nathan Poireault
 * @version 1.0
 */

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
	
	 /**
     * Transforme une liste de vecteurs en une matrice 2D.
     * @author Nathan Poireault
     * @param ListeVecteurs la liste de vecteurs à transformer
     * @return une matrice contenant les valeurs des vecteurs
     */
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
	
	 /**
     * Transforme une matrice en une liste de vecteurs.
     * @author Nathan Poireault
     * @param matrice la matrice à transformer
     * @return une liste de vecteurs contenant les lignes de la matrice
     */
	public static ArrayList<Vecteur> matriceToList(double[][] matrice) {
		int nblignes = matrice.length;
		ArrayList<Vecteur> listeVecteur = new ArrayList<Vecteur>();
		
		for(int i = 0; i < nblignes; i++) {
			Vecteur newvecteur = new Vecteur(matrice[i].length,matrice[i]);
			listeVecteur.add(newvecteur);
		}
	    return listeVecteur;
	}
	
	/**
     * Centre et réduit les vecteurs d'une liste.
     * @author Nathan Poireault
     * @param vecteurs la liste de vecteurs à centrer et réduire
     * @return une nouvelle liste de vecteurs centrés et réduits
     */
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
	
	 /**
     * Getter pour la matrice de covariance.
     *
     * @return La matrice de covariance
     */
	public RealMatrix getCovariance() {
		return covariance;
	}

	/**
     * Setter pour la matrice de covariance.
     *
     * @param covariance La nouvelle matrice de covariance
     */
	public void setCovariance(RealMatrix covariance) {
		this.covariance = covariance;
	}
	
	/**
     * Getter pour les vecteurs centrés.
     *
     * @return La matrice des vecteurs centrés
     */
	public RealMatrix getVecteursCentres() {
		return vecteursCentres;
	}

	 /**
     * Setter pour les vecteurs centrés.
     *
     * @param vecteursCentres La nouvelle matrice des vecteurs centrés
     */
	public void setVecteursCentres(RealMatrix vecteursCentres) {
		this.vecteursCentres = vecteursCentres;
	}
	
	 /**
     * Fonction qui réalise une ACP.
     * @author Nathan Poireault
     * @param monACP l'objet sur lequel effectuer l'analyse. La matrice de covariance de cet objet sera utilisée pour la décomposition en valeurs propres.
     * @return une liste d'objets représentant la base orthonormale (vecteurs propres) de la matrice de covariance.
     */
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
	
	   /**
     * Applique un débruitage aux vecteurs en utilisant l'ACP et un seuillage.
     * @author Nathan Poireault
     * @param vecteurs la liste des vecteurs à débruiter. Tous les vecteurs doivent avoir la même dimension.
     * @param typeSeuillage le type de seuillage à appliquer. Les valeurs possibles sont "dur" pour un seuillage dur, et toute autre valeur pour un seuillage doux.
     * @param methodeSeuil la méthode utilisée pour calculer le seuil. Les valeurs possibles sont "VisuShrink" et "BayesShrink".
     * @param sigma une estimation de l'écart-type du bruit. Cette valeur est utilisée dans le calcul du seuil.
     * @return une nouvelle liste d'objets représentant les vecteurs débruités.
     */
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
	    if (B.transpose().getColumnDimension() != X.getRowDimension()) {
	        System.err.println("Dimension mismatch for multiplication B^T * X: " + B.transpose().getColumnDimension() + " != " + X.getRowDimension());
	        throw new IllegalArgumentException("Dimension mismatch for multiplication B^T * X");
	    }
	    RealMatrix coeffs = B.transpose().multiply(X);

	    // 6. Récupération des coefficients pour seuillage
	    List<double[]> listCoeff = new ArrayList<>();
	    for (int i = 0; i < coeffs.getColumnDimension(); i++) {
	        double[] colonne = coeffs.getColumn(i);
	        listCoeff.add(colonne);
	    }

	    // Stats avant seuillage
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

	    // Stats après seuillage
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

	    // Stats après reconstruction
	    double[] allValues = Arrays.stream(X_denoised.getData())
	            .flatMapToDouble(Arrays::stream)
	            .toArray();
	    printStats("Image reconstruite (X_denoised)", allValues);

	    // 11. Transpose et conversion en liste de vecteurs
	    double[][] dataDenoised = X_denoised.transpose().getData();
	    ArrayList<Vecteur> resultat = matriceToList(dataDenoised);

	    return resultat;
	}


	 /**
     * Affiche les statistiques de base d'un tableau de nombres à virgule flottante.
     * @author Nathan Poireault
     * @param label une chaîne de caractères descriptive pour identifier les statistiques affichées.
     * @param data le tableau de nombres à virgule flottante pour lequel calculer et afficher les statistiques.
     */
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
