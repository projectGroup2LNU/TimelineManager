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
	private int Priority;
	private static long counter = 1;
	
	/**
	 * The constructor adds a unique ID to each created object of the class.
	 */
    public Task(){
		this.id = counter++;
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
	
    public void setPriority(int Priority){
    	this.Priority = Priority;}
    
    public int getPriority(){
    	return Priority;}
	
    
}
