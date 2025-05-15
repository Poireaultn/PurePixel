package Image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;



import java.util.ArrayList;
import java.util.List;

public class Image {
	private double[][] pixels;
	private int height;
	private int width;

	public Image(String filePath) throws IOException {
		BufferedImage bufferedImage = ImageIO.read(new File(filePath));
		this.width = bufferedImage.getWidth();
		this.height = bufferedImage.getHeight();
		this.pixels = toMatrix(bufferedImage);
	}

    public Image(double[][] pixels, int height, int width) {
        this.pixels = pixels;
        this.height = height;
        this.width = width;
    }

	private double[][] toMatrix(BufferedImage image) {
		double[][] gray = new double[this.height][this.width];
		for (int y = 0; y < this.height; y++) {
			for (int x = 0; x < this.width; x++) {
				int pixel = image.getRGB(x, y);
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = pixel & 0xff;
				gray[y][x] = (red + green + blue) / 3.0;
			}
		}
		return gray;
	}
	
	public static void EnregistrerImage(Image img, String filePath) throws IOException {
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
		ImageIO.write(bi, "png", new File(filePath));
		System.out.println("Image en niveaux de gris sauvegardée.");
	}

	public void afficherMatrice() {
		for (int x = 0; x < this.height; x++) {
			System.out.print("[ ");
			for (int y = 0; y < this.width; y++) {
				System.out.printf("%.2f ", this.pixels[x][y]);
			}
			System.out.println("]");
		}
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
	public double[][] getPixels() {
	    return this.pixels;
	}
	
	public static Image noising(Image img,double ecart_type) {
		for(int i = 0; i < img.getHeight(); i++) {
			for(int j = 0; j < img.getWidth(); j++) {
				// Simulation d'une loi normale centré avec l'écart type en paramètre
				double S = 0;
				for(int s = 0; s < 12; s++) {
					// Simulation de loi uniforme sur [0;1]
					double X = Math.random();
					S = S + X;					
				}
				S = S - 6; // S suit une loi normale centrée réduite
				
				S = S*ecart_type; // S suit loi normale centrée en 0 et de variance σ²=ecart_type²
				
				// On ajoute le bruit gaussien
				img.pixels[i][j]=img.pixels[i][j]+S;
				
				// On empêche les valeurs au dessus de 255 et en dessous de 0
				if(img.pixels[i][j]>255) {
					img.pixels[i][j]=255;
				}
				if(img.pixels[i][j]<0) {
					img.pixels[i][j]=0;
				}				
			}
		}
		return img;
	}

    public List<Image> decoupeImage(int taille, int nombre) {
        List<Image> subImages = new ArrayList<>();
        int rows = (int) Math.ceil((double) this.height / taille);
        int cols = (int) Math.ceil((double) this.width / taille);

        int count = 0;
        for (int row = 0; row < rows && count < nombre; row++) {
            for (int col = 0; col < cols && count < nombre; col++) {
                int startX = col * taille;
                int startY = row * taille;
                int endX = Math.min(startX + taille, this.width);
                int endY = Math.min(startY + taille, this.height);

                double[][] subPixels = new double[endY - startY][endX - startX];
                for (int y = startY; y < endY; y++) {
                    for (int x = startX; x < endX; x++) {
                        subPixels[y - startY][x - startX] = this.pixels[y][x];
                    }
                }

				try{
                Image subImage = new Image(subPixels, endY - startY, endX - startX);
                subImages.add(subImage);
                count++;
            }catch (Exception e) {
				System.out.println("Erreur lors de la découpe de l'image : " + e.getMessage());
			}
        }
        }
		return subImages;

    }
}


