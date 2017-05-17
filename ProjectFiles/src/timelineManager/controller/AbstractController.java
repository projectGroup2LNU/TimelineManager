/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timelineManager.controller;

import timelineManager.helpClasses.TimelineViewer;

/**
 *
 * @author beysimeryalmaz
 */
public abstract class AbstractController {
    
    private ModelAccess modelAccess;
    TimelineViewer timelineViewer;
    
    public AbstractController(ModelAccess modelAccess, TimelineViewer timelineViewer){
        this.modelAccess=modelAccess;
        this.timelineViewer = timelineViewer;
    }
    
    
    public ModelAccess getModelAccess(){
        return modelAccess;
    }
    
}
