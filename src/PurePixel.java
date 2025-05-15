import java.util.Scanner;
import javafx.scene.image.Image;
import java.io.File;

public class PurePixel {

	public static void main(String[] args) {
		
		String filename="src/Image/Images";
		File file =(new File(filename));
		String[] tab = file.list();
		int choixMode;
		int choixImage;
		
		int valeurBruitage;
		
		
		System.out.println("Bienvenue ! Choisit ton mode :");
		System.out.println("1 : Débruitage");
		System.out.println("2 : Bruitage");
		System.out.println("3 : Quitter");
		
		Scanner sc = new Scanner(System.in);
		choixMode= sc.nextInt();
		
		
		switch(choixMode) {
		case 1:
			System.out.println("Vous avez choisi le débruitage");
			System.out.println();
			for (int i=0;i<tab.length;i++) {
				System.out.println(i + " : " + tab[i]);
			}
			choixImage = sc.nextInt();
			System.out.println("Tu as choisi cette image : "  + tab[choixImage]);
			
			System.out.println("A présent choisi ta valeur de bruitage (de 0 à 50)");
			valeurBruitage = sc.nextInt();
			
			
			break;
		case 2:
			System.out.println("Vous avez choisi le bruitage.");
			
			System.out.println("Veuillez choisir une image dans la liste :");
			
			System.out.println();
			for (int i=0;i<tab.length;i++) {
				System.out.println(i + " : " + tab[i]);
			}
			
			choixImage = sc.nextInt();
			System.out.println("Tu as choisi cette image : "  + tab[choixImage]);
			
			System.out.println();
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
