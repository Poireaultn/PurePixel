package noising;


import java.io.IOException;

public class testImage {
	public static void main(String[] args) {
		try {
			Image ney = new Image("ressources/neymar.jpg"); // Charge l’image
			ney.afficherMatrice(); // Affiche la matrice
		} catch (IOException e) {
			System.out.println("Erreur lors du traitement de l'image : " + e.getMessage());
		}
	}
	
}
