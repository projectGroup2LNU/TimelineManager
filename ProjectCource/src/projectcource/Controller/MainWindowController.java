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
    
    
    
    public void openAddTimelineWindow(ActionEvent e){
        ViewFactory viewFactory=new ViewFactory();
        Scene scene = viewFactory. getAddTimelineScene();
        Stage stage=new Stage();
        stage.setScene(scene);
        stage.show();
    
    }
    
    
    
}
