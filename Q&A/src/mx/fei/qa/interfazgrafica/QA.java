package mx.fei.qa.interfazgrafica;

import mx.fei.qa.utileria.UtileriaInterfazUsuario;
import javafx.application.Application;
import static javafx.application.Application.launch;
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
        UtileriaInterfazUsuario.mostrarVentana(getClass(), "key.principal", 
                "Principal.fxml", null);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
