package Java;

import java.util.Scanner;

import Java.Image.Patch;
import Java.Image.Image;

import java.io.File;
import java.io.IOException;

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
public class PurePixel {
	
	 /**
     * L'utilisateur est invité à choisir un mode (Bruitage, Débruitage ou Quitter), puis une image parmi celles présentes dans le dossier spécifié.
     * @param args les arguments de ligne de commande (non utilisés ici)
     * @author Charles Mendiburu
     * @version 1.0
     */
	public static void main(String[] args) {
		 // Chemin vers le dossier contenant les images
		String filename = "src/resources/Image/Images";
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
                  File dossierResultats = new File("src/resources/Image/Resultats");
                  if (!dossierResultats.exists()) {
                      dossierResultats.mkdirs(); // Crée le dossier (et les parents manquants)
                  }

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
			
			// Choix de la méthode de calcul du seuil
			System.out.println("Choisissez la méthode de calcul du seuil :");
			System.out.println("1 : Seuil VisuShrink");
			System.out.println("2 : Seuil BayesShrink");
			int choixSeuil = sc.nextInt();
			
			// Choix de la méthode de calcul du seuil
			System.out.println("Choisissez la taille des patchs :");
			int patchSize = sc.nextInt();

			int choixTailleImagettes = 0;
			
			try {
			    Image imgBruitée = new Image(cheminBruitee);
			    Image imgDebruitee = null;
			    String typeSeuillage = (choixSeuillage == 1) ? "dur" : "doux";
			    String methodeSeuil = (choixSeuil == 1) ? "VisuShrink" : "BayesShrink";
			    double sigma = Math.sqrt(50); // écart type bruit, à ajuster ou demander à l'utilisateur

			    

			    if (choixMethode == 1) {
			        
			    	imgDebruitee = Image.denoisingGlobalPCA(imgBruitée, patchSize, typeSeuillage, methodeSeuil, sigma);

			    } else {
			    	
			    	
			    	int shift = Patch.calculerShift(imgBruitée, patchSize);
			    	int tailleImagetteMinimal = Image.tailleImagetteMinimale(patchSize, shift);

			    	// Choix de la taille des imagettes
			    	do {
			    	    System.out.println("Choisissez la taille des imagettes (min " + tailleImagetteMinimal + ") : ");
			    	    choixTailleImagettes = sc.nextInt();

			    	    if (choixTailleImagettes < tailleImagetteMinimal) {
			    	        System.out.println("❌ Taille trop petite. Veuillez entrer une valeur ≥ " + tailleImagetteMinimal);
			    	    }
			    	} while (choixTailleImagettes < tailleImagetteMinimal);
					
					imgDebruitee = Image.denoisingLocalPCA(imgBruitée, choixTailleImagettes, patchSize, typeSeuillage, methodeSeuil, sigma);
					
			    }
			    String methode;
			    String filefinalname;
			    if (imgDebruitee != null) {
			    	if(methodeSeuil == "VisuShrink") {
			    		methodeSeuil = "VS";
			    	}else {
			    		methodeSeuil = "BS";
			    	}
			    	if(choixMethode == 1) {
			    		methode = "GB";
			    		filefinalname = "src/resources/Image/Resultats/IMG_debruitee_"+methode+"_"+methodeSeuil+"_"+typeSeuillage+"_patch"+patchSize+".png";
			    	}else {
			    		methode = "LC";
			    		filefinalname = "src/resources/Image/Resultats/IMG_debruitee_"+methode+"_"+methodeSeuil+"_"+typeSeuillage+"_patch"+patchSize+"_imagette"+choixTailleImagettes+".png";
			    	}
			    	
			    	double MSE = Image.MSE(imgBruitée,imgDebruitee);
			    	System.out.println("MSE : "+MSE);
			    	double PSNR = Image.PSNR(MSE);
			    	System.out.println("PSNR : "+PSNR);
			    	
		            Image.EnregistrerImage(imgDebruitee,filefinalname);
		            System.out.println("Image débruitée sauvegardée dans : "+filefinalname);
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
		String filename = "src/resources/Image/Images";
		File file =(new File(filename));
		// Liste les fichiers dans le dossier
		String[] tab = file.list();
		 
        // Affichage de chaque image
		for (String img : tab) {
			System.out.println(img);
		}
	}
}
