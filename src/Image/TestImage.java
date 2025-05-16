package Image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

public class TestImage {




	public static void saveImages(List<Image> images, String outputDir) {
				int index = 1;
		for (Image img : images) {
			try {
				int width = img.getWidth();
				int height = img.getHeight();
				double[][] pixels = img.getPixels();
	
				BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
	
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						int gray = (int) pixels[y][x];
						int rgb = (gray << 16) | (gray << 8) | gray;
						bufferedImage.setRGB(x, y, rgb);
					}
				}
	
				// Sauvegarder l'image
				File outputFile = new File(outputDir + "/subImage_" + index + ".png");
				ImageIO.write(bufferedImage, "png", outputFile);
				System.out.println("Image sauvegardée : " + outputFile.getAbsolutePath());
				index++;
			} catch (IOException e) {
				System.out.println("Erreur lors de la sauvegarde de l'image : " + e.getMessage());
			}
		}
	}

	public static void main(String[] args) {
		try {
			Image img = new Image("Image/lena.jpg"); // Charge l’image
			img.afficherMatrice(); // Affiche la matrice
			
			img = Image.noising(img,10);
			
			Image.EnregistrerImage(img,"Image/test2.jpg");

			Patch.ExtractPatchs(img, 1,10);
			

			// Découper l'image
            int taille = 256; // Taille des sous-images
            int nombre = 4; // Nombre maximum de sous-images
            List<Image> subImages = img.decoupeImage(taille, nombre);

			saveImages(subImages, "C:\\Users\\DELL\\Documents\\GitHub\\PurePixel\\src\\Image\\output"); // Sauvegarde les sous-images

            // Vérifier les résultats
            System.out.println("Nombre de sous-images générées : " + subImages.size());
            for (int i = 0; i < subImages.size(); i++) {
                Image subImage = subImages.get(i);
                System.out.println("Sous-image " + (i + 1) + " : " +
                        "Hauteur = " + subImage.getHeight() +
                        ", Largeur = " + subImage.getWidth());
            }


		} catch (IOException e) {
			System.out.println("Erreur lors du traitement de l'image : " + e.getMessage());
		}
	}
}
