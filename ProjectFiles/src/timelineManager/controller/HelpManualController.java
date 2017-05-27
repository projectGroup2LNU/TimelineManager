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
 * This class is used to show the Manual/Help in a window to the user.
 * @author beysimeryalmaz
 */
public class HelpManualController extends AbstractController implements Initializable {
    
    @FXML
    private WebView webView;
    
    /**
     * Constructor that makes access to the Model, TimelineViewer, DaveViewer and TimelineTable
     * @param modelAccess The model access of the Timeline Manager
     * @param timelineViewer The timelineViewer that shows all Timelines and tasks on the main window
     * @param timelineTable The timelineTable which shows all timelines as a list on the main window
     * @param dateViewer The dateviewer that shows all dates in the main window
     */
    public HelpManualController(ModelAccess modelAccess, TimelineViewer timelineViewer, TableView<Timeline> timelineTable, DateViewer dateViewer) {
        super(modelAccess, timelineViewer, timelineTable, dateViewer);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        URL url = getClass().getResource("/timelineManager/view/htmlFiles/Help.html");
        webView.getEngine().load(url.toExternalForm());
    }
}