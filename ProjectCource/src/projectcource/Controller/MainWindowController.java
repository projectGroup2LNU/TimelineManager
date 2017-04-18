/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectcource.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import projectcource.View.ViewFactory;

/**
 *
 * @author beysimeryalmaz
 */
public class MainWindowController {
    
    @FXML
    private JFXDatePicker MainWindowDatePicker;

    @FXML
    private JFXButton todaysDateButton;

    @FXML
    private JFXButton addTimelineButton;

    @FXML
    private JFXButton AddTaskButton;

    @FXML
    private JFXButton goLeftButton;

    @FXML
    private JFXButton goRightButton;
    
     //date of the day
    private LocalDate currentDate = LocalDate.now();
       
    public void openAddTimelineWindow(ActionEvent e){
        ViewFactory viewFactory=new ViewFactory();
        Scene scene = viewFactory. getAddTimelineScene();
        Stage stage=new Stage();
        stage.setScene(scene);
        stage.show();
    
    }
    
    //to be connected to the right button, changing the date to a week ahead
    public void goRight(){
    	currentDate = currentDate.plus(7,ChronoUnit.DAYS);
    }
    
    //to be connected to the left button, changing the date to a week back
    public void goLeft(){
    	currentDate = currentDate.minus(7, ChronoUnit.DAYS);
    }
    
    //to be connected to a reset button, that will change the date back to the actual date of the day
    public void resetDate(){
    	currentDate = LocalDate.now();
    }
   
    
    
}
