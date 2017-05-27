package timelineManager.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TableView;
import timelineManager.helpClasses.DateViewer;
import timelineManager.helpClasses.TimelineViewer;
import timelineManager.model.Task;
import timelineManager.model.Timeline;

/**
 * This class is an abstract controller which is a parent to all controllers
 * in the timeline manager application. Since it holds a ModelAcess, TimelineViewer and DateViewer
 * all inherited classes will have the access to the same variables.
 */
public abstract class AbstractController {
    
    private TableView<Timeline> timelineTable;
    private ModelAccess modelAccess;
    private TimelineViewer timelineViewer;
    private DateViewer dateViewer;
    
    /**
     * Constructor for the Abstract Controller
     * @param modelAccess The model access of the Timeline Manager
     * @param timelineViewer The timelineViewer that shows all Timelines and tasks on the main window
     * @param timelineTable The timelineTable which shows all timelines as a list on the main window
     * @param dateViewer The dateviewer that shows all dates in the main window
     */
    public AbstractController(ModelAccess modelAccess, TimelineViewer timelineViewer,
                              TableView<Timeline> timelineTable, DateViewer dateViewer){
        this.modelAccess=modelAccess;
        this.timelineViewer = timelineViewer;
        this.timelineTable = timelineTable;
        this.dateViewer = dateViewer;
    }
    
    /**
     * This function creates a connection to the database.
     */
    protected void getDatabaseConnection(){
    
         try {
            getModelAccess().database.connectToDatabase();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * This function read all timelines and tasks from the database and add
     * them to the model in the running system.
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    protected void populateTimelineModel() throws SQLException, ClassNotFoundException{
    
        ResultSet rSet=modelAccess.database.displaySetOfAllTimeLines();
        Timeline timeline;
        
        String title,desc;
        long id;
        LocalDate start,end;
        
        while(rSet.next()){
            
            title=rSet.getString("TimeLineTitle");
            desc=rSet.getString("TimeLineDesc");
            start=LocalDate.parse(rSet.getString("TimeLineStart"));
            end=LocalDate.parse(rSet.getString("TimeLineEnd"));
            id= new Long(rSet.getString("TimeLineID"));
          
            timeline=new Timeline(title, desc, start, end);
            modelAccess.timelineModel.addTimelineToList(timeline);
            modelAccess.setSelectedTimeline(timeline);
            Task task;
              
            ResultSet taskRSet=modelAccess.database.getAllTaskofTheTimeline(id);
            while(taskRSet.next()){
                
                title=taskRSet.getString("TTitle");
                desc=taskRSet.getString("TaskDesc");
                start=LocalDate.parse(taskRSet.getString("TaskStart"));
                end=LocalDate.parse(taskRSet.getString("TaskEnd"));
                
                task=new Task(title, desc, start, end, timeline);
              //  timeline.setTaskColor(task);
                timeline.addTask(task);
                
            }
        }
    }
    
    /**
     * This function removes all data from the database
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    protected void cleanDb() throws ClassNotFoundException, SQLException{
       
        getModelAccess().database.cleanTimelineTable();
        getModelAccess().database.cleanTaskTable();
    }
    
    /**
     * This function recreates the Tables in the database.
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    protected void reInititilizeTables() throws ClassNotFoundException, SQLException{
        String title,desc,start,end;
        int id,tmListSize,taskListSize,tasksId;
        
        tmListSize=modelAccess.timelineModel.timelineList.size();
     
        for (int i = 0; i < tmListSize; i++) {
            id=(int) modelAccess.timelineModel.timelineList.get(i).getId();
            title=modelAccess.timelineModel.timelineList.get(i).getTitle();
            desc=modelAccess.timelineModel.timelineList.get(i).getDescription();
            start=modelAccess.timelineModel.timelineList.get(i).getStartTime().toString();
            end=modelAccess.timelineModel.timelineList.get(i).getEndTime().toString();
            
            modelAccess.database.addTimeLine(id, title, desc, start, end);
            taskListSize=modelAccess.timelineModel.timelineList.get(i).taskList.size();
            
            for (int j = 0; j < taskListSize; j++){
                tasksId=(int) modelAccess.timelineModel.timelineList.get(i).taskList.get(j).getId();
                title=modelAccess.timelineModel.timelineList.get(i).taskList.get(j).getTitle();
                desc=modelAccess.timelineModel.timelineList.get(i).taskList.get(j).getDescription();
                start=modelAccess.timelineModel.timelineList.get(i).taskList.get(j).getStartTime().toString();
                end=modelAccess.timelineModel.timelineList.get(i).taskList.get(j).getEndTime().toString();
            
                modelAccess.database.addTask(tasksId, title, desc, start, end, id);
            
            }
        }
    }
    
    // Gettes and Setters
    
    
    public TableView<Timeline> getTimelineTable()
    {
        return timelineTable;
    }
    
    public void setTimelineTable(TableView<Timeline> timelineTable)
    {
        this.timelineTable = timelineTable;
    }
    
    public ModelAccess getModelAccess()
    {
        return modelAccess;
    }
    
    public TimelineViewer getTimelineViewer()
    {
        return timelineViewer;
    }
    
    public DateViewer getDateViewer()
    {
        return dateViewer;
    }
}
