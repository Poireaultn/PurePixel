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
			
			int width = img.getWidth();
			int height = img.getHeight();
			double[][] pixels = img.getPixels(); // Récupère les pixels avec un getter

			BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int gray = (int) pixels[y][x];
					int rgb = (gray << 16) | (gray << 8) | gray;
					bi.setRGB(x, y, rgb);
				}
			}

			// Sauvegarde de l’image résultante
			ImageIO.write(bi, "png", new File("Image/test2.jpg"));
			System.out.println("Image en niveaux de gris sauvegardée.");

		} catch (IOException e) {
			System.out.println("Erreur lors du traitement de l'image : " + e.getMessage());
		}
	}
}

