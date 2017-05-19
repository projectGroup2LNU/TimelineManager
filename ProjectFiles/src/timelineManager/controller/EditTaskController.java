/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timelineManager.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import timelineManager.helpClasses.TimelineViewer;
import timelineManager.model.Task;
import timelineManager.model.Timeline;

/**
 *
 * @author beysimeryalmaz
 */
public class EditTaskController extends AbstractController implements Initializable {
    
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
    
    private boolean isTestMode = false; // Used for JUnit tests
    
    int indexOfTimeline,indexOfTask;
  
    String title,desc;
    
     Task task=getModelAccess().getSelectedTask();
     Timeline tm=getModelAccess().getSelectedTimeline();
    
    LocalDate start,oldStart,end,oldEnd;
    private Callback<DatePicker, DateCell> dayCellFactory;
    
    public EditTaskController(ModelAccess modelAccess, TimelineViewer timelineViewer)
    {
        super(modelAccess, timelineViewer);
    }
    
    
    public void editTask(ActionEvent e){
        title=titleField.getText();
        desc=descriptionField.getText();
        start=startDate.getValue();
        end=endDate.getValue();
        indexOfTimeline=getModelAccess().timelineModel.timelineList.indexOf(tm);

        try {
    		errorCheck(); // Throws exception if there's any invalid or missing information
            
            Task temp=new Task(title, desc, start, end);
             getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList.remove(indexOfTask);
             getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList.add(temp);
    
            timelineViewer.update(getModelAccess().timelineModel);
             /*
            getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList.get(indexOfTask).setTitle(title);
            getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList.get(indexOfTask).setDescription(desc);
            getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList.get(indexOfTask).setStartTime(start);
            getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList.get(indexOfTask).setEndTime(end);
               */

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
        timelineViewer.update(getModelAccess().timelineModel);
    }
    
    public void cancelTask(){
    	Stage stage = (Stage) cancelButton.getScene().getWindow();
    	stage.close();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        indexOfTask=tm.taskList.indexOf(task);
        
        if(indexOfTask<0){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning ");
            alert.setHeaderText("To be able to edit the task you should select the corret timeline!");
            alert.initModality(Modality.APPLICATION_MODAL);
            
            alert.showAndWait();
            
            saveButton.setDisable(true);
            
        }
        else{
            
            titleField.setText(task.getTitle());
            descriptionField.setText(task.getDescription());
            startDate.setValue(task.getStartTime());
            endDate.setValue(task.getEndTime());
            
            dayCellFactory = new Callback<DatePicker, DateCell>() {
        	public DateCell call(final DatePicker datePicker) {
        		return new DateCell() {
        			@Override public void updateItem(LocalDate item, boolean empty) {
        				super.updateItem(item, empty);
        				
        				if(item.isBefore(tm.getStartTime()) || item.isAfter(tm.getEndTime())) {
        					setDisable(true);
        				}
        			}
        		};
        	}
        };
        	
	 	startDate.setDayCellFactory(dayCellFactory);
		endDate.setDayCellFactory(dayCellFactory);
        }
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
    	} else if(end.isAfter(getModelAccess().getSelectedTimeline().getEndTime())) {
    		errorMessage = "Task end date cannot be after timeline end date";
    	} else if(start.isBefore(getModelAccess().getSelectedTimeline().getStartTime())) {
    		errorMessage = "Task start date cannot be before timeline start date";
    	} else {
    		errorFound = false;
    	}

    	if(errorFound) {
    		throw new RuntimeException(errorMessage);
    	}
    }  
}
