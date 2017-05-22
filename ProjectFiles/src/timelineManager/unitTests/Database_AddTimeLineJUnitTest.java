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
import timelineManager.controller.AddTimelineController;
import timelineManager.controller.ModelAccess;
import timelineManager.model.Timeline;
import static org.junit.Assert.assertEquals;

public class Database_AddTimeLineJUnitTest {
	 private static Connection con;
	 private ModelAccess modelAccess = new ModelAccess();
	private AddTimelineController controller = new AddTimelineController(modelAccess, null, null,null);
	 private String fxmlPath = "/timelineManager/view/AddTimelineView.fxml";
	 private ObservableList<Timeline> timelines;
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
		  
		  controller.setTestMode(true);
		  new JFXPanel();
		  timelines = modelAccess.timelineModel.timelineList;

		 
		 // If the db file is already available the file will not be created again, Currently there are two table for tasks and timelines
		  try {
			con = DriverManager.getConnection("jdbc:sqlite:DB_Testing.db");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		  
		 
		  try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
				loader.setController(controller);
				loader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
		  
	}
	 
	@Test
	public void testTimeLines() throws Exception {

		// Adding 200 timelines to db
		addTimelines(200);
		
		// Returning a set of all TimeLines from the db with .next() method to compare with expected data
		ResultSet rsTestingTimeLines=this.displaySetOfAllTimeLines();
		int counter=0;
		
		// Test each row for integrity of data
		while(rsTestingTimeLines.next()){
			
			assertEquals(rsTestingTimeLines.getString("TimeLineTitle"), timelines.get(counter).getTitle());
			assertEquals(rsTestingTimeLines.getString("TimeLineDesc"), timelines.get(counter).getDescription());
			assertEquals(rsTestingTimeLines.getString("TimeLineStart"), timelines.get(counter).getStartTime().toString());
			assertEquals(rsTestingTimeLines.getString("TimeLineEnd"), timelines.get(counter).getEndTime().toString());
			counter++;
		}
		
		// Clear the TimelineTable data to make it ready for testing next time
		deleteAllTimeLines();
		
	}
	
	public void addTimeLineToDB(String Title, String TimeLineDescription, String TimeLineStartDate, String TimeLineEndDate) throws ClassNotFoundException, SQLException {
		 if(con == null) {
			 // get connection
			 Class.forName("org.sqlite.JDBC");
			 con = DriverManager.getConnection("jdbc:sqlite:DB_Testing.db");
		 }
		  PreparedStatement prep = con
				    .prepareStatement("insert into TimeLinesTable values(?,?,?,?,?,?,?);");
				  prep.setInt(2, UniqueName);
				  prep.setString(3, Title);
				  prep.setString(4, TimeLineDescription);
				  prep.setString(5, TimeLineStartDate);
				  prep.setString(6, TimeLineEndDate);
				  
				  // TimeLine data type is just for testing
				  prep.setString(7, "For_Test");
				  
				  
				  
				  //Increment ID number for timelines
				  UniqueName++;
				  
				  prep.execute();
	 }
	
	
	public void deleteAllTimeLines() {
        String sql = "DELETE FROM TimeLinesTable WHERE type = 'For_Test'";
        try {
			con = DriverManager.getConnection("jdbc:sqlite:DB_Testing.db");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        try {
              PreparedStatement  preparedStatement = con.prepareStatement(sql);
              preparedStatement.executeUpdate();
 
        } catch (SQLException e) {
             System.out.println(e.getMessage());
        }
    } 
	
	private void addTimelines(int amount) throws SQLException, Exception {
		int startNumber = timelines.size();
		
		for(int i = startNumber; i < amount + startNumber; i++) {
			controller.setTitle("TestTitle" + i);
			controller.setDescription("TestDescription" + i);
			controller.setStartDate(LocalDate.now().plusDays(i));
			controller.setEndDate(LocalDate.now().plusDays(i + 1));
			
			// Adding the data into db to check for integrity and expected data afterwards
			addTimeLineToDB("TestTitle"+i,"TestDescription"+i,LocalDate.now().plusDays(i).toString(),LocalDate.now().plusDays(i + 1).toString());
			controller.addTheTimeline(new ActionEvent());
		}	
	}
	
			
	
	private ResultSet displaySetOfAllTimeLines() throws SQLException, ClassNotFoundException {
		 
		if(con == null) {
			 // get connection
			 Class.forName("org.sqlite.JDBC");
			 con = DriverManager.getConnection("jdbc:sqlite:DB_Testing.db");
		 }
		 Statement state = con.createStatement();
		 ResultSet res = state.executeQuery("select TimeLineID, TimeLineTitle, TimeLineDesc, TimeLineStart, TimeLineEnd, type from TimeLinesTable");
		 return res;
	 }

}
