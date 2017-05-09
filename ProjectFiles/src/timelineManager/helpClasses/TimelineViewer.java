package timelineManager.helpClasses;

import javafx.collections.ListChangeListener;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import timelineManager.controller.MainWindowController;
import timelineManager.controller.ModelAccess;
import timelineManager.model.Task;
import timelineManager.model.Timeline;
import timelineManager.model.TimelineModel;

import java.time.LocalDate;
import java.util.ArrayList;

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
            Rectangle rect = new Rectangle(DAY_PIXEL_SIZE-1, 20);
            rect.setVisible(false);
            grid.add(rect, i, 0, 1, 1);
        }
        
        grid.getColumnConstraints().setAll(new ColumnConstraints(DAY_PIXEL_SIZE, DAY_PIXEL_SIZE, DAY_PIXEL_SIZE));
        grid.getRowConstraints().set(0, new RowConstraints(5, 5, 5));
        
        startDate = currentDate.minusDays(4);
        endDate = currentDate.plusDays(12);
 
        update(currentDate, modelAccess.timelineModel);
        timelineListener();
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
    
    public void update(LocalDate inputDate, TimelineModel inputModel)
    {
        currentDate = inputDate;
        tm = inputModel;
        LocalDate resetHPosDate = null;
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
            
            if(timelineRectangle.getTimeline().getStartTime().compareTo(startDate) < 0)
            {
                timelineStart = startDate;
                timelineRectangle.setLeftCutoff(true);
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
            if(timeline == modelAccess.getSelectedTimeline())
            {
                timelineRectangle.getRectangle().setStroke(Color.rgb(0,0,0));
            }
            timelineList.add(timelineRectangle);
            
            if(resetHPosDate == null)
            {
                
            }
            else if(timelineRectangle.getTimeline().getStartTime().compareTo(resetHPosDate.plusDays(1)) > 0)
            {
                hPos = 1;
            }
            
            timelineRectangle.setOnMouseClicked(event ->
            {
                
                modelAccess.setSelectedTimeline(timeline);
                update(inputDate,inputModel);
                
            });
            grid.add(timelineRectangle, startDate.until(timelineStart).getDays(), hPos++, timelineDuration, 1);
            if(resetHPosDate == null || (resetHPosDate.compareTo(timelineRectangle.getTimeline().getEndTime()) < 0))
            {
                resetHPosDate = timelineRectangle.getTimeline().getEndTime();
            }

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
            
                taskList.add(taskRectangle);
            
                taskRectangle.setOnMouseClicked(event ->
                {
                    System.out.println(taskRectangle.getTask().getTitle());
                });
                grid.add(taskRectangle, startDate.until(taskStart).getDays(), hPos++, taskDuration, 1);
                
            }
            hPos++;  // makes a space in Vertical space to next timeline
            
        }
        // a filler to get space between last task and the bottom
        Rectangle filler = new Rectangle(DAY_PIXEL_SIZE,20);
        filler.setOpacity(0);
        grid.add(filler,0,hPos );
        
        // adds a listener for task list in each timeline
        for(Timeline timeline : modelAccess.getTimelineModel().timelineList)
            timeline.taskList.addListener((ListChangeListener)( c -> {
                update(currentDate, modelAccess.timelineModel);
            }));
        
    }
    
}
