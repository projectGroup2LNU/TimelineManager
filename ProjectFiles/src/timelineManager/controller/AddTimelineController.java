package timelineManager.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Tooltip;
import javafx.stage.Modality;
import javafx.stage.Stage;
import timelineManager.helpClasses.TimelineViewer;
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
    
    String title;
    LocalDate start,end;
    String desc = "";
    

    public AddTimelineController(ModelAccess modelAccess, TimelineViewer timelineViewer) {
        super(modelAccess, timelineViewer);
    }
    
    public void addTheTimeline(ActionEvent e) throws ClassNotFoundException, SQLException, Exception {
    	title = titleField.getText();
    	desc = descriptionField.getText();
    	start = startDate.getValue();
    	end = endDate.getValue();

    	if(!errorCheck()) { // Checks if there's any missing or invalid information in the fields.
    		Timeline timeline = new Timeline(title,desc,start,end);
    		getModelAccess().setSelectedTimeline(timeline);
    		getModelAccess().timelineModel.addTimelineToList(timeline);

    		// If check is needed for JUnit tests
    		if(!isTestMode) {
    			super.timelineViewer.update(getModelAccess().timelineModel);
    			int id = (int) timeline.getId();
    			getDatabaseConnection();
    			getModelAccess().database.addTimeLine(id, title, desc, start.toString(), end.toString());
    			//closes the connection
    			getModelAccess().database.getConnection().close();

    			// Window closes itself after user clicks the Save button
    			final Node source = (Node) e.getSource();
    			final Stage stage = (Stage) saveButton.getScene().getWindow();
    			stage.close();
    		}
    	}
    } 
        	/*if(!isTestMode) {
        		Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning ");
                alert.setHeaderText(exception.getMessage());
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.showAndWait();
        	} else {
        		throw exception;
        	}
        }
    
            if(!isTestMode)
            {
                if(title.isEmpty())
                    titleField.setTooltip(new Tooltip("Please insert title"));
                else if(start == null)
                    startDate.setTooltip(new Tooltip("Select start date"));
                else if(end == null)
                    endDate.setTooltip(new Tooltip("Select end date"));
                else if(end.isBefore(start))
                    endDate.setTooltip(new Tooltip("End date cannot be before start date"));
            } else
                throw exception;
        }
        */

    
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
    
    // Getters
    public JFXTextField getTitleField() {
    	return titleField;
    }
    
    public JFXTextArea getDescField() {
    	return descriptionField;
    }
    
    public JFXDatePicker getStartDate() {
    	return startDate;
    }
    
    public JFXDatePicker getEndDate() {
    	return endDate;
    }
	
    
    // Private methods
    // Checks for any invalid or missing information and throws and exception if found
   /* private void errorCheck() {
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
    } */
    
    private boolean errorCheck() {
    	boolean errorFound = true;
    	
    	if(title.trim().isEmpty()) {
    		titleField.setStyle("-fx-border-color: orangered;"+"-fx-border-width: 3;");
    		titleField.setTooltip(new Tooltip("Please insert title"));
    	} else if (start == null) {
    		startDate.setStyle("-fx-border-color: orangered;"+"-fx-border-width: 3;");
    		startDate.setTooltip(new Tooltip("Select start date"));
    	} else if (end == null) {
    		endDate.setStyle("-fx-border-color: orangered;"+"-fx-border-width: 3;");
    		endDate.setTooltip(new Tooltip("Select end date"));
    	} else if(end.isBefore(start)) {
    		endDate.setStyle("-fx-border-color: orangered;"+"-fx-border-width: 3;");
    		endDate.setTooltip(new Tooltip("End date cannot be before start date"));
    	} else {
    		errorFound = false;
    	}
    	
    	return errorFound;
    }
}
