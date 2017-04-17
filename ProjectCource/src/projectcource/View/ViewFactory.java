/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectcource.View;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

/**
 *
 * @author beysimeryalmaz
 */
public class ViewFactory {
    
    public Scene getMainScene(){
		Pane pane;		
		try {
			pane = FXMLLoader.load(getClass().getResource("/projectcource/View/MainView.fxml"));
		} catch (IOException e) {
                    System.out.println("yok burda");
			pane = null;
		}
		Scene scene = new Scene(pane);
		//scene.getStylesheets().add(getClass().getResource("emaildetails.css").toExternalForm());
		return scene;
		
	}
    
     public Scene getAddTimelineScene(){
         Pane pane;		
		try {
			pane = FXMLLoader.load(getClass().getResource("/projectcource/View/addTimelineView.fxml"));
		} catch (IOException e) {
                    System.out.println("yok burda");
			pane = null;
		}
		Scene scene = new Scene(pane);
		//scene.getStylesheets().add(getClass().getResource("emaildetails.css").toExternalForm());
		return scene;
     
     
     }
    
}
