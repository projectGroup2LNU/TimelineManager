/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timelineManager.controller;

import timelineManager.model.ApplicationDB;
import timelineManager.model.Task;
import timelineManager.model.Timeline;
import timelineManager.model.TimelineModel;

/**
 *
 * @author beysimeryalmaz
 * 
 * This class is a common reference between controllers 
 * to enable communication between controllers
 */
public class ModelAccess
{
    private Task selectedTask;
    private Timeline selectedTimeline;
    public TimelineModel timelineModel=new TimelineModel();
    public ApplicationDB database=new ApplicationDB();

    
    public Timeline getSelectedTimeline(){
        return selectedTimeline;
    }
    
    public void setSelectedTimeline(Timeline timeline){
        selectedTimeline=timeline;
    }
            
    public TimelineModel getTimelineModel(){
        return timelineModel;
    }
    
     public Task getSelectedTask(){
        return selectedTask;
    }
    
    public void setSelectedTask(Task task){
        selectedTask=task;
    }
    
    
}
