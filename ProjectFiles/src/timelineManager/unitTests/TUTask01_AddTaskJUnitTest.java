package timelineManager.unitTests;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import timelineManager.model.Task;
import java.time.LocalDateTime;
import java.time.LocalDate;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class TUTask01_AddTaskJUnitTest {
	private ArrayList<Task> tasks = new ArrayList<Task>();
	
	@Test
	public void testTasks() {
		addTasks(1);
			
		// Tests only one task
		assertEquals("TestTitle0", tasks.get(0).getTitle());
		assertEquals("TestDescription0", tasks.get(0).getDescription());
		assertEquals(LocalDate.now(), tasks.get(0).getStartTime().toLocalDate());
		assertEquals(LocalDate.now().plusDays(1), tasks.get(0).getEndTime().toLocalDate());
		assertEquals(Color.rgb(0, 0, 0), tasks.get(0).getColor());	
		assertEquals(1, tasks.get(0).getId());
		assertEquals("0", tasks.get(0).getPriority());
		
		addTasks(100);
		
		// Tests first task again after adding more tasks
		assertEquals("TestTitle0", tasks.get(0).getTitle());
		assertEquals("TestDescription0", tasks.get(0).getDescription());
		assertEquals(LocalDate.now(), tasks.get(0).getStartTime().toLocalDate());
		assertEquals(LocalDate.now().plusDays(1), tasks.get(0).getEndTime().toLocalDate());
		assertEquals(Color.rgb(0, 0, 0), tasks.get(0).getColor());
		assertEquals(1, tasks.get(0).getId());
		assertEquals("0", tasks.get(0).getPriority());
		
		// Tests middle task
		assertEquals("TestTitle50", tasks.get(50).getTitle());
		assertEquals("TestDescription50", tasks.get(50).getDescription());
		assertEquals(LocalDate.now().plusDays(50), tasks.get(50).getStartTime().toLocalDate());
		assertEquals(LocalDate.now().plusDays(51), tasks.get(50).getEndTime().toLocalDate());
		assertEquals(Color.rgb(50, 0, 0), tasks.get(50).getColor());
		assertEquals(51, tasks.get(50).getId());
		assertEquals("50", tasks.get(50).getPriority());
		
		// Tests last task
		assertEquals("TestTitle100", tasks.get(100).getTitle());
		assertEquals("TestDescription100", tasks.get(100).getDescription());
		assertEquals(LocalDate.now().plusDays(100), tasks.get(100).getStartTime().toLocalDate());
		assertEquals(LocalDate.now().plusDays(101), tasks.get(100).getEndTime().toLocalDate());
		assertEquals(Color.rgb(100, 0, 0), tasks.get(100).getColor());	
		assertEquals(101, tasks.get(100).getId());
		assertEquals("100", tasks.get(100).getPriority());
	}
	
	// Adds new tasks to the ArrayList
	private void addTasks(int amount) {
		int startNumber = tasks.size();
		
		for(int i = startNumber; i < amount + startNumber; i++) {
			tasks.add(new Task("TestTitle" + i, "TestDescription" + i, LocalDateTime.now().plusDays(i), LocalDateTime.now().plusDays(i + 1), Color.rgb(i,0,0), "" + i));
		}
	}
}
