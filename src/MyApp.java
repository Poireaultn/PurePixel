import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
public class MyApp extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {

        /* donner un nom à la fenêtre */
        primaryStage.setTitle("PurePixel");

        
    	
        /* création d'une fenêtre */
        StackPane root = new StackPane();


        /* ajouter le bouton à la scene */
        root.getChildren().add(b);

        /* création d'une scene et de son association avec */
        /* la fenêtre + taille */
        Scene scene = new Scene(root, 500, 300);
        scene.getStylesheets().add("/style.css");
        /* Ajouter la scene */
        primaryStage.setScene(scene);

        /* rendre la fenêtre visible */
        primaryStage.show();

        }
    
        public static void main (String[] args) {
            launch (args);
        }
        
        
}