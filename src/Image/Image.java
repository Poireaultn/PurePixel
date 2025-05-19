package Image;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import ACP.ACP;
import Vecteur.Vecteur;

public class Image {
    private double[][] pixels;
    private int height;
    private int width;

    public Image(String filePath) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new File(filePath));
        this.width = bufferedImage.getWidth();
        this.height = bufferedImage.getHeight();

        if (bufferedImage.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            this.pixels = toGrayMatrix(bufferedImage);
        } else {
            this.pixels = toMatrix(bufferedImage); // conversion RGB → gris sinon
        }
    }

    private double[][] toGrayMatrix(BufferedImage image) {
        double[][] gray = new double[this.height][this.width];
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                int value = image.getRaster().getSample(x, y, 0); // canal unique
                gray[y][x] = value;
            }
        }
        return gray;
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
                gray[y][x] = 0.299 * red + 0.587 * green + 0.114 * blue;
            }
        }
        return gray;
    }

    public static void EnregistrerImage(Image img, String filePath) throws IOException {
        int width = img.getWidth();
        int height = img.getHeight();
        double[][] pixels = img.getPixels();

        // Normalisation si nécessaire
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (pixels[i][j] < min) min = pixels[i][j];
                if (pixels[i][j] > max) max = pixels[i][j];
            }
        }

        if (max - min > 1e-3) { // éviter division par 0
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    pixels[i][j] = 255 * (pixels[i][j] - min) / (max - min);
                }
            }
        }

        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = bi.getRaster();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int gray = (int) Math.round(pixels[y][x]);
                gray = Math.max(0, Math.min(255, gray)); // Clamp
                raster.setSample(x, y, 0, gray);
            }
        }

        ImageIO.write(bi, "png", new File(filePath));
        System.out.println("Image en niveaux de gris sauvegardée dans : " + filePath);
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


	public static Image reconstructPatchs(List<Patch> patchs, int imageHeight, int imageWidth, int shift) {
		if (patchs == null || patchs.isEmpty()) return null;

	    int patchSize = patchs.get(0).getTaille(); 
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

	    
	    for (int i = 0; i < imageHeight; i++) {
	        for (int j = 0; j < imageWidth; j++) {
	            if (count[i][j] > 0) {
	                pixels[i][j] /= count[i][j];
	            } else {
	                // Avertissement si un pixel n’a jamais été couvert
	              
	                pixels[i][j] = 0; // Valeur par défaut (noir)
	            }
	        }
	    }


	    return new Image(pixels, imageHeight, imageWidth);
	}
	
	

	
	public static Image denoisingGlobalPCA(Image imgNoisy, int patchSize, String typeSeuillage, String methodeSeuil, double sigma) {
	    // 1. Calculer le shift adapté
	    int shift = Patch.calculerShift(imgNoisy, patchSize);
	    System.out.println("Taille shift" + shift);
	    System.out.println("Taille Patch" + patchSize);
	    // 2. Extraire les patchs avec ce shift
	    List<Patch> patchs = Patch.ExtractPatchs(imgNoisy, patchSize, shift);
	    
	    // 3. Convertir les patchs en vecteurs
	    List<Vecteur> vecteurs = Patch.VectorPatchs(patchs);
	    
	    // 4. Appliquer le débruitage PCA
	    ArrayList<Vecteur> vecteursArrayList = new ArrayList<>(vecteurs);
	    List<Vecteur> vecteursDen = ACP.denoisingACP(vecteursArrayList, typeSeuillage, methodeSeuil, sigma);
	    System.out.println("test");
	    // 5. Reconversion des vecteurs débruités en patchs
	    List<Patch> patchsDen = Patch.PatchVectors(vecteursDen);
	    
	    // 6. Reconstruction de l’image en utilisant le même shift que celui utilisé pour extraire les patchs
	    Image imgDenoised = Image.reconstructPatchs(patchsDen, imgNoisy.getHeight(), imgNoisy.getWidth(), shift);
	    
	    // 7. Normaliser l’image pour que les pixels soient entre 0 et 255
	    double[][] pixels = imgDenoised.getPixels();
	    double min = Double.MAX_VALUE;
	    double max = Double.MIN_VALUE;

	    for (int i = 0; i < imgDenoised.getHeight(); i++) {
	        for (int j = 0; j < imgDenoised.getWidth(); j++) {
	            if (pixels[i][j] < min) min = pixels[i][j];
	            if (pixels[i][j] > max) max = pixels[i][j];
	        }
	    }

	    for (int i = 0; i < imgDenoised.getHeight(); i++) {
	        for (int j = 0; j < imgDenoised.getWidth(); j++) {
	            pixels[i][j] = 255.0 * (pixels[i][j] - min) / (max - min);
	        }
	    }

	    // 8. Retourner la nouvelle image normalisée
	    return new Image(pixels, imgDenoised.getHeight(), imgDenoised.getWidth());
	}



}


