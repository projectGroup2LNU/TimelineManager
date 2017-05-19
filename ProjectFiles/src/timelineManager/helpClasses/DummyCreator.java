package timelineManager.helpClasses;

import javafx.scene.paint.Color;
import timelineManager.controller.ModelAccess;
import timelineManager.model.Task;
import timelineManager.model.Timeline;

import java.time.LocalDate;

/**
 * A class that creates dummy data so the system can be stress tested
 */
public class DummyCreator
{
    ModelAccess acess;
    
    public DummyCreator(ModelAccess inputModelAcess)
    {
        acess = inputModelAcess;
        
        for(int i = 0; i< 100; i++)
        {
            LocalDate date = LocalDate.of(2017,5,1);
            date = date.plusDays(i*3);
            Timeline tm = new Timeline();
            
            tm.setTitle("Timeline " + i);
            tm.setDescription("Description" + i);
            tm.setStartTime(date);
            tm.setEndTime(date.plusDays(14));
            acess.timelineModel.addTimelineToList(tm);
            
            acess.setSelectedTimeline(tm);
            for(int j = 0; j < 12; j++)
            {
                Task task = new Task();
                task.setTitle("Task" + j);
                task.setDescription("Description" + j);
                date = date.plusDays(j);
                task.setStartTime(tm.getStartTime().plusDays(j));
                task.setEndTime(task.getStartTime().plusDays(3));
                tm.taskList.add(task);
                tm.setTaskColor(task);
            }
        }
    }
}
