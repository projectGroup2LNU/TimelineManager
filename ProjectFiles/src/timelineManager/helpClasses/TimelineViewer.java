package timelineManager.helpClasses;

import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import timelineManager.controller.MainWindowController;
import timelineManager.controller.ModelAccess;
import timelineManager.model.Task;
import timelineManager.model.Timeline;
import timelineManager.model.TimelineModel;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import timelineManager.view.ViewFactory;

/**
 * This class handles the logic for displaying the timelines and tasks in a gridpane.
 * It's specifically made as a helper class for timeline manager.
 */
public class TimelineViewer
{
    ModelAccess modelAccess;
    private GridPane grid;
    private int hPos = 1;
    private final int timelineHeight = 20;
    private final int taskHeight = 40;
    private LocalDate startDate;
    private LocalDate endDate;
    ArrayList<TimelineRectangle> timelineList = new ArrayList<>();
    ArrayList<TaskRectangle> taskList = new ArrayList<>();
    TimelineModel tm;
    LocalDate currentDate;
    ContextMenu contextMenuTimeline = new ContextMenu();
    ContextMenu contextMenuTask = new ContextMenu();
    ViewFactory viewFactory=ViewFactory.defaultFactory;
 
    MenuItem editTimeline = new MenuItem("Edit Timeline");
    MenuItem editTask = new MenuItem("Edit Task");
    MenuItem deleteTimeline=new MenuItem("Delete Timeline");
    MenuItem deleteTask=new MenuItem("Delete Task");

    private final int DAY_PIXEL_SIZE = MainWindowController.DAY_PIXEL_SIZE;
    
    public TimelineViewer(LocalDate currentDate, GridPane inGrid, ModelAccess inputModelAccess)
    {
        
        modelAccess = inputModelAccess;
        tm = inputModelAccess.timelineModel;
        grid = inGrid;
        grid.setVgap(5);
        
    
        // Filler for first row to make grid correct size
        for(int i = 0; i < 16; i++)
        {
            Rectangle rect = new Rectangle(DAY_PIXEL_SIZE, 20);
            rect.setVisible(false);
            grid.add(rect, i, 0, 1, 1);
        }
        
        grid.getColumnConstraints().setAll(new ColumnConstraints(DAY_PIXEL_SIZE, DAY_PIXEL_SIZE, DAY_PIXEL_SIZE));
        grid.getRowConstraints().set(0, new RowConstraints(5, 5, 5));
        
        startDate = currentDate.minusDays(4);
        endDate = currentDate.plusDays(12);
 
        update(currentDate, modelAccess.timelineModel);
        timelineListener();
        
        editTimeline.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                Scene scene = viewFactory.getEditTimelineScene();
                Stage stage=new Stage();
                stage.centerOnScreen();
                stage.setResizable(false);
                stage.setTitle("Edit Timeline");
                stage.setScene(scene);
                
                stage.show();
                
        
            }
        });
        
        deleteTimeline.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                
                int indexOfTimeline=modelAccess.timelineModel.timelineList.indexOf(modelAccess.getSelectedTimeline());
                modelAccess.timelineModel.timelineList.remove(indexOfTimeline);
                
                 
            }
        });
        
        contextMenuTimeline.getItems().addAll(editTimeline,deleteTimeline);
        
        editTask.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                Scene scene = viewFactory.getEditTaskScene();
                Stage stage=new Stage();
                stage.centerOnScreen();
                stage.setResizable(false);
                stage.setTitle("Edit Task");
                stage.setScene(scene);
                
                stage.show();
        
            }
        });
        
        deleteTask.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                int indexOfTimeline=modelAccess.timelineModel.timelineList.indexOf(modelAccess.getSelectedTimeline());
                modelAccess.timelineModel.timelineList.get(indexOfTimeline).taskList.remove(modelAccess.getSelectedTask());
            }
        });
        
        contextMenuTask.getItems().addAll(editTask,deleteTask);
        
    }
    
    /**
     *  Listends to changes made in the timeline list and updates the screen if any changes been made
     */
    private void timelineListener()
    {
        modelAccess.timelineModel.timelineList.addListener((ListChangeListener)(c -> {
    
            update(currentDate, modelAccess.timelineModel);
        }));
    
    }
    /**
     * This function updates the screen by recalculate all positions for dates and rectangles and
     * draw them to their grids.
     */
    public void update(LocalDate inputDate, TimelineModel inputModel)
    {
        currentDate = inputDate;
        tm = inputModel;
        grid.getChildren().removeAll(timelineList);
        grid.getChildren().removeAll(taskList);
        hPos = 1;
        
        startDate = inputDate.minusDays(4);
            endDate = inputDate.plusDays(12);
        ArrayList<Timeline> inputTimelines = inputModel.getTimelinesToDisplay(startDate,endDate);
        
        for(Timeline timeline : inputTimelines)
        {
            LocalDate timelineStart = null;
            LocalDate timelineEnd = null;
            TimelineRectangle timelineRectangle = new TimelineRectangle(timeline);
            String tooltipTimelineString = timeline.getTitle() +
                    "\nfrom: " + timeline.getStartTime() +
                    " to: " + timeline.getEndTime() +
                    "\nDescription: " + timeline.getDescription();
            
            final Tooltip timelineTooltip = new Tooltip();
            timelineTooltip.setFont(new Font(11));
            timelineTooltip.setWrapText(true);
            timelineTooltip.setMaxWidth(400);
            timelineTooltip.setText(tooltipTimelineString);
            
            Tooltip.install(
                    timelineRectangle, timelineTooltip);
            
            // (timeline streches outside the view
            if(timelineRectangle.getTimeline().getStartTime().compareTo(startDate) < 0)
            {
                
                timelineStart = startDate;
                timelineRectangle.setLeftCutoff(true);
                Rectangle startRect = new Rectangle(5,10);
                startRect.setFill(timelineRectangle.getRectangle().getFill());
                timelineRectangle.getChildren().add(startRect);
                Line topLine = new Line();
                topLine.setStartX(0);
                topLine.setStartY(0);
                topLine.setEndX(5);
                topLine.setEndY(0);
                
                Line bottomLine = new Line();
                bottomLine.setStartX(0);
                bottomLine.setStartY(0);
                bottomLine.setEndX(5);
                bottomLine.setEndY(0);
    
                timelineRectangle.getChildren().add(topLine);
                timelineRectangle.getChildren().add(bottomLine);
    
                timelineRectangle.setAlignment(topLine,Pos.TOP_LEFT);
                timelineRectangle.setAlignment(bottomLine, Pos.CENTER_LEFT);
                
                if(!timeline.equals(modelAccess.getSelectedTimeline()))
                {
                    topLine.setStroke(timelineRectangle.getRectangle().getStroke());
                    bottomLine.setStroke(timelineRectangle.getRectangle().getStroke());
                }
                
                
                timelineRectangle.setAlignment(startRect,Pos.TOP_LEFT);
                
            }
            else
            {
                timelineStart = timelineRectangle.getTimeline().getStartTime();
            }
        
            if(timelineRectangle.getTimeline().getEndTime().compareTo(endDate) > 0)
            {
                timelineEnd = endDate;
                timelineRectangle.setRightCutoff(true);
            } else
            {
                timelineEnd = timelineRectangle.getTimeline().getEndTime();
            }
            int timelineDuration = timelineStart.until(timelineEnd).getDays() + 1;
            timelineRectangle.getRectangle().setWidth(timelineDuration * DAY_PIXEL_SIZE);
            timelineRectangle.getText().setMaxWidth(timelineRectangle.getRectangle().getWidth()- 10);
            timelineRectangle.setAlignment(timelineRectangle.getText(), Pos.TOP_CENTER);
            
    
            if(timeline == modelAccess.getSelectedTimeline())
            {
                timelineRectangle.getRectangle().setStroke(Color.rgb(0,0,0));
            }
            timelineList.add(timelineRectangle);

            timelineRectangle.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
 
            @Override
            public void handle(ContextMenuEvent event) {
 
                contextMenuTimeline.show(timelineRectangle, event.getScreenX(), event.getScreenY());
            }
        });

            timelineRectangle.setOnMouseClicked(event ->
            {
                modelAccess.setSelectedTimeline(timeline);
                update(inputDate,inputModel);
            });
            
            grid.add(timelineRectangle, startDate.until(timelineStart).getDays(), hPos++, timelineDuration, 1);
            ArrayList<Task> tasksInView = inputModel.getTaskToDisplay(timeline,startDate,endDate);
            
            for(Task task : tasksInView )
            {
                LocalDate taskStart = null;
                LocalDate taskEnd = null;
                TaskRectangle taskRectangle = new TaskRectangle(task);
                if(taskRectangle.getTask().getStartTime().compareTo(startDate) < 0)
                {
                    taskStart = startDate;
                    taskRectangle.setLeftCutoff(true);
                }
                else
                {
                    taskStart = taskRectangle.getTask().getStartTime();
                
                }
            
                if(taskRectangle.getTask().getEndTime().compareTo(endDate) > 0)
                {
                    taskEnd = endDate;
                    taskRectangle.setRightCutoff(true);
                }
                else
                {
                    taskEnd = taskRectangle.getTask().getEndTime();
                }
                int taskDuration = taskStart.until(taskEnd).getDays() + 1;
                taskRectangle.getRectangle().setWidth(taskDuration * DAY_PIXEL_SIZE);
                taskRectangle.getText().setMaxWidth(taskRectangle.getRectangle().getWidth()- 10);
    
               
               
                if(task.getStartTime().equals(task.getEndTime()))  // makes a non duration task
                {
                    taskRectangle.getRectangle().setWidth(20);
                    
                    taskRectangle.getRectangle().setRotate(45);
                    taskRectangle.getText().setVisible(false);

                    
                    
                }
                String tooltipTaskString = task.getTitle() +
                        "\nfrom: " + task.getStartTime() +
                        " to: " + task.getEndTime() +
                        "\nDescription: " + task.getDescription();
                                
                taskList.add(taskRectangle);
                
                final Tooltip taskTooltip = new Tooltip();
                taskTooltip.setFont(new Font(11));
                taskTooltip.setMaxWidth(400);
                taskTooltip.setText(tooltipTaskString);
                taskTooltip.setWrapText(true);
                Tooltip.install(
                        taskRectangle, taskTooltip);
                taskRectangle.setOnMouseClicked(event ->
                {
                    modelAccess.setSelectedTask(task);
                    
                });
                grid.add(taskRectangle, startDate.until(taskStart).getDays(), hPos++, taskDuration, 1);        
                
            taskRectangle.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
 
            @Override
            public void handle(ContextMenuEvent event) {
 
                contextMenuTask.show(taskRectangle, event.getScreenX(), event.getScreenY());
            }
        });

            }
            hPos++;  // makes a space in Vertical space to next timeline
        }
        // a filler to get space between last task and the bottom
        Rectangle filler = new Rectangle(DAY_PIXEL_SIZE,20);
        filler.setOpacity(0);
        grid.add(filler,0,hPos);
        
        // adds a listener for task list in each timeline
        for(Timeline timeline : modelAccess.getTimelineModel().timelineList)
            timeline.taskList.addListener((ListChangeListener)( c -> {
                update(currentDate, modelAccess.timelineModel);
            }));
    }
    
}
