package timelineManager.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
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
import timelineManager.model.Timeline;
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
    private Button addTaskPlusButton;
    
    @FXML
    private Button goLeftButton;
    
    @FXML
    private Button goRightButton;
    
    @FXML
    private Button helpButton;
    
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
    
    @FXML
    private RadioButton radioButtonAllTimelines;
    
    @FXML
    private RadioButton radioButtonSelectedTimeline;
    
    private MenuItem showDetails = new MenuItem("show details");
    
    
    public static final int DAY_PIXEL_SIZE = 59;  // this is the width for a day in the the grids
    
    DateViewer dateViewer;
    
    ViewFactory viewFactory=ViewFactory.defaultFactory;
    
    //date of the current day
    private LocalDate currentDate = LocalDate.now();
    
    /**
     * Constructor that makes access to the Model, TimelineViewer, DaveViewer and TimelineTable
     * It also sets local variable timelineTable and dateViewer from the input.
     * @param modelAccess The model access of the Timeline Manager
     * @param timelineViewer The timelineViewer that shows all Timelines and tasks on the main window
     * @param timelineTable The timelineTable which shows all timelines as a list on the main window
     * @param dateViewer The dateviewer that shows all dates in the main window
     */
    public MainWindowController(ModelAccess modelAccess, TimelineViewer timelineViewer, DateViewer dateViewer, TableView<Timeline> timelineTable) {
        
        super(modelAccess, timelineViewer, timelineTable, dateViewer);
        this.dateViewer = dateViewer;
        this.timelineTable = timelineTable;
       
    }
    
    /**
     * Opens a new window for adding a new Timeline
     * @param e
     */
    public void openAddTimelineWindow(ActionEvent e){
        
        Scene scene = viewFactory. getAddTimelineScene();
        Stage stage=new Stage();
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setTitle("Adding a new Timeline");
        stage.getIcons().add(new Image("file:../TimelineManager/ProjectFiles/src/timelineManager/resource/image/icon.png"));
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        stage.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (KeyCode.ESCAPE == event.getCode() && stage.isFocused()==true) {
                stage.close();
            }
            
        });
   
    }
    
    /**
     * Open a new window for adding a new task in the active timeline
     * @param e
     */
    public void openAddTaskWindow(ActionEvent e){
        
        Scene scene = viewFactory. getAddTaskScene();
        Stage stage=new Stage();
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setTitle("Adding a new Task");
        stage.getIcons().add(new Image("file:../TimelineManager/ProjectFiles/src/timelineManager/resource/image/icon.png"));
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        stage.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (KeyCode.ESCAPE == event.getCode() && stage.isFocused()==true) {
                stage.close();
            }
        });
    }
    
    /**
     * Opens the help / manual window
     * @param e
     */
    public void openHelpWindow(ActionEvent e){
        
        Scene scene = viewFactory.getHelpScene();
        Stage stage=new Stage();
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setTitle("Help");
        stage.getIcons().add(new Image("file:../TimelineManager/ProjectFiles/src/timelineManager/resource/image/icon.png"));
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
        currentDate = dateViewer.getCurrentDate();
        currentDate = currentDate.plus(7,ChronoUnit.DAYS);
        datePickerUpdate(currentDate);
        dateViewer.showDates(currentDate);
        super.getTimelineViewer().update(currentDate, super.getModelAccess().timelineModel);
    }
    
    /**
     * method for change the view on week previous in time.
     * This is intended for the go left button in the month view.
     */
    public void goLeft(){
        currentDate = dateViewer.getCurrentDate();
        currentDate = currentDate.minus(7, ChronoUnit.DAYS);
        datePickerUpdate(currentDate);
        dateViewer.showDates(currentDate);
        super.getTimelineViewer().update(currentDate, super.getModelAccess().timelineModel);
        
    }
    
    /**
     * Sets the view to the current day.
     * This are to be connected to the "Today" button.
     */
    public void resetDate(){
        currentDate = LocalDate.now();
        datePickerUpdate(currentDate);
        
        dateViewer.showDates(currentDate);
        super.getTimelineViewer().update(currentDate, super.getModelAccess().timelineModel);
        
    }
    
    /**
     * This function takes the date from the datepicker and set the view to that date.
     */
    public void setDate(){
        currentDate= mainWindowDatePicker.getValue();
        datePickerUpdate(currentDate);
        dateViewer.showDates(currentDate);
        super.getTimelineViewer().update(currentDate, super.getModelAccess().timelineModel);
    }
    
    /**
     * This function updates the date of the datepicker, are to be used when other buttons affect the time than the datepicker.
     * @param inputDate the date to set the date picker
     */
    public void datePickerUpdate(LocalDate inputDate){
        mainWindowDatePicker.setValue(inputDate);
    }
    
    
    
    
   
    
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        
        
        timelineScrollPane.setFitToWidth(true);
        timelineScrollPane.setFitToHeight(true);
        datePickerUpdate(currentDate);
        dateViewer.initializeCalendarView(dateGrid);
        dateGrid.getColumnConstraints().setAll(new ColumnConstraints(DAY_PIXEL_SIZE,DAY_PIXEL_SIZE,DAY_PIXEL_SIZE));
        dateGrid.getRowConstraints().setAll(new RowConstraints(20,20,20));
        dateGrid.getRowConstraints().add(0, new RowConstraints(40,40,40));
        timelineGrid.setMaxWidth(1100);
        timelineGrid.setMinWidth(900);
        timelineScrollPane.setMaxWidth(1500);
        timelineScrollPane.setMinWidth(1000);
        timelineGrid.getRowConstraints().setAll(new RowConstraints(20,20,20));
        timelineGrid.getColumnConstraints().setAll(new ColumnConstraints(DAY_PIXEL_SIZE,DAY_PIXEL_SIZE,DAY_PIXEL_SIZE));
        titleOfTable.setCellValueFactory(new PropertyValueFactory<Timeline, String>("title"));
        timelineTable.setItems(getModelAccess().timelineModel.timelineList);
        
        // disables the add task button if there is no timeline
        addTaskButton.disableProperty().bind(Bindings.isEmpty(getModelAccess().getTimelineModel().timelineList));
        addTaskPlusButton.disableProperty().bind(Bindings.isEmpty(getModelAccess().getTimelineModel().timelineList));
        
        
        getDatabaseConnection();
        try {
            populateTimelineModel();
            
        }
        catch (SQLException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            cleanDb();
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SQLException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            reInititilizeTables();
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SQLException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try {
                getModelAccess().database.getConnection().close();
            }
            catch (Exception ex) {
                Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        getTimelineViewer().timelineViewerInitialize(currentDate, timelineGrid, super.getModelAccess(), radioButtonAllTimelines, radioButtonSelectedTimeline);
        
        timelineTable.setOnMouseClicked(e->
        {
            if(e.getClickCount() == 1)
            {
                Timeline timeline = timelineTable.getSelectionModel().getSelectedItem();
                if(timeline != null)
                {
                    getModelAccess().setSelectedTimeline(timeline);
                }
                super.getTimelineViewer().update(currentDate, super.getModelAccess().timelineModel);
                
            }
            else if(e.getClickCount() == 2) {
                
                datePickerUpdate(timelineTable.getSelectionModel().getSelectedItem().getStartTime());
            }
        });
        
        
 
        getModelAccess().timelineModel.timelineList.addListener(new ListChangeListener<Timeline>() {
            @Override
            public void onChanged(Change<? extends Timeline> c) {
                while (c.next()) {
                    if(c.wasAdded()){
                        datePickerUpdate(getModelAccess().timelineModel.timelineList.get(getModelAccess().timelineModel.timelineList.size()-1).getStartTime());
                    }
                }
            }
        });
        
        
    }
    
    // getters
    public int getPixelDayWidth()
    {
        return DAY_PIXEL_SIZE;
    }
    
    public RadioButton getRadioButtonAllTimelines()
    {
        return radioButtonAllTimelines;
    }
    
    public RadioButton getRadioButtonSelectedTimeline()
    {
        return radioButtonSelectedTimeline;
    }
    
    public LocalDate getCurrentDate() {
        return currentDate;
    }
}
