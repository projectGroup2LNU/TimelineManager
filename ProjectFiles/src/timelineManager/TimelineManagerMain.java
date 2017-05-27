/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timelineManager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import timelineManager.view.ViewFactory;

/**
 * The main class of Timeline Manager. It calles the viewFactory
 * and sets the legal window sizes of the application.
 *
 */
public class TimelineManagerMain extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        ViewFactory viewFactory=ViewFactory.defaultFactory;
        Scene scene = viewFactory.getMainScene();
        stage.setMaxWidth(1200);
        stage.setMinWidth(640);
        stage.setMinHeight(580);
        stage.setMaxHeight(640);
        stage.setResizable(true);
        stage.setTitle("Group 2 Timeline Manager Project");
        stage.getIcons().add(new Image("file:../TimelineManager/ProjectFiles/src/timelineManager/resource/image/icon.png"));
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
