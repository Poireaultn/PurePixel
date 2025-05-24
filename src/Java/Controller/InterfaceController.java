package Java.Controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import Java.Image.Image; // Assurez-vous que ce n'est pas javafx.scene.image.Image
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class InterfaceController {

    @FXML private ComboBox<String> comboMethode;
    @FXML private ComboBox<String> comboSeuilType;
    @FXML private ComboBox<String> comboSeuilMeth;
    @FXML private Slider sliderNiveauBruit;
    @FXML private TextField tfTaillePatch;
    @FXML private TextField tfNbImagettes;

    @FXML private Button btnCharger;
    @FXML private Button btnAppliquer;
    @FXML private Button btnAppliquer2;
    @FXML private Button btnTelecharger;

    @FXML private ImageView imageOriginale;
    @FXML private ImageView imageTraitee;
    @FXML private ListView<String> listeImages;
    @FXML private VBox boxParametresLocaux;

    private Map<String, Image> imagesChargees = new HashMap<>();

    @FXML
    public void initialize() {
        // Initialisation des choix
        comboMethode.getItems().addAll("Globale", "Locale");
        comboSeuilType.getItems().addAll("Dur", "Doux");
        comboSeuilMeth.getItems().addAll("VisuShrink", "BayesShrink");

        comboMethode.getSelectionModel().selectFirst();
        comboSeuilType.getSelectionModel().selectFirst();
        comboSeuilMeth.getSelectionModel().selectFirst();

        // Gestion dynamique de l'affichage des paramètres locaux
        comboMethode.setOnAction(e -> updateAffichageParametres());
        updateAffichageParametres();

        btnCharger.setOnAction(e -> {
			try {
				chargerImage();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
        btnTelecharger.setOnAction(e -> telechargerImage());

        btnAppliquer.setOnAction(e -> {
            try {
                appliquerTraitement();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        btnAppliquer2.setOnAction(e -> {
            try {
                appliquerTraitement();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        listeImages.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                Image img = imagesChargees.get(newVal);
                if (img != null) {
                    imageOriginale.setImage(SwingFXUtils.toFXImage(img.toBufferedImage(), null));
                }
            }
        });

        // Configuration des ImageView
        imageOriginale.setPreserveRatio(true);
        imageOriginale.setFitWidth(300);
        imageTraitee.setPreserveRatio(true);
        imageTraitee.setFitWidth(300);
    }

    private void updateAffichageParametres() {
        String methode = comboMethode.getValue();
        boolean isLocale = "Locale".equals(methode);
        boxParametresLocaux.setVisible(isLocale);
        boxParametresLocaux.setManaged(isLocale);
    }

    private void appliquerTraitement() throws IOException {
        String selectedNom = listeImages.getSelectionModel().getSelectedItem();
        if (selectedNom == null) {
            System.out.println("Veuillez sélectionner une image à traiter.");
            return;
        }

        Image image = imagesChargees.get(selectedNom);
        if (image == null) {
            System.out.println("Image non trouvée.");
            return;
        }

        String methode = comboMethode.getValue();
        String typeSeuillage = comboSeuilType.getValue();
        String methodeSeuil = comboSeuilMeth.getValue();
        double sigma = sliderNiveauBruit.getValue();

        Image imgDebruitee;

        if (methode.equals("Globale")) {
            imgDebruitee = Image.denoisingGlobalPCA(image, 8, typeSeuillage, methodeSeuil, sigma);
        } else {
            try {
                int patchSize = Integer.parseInt(tfTaillePatch.getText());
                int nbImagettes = Integer.parseInt(tfNbImagettes.getText());

                imgDebruitee = Image.denoisingLocalPCA(image, nbImagettes, patchSize, typeSeuillage, methodeSeuil, sigma);
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer des valeurs numériques valides pour les paramètres locaux.");
                return;
            }
        }

        // Sauvegarde dans un fichier (optionnel)
        String fileFinalName = "src/resources/Image/Resultats/IMG_debruitee.png";
        Image.EnregistrerImage(imgDebruitee, fileFinalName);

        // Affichage de l'image débruitée
        imageTraitee.setImage(SwingFXUtils.toFXImage(imgDebruitee.toBufferedImage(), null));
    }

    @FXML
    private void chargerImage() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une ou plusieurs images");

        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        List<File> fichiers = fileChooser.showOpenMultipleDialog(null);

        if (fichiers != null) {
            listeImages.getItems().clear();
            imagesChargees.clear();
            for (File fichier : fichiers) {
            	Image image = new Image(fichier.getAbsolutePath());
                String nom = fichier.getName();
                imagesChargees.put(nom, image);
                listeImages.getItems().add(nom);
            }
        }
    }

    @FXML
    private void telechargerImage() {
        if (imageTraitee.getImage() == null) {
            System.out.println("Aucune image à sauvegarder.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer l'image");

        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("PNG Image", "*.png"),
            new FileChooser.ExtensionFilter("JPG Image", "*.jpg")
        );

        File fichier = fileChooser.showSaveDialog(null);

        if (fichier != null) {
            try {
                WritableImage writableImage = imageTraitee.snapshot(null, null);
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);

                String extension = fichier.getName().toLowerCase().endsWith(".jpg") ? "jpg" : "png";
                ImageIO.write(bufferedImage, extension, fichier);

                System.out.println("Image enregistrée : " + fichier.getAbsolutePath());

            } catch (IOException e) {
                System.out.println("Erreur lors de l'enregistrement : " + e.getMessage());
            }
        }
    }
}
