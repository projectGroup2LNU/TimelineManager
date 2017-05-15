package timelineManager.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import timelineManager.model.Timeline;

/**
 * This is a controller class for the AddTimelineView.
 * it holds the variables and logics needed to create a new timeline from GUI.
 * @author beysimeryalmaz
 */
public class AddTimelineController extends AbstractController{
    
    @FXML
    private JFXTextField titleField;

    @FXML
    private JFXDatePicker startDate;

    @FXML
    private JFXDatePicker endDate;

    @FXML
    private JFXTextArea descriptionField;

    @FXML
    private JFXButton saveButton;

    @FXML
    private JFXButton cancelButton;
    
    private boolean isTestMode = false; // Used for Junit tests
    
    String title,desc;
    LocalDate start,end;
    

    public AddTimelineController(ModelAccess modelAccess) {
        super(modelAccess);
       
    }
    
    public void addTheTimeline(ActionEvent e){
        title = titleField.getText();
        desc = descriptionField.getText();
        start = startDate.getValue();
        end = endDate.getValue();
        
        // Tries to create the timeline
        try {
            errorCheck(); // Throws exception if there's any invalid or missing information
           
            Timeline timeline = new Timeline(title,desc,start,end);
            getModelAccess().setSelectedTimeline(timeline);
            getModelAccess().timelineModel.addTimelineToList(timeline);
            
            // If check is needed for JUnit tests
            if(!isTestMode) {
	            // Window closes itself after user clicks the Save button
	            final Node source = (Node) e.getSource();
	            final Stage stage = (Stage) source.getScene().getWindow();
	            stage.close();
            }
        } catch (RuntimeException exception) {
        	if(!isTestMode) {
        		Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning ");
                alert.setHeaderText(exception.getMessage());
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.showAndWait();
        	} else {
        		throw exception;
        	}
        }
    }
    
    public void cancelTimeline(){
    	Stage stage = (Stage) cancelButton.getScene().getWindow();
    	stage.close();
    }
    
    // Setters
    public void setTitle(String title) {
    	titleField.setText(title);
    }
    
    public void setStartDate(LocalDate startDate) {
    	this.startDate.setValue(startDate);
    }
    
    public void setEndDate(LocalDate endDate) {
    	this.endDate.setValue(endDate);
    }
    
    public void setDescription(String description) {
    	descriptionField.setText(description);
    }
    
    public void setTestMode(boolean isTestMode){
    	this.isTestMode = isTestMode;
    }
	
    
    // Private methods
    // Checks for any invalid or missing information and throws and exception if found
    private void errorCheck() {
    	boolean errorFound = true;
    	String errorMessage = "";

    	if(title.trim().isEmpty()){
    		errorMessage = "Please select a title";
    	} else if(start == null){
    		errorMessage = "Please select start date";
    	} else if(end == null) {
    		errorMessage = "Please select end date";
    	} else if(end.isBefore(start)) {
    		errorMessage = "End date cannot be before start date";
    	} else {
    		errorFound = false;
    	}

    	if(errorFound) {
    		throw new RuntimeException(errorMessage);
    	}
    } 

}
