package timelineManager.view;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import timelineManager.controller.AbstractController;
import timelineManager.controller.AddTaskController;
import timelineManager.controller.AddTimelineController;
import timelineManager.controller.MainWindowController;
import timelineManager.controller.ModelAccess;

/**
 * This class is a helper class for loading the views from .fxml files.
 * Also initilazes controllers to the fxml files with common reference to ModelAccess Class
 * @author beysimeryalmaz
 */
public class ViewFactory {
    
    public static ViewFactory defaultFactory=new ViewFactory();
    
    private ModelAccess modelAccess=new ModelAccess();
    private AddTaskController addTaskController;
    private AddTimelineController addTimelineController;
    private MainWindowController mainWindowController;
    
    private final String MAIN_FXML="/timelineManager/view/MainView.fxml";
    private final String ADD_TIMELINE_FXML="/timelineManager/view/AddTimelineView.fxml";
    private final String ADD_TASK_FXML="/timelineManager/view/AddTaskView.fxml";
    
    
    
     public Scene getMainScene(){
	mainWindowController=new MainWindowController(modelAccess);
        return initilazeScene(MAIN_FXML, mainWindowController);
		
	}
    
     public Scene getAddTimelineScene(){
        addTimelineController=new AddTimelineController(modelAccess);
        return initilazeScene(ADD_TIMELINE_FXML, addTimelineController);
     
     
     }
	
	 public Scene getAddTaskScene(){
         addTaskController=new AddTaskController(modelAccess);
         return initilazeScene(ADD_TASK_FXML, addTaskController);
     
        }
         
         
         
         
         public Scene initilazeScene(String fxmlPath, AbstractController abstractController){
         FXMLLoader loader;
		Parent parent;
		Scene scene;
		try {
			loader = new FXMLLoader(getClass().getResource(fxmlPath));
			loader.setController(abstractController);
			parent = loader.load();
		} catch (Exception e) {
			return null;
		}
		
		scene = new Scene(parent);
		//scene.getStylesheets().add(getClass().getResource(DEFAULT_CSS).toExternalForm());
		return scene;
         }
    
}
