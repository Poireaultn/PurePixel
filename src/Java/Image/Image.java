package Java.Image;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import Java.ACP.*;
import Java.Vecteur.*;

/**
 * Représente une image en niveaux de gris avec des opérations de traitement
 * comme l'ajout de bruit, la découpe en sous-images et le débruitage par ACP.
 * 
 * Cette classe sert de base pour toutes les opérations principales sur l'image.
 * 
 * @author Charles, Hichem, Ilyas, Nathan
 * @version 1.0
 */
public class Image {
    private double[][] pixels;
    private int height;
    private int width;
    
    /**
     * Crée une image à partir d'un fichier image (JPEG, PNG...).
     *
     * @param filePath Le chemin du fichier image.
     * @throws IOException Si le fichier ne peut pas être lu.
     */
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
    
    /**
     * Récupére les valeurs des pixels de l'image qui sont déjà en niveau de gris.
     *
     * @param image Notre image.
     * @return Les pixels de l'image en niveau de gris.
     */
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

    /**
     * Crée une image à partir d'une matrice.
     *
     * @param pixels La matrice de pixels de l'image.
     * @param height La hauteur de l'image.
     * @param width La largeur de l'image.
     */
    public Image(double[][] pixels, int height, int width) {
        this.pixels = pixels;
        this.height = height;
        this.width = width;
    }
    
    /**
     * Récupére les valeurs des pixels de l'image et les transforme en niveau de gris.
     *
     * @param image Notre image.
     * @return Les pixels de l'image en niveau de gris.
     */
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
    
    /**
     * Enregistre une image au format PNG à partir d'une instance Image.
     *
     * @param img      L'image à sauvegarder.
     * @param filePath Le chemin du fichier de sortie.
     * @throws IOException Si une erreur d'écriture survient.
     */
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
    
    /**
     * Affiche la matrice des pixels dans la console.
     */
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

    /**
     * Ajoute du bruit gaussien à une image.
     *
     * @param img         L'image d'origine.
     * @param ecart_type  L'écart-type du bruit (sigma).
     * @return L'image bruitée.
     */
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
	
	/**
	 * Calcule la taille minimale d'une imagette pour qu'elle contienne
	 * au moins autant de patchs que la dimension du patch (taillePatch²),
	 * en fonction d'un shift donné.
	 *
	 * @param taillePatch la taille (en pixels) d'un côté du patch (ex : 8 pour un patch 8x8)
	 * @param shift       le pas de déplacement entre deux patchs
	 * @return la taille minimale (en pixels) de l'imagette à utiliser
	 */
	public static int tailleImagetteMinimale(int taillePatch, int shift) {
	    if (taillePatch <= 0 || shift <= 0) {
	        throw new IllegalArgumentException("taillePatch et shift doivent être strictement positifs.");
	    }

	    int dimPatch = taillePatch * taillePatch;

	    // nombre minimal de patchs nécessaires
	    int nbPatchsCible = dimPatch;

	    // On cherche la plus petite taille L telle que :
	    // ((L - taillePatch) / shift + 1)^2 >= nbPatchsCible
	    int taille = taillePatch; // on commence au minimum possible
	    while (true) {
	        int n = (taille - taillePatch) / shift + 1;
	        int nbPatchs = n * n;

	        if (nbPatchs >= nbPatchsCible) {
	            return taille;
	        }

	        taille++;
	    }
	}

	
	/**
     * Découpe l'image en sous-images carrées de taille donnée.
     *
     * @param taille Taille d'un bloc carré (ex : 128x128).
     * @return Liste de sous-images.
     */
	public ArrayList<Image> decoupeImage(int taille) {
		ArrayList<Image> subImages = new ArrayList<>();
		double[][] pixels = this.getPixels();

		System.out.println("height : " + this.height);	
		System.out.println("width : " + this.width);
		System.out.println("taille : " + taille);
		for (int i = 0; i < this.height; i += taille) {
			for (int j = 0; j < this.width; j += taille) {
				double[][] subImagePixels = new double[taille][taille];
				for (int k = 0; k < taille; k++) {
					for (int l = 0; l < taille; l++) {
						if (i + k < this.height && j + l < this.width) {
							subImagePixels[k][l] = pixels[i + k][j + l];
						} else {
							subImagePixels[k][l] = 0; // Remplissage avec 0 si on dépasse les dimensions
						}
					}
				}
				Image subImage = new Image(subImagePixels, taille, taille);
				subImages.add(subImage);
			}
		}
		return subImages;
	}

    /**
     * Reconstitue une image à partir de patches en moyennant les zones qui se recouvrent.
     *
     * @param patchs      Liste des patches.
     * @param imageHeight Hauteur de l'image finale.
     * @param imageWidth  Largeur de l'image finale.
     * @param shift       Décalage entre deux patches.
     * @return Image reconstruite.
     */
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
	
	public static Image reconstruction(ArrayList<Image> subImages, int imageHeight, int imageWidth) {
		if (subImages == null || subImages.isEmpty()) return null;

		int patchSize = subImages.get(0).getHeight();

		int real_height = (int) Math.ceil((double) imageHeight / patchSize) * patchSize;
		int real_width = (int) Math.ceil((double) imageWidth / patchSize) * patchSize;
		double[][] pixels = new double[real_height][real_width];

		int index = 0;
		for (int i = 0; i < real_height; i += patchSize) {
			for (int j = 0; j < real_width; j += patchSize) {
				if (index >= subImages.size()) break;

				double[][] patchPixels = subImages.get(index).getPixels();
				for (int k = 0; k < patchSize; k++) {
					for (int l = 0; l < patchSize; l++) {
						int y = i + k;
						int x = j + l;
						if (y < real_height && x < real_width) {
							pixels[y][x] = patchPixels[k][l];
						}
					}
				}
				index++;
			}
		}

		// Recadrage à la taille originale
		double[][] finalPixels = new double[imageHeight][imageWidth];
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				finalPixels[i][j] = pixels[i][j];
			}
		}

		return new Image(finalPixels, imageHeight, imageWidth);
	}
	
	/**
     * Applique un débruitage global par ACP sur l'image.
     *
     * @param imgNoisy       Image bruitée.
     * @param patchSize      Taille des patches.
     * @param typeSeuillage  Type de seuillage.
     * @param methodeSeuil   Méthode de calcul du seuil.
     * @param sigma          Bruit estimé (sigma).
     * @return Image débruitée.
     */
	public static Image denoisingGlobalPCA(Image imgNoisy, int patchSize, String typeSeuillage, String methodeSeuil, double sigma) {
	    // 1. Calculer le shift adapté
	    int shift = Patch.calculerShift(imgNoisy, patchSize);
	    System.out.println("Taille du shift : " + shift);
	    System.out.println("Taille des Patchs : " + patchSize + " pixels");
	    // 2. Extraire les patchs avec ce shift
	    List<Patch> patchs = Patch.ExtractPatchs(imgNoisy, patchSize, shift);
	    
	    // 3. Convertir les patchs en vecteurs
	    List<Vecteur> vecteurs = Patch.VectorPatchs(patchs);
	    
	    // 4. Appliquer le débruitage PCA
	    ArrayList<Vecteur> vecteursArrayList = new ArrayList<>(vecteurs);
	    List<Vecteur> vecteursDen = ACP.denoisingACP(vecteursArrayList, typeSeuillage, methodeSeuil, sigma);
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
	
	/**
     * Applique un débruitage local par ACP sur l'image.
     *
     * @param imgNoisy       Image bruitée.
     * @param imagetteSize   Taille des imagettes.
     * @param patchSize      Taille des patches.
     * @param typeSeuillage  Type de seuillage.
     * @param methodeSeuil   Méthode de calcul du seuil.
     * @param sigma          Bruit estimé (sigma).
     * @return Image débruitée.
     */
	public static Image denoisingLocalPCA(Image imgNoisy, int imagetteSize, int patchSize, String typeSeuillage, String methodeSeuil, double sigma) {
		// On découpe l'image en plusieurs imagettes
		ArrayList<Image> listeImagettes = imgNoisy.decoupeImage(imagetteSize);
		
		// On applique la méthode globale sur chaque imagette
		ArrayList<Image> listeImgDenoised = new ArrayList<Image>();
		for (int i = 0; i < listeImagettes.size(); i++) {
			Image imgDenoised = denoisingGlobalPCA(listeImagettes.get(i),patchSize,typeSeuillage,methodeSeuil,sigma);
			listeImgDenoised.add(imgDenoised);
		}
		
		// on reconstruit l'image finale
		Image imgDenoised = reconstruction(listeImgDenoised,imgNoisy.getHeight(),imgNoisy.getWidth());
		
		return new Image(imgDenoised.getPixels(), imgDenoised.getHeight(), imgDenoised.getWidth());
	}
	
	/**
     * Calcule l'erreur quadratique moyenne (MSE).
     *
     * @param imgNoisy      Image bruitée.
     * @param imgDenoised   Image debruitée.
     * @return MSE.
     */
	public static double MSE(Image imgNoisy, Image imgDenoised) {
		
		int hauteur = imgNoisy.getHeight();
		int largeur = imgNoisy.getWidth();
		double[][] pixelsImgNoisy = imgNoisy.getPixels();
		double[][] pixelsImgDenoised = imgDenoised.getPixels();
		
		// Calcul du MSE
		double somme = 0.0;
		for (int i = 0; i < hauteur; i++) {
			for (int j =0; j < largeur; j++) {
				somme += Math.pow(pixelsImgNoisy[i][j]-pixelsImgDenoised[i][j],2);
			}
		}
		
		double mse = somme / (hauteur*largeur);
		
		return mse;
	}
	
	/**
     * Calcule le PSNR.
     *
     * @param MSE      Valeur du MSE.
     * @return PSNR.
     */
	public static double PSNR(double MSE) {
		return 10*Math.log10(Math.pow(255, 2)/MSE);
	}

	
	/**
	 * Convertit cette instance d'Image (matrice de niveaux de gris) en BufferedImage.
	 *
	 * @return BufferedImage représentant l'image.
	 */
	public BufferedImage toBufferedImage() {
	    BufferedImage bi = new BufferedImage(this.width, this.height, BufferedImage.TYPE_BYTE_GRAY);
	    WritableRaster raster = bi.getRaster();

	    for (int y = 0; y < this.height; y++) {
	        for (int x = 0; x < this.width; x++) {
	            int gray = (int) Math.round(this.pixels[y][x]);
	            gray = Math.max(0, Math.min(255, gray)); // Clamp pour rester entre 0 et 255
	            raster.setSample(x, y, 0, gray);
	        }
	    }

	    return bi;
	}

}


