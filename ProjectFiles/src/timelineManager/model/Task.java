package timelineManager.model;

import java.time.LocalDateTime;
import javafx.scene.paint.Color;
import java.time.Duration;

public class Task {
	private long id;
	private String title = "";
	private String description = "";
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private Color color;
	private String priority;
	private static long counter = 1;
	
	/**
	 * The constructor adds a unique ID to each created object of the class.
	 */
    public Task(){
		this.id = counter++;
	}
    
    public Task(String title, String description, LocalDateTime startTime, LocalDateTime endTime, Color color, int priority) {
    	this.id = counter++;
		this.title = title;
		this.description = description;
		this.startTime = startTime;
		this.endTime = endTime;
		this.color = color;
		this.Priority = priority;
    }
	
	public Duration getDuration(LocalDateTime startTime , LocalDateTime endTime){
		Duration Duration = java.time.Duration.between(startTime, endTime);
		return Duration;
	}
	
	/*
			Getters & Setters
	 */
	public long getId(){
		return id;}
	
	public void setTitle(String title){
		this.title = title;}
	
	public String getTitle(){
		return title;}
	
	public void setDescription(String description){
		this.description = description;}
	
	public String getDescription(){
		return description;}
	
	public void setStartTime(LocalDateTime startTime){
		this.startTime = startTime;}
	
	public LocalDateTime getStartTime(){
		return startTime;}
	
	public void setEndTime(LocalDateTime endTime){
		this.endTime = endTime;}
	
	public LocalDateTime getEndTime(){
		return endTime;}
	
	public void setColor(Color color){
		this.color = color;}
	
	public Color getColor(){
		return color;}
	
    public void setPriority(String priority){
    	this.priority = priority;}
    
    public String getPriority(){
    	return priority;}
	
    
}
