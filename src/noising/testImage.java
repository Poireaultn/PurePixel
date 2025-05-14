package noising;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.List;

public class testImage{
    public static void main(String[] args) {
        try {
            // Assuming Image is a custom class that loads and manipulates an image
            Image ney = new Image("ressources/neymar.jpg"); // Charge l’image
            ney.afficherMatrice(); // Affiche la matrice de l'image
            
            // Extract patches from the image
            List<Patch> patchs = Patch.ExtractPatchs(ney, 28); 
            System.out.println("Le nombre de patches est : " + patchs.size());
            
            // Display the first two patches
            Patch patch1 = patchs.get(0);
            Patch patch2 = patchs.get(1);
            Patch.afficherPatch(patch1);
            Patch.afficherPatch(patch2);
            
            // Reconstruct the image from patches
            Image reconstructedImage = Image.reconstructPatchs(patchs, ney.getHeight(), ney.getWidth(), 12); // Specify width, height, and other params
            System.out.println("Image reconstruite : ");
            reconstructedImage.afficherMatrice();
            
            // Convert the reconstructed image to BufferedImage and save it
            BufferedImage buffered = Image.convertToBufferedImage(reconstructedImage);
            ImageIO.write(buffered, "png", new File("output.png"));
            JFrame frame = new JFrame("Reconstructed Image");
            JLabel label = new JLabel(new ImageIcon(buffered));
            frame.add(label);
            frame.setSize(ney.getHeight(), ney.getWidth()); // Adjust the size as needed
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            
        } catch (IOException e) {
            System.out.println("Erreur lors du traitement de l'image : " + e.getMessage());
        }
    }
}
