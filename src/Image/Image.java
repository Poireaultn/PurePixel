package Image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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

	public void afficherMatrice() {
		for (int y = 0; y < this.height; y++) {
			System.out.print("[ ");
			for (int x = 0; x < this.width; x++) {
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


}
