package timelineManager.unitTests;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import timelineManager.model.Task;
import javafx.scene.paint.Color;
import java.time.LocalDate;
import java.util.ArrayList;

public class TUTask02_EditTaskJUnitTest {
	private ArrayList<Task> tasks = new ArrayList<Task>();
	
	@Test
	public void testSeveralTasks() {
		addTasks(101);
		
		// Test first task
		tasks.get(0).setTitle("TestTitle0");
		tasks.get(0).setDescription("TestDescription0");
		tasks.get(0).setStartTime(LocalDate.now());
		tasks.get(0).setEndTime(LocalDate.now().plusDays(1));
		tasks.get(0).setColor(Color.rgb(0, 0, 0));
		tasks.get(0).setPriority("0");
		
		assertEquals("TestTitle0", tasks.get(0).getTitle());
		assertEquals("TestDescription0", tasks.get(0).getDescription());
		assertEquals(LocalDate.now(), tasks.get(0).getStartTime());
		assertEquals(LocalDate.now().plusDays(1), tasks.get(0).getEndTime());
		assertEquals(Color.rgb(0, 0, 0), tasks.get(0).getColor());
		assertEquals("0", tasks.get(0).getPriority());
		
		// Test middle task
		tasks.get(50).setTitle("TestTitle50");
		tasks.get(50).setDescription("TestDescription50");
		tasks.get(50).setStartTime(LocalDate.now().plusDays(50));
		tasks.get(50).setEndTime(LocalDate.now().plusDays(51));
		tasks.get(50).setColor(Color.rgb(50, 0, 0));
		tasks.get(50).setPriority("50");
		
		assertEquals("TestTitle50", tasks.get(50).getTitle());
		assertEquals("TestDescription50", tasks.get(50).getDescription());
		assertEquals(LocalDate.now().plusDays(50), tasks.get(50).getStartTime());
		assertEquals(LocalDate.now().plusDays(51), tasks.get(50).getEndTime());
		assertEquals(Color.rgb(50, 0, 0), tasks.get(50).getColor());
		assertEquals("50", tasks.get(50).getPriority());
		
		// Test last task
		tasks.get(100).setTitle("TestTitle100");
		tasks.get(100).setDescription("TestDescription100");
		tasks.get(100).setStartTime(LocalDate.now().plusDays(100));
		tasks.get(100).setEndTime(LocalDate.now().plusDays(101));
		tasks.get(100).setColor(Color.rgb(100, 0, 0));
		tasks.get(100).setPriority("100");
		
		assertEquals("TestTitle100", tasks.get(100).getTitle());
		assertEquals("TestDescription100", tasks.get(100).getDescription());
		assertEquals(LocalDate.now().plusDays(100), tasks.get(100).getStartTime());
		assertEquals(LocalDate.now().plusDays(101), tasks.get(100).getEndTime());
		assertEquals(Color.rgb(100, 0, 0), tasks.get(100).getColor());
		assertEquals("100", tasks.get(100).getPriority());
		
		// Test first task again
		assertEquals("TestTitle0", tasks.get(0).getTitle());
		assertEquals("TestDescription0", tasks.get(0).getDescription());
		assertEquals(LocalDate.now(), tasks.get(0).getStartTime());
		assertEquals(LocalDate.now().plusDays(1), tasks.get(0).getEndTime());
		assertEquals(Color.rgb(0, 0, 0), tasks.get(0).getColor());
		assertEquals("0", tasks.get(0).getPriority());
	}
	
	@Test
	public void testTaskTwice() {
		addTasks(1);
		
		tasks.get(0).setTitle("TestTitle0");
		tasks.get(0).setDescription("TestDescription0");
		tasks.get(0).setStartTime(LocalDate.now());
		tasks.get(0).setEndTime(LocalDate.now().plusDays(1));
		tasks.get(0).setColor(Color.rgb(0, 0, 0));
		tasks.get(0).setPriority("0");
		
		// Edits the same task again
		tasks.get(0).setTitle("TestTitle100");
		tasks.get(0).setDescription("TestDescription100");
		tasks.get(0).setStartTime(LocalDate.now().plusDays(100));
		tasks.get(0).setEndTime(LocalDate.now().plusDays(101));
		tasks.get(0).setColor(Color.rgb(100, 0, 0));
		tasks.get(0).setPriority("100");
		
		assertEquals("TestTitle100", tasks.get(0).getTitle());
		assertEquals("TestDescription100", tasks.get(0).getDescription());
		assertEquals(LocalDate.now().plusDays(100), tasks.get(0).getStartTime());
		assertEquals(LocalDate.now().plusDays(101), tasks.get(0).getEndTime());
		assertEquals(Color.rgb(100, 0, 0), tasks.get(0).getColor());
		assertEquals("100", tasks.get(0).getPriority());
	}
	
	private void addTasks(int amount) {
		for(int i = 0; i < amount; i++) {
			tasks.add(new Task());
		}
	}
}
