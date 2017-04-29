/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timelineManager.controller;

import timelineManager.model.Timeline;
import timelineManager.model.TimelineModel;

/**
 *
 * @author beysimeryalmaz
 * 
 * This class is a common reference between controllers 
 * to enable communication between controllers
 */
public class ModelAccess {
    
    private Timeline selectedTimeline;
    public TimelineModel timelineModel=new TimelineModel();
    
    public Timeline getSelectedTimeline(){
        return selectedTimeline;
    }
    
    public void setSelectedTimeline(Timeline timeline){
        selectedTimeline=timeline;
    }
            
    public TimelineModel getTimelineModel(){
        return timelineModel;
    }
    
}
