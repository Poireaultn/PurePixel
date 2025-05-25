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
import javafx.scene.control.Label;
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
  
    @FXML private Label labelValeurBruit;
    @FXML private ImageView imageOriginale;
    @FXML private ImageView imageTraitee;
    @FXML private ListView<String> listeImages;
    @FXML private VBox boxParametresLocaux;
    @FXML private ComboBox<String> comboMode;
    @FXML private VBox boxDebruitage;
    @FXML private VBox boxBruitage;
    @FXML private Slider sliderNiveauBruitBruitage;
    @FXML private Label labelValeurBruitBruitage;
    @FXML private ComboBox<String> comboTraitement;


    private Map<String, Image> imagesChargees = new HashMap<>();

    @FXML
    public void initialize() {
        // === Initialisation des ComboBox ===
        comboTraitement.getItems().addAll("Débruitage", "Bruitage");
        comboTraitement.setValue("Débruitage"); // Option par défaut

        comboMethode.getItems().addAll("Globale", "Locale");
        comboMethode.getSelectionModel().selectFirst();

        comboSeuilType.getItems().addAll("Dur", "Doux");
        comboSeuilType.getSelectionModel().selectFirst();

        comboSeuilMeth.getItems().addAll("VisuShrink", "BayesShrink");
        comboSeuilMeth.getSelectionModel().selectFirst();

        // === Mise à jour dynamique de l'affichage des paramètres locaux ===
        comboMethode.setOnAction(e -> updateAffichageParametres());
        updateAffichageParametres();

        // === Gestion du changement de mode traitement (Débruitage / Bruitage) ===
        comboTraitement.valueProperty().addListener((obs, oldVal, newVal) -> {
            if ("Bruitage".equals(newVal)) {
                boxDebruitage.setVisible(false);
                boxDebruitage.setManaged(false);
                boxBruitage.setVisible(true);
                boxBruitage.setManaged(true);
            } else {
                boxDebruitage.setVisible(true);
                boxDebruitage.setManaged(true);
                boxBruitage.setVisible(false);
                boxBruitage.setManaged(false);
            }
        });

        // === Sliders : mise à jour des labels associés ===
        sliderNiveauBruit.valueProperty().addListener((obs, oldVal, newVal) -> {
            labelValeurBruit.setText(String.format("Valeur : %.0f", newVal));
        });

        sliderNiveauBruitBruitage.valueProperty().addListener((obs, oldVal, newVal) -> {
            labelValeurBruitBruitage.setText(String.format("Valeur : %.0f", newVal));
        });

        // === Boutons d'action ===
        btnCharger.setOnAction(e -> {
            try {
                chargerImage();
            } catch (IOException e1) {
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

        // === Liste d'images : sélection et affichage ===
        listeImages.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                Image img = imagesChargees.get(newVal);
                if (img != null) {
                    imageOriginale.setImage(SwingFXUtils.toFXImage(img.toBufferedImage(), null));
                }
            }
        });
    }

    @FXML
    private void updateAffichageParametres() {
        String methode = comboMethode.getValue();
        boolean isLocale = "Locale".equals(methode);
        boxParametresLocaux.setVisible(isLocale);
        boxParametresLocaux.setManaged(isLocale);
    }

    @FXML
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
    
        String traitement = comboTraitement.getValue();
        Image resultat;
    
        if ("Bruitage".equals(traitement)) {
            double sigma = sliderNiveauBruitBruitage.getValue();
            resultat = Image.noising(image, sigma);
        } else { // Débruitage
            String methode = comboMethode.getValue();
            String typeSeuillage = comboSeuilType.getValue();
            String methodeSeuil = comboSeuilMeth.getValue();
            double sigma = sliderNiveauBruit.getValue();
    
            if ("Globale".equals(methode)) {
                resultat = Image.denoisingGlobalPCA(image, 8, typeSeuillage, methodeSeuil, sigma);
            } else {
                try {
                    int patchSize = Integer.parseInt(tfTaillePatch.getText());
                    int nbImagettes = Integer.parseInt(tfNbImagettes.getText());
                    resultat = Image.denoisingLocalPCA(image, nbImagettes, patchSize, typeSeuillage, methodeSeuil, sigma);
                } catch (NumberFormatException e) {
                    System.out.println("Veuillez entrer des valeurs numériques valides pour les paramètres locaux.");
                    return;
                }
            }
        }
    
        // Sauvegarde (optionnelle)
        String fileFinalName = "src/resources/Image/Resultats/IMG_resultat.png";
        Image.EnregistrerImage(resultat, fileFinalName);
    
        // Affichage
        imageTraitee.setImage(SwingFXUtils.toFXImage(resultat.toBufferedImage(), null));
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
