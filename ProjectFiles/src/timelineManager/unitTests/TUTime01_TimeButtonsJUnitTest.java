package timelineManager.unitTests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import timelineManager.controller.MainWindowController;
import timelineManager.controller.ModelAccess;
import timelineManager.helpClasses.TimelineViewer;

import java.io.IOException;
import java.time.LocalDate;

public class TUTime01_TimeButtonsJUnitTest {
	private ModelAccess modelAccess = new ModelAccess();
	private TimelineViewer timelineViewer = new TimelineViewer();
	private MainWindowController time = new MainWindowController(modelAccess, timelineViewer, null, null);
	private String fxmlPath = "/timelineManager/view/MainView.fxml";
	
	@Before
	public void setUp() {
		// Initializes JavaFX
		new JFXPanel();

		// Loads the FXML file
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
			loader.setController(time);
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCurrentDate() {
		assertEquals(time.getCurrentDate(), LocalDate.now());
	}
	
	@Test
	public void testRightAndLeft() {
		goRight(10);
		assertEquals(time.getCurrentDate(), LocalDate.now().plusDays(70));
		
		time.resetDate();
		assertEquals(time.getCurrentDate(), LocalDate.now());
		
		goLeft(10);
		assertEquals(time.getCurrentDate(), LocalDate.now().minusDays(70));
		goRight(5);
		assertEquals(time.getCurrentDate(), LocalDate.now().minusDays(35));
		goLeft(10);
		assertEquals(time.getCurrentDate(), LocalDate.now().minusDays(105));
		
		time.resetDate();
		assertEquals(time.getCurrentDate(), LocalDate.now());
	}
	
	private void goRight(int amount) {
		for(int i = 0; i < amount; i++) {
			time.goRight();
		}
	}
	
	private void goLeft(int amount) {
		for(int i = 0; i < amount; i++) {
			time.goLeft();
		}
	}
}