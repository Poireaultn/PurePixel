import Image.Image;
import Image.Patch;
import Vecteur.Vecteur;

import java.util.List;
import java.util.ArrayList;

public class TestDenoising {

    public static void main(String[] args) throws Exception {
        // 1. Charger une image
        Image imgOriginal = new Image("chemin/vers/image.png");

        // 2. Ajouter du bruit gaussien
        double sigma = 20.0; // écart-type bruit
        Image imgNoisy = Image.noising(imgOriginal, sigma);

        // 3. Découper en patchs (ex : taille 8x8, extraction avec shift=8)
        int patchSize = 8;
        int nombrePatchs = 1000; // limite arbitraire pour test
        List<Image> subImages = imgNoisy.decoupeImage(patchSize, nombrePatchs);

        // 4. Convertir chaque subImage en Patch (à partir de ta classe Patch)
        List<Patch> patchs = new ArrayList<>();
        for (Image subImg : subImages) {
            patchs.add(new Patch(subImg.getPixels(), patchSize));
        }

        // 5. Vectoriser les patchs
        List<Vecteur> vecteurs = Patch.VectorPatchs(patchs);

        // 6. Appliquer le débruitage ACP (exemple avec seuillage dur + VisuShrink)
        List<Vecteur> vecteursDenoised = ACP.denoisingACP(
            new ArrayList<>(vecteurs),
            "dur",
            "VisuShrink",
            sigma
        );

        // 7. Reconstruire les patchs débruités
        List<Patch> patchsDenoised = Patch.PatchVectors(vecteursDenoised);

        // 8. Reconstruire l’image débruitée complète
        Image imgDenoised = Image.reconstructPatchs(patchsDenoised, imgOriginal.getHeight(), imgOriginal.getWidth(), patchSize);

        // 9. Sauvegarder les images bruitées et débruitées
        Image.EnregistrerImage(imgNoisy, "image_bruitee.png");
        Image.EnregistrerImage(imgDenoised, "image_debruitee.png");

        System.out.println("Test terminé, images sauvegardées.");
    }
}
