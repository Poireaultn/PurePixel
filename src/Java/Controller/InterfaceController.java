package Java.Controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import Java.Image.Image; //classe Image personnalisée
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.geometry.Insets;


public class InterfaceController {

    @FXML private ComboBox<String> comboMethode;
    @FXML private ComboBox<String> comboSeuilType;
    @FXML private ComboBox<String> comboSeuilMeth;
    @FXML private Slider sliderNiveauBruit;
    @FXML private TextField tfTaillePatch;
    @FXML private TextField tfNbImagettes;
    @FXML private Label labelOriginaleInfo;
    @FXML private Label labelTraiteeInfo;
    
    @FXML private Button btnCharger;
    @FXML private Button btnAppliquer;
    @FXML private Button btnAppliquer2;
    @FXML private Button btnTelecharger;

    @FXML private Label labelValeurBruit;
    @FXML private ImageView imageOriginale;
    @FXML private ImageView imageTraitee;

    private Image imageTraiteeBack;  // <-- Stocke l'image traitée pour export 
    @FXML private ListView<String> listeImages;
    @FXML private VBox boxParametresLocaux;
    @FXML private ComboBox<String> comboMode;
    @FXML private VBox boxDebruitage;
    @FXML private VBox boxBruitage;
    @FXML private Slider sliderNiveauBruitBruitage;
    @FXML private Label labelValeurBruitBruitage;
    @FXML private ComboBox<String> comboTraitement;
    @FXML private Label labelMSE;
    @FXML private Label labelPSNR;
    @FXML private HBox footerButtons;

    private Map<String, Image> imagesOriginales = new HashMap<>();


    private Map<String, Image> imagesChargees = new HashMap<>();

    @FXML
    public void initialize() {
    	
    	
    	Platform.runLater(() -> {
    	    String imagePath = new File("src/resources/Image/Background/BG.jpg").toURI().toString();
    	    imageOriginale.getScene().getRoot().setStyle(
    	        "-fx-background-image: url('" + imagePath + "');" +
    	        "-fx-background-size: cover;" +
    	        "-fx-background-repeat: no-repeat;" +
    	        "-fx-background-position: center;"
    	    );
    	});

    	
    	
        comboTraitement.getItems().addAll("Débruitage", "Bruitage");
        comboTraitement.setValue("Débruitage");

        comboMethode.getItems().addAll("Globale", "Locale");
        comboMethode.getSelectionModel().selectFirst();

        comboSeuilType.getItems().addAll("Dur", "Doux");
        comboSeuilType.getSelectionModel().selectFirst();

        comboSeuilMeth.getItems().addAll("VisuShrink", "BayesShrink");
        comboSeuilMeth.getSelectionModel().selectFirst();
        
        labelOriginaleInfo.setVisible(true);
        labelTraiteeInfo.setVisible(true);

        comboMethode.setOnAction(e -> updateAffichageParametres());
        updateAffichageParametres();

        comboTraitement.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean bruitage = "Bruitage".equals(newVal);
            boxDebruitage.setVisible(!bruitage);
            boxDebruitage.setManaged(!bruitage);
            boxBruitage.setVisible(bruitage);
            boxBruitage.setManaged(bruitage);
        });

        sliderNiveauBruit.valueProperty().addListener((obs, oldVal, newVal) -> {
            labelValeurBruit.setText(String.format("Valeur : %.0f", newVal));
        });

        sliderNiveauBruitBruitage.valueProperty().addListener((obs, oldVal, newVal) -> {
            labelValeurBruitBruitage.setText(String.format("Valeur : %.0f", newVal));
        });

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

        listeImages.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                Image img = imagesChargees.get(newVal);
                if (img != null) {
                    imageOriginale.setImage(SwingFXUtils.toFXImage(img.toBufferedImage(), null));
                    labelOriginaleInfo.setVisible(false); // Cache le message après sélection
                }
            }
        });
    }

    private void updateAffichageParametres() {
        boolean isLocale = "Locale".equals(comboMethode.getValue());
        boxParametresLocaux.setVisible(isLocale);
        boxParametresLocaux.setManaged(isLocale);

        if (footerButtons != null) {
            footerButtons.setPadding(isLocale ? new Insets(0, 10, 0, 10) : new Insets(10, 10, 10, 10));
        }
    }

    
    
    @FXML
    private void appliquerTraitement() throws IOException {
        String selectedNom = listeImages.getSelectionModel().getSelectedItem();
        if (selectedNom == null) {
            System.out.println("Veuillez sélectionner une image à traiter.");
            return;
        }

        Image imageOriginaleRef = imagesChargees.get(selectedNom);
        if (imageOriginaleRef == null) {
            System.out.println("Image non trouvée.");
            return;
        }

        // Cloner l'image
        double[][] pixelsClone = new double[imageOriginaleRef.getHeight()][imageOriginaleRef.getWidth()];
        for (int i = 0; i < imageOriginaleRef.getHeight(); i++) {
            System.arraycopy(imageOriginaleRef.getPixels()[i], 0, pixelsClone[i], 0, imageOriginaleRef.getWidth());
        }
        Image image = new Image(pixelsClone, imageOriginaleRef.getHeight(), imageOriginaleRef.getWidth());

        String traitement = comboTraitement.getValue();
        Image resultat;

        // Extraire le nom sans extension
        String baseName = selectedNom.contains(".") ? selectedNom.substring(0, selectedNom.lastIndexOf('.')) : selectedNom;
        String extension = ".png"; // on standardise l'export en PNG

        if ("Bruitage".equals(traitement)) {
            //  Créer le dossier originals/ s’il n'existe pas
            File originalDir = new File("src/resources/Image/originals/");
            if (!originalDir.exists()) {
                originalDir.mkdirs();
            }

            //  Sauvegarder l'image propre AVANT bruitage
            String originalFileName = "src/resources/Image/originals/originale_" + baseName + extension;
            Image.EnregistrerImage(image, originalFileName);

            //  Appliquer le bruit
            double sigma = sliderNiveauBruitBruitage.getValue();
            resultat = Image.noising(image, sigma);

            // Pas de calcul de MSE/PSNR ici
            labelMSE.setText("MSE : —");
            labelPSNR.setText("PSNR : —");

        } else {
            //  Débruitage
            String methode = comboMethode.getValue();
            String typeSeuillage = comboSeuilType.getValue();
            String methodeSeuil = comboSeuilMeth.getValue();
            double sigma = Math.sqrt(sliderNiveauBruit.getValue());

            if ("Globale".equals(methode)) {
                int patchSize = 8;
                resultat = Image.denoisingGlobalPCA(image, patchSize, typeSeuillage, methodeSeuil, sigma);
            } else {
                try {
                    String patchStr = tfTaillePatch.getText();
                    String imagetteStr = tfNbImagettes.getText();

                    if (patchStr.isEmpty() || imagetteStr.isEmpty()) {
                        System.out.println("Veuillez remplir les champs taille de patch et taille d'imagette.");
                        return;
                    }

                    int patchSize = Integer.parseInt(patchStr);
                    int imagetteSize = Integer.parseInt(imagetteStr);

                    resultat = Image.denoisingLocalPCA(image, imagetteSize, patchSize, typeSeuillage, methodeSeuil, sigma);
                } catch (NumberFormatException e) {
                    System.out.println("Erreur : les valeurs entrées doivent être des entiers valides.");
                    return;
                }
            }

            // Tenter de retrouver l'image originale sauvegardée
            String originalFileName = "src/resources/Image/originals/originale_" + baseName + extension;
            File originalFile = new File(originalFileName);
            if (originalFile.exists()) {
                Image imagePropre = new Image(originalFile.getAbsolutePath());
                double mse = Image.MSE(imagePropre, resultat);
                double psnr = Image.PSNR(mse);
                labelMSE.setText(String.format("MSE : %.2f", mse));
                labelPSNR.setText(String.format("PSNR : %.2f dB", psnr));
            } else {
                labelMSE.setText("MSE : —");
                labelPSNR.setText("PSNR : —");
                System.out.println(" Image originale non trouvée pour : " + baseName);
            }
        }

        // 📤 Sauvegarde de l’image traitée sous le même nom
        File resultDir = new File("src/resources/Image/Resultats/");
        if (!resultDir.exists()) {
            resultDir.mkdirs();
        }
        String fileFinalName = "src/resources/Image/Resultats/" + baseName + extension;
        Image.EnregistrerImage(resultat, fileFinalName);

        // Affichage dans l’interface
        imageTraiteeBack = resultat;
        labelTraiteeInfo.setVisible(false); // Cache le message une fois l'image traitée affichée
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
            for (File fichier : fichiers) {
                String nom = fichier.getName();
                if (!imagesChargees.containsKey(nom)) {  // Évite les doublons
                    Image image = new Image(fichier.getAbsolutePath());
                    imagesChargees.put(nom, image);
                    listeImages.getItems().add(nom);
                }
            }
        }
    }

    
    
    
    
    

    @FXML
    private void telechargerImage() {
        if (imageTraiteeBack == null) {
            System.out.println("Aucune image traitée à sauvegarder.");
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
                // Utiliser l'image traitée directement
                BufferedImage bufferedImage = imageTraiteeBack.toBufferedImage();

                String extension = fichier.getName().toLowerCase().endsWith(".jpg") ? "jpg" : "png";
                ImageIO.write(bufferedImage, extension, fichier);

                System.out.println("Image enregistrée : " + fichier.getAbsolutePath());

            } catch (IOException e) {
                System.out.println("Erreur lors de l'enregistrement : " + e.getMessage());
            }
        }
    }
}
