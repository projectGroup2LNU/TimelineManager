package timelineManager.model;


import java.time.LocalDate;
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
    
    
    
    
   
    
}
