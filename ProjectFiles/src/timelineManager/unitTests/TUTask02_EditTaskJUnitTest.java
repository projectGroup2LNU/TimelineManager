package timelineManager.unitTests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import timelineManager.controller.EditTaskController;
import timelineManager.controller.EditTimelineController;
import timelineManager.controller.ModelAccess;
import timelineManager.helpClasses.TimelineViewer;
import timelineManager.model.Task;
import timelineManager.model.Timeline;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class TUTask02_EditTaskJUnitTest {
	private ModelAccess modelAccess = new ModelAccess();
	private EditTaskController controller;
	private String fxmlPath = "/timelineManager/view/EditTaskView.fxml";
	
	@Before
	public void setUp() {
		modelAccess.setSelectedTimeline(new Timeline("TestTitle", "TestDescription", LocalDate.now(), LocalDate.now().plusDays(1)));
		
		// Initializes Timelines so that they are editable
		initializeTasks();
				
		controller = new EditTaskController(modelAccess, null, null, null);
		//Initializes test mode and JavaFX
		controller.setTestMode(true);
		new JFXPanel();
		

		// Loads the FXML file so that we can modify the text areas in the controller
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
			loader.setController(controller);
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSeveralTasks() {
		// Test first task
		setCurrentTask(0);
		editTask(10);
		assertEquals("TestTitle10", modelAccess.getSelectedTask().getTitle());
		assertEquals("TestDescription10", modelAccess.getSelectedTask().getDescription());
		assertEquals(LocalDate.now().plusDays(10), modelAccess.getSelectedTask().getStartTime());
		assertEquals(LocalDate.now().plusDays(11), modelAccess.getSelectedTask().getEndTime());
		
		// Test middle task
		setCurrentTask(50);
		editTask(50);
		assertEquals("TestTitle50", modelAccess.getSelectedTask().getTitle());
		assertEquals("TestDescription50", modelAccess.getSelectedTask().getDescription());
		assertEquals(LocalDate.now().plusDays(50), modelAccess.getSelectedTask().getStartTime());
		assertEquals(LocalDate.now().plusDays(51), modelAccess.getSelectedTask().getEndTime());
		
		// Test last task
		setCurrentTask(99);
		editTask(99);
		assertEquals("TestTitle99", modelAccess.getSelectedTask().getTitle());
		assertEquals("TestDescription99", modelAccess.getSelectedTask().getDescription());
		assertEquals(LocalDate.now().plusDays(99), modelAccess.getSelectedTask().getStartTime());
		assertEquals(LocalDate.now().plusDays(100), modelAccess.getSelectedTask().getEndTime());
		
		// Test first task again
		setCurrentTask(0);
		assertEquals("TestTitle10", modelAccess.getSelectedTask().getTitle());
		assertEquals("TestDescription10", modelAccess.getSelectedTask().getDescription());
		assertEquals(LocalDate.now().plusDays(10), modelAccess.getSelectedTask().getStartTime());
		assertEquals(LocalDate.now().plusDays(11), modelAccess.getSelectedTask().getEndTime());
	}
		

	@Test // Edits the same task twice in a row
	public void testTaskTwice() {
		setCurrentTask(0);
		
		editTask(0);
		
		assertEquals("TestTitle0", modelAccess.getSelectedTask().getTitle());
		assertEquals("TestDescription0", modelAccess.getSelectedTask().getDescription());
		assertEquals(LocalDate.now().plusDays(0), modelAccess.getSelectedTask().getStartTime());
		assertEquals(LocalDate.now().plusDays(1), modelAccess.getSelectedTask().getEndTime());
		
		editTask(100);
		
		assertEquals("TestTitle100", modelAccess.getSelectedTask().getTitle());
		assertEquals("TestDescription100", modelAccess.getSelectedTask().getDescription());
		assertEquals(LocalDate.now().plusDays(100), modelAccess.getSelectedTask().getStartTime());
		assertEquals(LocalDate.now().plusDays(101), modelAccess.getSelectedTask().getEndTime());
	}
	
	@Test
	public void testErrors() throws ClassNotFoundException, Exception{
		// Tests empty title
		controller.setTitle("");
		controller.setStartDate(LocalDate.now());
		controller.setEndDate(LocalDate.now().plusDays(1));
		controller.editTask(new ActionEvent());
		assertEquals("Please insert title", controller.getTitleField().getTooltip().getText());
		
		// Tests empty start date
		controller.setTitle("TestTitle");
		controller.setStartDate(null);
		controller.setEndDate(LocalDate.now().plusDays(1));
		controller.editTask(new ActionEvent());
		assertEquals("Select start date", controller.getStartDate().getTooltip().getText());
		
		// Tests empty end date
		controller.setTitle("TestTitle");
		controller.setStartDate(LocalDate.now());
		controller.setEndDate(null);
		controller.editTask(new ActionEvent());
		assertEquals("Select end date", controller.getEndDate().getTooltip().getText());
		
		// Tests end date before start date
		controller.setTitle("TestTitle");
		controller.setStartDate(LocalDate.now());
		controller.setEndDate(LocalDate.now().minusDays(1));
		controller.editTask(new ActionEvent());
		assertEquals("End date cannot be before start date", controller.getEndDate().getTooltip().getText());
	}
	

	// Creates 100 tasks to edit
	private void initializeTasks() {
		// Creates a Timeline that we'll add tasks to
		modelAccess.timelineModel.addTimelineToList(new Timeline("TestTitle", "TestDescription", LocalDate.now(), LocalDate.now().plusDays(300)));
		modelAccess.setSelectedTimeline(modelAccess.timelineModel.timelineList.get(0));
		
		for(int i = 0; i < 100; i++) {
			modelAccess.getSelectedTimeline().taskList.add(new Task("TestTitle" + i, "TestDescription" + i, LocalDate.now().plusDays(i), LocalDate.now().plusDays(1 + i), modelAccess.getSelectedTimeline()));
		}
		
		setCurrentTask(0);
	}
	
	// Sets a certain task to be editable
	private void setCurrentTask(int index) {
		modelAccess.setSelectedTask(modelAccess.getSelectedTimeline().taskList.get(index));
	}
	
	private void editTask(int toAdd) {
		controller.setTitle("TestTitle" + toAdd);
		controller.setDescription("TestDescription" + toAdd);
		controller.setStartDate(LocalDate.now().plusDays(toAdd));
		controller.setEndDate(LocalDate.now().plusDays(toAdd + 1));
		
		try {
			controller.editTask(new ActionEvent());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
