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
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import timelineManager.helpClasses.DateViewer;
import timelineManager.helpClasses.TimelineViewer;
import timelineManager.model.Task;
import timelineManager.model.Timeline;

/**
 *
 * @author beysimeryalmaz
 */
public class MoveTimelineController extends AbstractController implements Initializable {
    
    
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
	
	String title,description;
	
	LocalDate start,oldStart,end,oldEnd;
    
    public MoveTimelineController(ModelAccess modelAccess, TimelineViewer timelineViewer, TableView<Timeline> timelineTable, DateViewer dateViewer) {
        super(modelAccess, timelineViewer, timelineTable, dateViewer);
    }

   
    public void moveTimeline(ActionEvent e) throws SQLException, Exception{
        
        
                oldStart = LocalDate.from(getModelAccess().getSelectedTimeline().getStartTime());
		oldEnd = LocalDate.from(getModelAccess().getSelectedTimeline().getEndTime());
		title = getModelAccess().getSelectedTimeline().getTitle();
		description = getModelAccess().getSelectedTimeline().getDescription();
		start = startDate.getValue();
		end = endDate.getValue();
		
		// Tries to edit the timeline
		try {
			errorCheck(); // Throws exception if there's any invalid or missing information
			
			ObservableList<Task> taskList = getModelAccess().getSelectedTimeline().taskList;
			long diffStart =  DAYS.between(oldStart, start);
			long diffEnd =  DAYS.between(oldEnd, end);
			boolean isInBoundries = true;
			boolean isDurationTheSame = false;
			boolean isCut = false;
			
			if(diffEnd == diffStart){
				isDurationTheSame = true;
			}
			
			
            if(isDurationTheSame == true || taskList.isEmpty()){
            	for(int i = 0; i < taskList.size(); i++){
            		LocalDate newStartforTask = taskList.get(i).getStartTime().plus(diffStart, DAYS);
            		taskList.get(i).setStartTime(newStartforTask);
            		LocalDate newEndforTask = taskList.get(i).getEndTime().plus(diffEnd, DAYS);
            		taskList.get(i).setEndTime(newEndforTask);
            	}

            	Timeline temp = super.getModelAccess().getSelectedTimeline();
		temp.setTitle(title);
		temp.setDescription(description);
		temp.setStartTime(start);
		temp.setEndTime(end);

            	temp.taskList = taskList;
            	
            	getModelAccess().setSelectedTimeline(temp);
                 editTimelinewithTasksInDB(temp);
                  
                  super.getTimelineViewer().update(getModelAccess().timelineModel);
               }
            
            else{
                Alert alert=new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Moving Edit");
		alert.setHeaderText("Move Error");
		alert.setContentText("Duration should remain same when moving the timeline with its tasks\n Please set correct dates!");
                alert.initModality(Modality.APPLICATION_MODAL);
		alert.showAndWait();
          }
			
			// if no tasks was affected by resize
                    
			// If check is needed for JUnit tests
			if(!isTestMode) {
				// // Window closes itself after user clicks the Save button
				final Node source = (Node) e.getSource();
				final Stage stage = (Stage) source.getScene().getWindow();
				stage.close();
			}
			
		} catch (RuntimeException exception) {
			if(!isTestMode) {
				exception.printStackTrace();
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


        private void errorCheck() {
                        boolean errorFound = true;
                        String errorMessage = "";

                        if(start == null){
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
        
        
        protected void editTimelinewithTasksInDB(Timeline timeline) throws ClassNotFoundException, SQLException, Exception{
		//Connects to Db
		getModelAccess().database.connectToDatabase();
		
		getModelAccess().database.deleteTimeLineByID((int)timeline.getId());
		getModelAccess().database.deleteAllTaskByID((int)timeline.getId());
		
		getModelAccess().database.addTimeLine((int)timeline.getId(), title, description, start.toString(), end.toString());
		for (int i = 0; i < timeline.taskList.size(); i++) {
			String Tasktitle=timeline.taskList.get(i).getTitle();
			String Taskdesc=timeline.taskList.get(i).getDescription();
			LocalDate Taskstart=timeline.taskList.get(i).getStartTime();
			LocalDate Taskend=timeline.taskList.get(i).getEndTime();
			getModelAccess().database.addTask((int)timeline.taskList.get(i).getId(), timeline.taskList.get(i).getTitle(), timeline.taskList.get(i).getDescription(), timeline.taskList.get(i).getStartTime().toString(), timeline.taskList.get(i).getEndTime().toString(),(int)timeline.getId() );
		}
		
		//closes the connection
		getModelAccess().database.getConnection().close();
	}
        
        
        @Override
	public void initialize(URL location, ResourceBundle resources) {
		getModelAccess().getSelectedTimeline();
		oldId=(int) getModelAccess().getSelectedTimeline().getId();
		oldEnd = LocalDate.from(getModelAccess().getSelectedTimeline().getEndTime());
		oldStart = LocalDate.from(getModelAccess().getSelectedTimeline().getStartTime());
		titleField.setText(getModelAccess().getSelectedTimeline().getTitle());
		indexOfTimeline = getModelAccess().timelineModel.timelineList.indexOf(getModelAccess().getSelectedTimeline());
		descriptionField.setText(getModelAccess().getSelectedTimeline().getDescription());
		startDate.setValue(getModelAccess().getSelectedTimeline().getStartTime());
		endDate.setValue(getModelAccess().getSelectedTimeline().getEndTime());
                titleField.setDisable(true);
                descriptionField.setDisable(true);
                long duration=DAYS.between(oldStart, oldEnd);
                
               startDate.valueProperty().addListener((ov, oldValue, newValue) -> {
                    endDate.setValue(newValue.plusDays(duration) );
                });
               
               
		
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
        
        
        public void cancelTimeline(){
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}

        private void disableDates() {
    	Callback<DatePicker, DateCell> dayCellFactory;
    	
		LocalDate newStart=startDate.getValue();
    	
        dayCellFactory = new Callback<DatePicker, DateCell>() {
        	public DateCell call(final DatePicker datePicker) {
        		return new DateCell() {
        			@Override public void updateItem(LocalDate item, boolean empty) {
        				super.updateItem(item, empty);
        				
        				if(item.isBefore(newStart)) {
        					setDisable(true);
        				}
        			}
        		};
        	}
        };
        	
	 	
		endDate.setDayCellFactory(dayCellFactory);
    }
    
}
