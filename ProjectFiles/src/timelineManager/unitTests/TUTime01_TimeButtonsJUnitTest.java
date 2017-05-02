package timelineManager.unitTests;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import timelineManager.controller.MainWindowController;
import timelineManager.controller.ModelAccess;

import java.time.LocalDate;

public class TUTime01_TimeButtonsJUnitTest {
	MainWindowController time = new MainWindowController(new ModelAccess());
	
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
