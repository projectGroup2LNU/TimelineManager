package timelineManager.unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javafx.collections.ObservableList;
import org.junit.Before;
import org.junit.Test;

import timelineManager.helpClasses.TimelineViewer;
import timelineManager.model.Task;
import timelineManager.model.Timeline;
import timelineManager.controller.AddTaskController;
import timelineManager.controller.ModelAccess;

import java.io.IOException;
import java.time.LocalDate;

import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

public class TUTask01_AddTaskJUnitTest {
	private ModelAccess modelAccess = new ModelAccess();
	private TimelineViewer timelineViewer = new TimelineViewer();
	private AddTaskController controller = new AddTaskController(modelAccess,timelineViewer);
	private String fxmlPath = "/timelineManager/view/AddTaskView.fxml";
	private ObservableList<Task> tasks;

	@Before
	public void setUp() {
		//Initializes test mode and JavaFX
		controller.setTestMode(true);
		new JFXPanel();

		// Creates a Timeline that we can add tasks to and initializes tasks
		modelAccess.setSelectedTimeline(new Timeline("Title", "Description", LocalDate.now().minusDays(1), LocalDate.now().plusDays(300)));
		tasks = modelAccess.getSelectedTimeline().taskList;  // (commented out this line after changing to observable list<Task> in Timeline class

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
	public void testTasks() {
		addTasks(1);

		// Tests only one task
		assertEquals("TestTitle0", tasks.get(0).getTitle());
		assertEquals("TestDescription0", tasks.get(0).getDescription());
		assertEquals(LocalDate.now(), tasks.get(0).getStartTime());
		assertEquals(LocalDate.now().plusDays(1), tasks.get(0).getEndTime());
		//assertEquals(Color.rgb(0, 0, 0), tasks.get(0).getColor());	
		//assertEquals(1, tasks.get(0).getId());
		//assertEquals("0", tasks.get(0).getPriority());

		addTasks(100);

		// Tests first task again after adding more tasks
		assertEquals("TestTitle0", tasks.get(0).getTitle());
		assertEquals("TestDescription0", tasks.get(0).getDescription());
		assertEquals(LocalDate.now(), tasks.get(0).getStartTime());
		assertEquals(LocalDate.now().plusDays(1), tasks.get(0).getEndTime());
		//assertEquals(Color.rgb(0, 0, 0), tasks.get(0).getColor());
		//assertEquals(1, tasks.get(0).getId());
		//assertEquals("0", tasks.get(0).getPriority());

		// Tests middle task
		assertEquals("TestTitle50", tasks.get(50).getTitle());
		assertEquals("TestDescription50", tasks.get(50).getDescription());
		assertEquals(LocalDate.now().plusDays(50), tasks.get(50).getStartTime());
		assertEquals(LocalDate.now().plusDays(51), tasks.get(50).getEndTime());
		//assertEquals(Color.rgb(50, 0, 0), tasks.get(50).getColor());
		//assertEquals(51, tasks.get(50).getId());
		//assertEquals("50", tasks.get(50).getPriority());

		// Tests last task
		assertEquals("TestTitle100", tasks.get(100).getTitle());
		assertEquals("TestDescription100", tasks.get(100).getDescription());
		assertEquals(LocalDate.now().plusDays(100), tasks.get(100).getStartTime());
		assertEquals(LocalDate.now().plusDays(101), tasks.get(100).getEndTime());
		//assertEquals(Color.rgb(100, 0, 0), tasks.get(100).getColor());	
		//assertEquals(101, tasks.get(100).getId());
		//assertEquals("100", tasks.get(100).getPriority());
	}

	@Test
	public void testExceptions() {
		// Tests empty title
		try {
			controller.setTitle("");
			controller.setStartDate(LocalDate.now());
			controller.setEndDate(LocalDate.now().plusDays(1));
			controller.addTheTask(new ActionEvent());
			fail("Expected RunTimeException: Please select a title");
		} catch (RuntimeException e) {
			assertEquals(e.getMessage(), "Please select a title");
		}

		// Tests empty start date
		try {
			controller.setTitle("TestTitle");
			controller.setStartDate(null);
			controller.setEndDate(LocalDate.now().plusDays(1));
			controller.addTheTask(new ActionEvent());
			fail("Expected RunTimeException: Please select start date");
		} catch (RuntimeException e) {
			assertEquals(e.getMessage(), "Please select start date");
		}

		// Tests empty end date
		try {
			controller.setTitle("TestTitle");
			controller.setStartDate(LocalDate.now());
			controller.setEndDate(null);
			controller.addTheTask(new ActionEvent());
			fail("Expected RunTimeException: Please select end date");
		} catch (RuntimeException e) {
			assertEquals(e.getMessage(), "Please select end date");
		}

		// Tests end date before start date
		try {
			controller.setTitle("TestTitle");
			controller.setStartDate(LocalDate.now());
			controller.setEndDate(LocalDate.now().minusDays(1));
			controller.addTheTask(new ActionEvent());
			fail("Expected RunTimeException: End date cannot be before start date");
		} catch (RuntimeException e) {
			assertEquals(e.getMessage(), "End date cannot be before start date");

		}

		// Tests end date before timeline end date
		try {
			controller.setTitle("TestTitle");
			controller.setStartDate(LocalDate.now());
			controller.setEndDate(LocalDate.now().plusDays(301));
			controller.addTheTask(new ActionEvent());
			fail("Expected RunTimeException: Task end date cannot be after timeline end date");
		} catch (RuntimeException e) {
			assertEquals(e.getMessage(), "Task end date cannot be after timeline end date");

		}

		// Tests end date before timeline end date
		try {
			controller.setTitle("TestTitle");
			controller.setStartDate(LocalDate.now().minusDays(2));
			controller.setEndDate(LocalDate.now().plusDays(1));
			controller.addTheTask(new ActionEvent());
			fail("Expected RunTimeException: Task start date cannot be before timeline start date");
		} catch (RuntimeException e) {
			assertEquals(e.getMessage(), "Task start date cannot be before timeline start date");

		}
	}

	// Adds new tasks to the Timeline
	private void addTasks(int amount) {
		int startNumber = tasks.size();

		for(int i = startNumber; i < amount + startNumber; i++) {
			controller.setTitle("TestTitle" + i);
			controller.setDescription("TestDescription" + i);
			controller.setStartDate(LocalDate.now().plusDays(i));
			controller.setEndDate(LocalDate.now().plusDays(i + 1));

			controller.addTheTask(new ActionEvent());
		}	
	}
}
