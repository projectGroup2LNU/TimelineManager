package timelineManager.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import timelineManager.view.ViewFactory;

/**
 * This class is the controller class for MainView.fxml
 * It holds all logics for user interface such as on screen buttons. 
 * @author beysimeryalmaz
 */
public class MainWindowController {
    
    @FXML
    private JFXDatePicker mainWindowDatePicker;

    @FXML
    private JFXButton todaysDateButton;

    @FXML
    private JFXButton addTimelineButton;

    @FXML
    private JFXButton addTaskButton;

    @FXML
    private JFXButton goLeftButton;

    @FXML
    private JFXButton goRightButton;
    
    
    
     //date of the day
    private LocalDate currentDate = LocalDate.now();
       
    public void openAddTimelineWindow(ActionEvent e){
        ViewFactory viewFactory=new ViewFactory();
        Scene scene = viewFactory. getAddTimelineScene();
        Stage stage=new Stage();
        stage.setResizable(false);
        stage.setTitle("Adding a new Timeline");
        stage.setScene(scene);
        stage.show();
    
    }
    
       
    //to be connected to the right button, changing the date to a week ahead
    public void goRight(){
    	currentDate = currentDate.plus(7,ChronoUnit.DAYS);
    	datePickerUpdate(currentDate);
    	
    	//Should be deleted later
    	System.out.println(currentDate.toString());
    }
    
    //to be connected to the left button, changing the date to a week back
    public void goLeft(){
    	currentDate = currentDate.minus(7, ChronoUnit.DAYS);
    	datePickerUpdate(currentDate);
    	
    	//Should be deleted later
    	System.out.println(currentDate.toString());    }
    
    //to be connected to a reset button, that will change the date back to the actual date of the day
    public void resetDate(){
    	currentDate = LocalDate.now();
    	datePickerUpdate(currentDate);
    	
    	//Should be deleted later
    	System.out.println(currentDate.toString());
    }
   
    /*to be connected to the choicebox where the user chooses the year, month, day.
     *  By doing so the current date will become the one he chose.
     */
    public void setDate(){
    	currentDate= mainWindowDatePicker.getValue();
    	datePickerUpdate(currentDate);
    	
    	//I have disabled this because it will print twice when value of date picker is set
    	//System.out.println(currentDate.toString());
    }
    
    
    public void datePickerUpdate(LocalDate inputDate){
    	// Null check is needed to be able to run JUnit tests
    	if(mainWindowDatePicker != null) {
    		mainWindowDatePicker.setValue(inputDate);
    	}
    }
    
    public LocalDate getCurrentDate() {
    	return currentDate;
    }
 
}
