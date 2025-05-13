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
	
	
	public Image reconstructPatchs(List<Patch> patchs) {
		double[][] pixels= {{1, 2, 3},{4, 5, 6},{7, 8, 9}};
		int height=100;
		int width=100;
		Image img = new Image(pixels, height, width);
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