package timelineManager.model;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

/**
 *  This class holds an ArrayList of Timelines
 */
public class TimelineModel{
  
  
    public static Color red = Color.rgb(242,67,71);
    public static Color orange = Color.rgb(235,141,47);
    public static Color yellow = Color.rgb(231,187,48);
    public static Color green = Color.rgb(90,167,59);
    public static Color blue = Color.rgb(62,160,230);
    public static Color purple = Color.rgb(98,0,255);
    public Color[] colorbar = {red,orange,yellow,green,blue,purple};
    public static Color[][] taskColorbar = {{Color.rgb(242,100,104),Color.rgb(242,133,137),Color.rgb(242,166,170),Color.rgb(242,199,203)},/*red bar*/
                                            {Color.rgb(235,106,47),Color.rgb(235,71,47),Color.rgb(235,36,47),Color.rgb(235,1,47)},/*orange bar*/
                                            {Color.rgb(231,162,48),Color.rgb(231,137,48),Color.rgb(231,112,48),Color.rgb(231,87,48)},/*yellow bar*/
                                            {Color.rgb(90,174,90),Color.rgb(90,182,121),Color.rgb(90,189,152),Color.rgb(57,197,187)},/*green bar*/
                                            {Color.rgb(79,143,230),Color.rgb(87,125,230),Color.rgb(94,108,230),Color.rgb(102,90,230)},/*blue bar*/
                                            {Color.rgb(150,80,255),Color.rgb(128,128,255),Color.rgb(150,150,255),Color.rgb(180,180,255)}};/*purple bar*/
    
    public ObservableList<Timeline> timelineList=FXCollections.observableArrayList();
    int current=0;
    
    public void addTimelineToList(Timeline timeline){
        timelineList.add(timeline);
        randomColor(timeline);
        current++;
    }
    public void randomColor(Timeline timeline){
        Random r = new Random();
        int i = r.nextInt(6);
        timeline.setColor(colorbar[i]);
        timeline.colorbar = taskColorbar[i];
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


    




       
    

    
       
    
    
   
    

