package Image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TestImage {
	public static void main(String[] args) {
		try {
			Image img = new Image("Image/lena.jpg"); // Charge l’image
			img.afficherMatrice(); // Affiche la matrice
			
			img = Image.noising(img,10);
			
			Image.EnregistrerImage(img,"Image/test2.jpg");

		} catch (IOException e) {
			System.out.println("Erreur lors du traitement de l'image : " + e.getMessage());
		}
	}
}