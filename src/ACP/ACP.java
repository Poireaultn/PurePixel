/**
 * Classe permettant de réaliser une Analyse en Composantes Principales (ACP) sur un ensemble de vecteurs.
 * Elle propose également des méthodes pour le traitement de données telles que la conversion de listes en matrices, le centrage-réduction, et le débruitage par seuillage.
 * @author Nathan Poireault
 * @version 1.0
 */

package ACP;

import java.util.ArrayList;
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
     * @param listeVecteurs la liste de vecteurs à transformer
     * @return une matrice contenant les valeurs des vecteurs
     */
	public static double[][] listToMatrice(ArrayList<Vecteur> listeVecteurs) {
	    int nbVecteurs = listeVecteurs.size();
	    int tailleVecteur = listeVecteurs.get(0).getTaille();
	    
	    double[][] matrice = new double[nbVecteurs][tailleVecteur];
	    
	    for (int i = 0; i < nbVecteurs; i++) {
	        double[] valeurs = listeVecteurs.get(i).getValeur();
	        for (int j = 0; j < tailleVecteur; j++) {
	            matrice[i][j] = valeurs[j];
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
	    int nbcolonnes = matrice[0].length;
	    ArrayList<Vecteur> listeVecteur = new ArrayList<>();

	    for (int i = 0; i < nblignes; i++) {
	        double[] ligne = new double[nbcolonnes];
	        for (int j = 0; j < nbcolonnes; j++) {
	            ligne[j] = matrice[i][j];
	        }
	        Vecteur newvecteur = new Vecteur(nbcolonnes, ligne);
	        listeVecteur.add(newvecteur);
	    }
	    return listeVecteur;
	}


	
	/**
     * Centre les vecteurs d'une liste.
     * @author Nathan Poireault
     * @param vecteurs la liste de vecteurs à centrer
     * @return une nouvelle liste de vecteurs centrés
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
	    
	    // Construction de la liste centrée-réduite
	    ArrayList<Vecteur> resultat = new ArrayList<Vecteur>();

	    for (int i = 0; i < nbVecteurs; i++) {
	        double[] valeurs = new double[tailleVecteur];
	        for (int j = 0; j < tailleVecteur; j++) {
	            valeurs[j] = (matrice[i][j] - moyenne[j]);
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
     * @author Charles Mendiburu, Nathan Poireault
     * @param vecteurs la liste des vecteurs à débruiter. Tous les vecteurs doivent avoir la même dimension.
     * @param typeSeuillage le type de seuillage à appliquer. Les valeurs possibles sont "dur" pour un seuillage dur, et toute autre valeur pour un seuillage doux.
     * @param methodeSeuil la méthode utilisée pour calculer le seuil. Les valeurs possibles sont "VisuShrink" et "BayesShrink".
     * @param sigma une estimation de l'écart-type du bruit. Cette valeur est utilisée dans le calcul du seuil.
     * @return une nouvelle liste d'objets représentant les vecteurs débruités.
     */
	public static ArrayList<Vecteur> denoisingACP(ArrayList<Vecteur> vecteurs, String typeSeuillage, String methodeSeuil, double sigma) {
	    // Centrage / réduction
	    ArrayList<Vecteur> vecteursCentres = moyCov(vecteurs);

	    // ACP sur les vecteurs centrés
	    ACP acp = new ACP(vecteursCentres);
	    ArrayList<Vecteur> base = acp.traitementACP(acp); // base orthonormale obtenue via ACP
	    System.out.println("B dimensions: " + base.size() + " vecteurs");

	    // Projection des vecteurs centrés dans la base ACP
	    List<Vecteur> coeffs = Vecteur.proj(base, vecteursCentres); // Projection des vecteurs centrés

	    printStats("Avant seuillage (1er vecteur)", coeffs.get(0).getValeur());

	    // Calcul du seuil
	    double seuil = 0;
	    if (methodeSeuil.equalsIgnoreCase("VisuShrink")) {
	        int tailleImage = vecteurs.size() * vecteurs.get(0).getTaille();
	        seuil = Seuillage.seuilV(sigma, tailleImage);
	    } else if (methodeSeuil.equalsIgnoreCase("BayesShrink")) {
	    	seuil = Seuillage.seuilB(sigma, coeffs);
	    }
	    
	    if (seuil < 2) {
	    	seuil = 2;
	    }
	    System.out.println("Seuil utilisé : " + seuil);
	    
	    // Application du seuillage
	    Seuillage seuillage = new Seuillage(seuil, coeffs);
	    List<Vecteur> coeffsSeuillees;
	    if (typeSeuillage.equalsIgnoreCase("dur")) {
	        coeffsSeuillees = seuillage.seuillageDur();
	    } else {
	        coeffsSeuillees = seuillage.seuillageDoux();
	    }
		
	    // Stats après seuillage
	    if (!coeffsSeuillees.isEmpty()) {
	        printStats("Après seuillage (1er vecteur)", coeffsSeuillees.get(0).getValeur());
	    }
	    
	    // Reconstruction de la matrice coeffs seuillees
	    // Conversion List<Vecteur> => double[][]
	    double[][] coeffsMatrix = listToMatrice(new ArrayList<>(coeffsSeuillees));

	    // Création RealMatrix avec vecteurs en lignes
	    RealMatrix matrix = MatrixUtils.createRealMatrix(coeffsMatrix);

	    // Transposition : vecteurs en colonnes attendue par la suite
	    RealMatrix coeffsSeuilleesMatrix = matrix.transpose();

	    // Création matrice base ACP B (p x k)
	    RealMatrix B = MatrixUtils.createRealMatrix(listToMatrice(base)).transpose();

	    // Multiplication B * coeffsSeuilleesMatrix
	    RealMatrix X_denoised = B.multiply(coeffsSeuilleesMatrix);

	    // Conversion en liste de vecteurs
	    double[][] dataDenoised = X_denoised.transpose().getData();
	    ArrayList<Vecteur> resultat = matriceToList(dataDenoised);

	    return resultat;
	}


	 /**
     * Affiche les statistiques de base d'un tableau de nombres à virgule flottante.
     * @author Charles Mendiburu
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