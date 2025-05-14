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


