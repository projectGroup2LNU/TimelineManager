package timelineManager.model;

import java.time.LocalDate;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

/**
 * This class is a Timeline, it holds the most important variables such as:
 * Title, Description, Start time, End time and Tasklist.
 * It also has a color variable to be used in the TimelineMangager and a unique ID
 * generated from a static counter.
 */
public class Timeline
{
	private long id;
	private SimpleStringProperty title;
	private SimpleStringProperty description;
	private LocalDate startTime;
	private LocalDate endTime;
	private Color color;
	public ObservableList<Task> taskList = FXCollections.observableArrayList();
	private static long counter = 1;
	public Color[] colorbar = new Color[4];
	
	/**
	 * The empty input constructor adds a unique ID to each created object of the class.
	 */
	public Timeline()
	{
		title = new SimpleStringProperty();
		description = new SimpleStringProperty();
		this.id = counter++;
	}
	
	/**
	 * This constructor has the most imortant variables as input to create the timeline
	 * @param title The title of the timeline
	 * @param description a description of the timeline
	 * @param startTime start time
	 * @param endTime end time
	 */
	public Timeline(String title, String description, LocalDate startTime, LocalDate endTime)
	{
		this.id = counter++;
		this.title = new SimpleStringProperty(title);
		this.description = new SimpleStringProperty(description);
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	/**
	 * Add the input task to the current timeline and set a color to the task
	 * that is similar to the timeline color.
	 * @param task a Task to be added to the timeline
	 */
	public void addTask(Task task)
	{
		if(task != null)
		{
			taskList.add(task);
		}
		setTaskColor(task);
	}
	
	/**
	 * Remove a task from the timeline.
	 * @param task the Task to be removed from the timeline.
	 */
	public void deleteTask(Task task) {
		taskList.remove(task);
	}
	
	/**
	 * This function sets a color to the task that is similar to the current timeline.
	 * @param task the Task that are to get the new color.
	 */
	public void setTaskColor(Task task) {
		
		task.setColor(colorbar[taskList.indexOf(task) % 4]);
	}
	
	
	
	/*
			Getters & Setters
	 */
	public long getId() {
		return id;
	}
	
	public void setTitle(String title) {
		this.title.set(title);
	}
	
	public String getTitle() {
		return title.get();
	}
	
	public void setDescription(String description) {
		this.description.set(description);
	}
	
	public String getDescription() {
		return description.get();
	}
	
	public void setStartTime(LocalDate startTime) {
		this.startTime = startTime;
	}
	
	public LocalDate getStartTime() {
		return startTime;
	}
	
	public void setEndTime(LocalDate endTime) {
		this.endTime = endTime;
	}
	
	public LocalDate getEndTime() {
		return endTime;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	
	public SimpleStringProperty titleProperty() {
		return title;
	}
}