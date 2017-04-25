package timelineManager.unitTests;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import timelineManager.model.Timeline;
import javafx.scene.paint.Color;
import java.time.LocalDate;
import java.util.ArrayList;

public class TUTimeline02_EditTimelineJUnitTest {
	private ArrayList<Timeline> timelines = new ArrayList<Timeline>();
	
	@Test
	public void testSeveralTimelines() {
		addTimelines(101);
		
		// Test first timeline
		timelines.get(0).setTitle("TestTitle0");
		timelines.get(0).setDescription("TestDescription0");
		timelines.get(0).setStartTime(LocalDate.now());
		timelines.get(0).setEndTime(LocalDate.now().plusDays(1));
		timelines.get(0).setColor(Color.rgb(0, 0, 0));
		
		assertEquals("TestTitle0", timelines.get(0).getTitle());
		assertEquals("TestDescription0", timelines.get(0).getDescription());
		assertEquals(LocalDate.now(), timelines.get(0).getStartTime());
		assertEquals(LocalDate.now().plusDays(1), timelines.get(0).getEndTime());
		assertEquals(Color.rgb(0, 0, 0), timelines.get(0).getColor());
		
		// Test middle timeline
		timelines.get(50).setTitle("TestTitle50");
		timelines.get(50).setDescription("TestDescription50");
		timelines.get(50).setStartTime(LocalDate.now().plusDays(50));
		timelines.get(50).setEndTime(LocalDate.now().plusDays(51));
		timelines.get(50).setColor(Color.rgb(50, 0, 0));
		
		assertEquals("TestTitle50", timelines.get(50).getTitle());
		assertEquals("TestDescription50", timelines.get(50).getDescription());
		assertEquals(LocalDate.now().plusDays(50), timelines.get(50).getStartTime());
		assertEquals(LocalDate.now().plusDays(51), timelines.get(50).getEndTime());
		assertEquals(Color.rgb(50, 0, 0), timelines.get(50).getColor());
		
		// Test last timeline
		timelines.get(100).setTitle("TestTitle100");
		timelines.get(100).setDescription("TestDescription100");
		timelines.get(100).setStartTime(LocalDate.now().plusDays(100));
		timelines.get(100).setEndTime(LocalDate.now().plusDays(101));
		timelines.get(100).setColor(Color.rgb(100, 0, 0));
		
		assertEquals("TestTitle100", timelines.get(100).getTitle());
		assertEquals("TestDescription100", timelines.get(100).getDescription());
		assertEquals(LocalDate.now().plusDays(100), timelines.get(100).getStartTime());
		assertEquals(LocalDate.now().plusDays(101), timelines.get(100).getEndTime());
		assertEquals(Color.rgb(100, 0, 0), timelines.get(100).getColor());
		
		// Test first timeline again
		assertEquals("TestTitle0", timelines.get(0).getTitle());
		assertEquals("TestDescription0", timelines.get(0).getDescription());
		assertEquals(LocalDate.now(), timelines.get(0).getStartTime());
		assertEquals(LocalDate.now().plusDays(1), timelines.get(0).getEndTime());
		assertEquals(Color.rgb(0, 0, 0), timelines.get(0).getColor());
	}
	
	@Test
	public void testTimelineTwice() {
		addTimelines(1);
		
		timelines.get(0).setTitle("TestTitle0");
		timelines.get(0).setDescription("TestDescription0");
		timelines.get(0).setStartTime(LocalDate.now());
		timelines.get(0).setEndTime(LocalDate.now().plusDays(1));
		timelines.get(0).setColor(Color.rgb(0, 0, 0));
		
		// Edits the same timeline again
		timelines.get(0).setTitle("TestTitle100");
		timelines.get(0).setDescription("TestDescription100");
		timelines.get(0).setStartTime(LocalDate.now().plusDays(100));
		timelines.get(0).setEndTime(LocalDate.now().plusDays(101));
		timelines.get(0).setColor(Color.rgb(100, 0, 0));
		
		assertEquals("TestTitle100", timelines.get(0).getTitle());
		assertEquals("TestDescription100", timelines.get(0).getDescription());
		assertEquals(LocalDate.now().plusDays(100), timelines.get(0).getStartTime());
		assertEquals(LocalDate.now().plusDays(101), timelines.get(0).getEndTime());
		assertEquals(Color.rgb(100, 0, 0), timelines.get(0).getColor());
	}
	
	private void addTimelines(int amount) {
		for(int i = 0; i < amount; i++) {
			timelines.add(new Timeline());
		}
	}

}
