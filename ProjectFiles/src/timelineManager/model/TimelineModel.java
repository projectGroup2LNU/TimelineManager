package timelineManager.model;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
       list.sort(new Comparator<Timeline>()
       {
           @Override
           public int compare(Timeline t1, Timeline t2)
           {
               if(t1.getEndTime().equals(t2.getEndTime()))
               {
                   return t1.getStartTime().compareTo(t2.getStartTime());
               }
               else
               {
                   return t1.getEndTime().compareTo(t2.getEndTime());  // end time is primarty for sort
               }
    
           }
       });
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
       list.sort(new Comparator<Task>()
       {
           @Override
           public int compare(Task t1, Task t2)
           {
               if(t1.getEndTime().equals(t2.getEndTime()))
               {
                   
                   return t1.getStartTime().compareTo(t2.getStartTime());
               }
               else
               {
                   return t1.getEndTime().compareTo(t2.getEndTime());  // end time is primarty for sort
               }
            
           }
       });
        return list;
   }
    
}


    




       
    

    
       
    
    
   
    

