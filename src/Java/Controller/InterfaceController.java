package Java.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;

public class InterfaceController {

    // Références aux composants FXML
    @FXML private ComboBox<String> comboMethode;
    @FXML private ComboBox<String> comboSeuilType;
    @FXML private ComboBox<String> comboSeuilMeth;
    @FXML private Slider sliderNiveauBruit;

    @FXML private Button btnCharger;
    @FXML private Button btnAppliquer;
    @FXML private Button btnAppliquer2;
    @FXML private Button btnTelecharger;

    @FXML private ImageView imageOriginale;
    @FXML private ImageView imageTraitee;

    // Méthode appelée automatiquement après le chargement du FXML
    @FXML
    public void initialize() {
        // Exemple : Initialiser les choix des ComboBox
        comboMethode.getItems().addAll("Globale", "Locale");
        comboSeuilType.getItems().addAll("Dur", "Doux");
        comboSeuilMeth.getItems().addAll("VisuShrink", "BayesShrink");

        comboMethode.getSelectionModel().selectFirst();
        comboSeuilType.getSelectionModel().selectFirst();
        comboSeuilMeth.getSelectionModel().selectFirst();

        // Ajout d'une action bouton exemple
        //btnAppliquer.setOnAction(e -> appliquerTraitement());
    }

    // Méthode appelée par le bouton Appliquer
    /*private void appliquerTraitement() {
        String methode = comboMethode.getValue();
        String typeSeuil = comboSeuilType.getValue();
        String methSeuil = comboSeuilMeth.getValue();
        double bruit = sliderNiveauBruit.getValue();

        System.out.println("Traitement appliqué avec :");
        System.out.println("- Méthode : " + methode);
        System.out.println("- Type de seuillage : " + typeSeuil);
        System.out.println("- Méthode de seuil : " + methSeuil);
        System.out.println("- Bruit : " + bruit);
        
        // TODO : Ajouter ton algorithme de traitement d'image ici
    }*/
}
