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
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import timelineManager.helpClasses.DateViewer;
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
    public EditTaskController(ModelAccess modelAccess, TimelineViewer timelineViewer, DateViewer dateViewer, TableView<Timeline> timelineTable)
    {
        super(modelAccess, timelineViewer, timelineTable, dateViewer);
    }
    
    /**
     * This function edits a task.
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
        

        if(!errorCheck()){
             getDatabaseConnection();
            Task taskInChange= getModelAccess().getSelectedTask();
            taskInChange. setTitle(title);
            taskInChange.setDescription(description);
            taskInChange.setStartTime(start);                                                                                                                                                                                                                           
            taskInChange.setEndTime(end);

            int timelineId = (int)taskInChange.getTimeline().getId();
            //
            System.out.println(taskInChange.getId());
            getModelAccess().database.deleteTaskByTaskID((int) taskInChange.getId());
            getModelAccess().database.addTask((int) taskInChange.getId(), title, description, start.toString(), end.toString(),timelineId );

            getModelAccess().database.getConnection().close();
            
            getTimelineViewer().update(getModelAccess().timelineModel);
            
        	// If check is needed for JUnit tests
    		if(!isTestMode) {
    			// Window closes itself after user clicks the Save button
    			final Node source = (Node) e.getSource();
    			final Stage stage = (Stage) source.getScene().getWindow();
    			stage.close();
    		}
            
        }
    		
    	
        getTimelineViewer().update(getModelAccess().timelineModel);
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
    private boolean errorCheck() {
        boolean errorFound = true;
    
        if(title.trim().isEmpty()) {
            Tooltip tooltip = new Tooltip("Please insert title");
            setTooltipStyle(tooltip);
            titleField.setStyle("-fx-border-color: orangered;"+"-fx-border-width: 3;");
            titleField.setTooltip(tooltip);
        
        } else if (start == null) {
            Tooltip tooltip = new Tooltip("Select start date");
            setTooltipStyle(tooltip);
            startDate.setStyle("-fx-border-color: orangered;"+"-fx-border-width: 3;");
            startDate.setTooltip(tooltip);
        } else if (end == null) {
            Tooltip tooltip = new Tooltip("Select end date");
            setTooltipStyle(tooltip);
            endDate.setStyle("-fx-border-color: orangered;"+"-fx-border-width: 3;");
            endDate.setTooltip(tooltip);
        } else if(end.isBefore(start)) {
            Tooltip tooltip = new Tooltip("End date cannot be before start date");
            setTooltipStyle(tooltip);
            endDate.setStyle("-fx-border-color: orangered;"+"-fx-border-width: 3;");
            endDate.setTooltip(tooltip);
        } else {
            errorFound = false;
        }
    
        return errorFound;
    }
    
    /**
     * This method set style to red (error message) of the input tooltip
     * @param tooltip the Tooltip to be edited
     */
    private void setTooltipStyle(Tooltip tooltip)
    {
        tooltip.setStyle("-fx-background: rgb(30,30,30);" +
                "-fx-background-color: rgba(255,0,0,0.3);" +
                "-fx-text-fill: orange;" +
                "-fx-background-radius: 6px;" +
                "-fx-background-insets: 0;" +
                "-fx-padding: 0.667em 0.75em 0.667em 0.75em;" +
                "-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.5) , 10, 0.0 , 0 , 3 );" +
                "-fx-font-size: 0.85em;");
    }
}
