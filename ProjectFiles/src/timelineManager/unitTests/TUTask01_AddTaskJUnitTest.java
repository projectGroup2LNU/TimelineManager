package timelineManager.unitTests;

import static org.junit.Assert.assertEquals;

import javafx.collections.ObservableList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import timelineManager.model.Task;
import timelineManager.model.Timeline;
import timelineManager.controller.AddTaskController;
import timelineManager.controller.ModelAccess;

import java.io.IOException;
import java.time.LocalDate;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import java.util.ArrayList;

public class TUTask01_AddTaskJUnitTest {
	private ModelAccess modelAccess = new ModelAccess();
	private AddTaskController controller = new AddTaskController(modelAccess);
	private String fxmlPath = "/timelineManager/view/AddTaskView.fxml";
	private ObservableList<Task> tasks;

	@Before
	public void setUp() {
		// Initializes JavaFX
		new JFXPanel();
		
		// Creates a Timeline that we can add tasks to
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

	@After
	public void tearDown() {
		Platform.exit();
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
