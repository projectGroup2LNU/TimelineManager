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
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
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

	int indexOfTimeline;
	Timeline tm;
	String title,desc;

	LocalDate start,oldStart,end,oldEnd;

	public EditTimelineController(ModelAccess modelAccess) {
		super(modelAccess);
	}

	public void editTimeline(ActionEvent e){
		title = titleField.getText();
		desc = descriptionField.getText();
		start = startDate.getValue();
		end = endDate.getValue();

		// Tries to edit the timeline
		try {
			errorCheck(); // Throws exception if there's any invalid or missing information

			ObservableList<Task> taskList = getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList;
			ObservableList<Task> newTaskList = FXCollections.observableArrayList();

			long diffStart = DAYS.between(oldStart, start);
			long diffEnd = DAYS.between(oldEnd, end);
			boolean isInBoundries = true;
			boolean isDurationTheSame = diffEnd == diffStart;
			boolean isCut = false;

			for(int i = 0; i < taskList.size(); i++){
				if(taskList.get(i).getStartTime().isBefore(start)){
					isCut=true;
				}
				if(taskList.get(i).getEndTime().isAfter(end)){
					isCut=true;
				}
			}

			// Checks if any task has a start time after the timeline end || any task as an end time before the timeline start
			for(int i = 0; i < taskList.size(); i++){
				if(taskList.get(i).getStartTime().isAfter(end) || taskList.get(i).getEndTime().isBefore(start)) {
					isInBoundries = false;
				} else {
					newTaskList.add(taskList.get(i));
				}
			}

			if(isDurationTheSame == true || taskList.isEmpty()){
				for(int i = 0; i < taskList.size(); i++) {
					LocalDate newStartforTask = taskList.get(i).getStartTime().plus(diffStart, DAYS);
					taskList.get(i).setStartTime(newStartforTask);
					LocalDate newEndforTask = taskList.get(i).getEndTime().plus(diffEnd, DAYS);
					taskList.get(i).setEndTime(newEndforTask);
				}

				Timeline tempTimeline = new Timeline(title, desc, start, end);

				tempTimeline.taskList = taskList;
				getModelAccess().timelineModel.timelineList.remove(indexOfTimeline);
				getModelAccess().timelineModel.timelineList.add(tempTimeline);
				getModelAccess().setSelectedTimeline(tempTimeline);

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

					Timeline tempTimeline = new Timeline(title, desc, start, end);
					tempTimeline.taskList = taskList;

					for(int i = 0; i < taskList.size(); i++){
						if(taskList.get(i).getStartTime().isBefore(start)){
							taskList.get(i).setStartTime(start);
						}
						if(taskList.get(i).getEndTime().isAfter(end)){
							taskList.get(i).setEndTime(end);
						}
					}

					getModelAccess().timelineModel.timelineList.remove(indexOfTimeline);
					getModelAccess().timelineModel.timelineList.add(tempTimeline);
					getModelAccess().setSelectedTimeline(tempTimeline);

				} else if (result.get() == CANCEL) {
				}    	

			} else if(!isCut){
				taskList = newTaskList;

				Timeline tempTimeline = new Timeline(title, desc, start, end);
				tempTimeline.taskList = taskList;

				getModelAccess().timelineModel.timelineList.remove(indexOfTimeline);
				getModelAccess().timelineModel.timelineList.add(tempTimeline);
				getModelAccess().setSelectedTimeline(tempTimeline);

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

					Timeline tempTimeline = new Timeline(title, desc, start, end);
					tempTimeline.taskList = taskList;

					for(int i = 0; i < tempTimeline.taskList.size(); i++){
						if(tempTimeline.taskList.get(i).getStartTime().isBefore(start)){
							tempTimeline.taskList.get(i).setStartTime(start);
						}
						if(tempTimeline.taskList.get(i).getEndTime().isAfter(end)){
							tempTimeline.taskList.get(i).setEndTime(end);
						}
					}

					getModelAccess().timelineModel.timelineList.remove(indexOfTimeline);
					getModelAccess().timelineModel.timelineList.add(tempTimeline);
					getModelAccess().setSelectedTimeline(tempTimeline);
				}
			}

			// Window closes itself after user clicks the Save button
			final Node source = (Node) e.getSource();
			final Stage stage = (Stage) source.getScene().getWindow();
			stage.close(); 

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
		Timeline timeLine = getModelAccess().getSelectedTimeline();
		oldEnd = timeLine.getEndTime();
		oldStart = timeLine.getStartTime();
		titleField.setText(timeLine.getTitle());
		indexOfTimeline = getModelAccess().timelineModel.timelineList.indexOf(timeLine);
		descriptionField.setText(timeLine.getDescription());
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
