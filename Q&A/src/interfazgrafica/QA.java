package interfazgrafica;

import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Carlos Onorio
 */
public class QA extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Locale locale = Locale.getDefault();
        Parent root = FXMLLoader.load(getClass().getResource("Principal.fxml"), ResourceBundle.getBundle("lang.lang", locale));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}