package timelineManager.model;

import javafx.scene.paint.Color;
import java.time.Duration;
import java.time.LocalDate;

/**
 * This class is a Task, it holds information such as title, description, start date,
 * end date. It also has a color parameter that will be used in the TimelineManager.
 * It's preparared with a priority value that are to be used in future releases.
 * All tasks will be given a unique id number by a static counter.
 */
public class Task {
	private long id;
	private String title = "";
	private String description = "";
	private LocalDate startTime;
	private LocalDate endTime;
	private Color color;
	private String priority;
	private static long counter = 1;
	private Timeline timeline;
	
	/**
	 * The empty input constructor adds a unique ID to each created object of the class.
	 */
	public Task(){
		this.id = counter++;
	}
	
	/**
	 * The main constructor for the TimelineManager where the most important features
	 * for the Task is added from constructor.
	 * @param title The title of the task.
	 * @param description The description of the task
	 * @param startTime Start time.
	 * @param endTime End time
	 * @param timeline The timeline the task belongs to.
	 */
	public Task(String title, String description, LocalDate startTime, LocalDate endTime, Timeline timeline) {
		this.id = counter++;
		this.title = title;
		this.description = description;
		this.startTime = startTime;
		this.endTime = endTime;
		this.timeline = timeline;
	}
	
	/**
	 * This method calculates the duration between the start time and end time and
	 * returns that as a Duration
	 * @param startTime Start time
	 * @param endTime End time
	 * @return a Duration calculated from start time and end time.
	 */
	public Duration getDuration(LocalDate startTime , LocalDate endTime){
		Duration duration = java.time.Duration.between(startTime, endTime);
		return duration;
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
	
	public Timeline getTimeline()
	{
		return timeline;
	}
	
	public void setTimeline(Timeline timeline)
	{
		this.timeline = timeline;
	}
}
