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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import timelineManager.helpClasses.TimelineViewer;
import timelineManager.model.Task;
import timelineManager.model.Timeline;

/**
 * A controller class for editing the timeline.
 * A timeline can be edited by either no tasks are affected,
 * tasks will be cut of to fit new timeline dates
 * or tasks will be removed since completely outside new timeline dates.
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
	
	String title,description;
	
	LocalDate start,oldStart,end,oldEnd;
	
	/**
	 * Constructor which takes the model acess and timeline viewer as inputs.
	 * @param modelAccess an access Class to the Model
	 * @param timelineViewer TimelineView is printing the timelines to GUI
	 */
	public EditTimelineController(ModelAccess modelAccess, TimelineViewer timelineViewer) {
		super(modelAccess, timelineViewer);
	}
	
	public void editTimeline(ActionEvent e) throws SQLException, Exception{
		oldStart = LocalDate.from(getModelAccess().getSelectedTimeline().getStartTime());
		oldEnd = LocalDate.from(getModelAccess().getSelectedTimeline().getEndTime());
		title = titleField.getText();
		description = descriptionField.getText();
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
			
			
			for(int k = 0;k<taskList.size();k++){
				// tcheck if cutof part of tasks
				if(taskList.get(k).getStartTime().isBefore(start)){
					isCut = true;
				}
				if(taskList.get(k).getEndTime().isAfter(end)){
					isCut = true;
				}
				//Checks for any overlaps
				if(taskList.get(k).getStartTime().isAfter(end) || taskList.get(k).getEndTime().isBefore(start) ){
					isInBoundries = false;
				}
			}
			
			if(diffEnd == diffStart){
				isDurationTheSame = true;
			}
			
			// THIS PART SHOULD BE MOVED TO A "MOVE" FUNCTION
            /*if(isDurationTheSame == true || taskList.isEmpty()){
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
               */
			
			// if no tasks was affected by resize
			if(!isCut)
			{
				Timeline timelineInChange = getModelAccess().getSelectedTimeline();
				timelineInChange.setTitle(title);
				timelineInChange.setDescription(description);
				timelineInChange.setStartTime(start);
				timelineInChange.setEndTime(end);
				editTimelinewithTasksInDB(timelineInChange);   // would be enough to only edit timeline in database in this case
				timelineViewer.update(getModelAccess().timelineModel);
				
			}
			// if at least a task is completely outside of new timeline dates
			else if(!isInBoundries)
			{
				
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Timeline Edit");
				alert.setHeaderText("Timeline edit will affect it's tasks.");
				alert.setContentText("If you click delete, program will delete the task which completly will be out after your changes. \n If you dont want to loose any task please click cancel and manually edit tasks first!");
				ButtonType DELETE = new ButtonType("Delete");
				ButtonType CANCEL = new ButtonType("Cancel");
				alert.getButtonTypes().setAll(DELETE,CANCEL);
				
				Optional<ButtonType> result = alert.showAndWait();
				
				if (result.get() == DELETE){
					
					Timeline timelineInChange = super.getModelAccess().getSelectedTimeline();
					timelineInChange.setTitle(title);
					timelineInChange.setDescription(description);
					timelineInChange.setStartTime(start);
					timelineInChange.setEndTime(end);
					ArrayList<Task> removeList = new ArrayList<>();
					for(int i = 0; i < taskList.size(); i++)
					{
						if(timelineInChange.getStartTime().isAfter(timelineInChange.taskList.get(i).getEndTime())
								|| timelineInChange.getEndTime().isBefore(timelineInChange.taskList.get(i).getStartTime()))
						{
							removeList.add(timelineInChange.taskList.get(i));
						}
						else
						{
							if(taskList.get(i).getStartTime().isBefore(start))
							{
								taskList.get(i).setStartTime(start);
							}
							if(taskList.get(i).getEndTime().isAfter(end))
							{
								taskList.get(i).setEndTime(end);
							}
						}
					}
					// removes the tasks that are completely outside the new timeline
					for(Task task : removeList)
					{
						timelineInChange.deleteTask(task);
					}
					editTimelinewithTasksInDB(timelineInChange);
					super.timelineViewer.update(getModelAccess().timelineModel);
					
					
				}
				else if (result.get() == CANCEL) {
				}
			}
			// if at least one task is party out of timeline dates.
			else
			{
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Timeline Edit");
				alert.setHeaderText("Timeline edit will affect it's tasks.");
				alert.setContentText("Some tasks are partly outside of new dates!\nIf you continue, program will cut off the dates which don't match");
				ButtonType EDIT = new ButtonType("Edit Anyway");
				ButtonType CANCEL = new ButtonType("Cancel");
				alert.getButtonTypes().setAll(EDIT,CANCEL);
				
				Optional<ButtonType> result = alert.showAndWait();
				
				if(result.get() == EDIT){
					Timeline timelineInChange = getModelAccess().getSelectedTimeline();
					timelineInChange.setTitle(title);
					timelineInChange.setDescription(description);
					timelineInChange.setStartTime(start);
					timelineInChange.setEndTime(end);
					
					for(int i = 0; i < timelineInChange.taskList.size(); i++){
						
						if(timelineInChange.taskList.get(i).getStartTime().isBefore(start)){
							timelineInChange.taskList.get(i).setStartTime(start);
						}
						if(timelineInChange.taskList.get(i).getEndTime().isAfter(end)){
							timelineInChange.taskList.get(i).setEndTime(end);
						}
					}
					
					editTimelinewithTasksInDB(timelineInChange);
					
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
	
	public void cancelTimeline(){
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
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
			getModelAccess().database.addTask((int)timeline.getId(), timeline.taskList.get(i).getTitle(), timeline.taskList.get(i).getDescription(), timeline.taskList.get(i).getStartTime().toString(), timeline.taskList.get(i).getEndTime().toString(), (int)timeline.taskList.get(i).getId());
		}
		
		//closes the connection
		getModelAccess().database.getConnection().close();
	}
	
}
