package timelineManager.model;

import java.time.LocalDate;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;


public class Timeline{
	private long id;
	private SimpleStringProperty title;

	private SimpleStringProperty description;
	private LocalDate startTime;
	private LocalDate endTime;
	private Color color;
	public ObservableList<Task> taskList= FXCollections.observableArrayList();
	private static long counter = 1;
	
	/**
	 * The constructor adds a unique ID to each created object of the class.
	 */
	public Timeline(){
		this.id = counter++;
	}
	
	public Timeline(String title, String description, LocalDate startTime, LocalDate endTime) {
		this.id = counter++;
		this.title = new SimpleStringProperty(title);
		this.description =  new SimpleStringProperty(description);
		this.startTime = startTime;
		this.endTime = endTime;
		
		// Temp color
		color = Color.rgb(200,175,160);
	}
	
	/*timelineManager.model.Task*/
	public void addTask(Task t){
                if(t!=null)
                    taskList.add(t);
                
		
	}
	public void editTask(Task t){
		
	}
	public void deleteTask(Task t){
		taskList.remove(t);
	}
	/*
			Getters & Setters
	 */
	public long getId(){
		return id;
	}
	public void setTitle(String title){
		this.title.set(title); 
	}
	public String getTitle(){
		return title.get();
	}
	
	
	public void setDescription(String description){
		this.description.set(description);
	}
	public String getDescription(){
		return description.get();
	}
	public void setStartTime(LocalDate startTime){
		this.startTime = startTime;
	}
	public LocalDate getStartTime(){
		return startTime;
	}
	public void setEndTime(LocalDate endTime){
		this.endTime = endTime;
	}
	public LocalDate getEndTime(){
		return endTime;
	}
	public void setColor(Color color){
		this.color = color;
	}
	public Color getColor(){
		return color;
	}
	
}

