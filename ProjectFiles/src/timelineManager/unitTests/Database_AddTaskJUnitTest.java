package timelineManager.unitTests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import timelineManager.controller.AddTaskController;
import timelineManager.controller.ModelAccess;
import timelineManager.model.Task;
import timelineManager.model.Timeline;

public class Database_AddTaskJUnitTest {
	 private static Connection con;
	 private ModelAccess modelAccess = new ModelAccess();
	 private AddTaskController controller = new AddTaskController(modelAccess, null, null, null);
	 private String fxmlPath = "/timelineManager/view/AddTaskView.fxml";
	 private ObservableList<Task> tasks;
	 private int UniqueName=1;
	
	 @Before 
	public void setUp(){
		
		// Testing the org.sqlite.JDBC if available
		  try {
			Class.forName("org.sqlite.JDBC");
		 } catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		 }
		  
		 
		 // If the db file is already available the file will not be created again, Currently there are two table for tasks and timeline
		  try {
			con = DriverManager.getConnection("jdbc:sqlite:DB_Testing.db");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		  
		 controller.setTestMode(true);
		 new JFXPanel();
		 modelAccess.setSelectedTimeline(new Timeline("Title", "Description", LocalDate.now().minusDays(1), LocalDate.now().plusDays(300)));
		 tasks = modelAccess.getSelectedTimeline().taskList;
		 
		 try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
				loader.setController(controller);
				loader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
		  
	}
	 
	@Test
	public void testTasks() throws Exception {
		
		
		addTasks(500);
		
		// Returning a set of all tasks from the db with .next() method
		ResultSet rsTestingTasks=this.displaySetOfAllTasks();
		int counter=0;
		
		// Test each row for integrity of data
		while(rsTestingTasks.next()){
			
			assertEquals(rsTestingTasks.getString("TTitle"), tasks.get(counter).getTitle());
			assertEquals(rsTestingTasks.getString("TaskDesc"), tasks.get(counter).getDescription());
			assertEquals(rsTestingTasks.getString("TaskStart"), tasks.get(counter).getStartTime().toString());
			assertEquals(rsTestingTasks.getString("TaskEnd"), tasks.get(counter).getEndTime().toString());
			counter++;
		}
		
		// Clear the TaskTable data to make it ready for testing next time
		deleteAllTasks();
		
	}
	
	public void addTasksToDB(String Title, String TaskDescription, String TaskStartDate, String TaskEndDate) throws ClassNotFoundException, SQLException {
		 if(con == null) {
			 // get connection
			 Class.forName("org.sqlite.JDBC");
			 con = DriverManager.getConnection("jdbc:sqlite:DB_Testing.db");
		 }
		  PreparedStatement prep = con
				    .prepareStatement("insert into TasksTable values(?,?,?,?,?,?,?,?);");
				  prep.setInt(2, UniqueName);
				  prep.setString(3, Title);
				  prep.setString(4, TaskDescription);
				  prep.setString(5, TaskStartDate);
				  prep.setString(6, TaskEndDate);
				  
				  // TimeLine ID is 1 in testing tasks for Timeline
				  prep.setInt(7, 1);
				  
				  // This Task Is for test only, the type of data is For_Test
				  prep.setString(8, "For_Test");
				  
				  //Increment ID number for tasks
				  UniqueName++;
				  
				  prep.execute();
	 }
	
	@Test
	public void deleteAllTasks() {
        String sql = "DELETE FROM TasksTable WHERE type = 'For_Test'";
         
        
        try {
              PreparedStatement  preparedStatement = con.prepareStatement(sql);
          //  preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
 
        } catch (SQLException e) {
                System.out.println("timelineManager.model.ApplicationDB.deleteAllTaskByID()");
            System.out.println(e.getMessage());
        }
    } 
	
	
	private void addTasks(int amount) throws SQLException, Exception {
		int startNumber = tasks.size();
		for(int i = startNumber; i < amount + startNumber; i++) {
			controller.setTitle("TestTitle" + i);
			controller.setDescription("TestDescription" + i);
			controller.setStartDate(LocalDate.now().plusDays(i));
			controller.setEndDate(LocalDate.now().plusDays(i + 1));
			addTasksToDB("TestTitle"+i,"TestDescription"+i,LocalDate.now().plusDays(i).toString(),LocalDate.now().plusDays(i + 1).toString());
			controller.addTheTask(new ActionEvent());
		}	
	}
	
	private ResultSet displaySetOfAllTasks() throws SQLException, ClassNotFoundException {
		 if(con == null) {
			 // get connection
			 Class.forName("org.sqlite.JDBC");
			 con = DriverManager.getConnection("jdbc:sqlite:DB_Testing.db");
		 }
		 Statement state = con.createStatement();
		 ResultSet res = state.executeQuery("select TaskUniqueID, TTitle, TaskDesc, TaskStart, TaskEnd, TimeLineID from TasksTable");
		 return res;
	 }

}
