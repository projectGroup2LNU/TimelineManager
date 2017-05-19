package timelineManager.unitTests;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import timelineManager.model.Timeline;
import javafx.scene.paint.Color;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import timelineManager.controller.EditTimelineController;
import timelineManager.controller.ModelAccess;
import timelineManager.helpClasses.TimelineViewer;
import timelineManager.model.Timeline;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.time.LocalDate;

public class TUTimeline02_EditTimelineJUnitTest {
	private ModelAccess modelAccess = new ModelAccess();
	private EditTimelineController controller = new EditTimelineController(modelAccess, null);
	private String fxmlPath = "/timelineManager/view/EditTimelineView.fxml";
	
	@Before
	public void setUp() {
		//Initializes test mode and JavaFX
		controller.setTestMode(true);
		new JFXPanel();
		
		// Initializes Timelines so that they are editable
		initializeTimelines();

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
	public void testSeveralTimelines() {
		
		// Test first timeline
		setCurrentTimeline(0);
		editTimeline(10);
		
		assertEquals("TestTitle10", modelAccess.getSelectedTimeline().getTitle());
		assertEquals("TestDescription10", modelAccess.getSelectedTimeline().getDescription());
		assertEquals(LocalDate.now().plusDays(10), modelAccess.getSelectedTimeline().getStartTime());
		assertEquals(LocalDate.now().plusDays(11), modelAccess.getSelectedTimeline().getEndTime());
		
		// Test middle timeline
		setCurrentTimeline(50);
		editTimeline(60);
		
		assertEquals("TestTitle60", modelAccess.getSelectedTimeline().getTitle());
		assertEquals("TestDescription60", modelAccess.getSelectedTimeline().getDescription());
		assertEquals(LocalDate.now().plusDays(60), modelAccess.getSelectedTimeline().getStartTime());
		assertEquals(LocalDate.now().plusDays(61), modelAccess.getSelectedTimeline().getEndTime());
		
		// Test last timeline
		setCurrentTimeline(97);
		editTimeline(110);

		assertEquals("TestTitle110", modelAccess.getSelectedTimeline().getTitle());
		assertEquals("TestDescription110", modelAccess.getSelectedTimeline().getDescription());
		assertEquals(LocalDate.now().plusDays(110), modelAccess.getSelectedTimeline().getStartTime());
		assertEquals(LocalDate.now().plusDays(111), modelAccess.getSelectedTimeline().getEndTime());
		
		// Test first timeline again (it will be on index 97 since new edited timeline is created at the end of the timeline list and the old one is deleted)
		setCurrentTimeline(97);
		
		assertEquals("TestTitle10", modelAccess.getSelectedTimeline().getTitle());
		assertEquals("TestDescription10", modelAccess.getSelectedTimeline().getDescription());
		assertEquals(LocalDate.now().plusDays(10), modelAccess.getSelectedTimeline().getStartTime());
		assertEquals(LocalDate.now().plusDays(11), modelAccess.getSelectedTimeline().getEndTime());
	}
	
	@Test
	public void testExceptions() {
		// Tests empty title
		try {
			controller.setTitle("");
			controller.setStartDate(LocalDate.now());
			controller.setEndDate(LocalDate.now().plusDays(1));
			controller.editTimeline(new ActionEvent());
			fail("Expected RunTimeException: Please select a title");
		} catch (RuntimeException e) {
			assertEquals(e.getMessage(), "Please select a title");
		}
		
		// Tests empty start date
		try {
			controller.setTitle("TestTitle");
			controller.setStartDate(null);
			controller.setEndDate(LocalDate.now().plusDays(1));
			controller.editTimeline(new ActionEvent());
			fail("Expected RunTimeException: Please select start date");
		} catch (RuntimeException e) {
			assertEquals(e.getMessage(), "Please select start date");
		}
		
		// Tests empty end date
		try {
			controller.setTitle("TestTitle");
			controller.setStartDate(LocalDate.now());
			controller.setEndDate(null);
			controller.editTimeline(new ActionEvent());
			fail("Expected RunTimeException: Please select end date");
		} catch (RuntimeException e) {
			assertEquals(e.getMessage(), "Please select end date");
		}
		
		// Tests end date before start date
		try {
			controller.setTitle("TestTitle");
			controller.setStartDate(LocalDate.now());
			controller.setEndDate(LocalDate.now().minusDays(1));
			controller.editTimeline(new ActionEvent());
			fail("Expected RunTimeException: End date cannot be before start date");
		} catch (RuntimeException e) {
			assertEquals(e.getMessage(), "End date cannot be before start date");
			
		}
	}
	
	// Creates 100 timelines to edit
	private void initializeTimelines() {
		for(int i = 0; i < 100; i++) {
			modelAccess.timelineModel.addTimelineToList(new Timeline("TestTitle" + i, "TestDescription" + i, LocalDate.now().plusDays(i), LocalDate.now().plusDays(1 + i)));
		}
		modelAccess.setSelectedTimeline(modelAccess.timelineModel.timelineList.get(0));
	}
	
	// Sets a certain timeline to be editable
	private void setCurrentTimeline(int index) {
		modelAccess.setSelectedTimeline(modelAccess.timelineModel.timelineList.get(index));
		controller.initialize(null, null);
	}
	
	private void editTimeline(int toAdd) {
		controller.setTitle("TestTitle" + toAdd);
		controller.setDescription("TestDescription" + toAdd);
		controller.setStartDate(LocalDate.now().plusDays(toAdd));
		controller.setEndDate(LocalDate.now().plusDays(toAdd + 1));
		
		controller.editTimeline(new ActionEvent());
	}
}
