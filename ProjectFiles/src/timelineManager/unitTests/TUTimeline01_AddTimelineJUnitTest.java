package timelineManager.unitTests;
/*
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import timelineManager.controller.AddTimelineController;
import timelineManager.controller.ModelAccess;
import timelineManager.model.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.time.LocalDate;

public class TUTimeline01_AddTimelineJUnitTest {
	private ModelAccess modelAccess = new ModelAccess();
	private AddTimelineController controller = new AddTimelineController(modelAccess);
	private String fxmlPath = "/timelineManager/view/AddTimelineView.fxml";
	private ObservableList<Timeline> timelines;
	
	@Before
	public void setUp() {
		// Initializes JavaFX
		new JFXPanel();
		
		// Initializes TimelineList
		timelines = modelAccess.timelineModel.timelineList;
		
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
	public void testTimelines() {
		addTimelines(1);
		
		// Tests only one timeline
		assertEquals("TestTitle0", timelines.get(0).getTitle());
		assertEquals("TestDescription0", timelines.get(0).getDescription());
		assertEquals(LocalDate.now(), timelines.get(0).getStartTime());
		assertEquals(LocalDate.now().plusDays(1), timelines.get(0).getEndTime());
		//assertEquals(Color.rgb(0, 0, 0), timelines.get(0).getColor());	
		//assertEquals(1, timelines.get(0).getId());
		
		addTimelines(100);
		
		// Tests first timeline again after adding more timelines
		assertEquals("TestTitle0", timelines.get(0).getTitle());
		assertEquals("TestDescription0", timelines.get(0).getDescription());
		assertEquals(LocalDate.now(), timelines.get(0).getStartTime());
		assertEquals(LocalDate.now().plusDays(1), timelines.get(0).getEndTime());
		//assertEquals(Color.rgb(0, 0, 0), timelines.get(0).getColor());
		//assertEquals(1, timelines.get(0).getId());
		
		// Tests middle timeline
		assertEquals("TestTitle50", timelines.get(50).getTitle());
		assertEquals("TestDescription50", timelines.get(50).getDescription());
		assertEquals(LocalDate.now().plusDays(50), timelines.get(50).getStartTime());
		assertEquals(LocalDate.now().plusDays(51), timelines.get(50).getEndTime());
		//assertEquals(Color.rgb(50, 0, 0), timelines.get(50).getColor());
		//assertEquals(51, timelines.get(50).getId());
		
		// Tests last timeline
		assertEquals("TestTitle100", timelines.get(100).getTitle());
		assertEquals("TestDescription100", timelines.get(100).getDescription());
		assertEquals(LocalDate.now().plusDays(100), timelines.get(100).getStartTime());
		assertEquals(LocalDate.now().plusDays(101), timelines.get(100).getEndTime());
		//assertEquals(Color.rgb(100, 0, 0), timelines.get(100).getColor());	
		//assertEquals(101, timelines.get(100).getId());
	}
	
	// Adds new timelines to the ArrayList
	private void addTimelines(int amount) {
		int startNumber = timelines.size();
		
		for(int i = startNumber; i < amount + startNumber; i++) {
			controller.setTitle("TestTitle" + i);
			controller.setDescription("TestDescription" + i);
			controller.setStartDate(LocalDate.now().plusDays(i));
			controller.setEndDate(LocalDate.now().plusDays(i + 1));

			controller.addTheTimeline(new ActionEvent());
		}	
	}
}
*/