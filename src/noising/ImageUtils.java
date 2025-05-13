package noising;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageUtils {

    // Convertit une image en niveaux de gris en matrice de double
    public static double[][] imageToMatrix(BufferedImage img) {
        int height = img.getHeight();
        int width = img.getWidth();
        double[][] matrix = new double[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(img.getRGB(x, y));
                // Convertir en niveaux de gris (luminance)
                double gray = 0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue();
                matrix[y][x] = gray;
            }
        }

        return matrix;
    }

    // Lit une image depuis un fichier
    public static BufferedImage readImage(String path) {
        try {
            BufferedImage img = ImageIO.read(new File(path));
            System.out.println("Image chargée : " + img.getWidth() + "x" + img.getHeight());
            return img;
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de l’image : " + e.getMessage());
            return null;
        }
    }
}

