package noising;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

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

	public void afficherMatrice() {
		for (int y = 0; y < this.height; y++) {
			System.out.print("[ ");
			for (int x = 0; x < this.width; x++) {
				System.out.printf("%.2f ", this.pixels[x][y]);
			}
			System.out.println("]");
		}
	}
	
	
	public static Image reconstructPatchs(List<Patch> patchs, int imageHeight, int imageWidth, int shift) {
		if (patchs == null || patchs.isEmpty()) return null;

	    int patchSize = patchs.get(0).getTaille(); // All patches assumed same size
	    double[][] pixels = new double[imageHeight][imageWidth];
	    int[][] count = new int[imageHeight][imageWidth];

	    int patchIndex = 0;

	    for (int y = 0; y <= imageHeight - patchSize; y += shift) {
	        for (int x = 0; x <= imageWidth - patchSize; x += shift) {
	            if (patchIndex >= patchs.size()) break;

	            double[][] patchValues = patchs.get(patchIndex++).getValeur();

	            for (int i = 0; i < patchSize; i++) {
	                for (int j = 0; j < patchSize; j++) {
	                    int imgY = y + i;
	                    int imgX = x + j;

	                    pixels[imgY][imgX] += patchValues[i][j];
	                    count[imgY][imgX]++;
	                }
	            }
	        }
	    }

	    // Average the pixel values where overlapping patches occurred
	    for (int i = 0; i < imageHeight; i++) {
	        for (int j = 0; j < imageWidth; j++) {
	            if (count[i][j] > 0) {
	                pixels[i][j] /= count[i][j];
	            }
	        }
	    }

	    return new Image(pixels, imageHeight, imageWidth);
	}

	
	
	
	
	
	public static BufferedImage convertToBufferedImage(Image image) {
	    int width = image.getWidth();
	    int height = image.getHeight();
	    double[][] data = image.getPixels(); // ou image.getValeur()

	    // Crée une nouvelle image avec les dimensions inversées pour la rotation de 90°
	    BufferedImage img = new BufferedImage(height, width, BufferedImage.TYPE_BYTE_GRAY);

	    // Parcourt tous les pixels pour appliquer la rotation de 90° anti-horaire
	    for (int y = 0; y < height; y++) {
	        for (int x = 0; x < width; x++) {
	            int value = (int) data[y][x];  // On prend les pixels dans l'ordre des coordonnées d'origine
	            value = Math.max(0, Math.min(255, value));  // Clamp les valeurs
	            int rgb = (value << 16) | (value << 8) | value;  // Conversion en couleur

	            // Rotation de 90° dans le sens inverse des aiguilles d'une montre
	            img.setRGB(y, x, rgb);
	        }
	    }

	    return img;
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
	

}
