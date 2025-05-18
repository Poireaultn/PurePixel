import java.util.Scanner;
import Image.Image;


import java.io.File;
import java.io.IOException;

public class PurePixel {

	public static void main(String[] args) {
		
		String filename="src/Image/Images";
		File file =(new File(filename));
		String[] tab = file.list();
		int choixMode;
		int choixImage;
		
		double valeurBruitage;
		
		
		System.out.println("Bienvenue ! Choisit ton mode :");
		System.out.println("1 : Bruitage");
		System.out.println("2 : Débruitage");
		System.out.println("3 : Quitter");
		
		Scanner sc = new Scanner(System.in);
		choixMode= sc.nextInt();
		
		
		switch(choixMode) {
		  case 1:
              System.out.println("Vous avez choisi le bruitage.");

              for (int i = 0; i < tab.length; i++) {
                  System.out.println(i + " : " + tab[i]);
              }

              System.out.print("Choisissez une image : ");
              choixImage = sc.nextInt();
              System.out.println("Tu as choisi cette image : " + tab[choixImage]);

              System.out.print("Choisis l'écart type du bruit (de 0 à 50) : ");
              double ecartType = sc.nextDouble();

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

		    System.out.print("Choisissez une image à débruiter : ");
		    choixImage = sc.nextInt();
		    System.out.println("Tu as choisi cette image : " + tab[choixImage]);

		    String cheminBruitee = filename + "/" + tab[choixImage];

		    try {
		        Image imgBruitée = new Image(cheminBruitee);

		      // appeler denoising ici ( à créer)
		        
		        Image.EnregistrerImage(imgDebruitee, "src/Image/Resultats/image_debruitee.png");

		        System.out.println("Image débruitée sauvegardée dans src/Image/Resultats/image_debruitee.png");

		    } catch (IOException e) {
		        System.out.println("Erreur lors du chargement ou de la sauvegarde : " + e.getMessage());
		    }

		    break;

		default:
			System.out.println("Vous quittez le programme ! A bientot");
			System.exit(0);
		}
		
		
	}

	
	
	public static void afficherChoixImage() {
		
		String filename="src/Image/Images";
		File file =(new File(filename));
		String[] tab = file.list();
		
		for (String img : tab) {
			System.out.println(img);
		}
	}
}
