/**
 * La classe PurePixel représente l'application principale permettant de gérer les opérations  de bruitage et de débruitage sur des images. 
 * L'utilisateur peut sélectionner une image depuis un répertoire spécifique, choisir un mode de traitement, et sauvegarder le résultat dans un emplacement défini.
 * Modes disponibles :
 * <ul>
 * <li>1 : Bruitage — Ajout de bruit gaussien sur l'image choisie.</li>
 * <li>2 : Débruitage — Application d'un débruitage par méthode globale.</li>
 * <li>3 : Quitter — Fermeture de l'application.</li>
 * </ul>
 * 
 * @author Charles Mendiburu
 * @version 1.0
 */

import java.util.Scanner;
import Image.Image;


import java.io.File;
import java.io.IOException;

public class PurePixel {
	
	 /**
     * L'utilisateur est invité à choisir un mode (Bruitage, Débruitage ou Quitter), puis une image parmi celles présentes dans le dossier spécifié.
     * @param args les arguments de ligne de commande (non utilisés ici)
     * @author Charles Mendiburu
     * @version 1.0
     */

	public static void main(String[] args) {
		 // Chemin vers le dossier contenant les images
		String filename="src/Image/Images";
		File file =(new File(filename));
		// Récupère la liste des images disponibles dans le dossier
		String[] tab = file.list();
		int choixMode;
		int choixImage;
		
		double valeurBruitage;
		
		// Affichage du menu principal
		System.out.println("Bienvenue ! Choisit ton mode :");
		System.out.println("1 : Bruitage");
		System.out.println("2 : Débruitage");
		System.out.println("3 : Quitter");
		
		// Lecture du choix de l'utilisateur
		Scanner sc = new Scanner(System.in);
		choixMode= sc.nextInt();
		
		 // Gestion des différents choix
		switch(choixMode) {
		  case 1:
              System.out.println("Vous avez choisi le bruitage.");
              // Affichage de la liste des images
              for (int i = 0; i < tab.length; i++) {
                  System.out.println(i + " : " + tab[i]);
              }
              // Sélection de l'image
              System.out.print("Choisissez une image : ");
              choixImage = sc.nextInt();
              System.out.println("Tu as choisi cette image : " + tab[choixImage]);

              // Définir l'écart type pour le bruit
              System.out.print("Choisis l'écart type du bruit (de 0 à 50) : ");
              double ecartType = sc.nextDouble();

             // Chemin complet de l'image choisie
              String chemin = filename + "/" + tab[choixImage];

              try {
                  Image imgOriginale = new Image(chemin);
                  Image imgBruitée = Image.noising(imgOriginale, ecartType);

                  Image.EnregistrerImage(imgBruitée, "src/Image/Resultats/image_bruitee.png");
              } catch (IOException e) {
                  System.out.println("Erreur lors du chargement ou de la sauvegarde : " + e.getMessage());
              }

              break;

		case 2:
			System.out.println("Vous avez choisi le débruitage.");

			// Afficher les images disponibles
			for (int i = 0; i < tab.length; i++) {
			    System.out.println(i + " : " + tab[i]);
			}

			// Sélection de l'image à débruiter
			System.out.print("Choisissez une image à débruiter : ");
			choixImage = sc.nextInt();
			System.out.println("Tu as choisi cette image : " + tab[choixImage]);

			String cheminBruitee = filename + "/" + tab[choixImage];

			 // Choix de la méthode de débruitage
			System.out.println("Choisissez le type de méthode de débruitage :");
			System.out.println("1 : Méthode globale");
			System.out.println("2 : Méthode locale");
			int choixMethode = sc.nextInt();
			
			 // Choix du type de seuillage
			System.out.println("Choisissez le type de seuillage :");
			System.out.println("1 : Seuillage dur");
			System.out.println("2 : Seuillage doux");
			int choixSeuillage = sc.nextInt();

			try {
			    Image imgBruitée = new Image(cheminBruitee);
			    Image imgDebruitee = null;
			    String typeSeuillage = (choixSeuillage == 1) ? "dur" : "dou";
			    String methodeSeuil = "VisuShrink"; 
			    int patchSize = 8;  // taille patch par défaut, modifiable
			    double sigma = 0.1; // écart type bruit, à ajuster ou demander à l'utilisateur

			    

			    if (choixMethode == 1) {
			        
			    	imgDebruitee = Image.denoisingGlobalPCA(imgBruitée, patchSize, typeSeuillage, methodeSeuil, sigma);

			    } else {
			        System.out.println("Méthode locale non encore implémentée.");
			        return;
			    }

			    if (imgDebruitee != null) {
		            Image.EnregistrerImage(imgDebruitee, "src/Image/Resultats/image_debruitee.png");
		            System.out.println("Image débruitée sauvegardée dans src/Image/Resultats/image_debruitee.png");
		        } else {
		            System.out.println("Échec du débruitage.");
		        }

			} catch (IOException e) {
			    System.out.println("Erreur lors du chargement ou de la sauvegarde : " + e.getMessage());
			}

		    break;

		default:
			System.out.println("Vous quittez le programme ! A bientot");
			System.exit(0);
		}
		
		
	}

	/**
     * Méthode permettant d'afficher les images disponibles dans le répertoire spécifié. 
     * Le chemin par défaut est "src/Image/Images". 
     * @author Charles Mendiburu
     * @version 1.0
     */
	
	public static void afficherChoixImage() {
		// Chemin du dossier contenant les images
		String filename="src/Image/Images";
		File file =(new File(filename));
		// Liste les fichiers dans le dossier
		String[] tab = file.list();
		 
        // Affichage de chaque image
		for (String img : tab) {
			System.out.println(img);
		}
	}
}
