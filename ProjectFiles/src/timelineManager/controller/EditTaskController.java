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
    
    
    int indexOfTimeline,indexOfTask;
  
    String title,desc;
    
     Task task=getModelAccess().getSelectedTask();
     Timeline tm=getModelAccess().getSelectedTimeline();
    
    LocalDate start,oldStart,end,oldEnd;
    private Callback<DatePicker, DateCell> dayCellFactory;
    
    public EditTaskController(ModelAccess modelAccess) {
        super(modelAccess);
    }
    
    
    
    public void editTask(ActionEvent e){
        title=titleField.getText();
        desc=descriptionField.getText();
        start=startDate.getValue();
        end=endDate.getValue();
        indexOfTimeline=getModelAccess().timelineModel.timelineList.indexOf(tm);
          
    
        if(!title.isEmpty() && start!=null && end!=null && (end.isAfter(start) || end.isEqual(start)) && ((tm.getStartTime().isBefore(start) ||tm.getStartTime().isEqual(start)) && (tm.getEndTime().isAfter(end) || tm.getEndTime().isEqual(end) )) ){
            
            Task temp=new Task(title, desc, start, end);
             getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList.remove(indexOfTask);
             getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList.add(temp);
             
             /*
            getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList.get(indexOfTask).setTitle(title);
            getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList.get(indexOfTask).setDescription(desc);
            getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList.get(indexOfTask).setStartTime(start);
            getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList.get(indexOfTask).setEndTime(end);
               */

          
          
          
            
            
            // If check is needed for JUnit tests
           
	            //It closes itself after user clicked Save button
	            final Node source = (Node) e.getSource();
	            final Stage stage = (Stage) source.getScene().getWindow();
	            stage.close();
            
        }
	    
	else if (title.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning ");
            alert.setHeaderText("Please select a title");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        
        }
        
        else if (start==null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning ");
            alert.setHeaderText("Please select start date ");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        
        }
        
        else if (end==null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning ");
            alert.setHeaderText("Please select end date ");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        
        }
        
        else if (!(end.isAfter(start)|| end.isEqual(start))){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning ");
            alert.setHeaderText("End date cannot be before start date ");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        
        }
	    
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning ");
            alert.setHeaderText("Invalid or missing value");
            alert.setContentText("Please fill all areas!!\nStart date should be before end date");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        
        }
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
    
    
    
    public void cancelTask(){
    	Stage stage = (Stage) cancelButton.getScene().getWindow();
    	stage.close();
    }
    
    
    
    
    
            
    
   
    
    
}
