/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timelineManager.controller;

/**
 *
 * @author beysimeryalmaz
 */
public abstract class AbstractController {
    
    private ModelAccess modelAccess;
    
    public AbstractController(ModelAccess modelAccess){
        this.modelAccess=modelAccess;
    }
    
    
    public ModelAccess getModelAccess(){
        return modelAccess;
    }
    
}
