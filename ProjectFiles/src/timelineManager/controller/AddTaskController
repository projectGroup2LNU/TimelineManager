package timelineManager.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

/*This is a controller class for the AddTimelineView.
it holds the variables and logics needed to create a new timeline from GUI*/

public class AddTaskController {   
	
	ObservableList<String> priorityList = FXCollections.observableArrayList("High","Medium","Low");
	
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
	    private JFXComboBox priorityBox;
	    
	    @FXML
	    private void initialize(){
	    	priorityBox.setItems(priorityList);
	    }
	    
	}

