package timelineManager.view;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

/**
 * This class is a helper class for loading the views from .fxml files.
 * @author beysimeryalmaz
 */
public class ViewFactory {
    
    public Scene getMainScene(){
		Pane pane;		
		try {
			pane = FXMLLoader.load(getClass().getResource("/timelineManager/view/MainView.fxml"));
		} catch (IOException e) {
                    System.out.println("MainView couldn't be loaded: " + e);
			pane = null;
		}
		Scene scene = new Scene(pane);
		//scene.getStylesheets().add(getClass().getResource("emaildetails.css").toExternalForm());
		return scene;
		
	}
    
     public Scene getAddTimelineScene(){
         Pane pane;		
		try {
			pane = FXMLLoader.load(getClass().getResource("/timelineManager/view/AddTimelineView.fxml"));
		} catch (IOException e) {
                    System.out.println("AddTimelineView couldn't be loaded: " + e);
			pane = null;
		}
		Scene scene = new Scene(pane);
		//scene.getStylesheets().add(getClass().getResource("emaildetails.css").toExternalForm());
		return scene;
     
     
     }
     
     public Scene getAddTaskScene(){
         Pane pane;		
		try {
			pane = FXMLLoader.load(getClass().getResource("/timelineManager/view/AddTaskView.fxml"));
		} catch (IOException e) {
                    System.out.println("AddTaskView couldn't be loaded: " + e);
			pane = null;
		}
		Scene scene = new Scene(pane);
		//scene.getStylesheets().add(getClass().getResource("emaildetails.css").toExternalForm());
		return scene;
     
     
     }
    
}
