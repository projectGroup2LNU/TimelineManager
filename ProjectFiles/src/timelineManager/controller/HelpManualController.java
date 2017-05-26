/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timelineManager.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.web.WebView;
import timelineManager.helpClasses.DateViewer;
import timelineManager.helpClasses.TimelineViewer;
import timelineManager.model.Timeline;

/**
 *
 * @author beysimeryalmaz
 */
public class HelpManualController extends AbstractController implements Initializable {
    
    @FXML
    private WebView webView;
    
    
    public HelpManualController(ModelAccess modelAccess, TimelineViewer timelineViewer, TableView<Timeline> timelineTable, DateViewer dateViewer) {
        super(modelAccess, timelineViewer, timelineTable, dateViewer);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
            URL url = getClass().getResource("/timelineManager/view/htmlFiles/Help.html");
            webView.getEngine().load(url.toExternalForm());
           
    }
    
}