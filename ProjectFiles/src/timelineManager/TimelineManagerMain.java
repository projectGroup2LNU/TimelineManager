/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timelineManager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import timelineManager.view.ViewFactory;

/**
 *
 * @author beysimeryalmaz
 */
public class TimelineManagerMain extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("/timelineManager/view/MainView.fxml"));
        
        //Scene scene = new Scene(root);
        
        ViewFactory viewFactory=new ViewFactory();
        Scene scene = viewFactory.getMainScene();
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
