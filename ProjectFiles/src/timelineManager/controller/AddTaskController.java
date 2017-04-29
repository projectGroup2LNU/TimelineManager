package timelineManager.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import timelineManager.model.Task;
import timelineManager.model.Timeline;

/*This is a controller class for the AddTimelineView.
it holds the variables and logics needed to create a new timeline from GUI*/

public class AddTaskController extends AbstractController{   
	
	
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
	    
	   
            
            String title,desc,priority;
            LocalDate start,end;
            
            

    public AddTaskController(ModelAccess modelAccess) {
        super(modelAccess);
    }
	   
            
        public void addTheTask(ActionEvent e){
        title=titleField.getText();
        desc=descriptionField.getText();
        start=startDate.getValue();
        end=endDate.getValue();
       
        //This ugly if checks if if the duration of task equal or smaller than duration o ftimeline
        //also validetes the input
        if(!title.isEmpty() && !desc.isEmpty()  && start!=null && end!=null && end.isAfter(start) && ((getModelAccess().getSelectedTimeline().getStartTime().isBefore(start) ||getModelAccess().getSelectedTimeline().getStartTime().isEqual(start)) && (getModelAccess().getSelectedTimeline().getEndTime().isAfter(end) || getModelAccess().getSelectedTimeline().getEndTime().isEqual(end) )) ) {
            
            Task task=new Task(title, desc, start, end);
           
            getModelAccess().getSelectedTimeline().addTask(task);
            
            //It closes itself after user clicked Save button
            final Node source = (Node) e.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning ");
            alert.setHeaderText("Invalid or missing value");
            alert.setContentText("Please fill all areas!!\nStart date should be before end date");

            alert.showAndWait();
        
        }
            
    
    
    }
	    
	}

