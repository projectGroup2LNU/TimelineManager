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
    
    
    int indexOfTimeline;
    Timeline tm;
    String title,desc;
    
    
    LocalDate start,oldStart,end,oldEnd;

    public EditTimelineController(ModelAccess modelAccess) {
        super(modelAccess);
    }
    

    
    
    
    public void editTimeline(ActionEvent e){
        title=titleField.getText();
        desc=descriptionField.getText();
        start=startDate.getValue();
        end=endDate.getValue();
        boolean isInBoundries=true;
        boolean isDurationTheSame=false;
        boolean isCut=false;
      
        ObservableList<Task> newTaskList;
        
        if(!title.isEmpty() && start!=null && end!=null &&(end.isAfter(start)|| end.isEqual(start))){
           
            long diffStart= DAYS.between(oldStart, start);
            long diffEnd= DAYS.between(oldEnd, end);
            
            isInBoundries=true;
            isDurationTheSame=false;
            isCut=false;
            
            
            for(int k=0;k<getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList.size();k++){
                            if(getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList.get(k).getStartTime().isBefore(start)){
                                isCut=true;
                            }
                            if(getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList.get(k).getEndTime().isAfter(end)){
                                isCut=true;
                            }
                        
                        }
            
            
            if(diffEnd==diffStart){
                isDurationTheSame=true;
            }
            
            newTaskList=FXCollections.observableArrayList();
            
            
            //Checks for any overlaps
            for(int i=0;i<getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList.size();i++){
               if(getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList.get(i).getStartTime().isAfter(end) || getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList.get(i).getEndTime().isBefore(start) ){
                     isInBoundries=false;
                     
                     
               }else{
                   
                   newTaskList.add(getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList.get(i));
               }
            
            }
            
            
            
           if(isDurationTheSame==true || getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList.isEmpty()){
           for(int j=0;j<getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList.size();j++){
                    LocalDate newStartforTask=getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList.get(j).getStartTime().plus(diffStart, DAYS);
                    getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList.get(j).setStartTime(newStartforTask);
                    LocalDate newEndforTask=getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList.get(j).getEndTime().plus(diffEnd, DAYS);
                    getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList.get(j).setEndTime(newEndforTask);
            }
           
                    Timeline temp=new Timeline(title, desc, start, end);
                    
                    temp.taskList=getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList;
                    getModelAccess().timelineModel.timelineList.remove(indexOfTimeline);
                    getModelAccess().timelineModel.timelineList.add(temp);
                    getModelAccess().setSelectedTimeline(temp);
           
           }
           else if(!isInBoundries){
               
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Timeline Edit");
                    alert.setHeaderText("Timeline edit will affect it's tasks.");
                    alert.setContentText("If you click delete, program will delete the task which completly will be out after your changes. \n If you dont want to loose any task please click cancel and manually edit tasks first!");
                    ButtonType DELETE = new ButtonType("Delete");
                    ButtonType CANCEL = new ButtonType("Cancel");
                    alert.getButtonTypes().setAll(DELETE,CANCEL);
                     
                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.get() == DELETE){
                        getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList=newTaskList;
                        
                        Timeline temp=new Timeline(title, desc, start, end);
                    
                        temp.taskList=getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList;
                        for(int k=0;k<getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList.size();k++){
                            if(getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList.get(k).getStartTime().isBefore(start)){
                                getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList.get(k).setStartTime(start);
                            }
                            if(getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList.get(k).getEndTime().isAfter(end)){
                                getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList.get(k).setEndTime(end);
                            }
                        
                        }
                        getModelAccess().timelineModel.timelineList.remove(indexOfTimeline);
                        getModelAccess().timelineModel.timelineList.add(temp);
                        
                        getModelAccess().setSelectedTimeline(temp);
                        
                        
                        
                        
                        
                    } else if (result.get() == CANCEL) {
    
                    }
              
           }else if(!isCut){
               
               getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList=newTaskList;
                        
                        Timeline temp=new Timeline(title, desc, start, end);
                    
                        temp.taskList=getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList;
                        getModelAccess().timelineModel.timelineList.remove(indexOfTimeline);
                        getModelAccess().timelineModel.timelineList.add(temp);
                        
                        getModelAccess().setSelectedTimeline(temp);
           
           
           
           }
           
           else {
               
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Timeline Edit");
                    alert.setHeaderText("Timeline edit will affect it's tasks.");
                    alert.setContentText("Some tasks are partly outside of new dates!\nIf you continue, program will cut off the dates which don't match");
                    ButtonType EDIT = new ButtonType("Edit Anyway");
                    ButtonType CANCEL = new ButtonType("Cancel");
                    alert.getButtonTypes().setAll(EDIT,CANCEL);
                     
                    Optional<ButtonType> result = alert.showAndWait();
                    
                    if(result.get()==EDIT){
                    
                        
                        getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList=newTaskList;
                        
                        Timeline temp=new Timeline(title, desc, start, end);
                        temp.taskList=getModelAccess().timelineModel.timelineList.get(indexOfTimeline).taskList;
                        for(int k=0;k<temp.taskList.size();k++){
                            if(temp.taskList.get(k).getStartTime().isBefore(start)){
                                temp.taskList.get(k).setStartTime(start);
                            }
                            if(temp.taskList.get(k).getEndTime().isAfter(end)){
                                temp.taskList.get(k).setEndTime(end);
                            }
                        
                        }
                    
                        
                        getModelAccess().timelineModel.timelineList.remove(indexOfTimeline);
                        getModelAccess().timelineModel.timelineList.add(temp);
                        
                        getModelAccess().setSelectedTimeline(temp);

                        
                    
                    }
               
               
               
                  }
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Timeline tm=getModelAccess().getSelectedTimeline();
        oldEnd=tm.getEndTime();
        oldStart=tm.getStartTime();
        titleField.setText(tm.getTitle());
        indexOfTimeline=getModelAccess().timelineModel.timelineList.indexOf(tm);
        descriptionField.setText(tm.getDescription());
        
        
    }
    
}
