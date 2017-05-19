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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import timelineManager.helpClasses.TimelineViewer;
import timelineManager.model.Task;
import timelineManager.model.Timeline;

/**
 *
 * @author beysimeryalmaz
 */
public class EditTimelineController extends AbstractController implements Initializable {
    
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
    
    int indexOfTimeline,oldId;
    
    String title,desc;
    
    LocalDate start,oldStart,end,oldEnd;

    public EditTimelineController(ModelAccess modelAccess, TimelineViewer timelineViewer) {
        super(modelAccess, timelineViewer);
    }
    
    public void editTimeline(ActionEvent e) throws SQLException, Exception{
        title = titleField.getText();
        desc = descriptionField.getText();
        start = startDate.getValue();
        end = endDate.getValue();
      
		// Tries to edit the timeline
		try {
			errorCheck(); // Throws exception if there's any invalid or missing information
			
			ObservableList<Task> taskList = getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList;
			ObservableList<Task> newTaskList = FXCollections.observableArrayList();
           
            long diffStart =  DAYS.between(oldStart, start);
            long diffEnd =  DAYS.between(oldEnd, end);
            
            boolean isInBoundries = true;
            boolean isDurationTheSame = false;
            boolean isCut = false;
            
            
            for(int k = 0;k<taskList.size();k++){
            	if(taskList.get(k).getStartTime().isBefore(start)){
            		isCut = true;
            	}
            	if(taskList.get(k).getEndTime().isAfter(end)){
            		isCut = true;
            	}
            }
            
            if(diffEnd == diffStart){
                isDurationTheSame = true;
            }
            
            //Checks for any overlaps
            for(int i = 0; i < taskList.size(); i++){
            	if(taskList.get(i).getStartTime().isAfter(end) || taskList.get(i).getEndTime().isBefore(start) ){
            		isInBoundries = false;
            	} else {
            		newTaskList.add(taskList.get(i));
            	}
            }

            if(isDurationTheSame == true || taskList.isEmpty()){
            	for(int i = 0; i < taskList.size(); i++){
            		LocalDate newStartforTask = taskList.get(i).getStartTime().plus(diffStart, DAYS);
            		taskList.get(i).setStartTime(newStartforTask);
            		LocalDate newEndforTask = taskList.get(i).getEndTime().plus(diffEnd, DAYS);
            		taskList.get(i).setEndTime(newEndforTask);
            	}

            	Timeline temp = new Timeline(title, desc, start, end);

            	temp.taskList = taskList;
            	getModelAccess().timelineModel.timelineList.remove(indexOfTimeline);
            	getModelAccess().timelineModel.timelineList.add(temp);
            	
                  editTimelinewithTasksInDB(temp,oldId);
                  getModelAccess().setSelectedTimeline(temp);
                  timelineViewer.update(getModelAccess().timelineModel);
                  

            } else if(!isInBoundries){
            	
            	Alert alert = new Alert(AlertType.CONFIRMATION);
            	alert.setTitle("Timeline Edit");
            	alert.setHeaderText("Timeline edit will affect it's tasks.");
            	alert.setContentText("If you click delete, program will delete the task which completly will be out after your changes. \n If you dont want to loose any task please click cancel and manually edit tasks first!");
            	ButtonType DELETE = new ButtonType("Delete");
            	ButtonType CANCEL = new ButtonType("Cancel");
            	alert.getButtonTypes().setAll(DELETE,CANCEL);

            	Optional<ButtonType> result = alert.showAndWait();

            	if (result.get() == DELETE){
            		taskList = newTaskList;

            		Timeline temp = new Timeline(title, desc, start, end);
            		temp.taskList = taskList;
            		
            		for(int i = 0; i < taskList.size(); i++){
            			if(taskList.get(i).getStartTime().isBefore(start)){
            				taskList.get(i).setStartTime(start);
            			}
            			if(taskList.get(i).getEndTime().isAfter(end)){
            				taskList.get(i).setEndTime(end);
            			}

            		}
            		
            		getModelAccess().timelineModel.timelineList.remove(indexOfTimeline);

            		getModelAccess().timelineModel.timelineList.add(temp);
                        editTimelinewithTasksInDB(temp,oldId);
                        getModelAccess().setSelectedTimeline(temp);

            		timelineViewer.update(getModelAccess().timelineModel);
  

            	} else if (result.get() == CANCEL) {
            	}

            } else if(!isCut){

            	taskList = newTaskList;
            	Timeline temp = new Timeline(title, desc, start, end);
            	temp.taskList = taskList;
            	
            
            	getModelAccess().timelineModel.timelineList.remove(indexOfTimeline);
            	getModelAccess().timelineModel.timelineList.add(temp);
                editTimelinewithTasksInDB(temp,oldId);
                getModelAccess().setSelectedTimeline(temp);

            	timelineViewer.update(getModelAccess().timelineModel);

            	
            } else {
            	Alert alert = new Alert(AlertType.CONFIRMATION);
            	alert.setTitle("Timeline Edit");
            	alert.setHeaderText("Timeline edit will affect it's tasks.");
            	alert.setContentText("Some tasks are partly outside of new dates!\nIf you continue, program will cut off the dates which don't match");
            	ButtonType EDIT = new ButtonType("Edit Anyway");
            	ButtonType CANCEL = new ButtonType("Cancel");
            	alert.getButtonTypes().setAll(EDIT,CANCEL);

            	Optional<ButtonType> result = alert.showAndWait();

            	if(result.get() == EDIT){
            		taskList = newTaskList;
            		Timeline temp = new Timeline(title, desc, start, end);
            		temp.taskList = taskList;
            		
            		for(int i = 0; i < temp.taskList.size(); i++){
            			if(temp.taskList.get(i).getStartTime().isBefore(start)){
            				temp.taskList.get(i).setStartTime(start);
            			}
            			if(temp.taskList.get(i).getEndTime().isAfter(end)){
            				temp.taskList.get(i).setEndTime(end);
            			}
            		}

            	
            		getModelAccess().timelineModel.timelineList.remove(indexOfTimeline);
            		getModelAccess().timelineModel.timelineList.add(temp);
                        editTimelinewithTasksInDB(temp,oldId);
                        getModelAccess().setSelectedTimeline(getModelAccess().timelineModel.timelineList.get(getModelAccess().timelineModel.timelineList.indexOf(temp)));

            		timelineViewer.update(getModelAccess().timelineModel);
            	}
            }
            // If check is needed for JUnit tests
            if(!isTestMode) {
            	// // Window closes itself after user clicks the Save button
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
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getModelAccess().getSelectedTimeline();
        oldId=(int) getModelAccess().getSelectedTimeline().getId();
        oldEnd = getModelAccess().getSelectedTimeline().getEndTime();
        oldStart = getModelAccess().getSelectedTimeline().getStartTime();
        titleField.setText(getModelAccess().getSelectedTimeline().getTitle());
        indexOfTimeline = getModelAccess().timelineModel.timelineList.indexOf(getModelAccess().getSelectedTimeline());
        descriptionField.setText(getModelAccess().getSelectedTimeline().getDescription());
        startDate.setValue(getModelAccess().getSelectedTimeline().getStartTime());
        endDate.setValue(getModelAccess().getSelectedTimeline().getEndTime());
        
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
        
        protected void editTimelinewithTasksInDB(Timeline temp,int OldId) throws ClassNotFoundException, SQLException, Exception{
                    //Connects to Db
                    getModelAccess().database.connectToDatabase();
                
                    getModelAccess().database.deleteTimeLineByID(OldId);
                    getModelAccess().database.deleteAllTaskByID( OldId);
                
                    int newId=(int) temp.getId();
                    getModelAccess().database.addTimeLine(newId, title, desc, start.toString(), end.toString());
                    for (int i = 0; i < temp.taskList.size(); i++) {
                        String Tasktitle=temp.taskList.get(i).getTitle();
                        String Taskdesc=temp.taskList.get(i).getDescription();
                        LocalDate Taskstart=temp.taskList.get(i).getStartTime();
                        LocalDate Taskend=temp.taskList.get(i).getEndTime();
                        System.out.println(Tasktitle+" "+ Taskdesc);
                        getModelAccess().database.addTask(newId, Tasktitle, Taskdesc, Taskstart.toString(), Taskend.toString(), newId);
                    }
                    
                    //closes the connection
                    getModelAccess().database.getConnection().close();
                    
    
}
   
}
