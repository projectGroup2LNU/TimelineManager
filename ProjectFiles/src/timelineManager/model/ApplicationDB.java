package softwarepulse.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
* @author Ahmadreza Vakilalroayayi
*/

public class ApplicationDB {
	 private static Connection con;
	 private static boolean hasData = false;
	 
	 private void getConnection() throws ClassNotFoundException, SQLException {
		  // sqlite driver
		  Class.forName("org.sqlite.JDBC");
		  // database path, if it's new database, it will be created in the project folder
		  con = DriverManager.getConnection("jdbc:sqlite:TimeLineManagerDatabase.db");
		  initialiseTimeLinesTable();
		  initialiseTasksTable();
	 }
	 
	 // This method works well but should be deleted in future because we create db on each action
	 public void deleteTaskByID(int id) {
	        String sql = "DELETE FROM TasksTable WHERE TaskUniqueID = ?";
	         
	         PreparedStatement preparedStatement = null;
	        try {
	        	Connection connection = DriverManager.getConnection("jdbc:sqlite:TimeLineManagerDatabase.db");
	        	preparedStatement = connection.prepareStatement(sql);
	            preparedStatement.setInt(1, id);
	            preparedStatement.executeUpdate();
	 
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
	    } 
	 
	 // If you delete a timeline tasks will also be gone, this method also can be just used for temp 
	 public void deleteTimeLineByID(int id) {
	        String sql = "DELETE FROM TimeLinesTable WHERE TimeLineID = ?";
            PreparedStatement preparedStatement = null;
	        try {
	        	Connection connection = DriverManager.getConnection("jdbc:sqlite:TimeLineManagerDatabase.db");
	        	preparedStatement = connection.prepareStatement(sql);
	            preparedStatement.setInt(1, id);
	            preparedStatement.executeUpdate();
	 
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
	    } 
	 
	 
	
	public void addTask(int UniqueName, String Title, String TaskDescription, String TaskStartDate, String TaskEndDate, int TimelineID) throws ClassNotFoundException, SQLException {
		 if(con == null) {
			 // get connection
			 getConnection();
		 }
		  PreparedStatement prep = con
				    .prepareStatement("insert into TasksTable values(?,?,?,?,?,?,?);");
				  prep.setInt(2, UniqueName);
				  prep.setString(3, Title);
				  prep.setString(4, TaskDescription);
				  prep.setString(5, TaskStartDate);
				  prep.setString(6, TaskEndDate);
				  prep.setInt(7, TimelineID);
				  prep.execute();
	 }
	
	public void addTimeLine(int TimeLineID, String TimeLineTitle, String TimelineDescription, String TimelineStartDate, String TimeLineEndDate) throws ClassNotFoundException, SQLException {
		 if(con == null) {
			 // get connection
			 getConnection();
		 }
		  PreparedStatement prep = con
				    .prepareStatement("insert into TimeLinesTable values(?,?,?,?,?,?);");
				  prep.setInt(2, TimeLineID);
				  prep.setString(3, TimeLineTitle);
				  prep.setString(4, TimelineDescription);
				  prep.setString(5, TimelineStartDate);
				  prep.setString(6, TimeLineEndDate);
				  prep.execute();
	 }
	 
	 public ResultSet displaySetOfAllTasks() throws SQLException, ClassNotFoundException {
		 if(con == null) {
			 // get connection
			 getConnection();
		 }
		 Statement state = con.createStatement();
		 ResultSet res = state.executeQuery("select TaskUniqueID, TTitle, TaskDesc, TaskStart, TaskEnd, TimeLineID from TasksTable");
		 return res;
	 }
	 
	 public ResultSet displaySetOfAllTimeLines() throws SQLException, ClassNotFoundException {
		 if(con == null) {
			 // get connection
			 getConnection();
		 }
		 Statement state = con.createStatement();
		 ResultSet res = state.executeQuery("select TimeLineID, TimeLineTitle, TimeLineDesc, TimeLineStart, TimeLineEnd from TimeLinesTable");
		 return res;
	 }
	 
	 public void printAllTasks(){
		 try {
			 ResultSet rs = this.displaySetOfAllTasks();
			  int TotalTasks=1;
			  System.out.println("Showing tasks:");
			  while (rs.next()) {
				  System.out.println("\tTask number "+TotalTasks+":");
				     System.out.println("\n\t\tTask unique ID: " +rs.getString("TaskUniqueID") + "\n\t\tTask Title: " + rs.getString("TTitle")+ "\n\t\tTask Description: " + rs.getString("TaskDesc")+"\n\t\tTask Starting Day: "+rs.getString("TaskStart")+"\n\t\tTask Ending Day: "+rs.getString("TaskEnd")+"\n\tThis Task Is Belongs To TimeLine With This Unique ID: "+rs.getString("TimeLineID")+"\n");
				     TotalTasks++;
				  }
			  
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
		 
	 }
	 
	 public void printAllTimelines(){
		 try {
			 ResultSet rs = this.displaySetOfAllTimeLines();
			  int TotalTimeline=1;
			  System.out.println("Showing timelines:");
			  while (rs.next()) {
				  System.out.println("\nTimeLine number "+TotalTimeline+":");
				     System.out.println("\n\tTimeLine unique ID: " +rs.getString("TimeLineID") + "\n\t\tTimeLine Title: " + rs.getString("TimeLineTitle")+ "\n\t\tTimeLine Description: " + rs.getString("TimeLineDesc")+"\n\t\tTimeLine Starting Day: "+rs.getString("TimeLineStart")+"\n\t\tTimeLine Ending Day: "+rs.getString("TimeLineEnd")+"\n");
				     TotalTimeline++;
				  }
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
		 
	 }
	 /* Show Tasks of TimeLines by ID that belongs to them, Example: All the tasks with "TimeLine ID=1" that belongs to "TimeLine with ID=1" will be returned. 
	  * RelationShip between tables(One-To-Many)*/
	 public void searchTasksByTheirTimeLineID(int id) throws SQLException, ClassNotFoundException {
		    ResultSet rs;
			try {
				  rs = this.displaySetOfAllTasks();
				  System.out.println("Tasks belongs to the timeline with ID= "+id+" shows as following:");
				  while (rs.next()) {
					  if (rs.getInt("TimeLineID")==id){
					     System.out.println(rs.getString("TaskUniqueID") + " " + rs.getString("TTitle")+ " " + rs.getString("TaskDesc")+" "+rs.getString("TaskStart")+" "+rs.getString("TaskEnd")+" Belongs to timeline number: "+rs.getString("TimeLineID"));
					  }
					  }
				  
			  } catch (Exception e) {
				  e.printStackTrace();
			  }
	 }
	 
	 private void initialiseTasksTable() throws SQLException {
		 if( !hasData ) {
			 hasData = true;
			 // check for database table
			 Statement TaskSt = con.createStatement();
			 ResultSet Task = TaskSt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='TasksTable'");
			 
			 if( !Task.next()) {
				 System.out.println("Building the Tasks Table:");
				 // need to build the table
				  Statement state2 = con.createStatement();
				  state2.executeUpdate("create table TasksTable(id integer,"
				    + "TaskUniqueID integer," + "TTitle varchar(60)," + "TaskDesc varchar(60)," + "TaskStart varchar(60)," + "TaskEnd varchar(60),"+ "TimeLineID integer," + "primary key (id));");
				  
			 }
			 
		 }
	 }
	 
	 private void initialiseTimeLinesTable() throws SQLException {
		 if( !hasData ) {
			 hasData = true;
			 // check for database table
			 Statement TimelineSt = con.createStatement();
			 ResultSet TimeLine = TimelineSt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='TimeLinesTable'");
			 
			// Statement TimelineSt = con.createStatement();
			// ResultSet TimeLine = TimelineSt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='TimelinesTable'");
			 
			 if( !TimeLine.next()) {
				 System.out.println("Building the TimeLine Table:");
				 // need to build the table
				  Statement state2 = con.createStatement();
				  state2.executeUpdate("create table TimeLinesTable(id integer,"
				    + "TimeLineID integer," + "TimeLineTitle varchar(60)," + "TimeLineDesc varchar(60)," + "TimeLineStart varchar(60)," + "TimeLineEnd varchar(60),"+ "primary key (id));");
			 }
			 
		 }
	 }
	 
}
