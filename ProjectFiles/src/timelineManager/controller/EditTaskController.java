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
import java.sql.SQLException;
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
 * This is a controller class for an FXML window which is used to edit Tasks
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
  
    String title,description;
    
     Task task=getModelAccess().getSelectedTask();
    
    
    LocalDate start,oldStart,end,oldEnd;
    private Callback<DatePicker, DateCell> dayCellFactory;
    
    /**
     * Constructor that makes access to the Model and TimelineViewer
     * @param modelAccess conection to the model
     * @param timelineViewer logics for printing timelines in GUI
     */
    public EditTaskController(ModelAccess modelAccess, TimelineViewer timelineViewer)
    {
        super(modelAccess, timelineViewer);
    }
    
    /**
     * This function edits an task.
     * @param e actionEvent
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws Exception
     */
    public void editTask(ActionEvent e) throws ClassNotFoundException, SQLException, Exception{
        title=titleField.getText();
        description=descriptionField.getText();
        start=startDate.getValue();
        end=endDate.getValue();
        

        try {
    		errorCheck(task); // Throws exception if there's any invalid or missing information
             getDatabaseConnection();
            Task taskInChange= getModelAccess().getSelectedTask();
            taskInChange. setTitle(title);
            taskInChange.setDescription(description);
            taskInChange.setStartTime(start);
            taskInChange.setEndTime(end);
            int timelineId = (int)taskInChange.getId();
            getModelAccess().database.deleteTaskByTaskID((int) getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList.get(indexOfTask).getId());
            getModelAccess().database.addTask((int) taskInChange.getId(), title, description, start.toString(), end.toString(),timelineId );
            getModelAccess().database.getConnection().close();
            
            timelineViewer.update(getModelAccess().timelineModel);
            
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
        
        titleField.setText(task.getTitle());
        descriptionField.setText(task.getDescription());
        startDate.setValue(task.getStartTime());
        endDate.setValue(task.getEndTime());
        
        dayCellFactory = new Callback<DatePicker, DateCell>() {
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        
                        if(item.isBefore(task.getTimeline().getStartTime()) || item.isAfter(task.getTimeline().getEndTime())) {
                            setDisable(true);
                        }
                    }
                };
            }
        };
        
        startDate.setDayCellFactory(dayCellFactory);
        endDate.setDayCellFactory(dayCellFactory);
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
    private void errorCheck(Task task) {
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
    	} else if(end.isAfter(task.getTimeline().getEndTime())) {
    		errorMessage = "Task end date cannot be after timeline end date";
    	} else if(start.isBefore(task.getTimeline().getStartTime())) {
    		errorMessage = "Task start date cannot be before timeline start date";
    	} else {
    		errorFound = false;
    	}

    	if(errorFound) {
    		throw new RuntimeException(errorMessage);
    	}
    }  
}
