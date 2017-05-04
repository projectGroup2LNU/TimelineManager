package timelineManager.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.time.LocalDate;
import javafx.application.Platform;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
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
    
    
    String title,desc;
    
    LocalDate start,end;
    

    public AddTimelineController(ModelAccess modelAccess) {
        super(modelAccess);
    }
    
    
    public void addTheTimeline(ActionEvent e){
        title=titleField.getText();
        desc=descriptionField.getText();
        start=startDate.getValue();
        end=endDate.getValue();
        
        if(!title.isEmpty() && !desc.isEmpty()  && start!=null && end!=null && end.isAfter(start)){
            
            Timeline timeline=new Timeline(title,desc,start,end);
            getModelAccess().timelineModel.addTimelineToList(timeline);
            
            // If check is needed for JUnit tests
            if(e.getSource() != Event.NULL_SOURCE_TARGET) {
	            //It closes itself after user clicked Save button
	            final Node source = (Node) e.getSource();
	            final Stage stage = (Stage) source.getScene().getWindow();
	            stage.close();
            }
        }
        else{
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning ");
            alert.setHeaderText("Invalid or missing value");
            alert.setContentText("Please fill all areas!!\nStart date should be before end date");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        
        }
    }
    
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
    
}
