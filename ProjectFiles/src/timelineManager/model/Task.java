package timelineManager.model;

import java.time.LocalDateTime;
import javafx.scene.paint.Color;
import java.time.Duration;
import java.time.LocalDate;

public class Task {
	private long id;
	private String title = "";
	private String description = "";
	private LocalDate startTime;
	private LocalDate endTime;
	private Color color;
	private String priority;
	private static long counter = 1;
	
	/**
	 * The constructor adds a unique ID to each created object of the class.
	 */
    public Task(){
		this.id = counter++;
	}
    
    public Task(String title, String description, LocalDate startTime, LocalDate endTime) {
                this.id = counter++;
		this.title = title;
		this.description = description;
		this.startTime = startTime;
		this.endTime = endTime;
	
		// Temp color
		color = Color.rgb(200,175,160);
    }
	
	public Duration getDuration(LocalDate startTime , LocalDate endTime){
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
	
	public void setStartTime(LocalDate startTime){
		this.startTime = startTime;}
	
	public LocalDate getStartTime(){
		return startTime;}
	
	public void setEndTime(LocalDate endTime){
		this.endTime = endTime;}
	
	public LocalDate getEndTime(){
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
