package timelineManager.model;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *  This class holds an ArrayList of Timelines
 */
public class TimelineModel{

    
    public ObservableList<Timeline> timelineList=FXCollections.observableArrayList();
    int current=0;
    public void addTimelineToList(Timeline timeline){
    timelineList.add(timeline);
        System.out.println(timelineList.get(current).getTitle());
        current++;
    }
    
    
    /*
    This method filters timelines to send display methods
    */
   public ArrayList<Timeline> getTimelinesToDisplay(LocalDate start, LocalDate end) {
       Iterator iter=timelineList.iterator();
       ArrayList<Timeline> list=new ArrayList();
       Timeline timeline;
       while(iter.hasNext()){
           timeline=(Timeline) iter.next();
       if(!start.isAfter(timeline.getEndTime()) && !timeline.getStartTime().isAfter(end)){
           list.add(timeline);
       }
       }
   
       return list;
   
   }

   /*
    This method filters tasks to send display methods
    */

   public ArrayList<Task> getTaskToDisplay(Timeline timeline,LocalDate start, LocalDate end){
        Iterator iter=timeline.taskList.iterator();
        ArrayList<Task> list=new ArrayList();
        Task task;
        while(iter.hasNext()){
            task=(Task) iter.next();
            if(!start.isAfter(task.getEndTime()) && !task.getStartTime().isAfter(end)){
                list.add(task);
            }
        }
        return list;
   }



}


    




       
    

    
       
    
    
   
    

