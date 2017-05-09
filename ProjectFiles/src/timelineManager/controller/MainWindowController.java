package timelineManager.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import timelineManager.helpClasses.DateViewer;
import timelineManager.helpClasses.TimelineViewer;
import timelineManager.model.Task;
import timelineManager.model.Timeline;
import timelineManager.model.TimelineModel;
import timelineManager.view.ViewFactory;

/**
 * This class is the controller class for MainView.fxml
 * It holds all logics for user interface such as on screen buttons.
 * @author beysimeryalmaz
 */
public class MainWindowController extends AbstractController implements Initializable{
    @FXML
    private TableView<Timeline> timelineTable;

    @FXML
    private TableColumn<Timeline, String> titleOfTable;
    
    @FXML
    private JFXDatePicker mainWindowDatePicker;
    
    @FXML
    private JFXButton todaysDateButton;
    
    @FXML
    private JFXButton addTimelineButton;
    
    @FXML
    private JFXButton addTaskButton;
    
    @FXML
    private JFXButton addTaskPlusButton;
    
    @FXML
    private JFXButton goLeftButton;
    
    @FXML
    private JFXButton goRightButton;
    
    @FXML
    private GridPane dateGrid;
    
    @FXML
    private ScrollPane timelineScrollPane;
    
    @FXML
    private GridPane timelineGrid;
    
    @FXML
    private AnchorPane buttonAnchorPane;
    
    @FXML
    private AnchorPane gridAnchor;
    
    private MenuItem showDetails = new MenuItem("show details");
    
    private TimelineViewer tm;
    public static final int DAY_PIXEL_SIZE = 59;  // this is the width for a day in the the grids
    
    DateViewer dateViewer;
    
    ViewFactory viewFactory=ViewFactory.defaultFactory;
    
    //TimelineModel timelineModel = new TimelineModel();
    
    
    //date of the day
    private LocalDate currentDate = LocalDate.now();

    public MainWindowController(ModelAccess modelAccess) {
        super(modelAccess);
    }
    
    public void openAddTimelineWindow(ActionEvent e){
       
        Scene scene = viewFactory. getAddTimelineScene();
        Stage stage=new Stage();
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setTitle("Adding a new Timeline");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        stage.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (KeyCode.ESCAPE == event.getCode() && stage.isFocused()==true) {
                stage.close();
            }
            
        });
        
    }
    
    public void openAddTaskWindow(ActionEvent e){
        
        Scene scene = viewFactory. getAddTaskScene();
        Stage stage=new Stage();
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setTitle("Adding a new Task");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        stage.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (KeyCode.ESCAPE == event.getCode() && stage.isFocused()==true) {
                stage.close();
            }
        });
    }
    
    
    //to be connected to the right button, changing the date to a week ahead
    public void goRight(){
        currentDate = currentDate.plus(7,ChronoUnit.DAYS);
        datePickerUpdate(currentDate);
        dateViewer.showDates(currentDate);
        tm.update(currentDate, super.getModelAccess().timelineModel);
    }
    
    //to be connected to the left button, changing the date to a week back
    public void goLeft(){
        currentDate = currentDate.minus(7, ChronoUnit.DAYS);
        datePickerUpdate(currentDate);
        dateViewer.showDates(currentDate);
        tm.update(currentDate, super.getModelAccess().timelineModel);
    }
    
    //to be connected to a reset button, that will change the date back to the actual date of the day
    public void resetDate(){
        currentDate = LocalDate.now();
        datePickerUpdate(currentDate);
        
        dateViewer.showDates(currentDate);
        tm.update(currentDate, super.getModelAccess().timelineModel);
        
    }
    
    /* to be connected to the choicebox where the user chooses the year, month, day.
     *  By doing so the current date will become the one he chose.
     */
    public void setDate(){
        currentDate= mainWindowDatePicker.getValue();
        datePickerUpdate(currentDate);
        dateViewer.showDates(currentDate);
        tm.update(currentDate, super.getModelAccess().timelineModel);
    }
    
    
    public void datePickerUpdate(LocalDate inputDate){
        // Null check is needed to be able to run JUnit tests
        if(mainWindowDatePicker != null) {
            mainWindowDatePicker.setValue(inputDate);
        }
    }
    
    public LocalDate getCurrentDate() {
        return currentDate;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        
        timelineScrollPane.setFitToWidth(true);
        timelineScrollPane.setFitToHeight(true);
        datePickerUpdate(currentDate);
        dateViewer = new DateViewer(currentDate, dateGrid);
        dateGrid.getColumnConstraints().setAll(new ColumnConstraints(DAY_PIXEL_SIZE,DAY_PIXEL_SIZE,DAY_PIXEL_SIZE));
        dateGrid.getRowConstraints().setAll(new RowConstraints(20,20,20));
        dateGrid.getRowConstraints().add(0, new RowConstraints(40,40,40));
        timelineGrid.setMaxWidth(950);
        timelineGrid.setMinWidth(950);
        timelineScrollPane.setMaxWidth(1005);
        timelineScrollPane.setMinWidth(1005);
        timelineGrid.getRowConstraints().setAll(new RowConstraints(20,20,20));
        timelineGrid.getColumnConstraints().setAll(new ColumnConstraints(DAY_PIXEL_SIZE,DAY_PIXEL_SIZE,DAY_PIXEL_SIZE));
       
       titleOfTable.setCellValueFactory(new PropertyValueFactory<Timeline, String>("title"));

       
       timelineTable.setItems(getModelAccess().timelineModel.timelineList);
       
       timelineTable.setContextMenu(new ContextMenu(showDetails));
       
       // disables the add task button if there is no timeline
       addTaskButton.disableProperty().bind(Bindings.isEmpty(getModelAccess().getTimelineModel().timelineList));
       addTaskPlusButton.disableProperty().bind(Bindings.isEmpty(getModelAccess().getTimelineModel().timelineList));
       
       //Button sets which timeline are we trying to add A Task
       /*
        addTaskButton.setOnMouseClicked(e->{
          Timeline selectedTimeline=timelineTable.getSelectionModel().getSelectedItem();
          if(selectedTimeline!=null){
              
              getModelAccess().setSelectedTimeline(selectedTimeline);}
       });
        
        
       addTaskPlusButton.setOnMouseClicked(e->{
          Timeline selectedTimeline=timelineTable.getSelectionModel().getSelectedItem();
          if(selectedTimeline!=null){
              
              getModelAccess().setSelectedTimeline(selectedTimeline);}
           
       });
        
        */
        
       showDetails.setOnAction(e->{
                    Timeline timeline=timelineTable.getSelectionModel().getSelectedItem();
                    Iterator iter=timeline.taskList.iterator();
                    while(iter.hasNext()){
                    Task task=(Task) iter.next();
                    System.out.println("Title of the task "+ task.getTitle());
                    }
		});	
        
        tm = new TimelineViewer(currentDate, timelineGrid, super.getModelAccess());
      
        goLeft();  // this solves a bug with dates showing a small space if
        goLeft();   // not a view with to dates has been visible
        goLeft();
        goRight();
        goRight();
        goRight();
    }
    
    // getter for PixelWidth
    public int getPixelDayWidth()
    {
        return DAY_PIXEL_SIZE;
    }
}

