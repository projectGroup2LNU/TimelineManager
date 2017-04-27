package timelineManager.unitTests;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import timelineManager.model.Timeline;
import javafx.scene.paint.Color;
import java.time.LocalDate;
import java.util.ArrayList;

public class TUTimeline01_AddTimelineJUnitTest {
	private ArrayList<Timeline> timelines = new ArrayList<Timeline>();
	
	@Test
	public void testTimelines() {
		addTimelines(1);
		
		// Tests only one timeline
		assertEquals("TestTitle0", timelines.get(0).getTitle());
		assertEquals("TestDescription0", timelines.get(0).getDescription());
		assertEquals(LocalDate.now(), timelines.get(0).getStartTime());
		assertEquals(LocalDate.now().plusDays(1), timelines.get(0).getEndTime());
		assertEquals(Color.rgb(0, 0, 0), timelines.get(0).getColor());	
		assertEquals(1, timelines.get(0).getId());
		
		addTimelines(100);
		
		// Tests first timeline again after adding more timelines
		assertEquals("TestTitle0", timelines.get(0).getTitle());
		assertEquals("TestDescription0", timelines.get(0).getDescription());
		assertEquals(LocalDate.now(), timelines.get(0).getStartTime());
		assertEquals(LocalDate.now().plusDays(1), timelines.get(0).getEndTime());
		assertEquals(Color.rgb(0, 0, 0), timelines.get(0).getColor());
		assertEquals(1, timelines.get(0).getId());
		
		// Tests middle timeline
		assertEquals("TestTitle50", timelines.get(50).getTitle());
		assertEquals("TestDescription50", timelines.get(50).getDescription());
		assertEquals(LocalDate.now().plusDays(50), timelines.get(50).getStartTime());
		assertEquals(LocalDate.now().plusDays(51), timelines.get(50).getEndTime());
		assertEquals(Color.rgb(50, 0, 0), timelines.get(50).getColor());
		assertEquals(51, timelines.get(50).getId());
		
		// Tests last timeline
		assertEquals("TestTitle100", timelines.get(100).getTitle());
		assertEquals("TestDescription100", timelines.get(100).getDescription());
		assertEquals(LocalDate.now().plusDays(100), timelines.get(100).getStartTime());
		assertEquals(LocalDate.now().plusDays(101), timelines.get(100).getEndTime());
		assertEquals(Color.rgb(100, 0, 0), timelines.get(100).getColor());	
		assertEquals(101, timelines.get(100).getId());
	}
	
	// Adds new timelines to the ArrayList
	private void addTimelines(int amount) {
		int startNumber = timelines.size();
		
		for(int i = startNumber; i < amount + startNumber; i++) {
			timelines.add(new Timeline("TestTitle" + i, "TestDescription" + i, LocalDate.now().plusDays(i), LocalDate.now().plusDays(i + 1), Color.rgb(i,0,0)));
		}
	}
}
