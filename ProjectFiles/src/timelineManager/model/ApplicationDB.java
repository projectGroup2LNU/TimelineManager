package timelineManager.model;

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
	 private boolean hasTimelineTable = false;
         private boolean hasTaskTable = false;
	 
	 public void connectToDatabase() throws ClassNotFoundException, SQLException {
		 
		   // sqlite driver
		  Class.forName("org.sqlite.JDBC");
		  // database path, if it's new database, it will be created in the project folder
		 
		  con = DriverManager.getConnection("jdbc:sqlite:TimeLineManagerDatabase.db");
		
		  initialiseTimeLinesTable();
		  initialiseTasksTable();
	 }
	 
	 public void deleteAllTaskByID(int id) {
	        String sql = "DELETE FROM TasksTable WHERE TimeLineID = '"+id+"'";
	         
	        
	        try {
                  PreparedStatement  preparedStatement = con.prepareStatement(sql);
	          //  preparedStatement.setInt(1, id);
	            preparedStatement.executeUpdate();
	 
	        } catch (SQLException e) {
                    System.out.println("timelineManager.model.ApplicationDB.deleteAllTaskByID()");
	            System.out.println(e.getMessage());
	        }
	    } 
         
          public void deleteTaskByTaskID(int id) {
	         String sql = "DELETE FROM TasksTable WHERE TaskUniqueID = '"+id+"'";
	       
            
	        try {
                   
                    PreparedStatement preparedStatement = con.prepareStatement(sql);
                     //   preparedStatement.setInt(1, id);
                        preparedStatement.executeUpdate();
	 
	        } catch (SQLException e) {
                     System.out.println("baba burda");    // TODO is this for test??
	            System.out.println(e.getMessage());
	        }
	    } 
	 
	 // If you delete a timeline tasks will also be gone, this method also can be just used for temp 
	 public void deleteTimeLineByID(int id) {
	        String sql = "DELETE FROM TimeLinesTable WHERE TimeLineID = ?";
            
	        try {
	        	PreparedStatement preparedStatement = con.prepareStatement(sql);
                        preparedStatement.setInt(1, id);
                        preparedStatement.executeUpdate();
	 
	        } catch (SQLException e) {
                    System.out.println("timelineManager.model.ApplicationDB.deleteTimeLineByID()");
	            System.out.println(e.getMessage());
	        }
	    } 
	 
	 
	
	public void addTask(int UniqueName, String Title, String TaskDescription, String TaskStartDate, String TaskEndDate, int TimelineID) throws ClassNotFoundException, SQLException {
		 if(con == null) {
			 // get connection
			 connectToDatabase();
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
			 connectToDatabase();
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
			 connectToDatabase();
		 }
		 Statement state = con.createStatement();
		 ResultSet res = state.executeQuery("select TaskUniqueID, TTitle, TaskDesc, TaskStart, TaskEnd, TimeLineID from TasksTable");
		 return res;
	 }
	 
	 public ResultSet displaySetOfAllTimeLines() throws SQLException, ClassNotFoundException {
		 if(con == null) {
			 // get connection
			 connectToDatabase();
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
	 
	 private void initialiseTasksTable() throws SQLException {
		 if( !hasTaskTable ) {
			 hasTaskTable = true;
			 // check for database table
			 Statement TaskSt = con.createStatement();
			 ResultSet Task = TaskSt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='TasksTable'");
			 
			 if( !Task.next()) {
				 // need to build the table
				  Statement state2 = con.createStatement();
				  state2.executeUpdate("create table TasksTable(id integer,"
				    + "TaskUniqueID integer," + "TTitle varchar(60)," + "TaskDesc varchar(60)," + "TaskStart varchar(60)," + "TaskEnd varchar(60),"+ "TimeLineID integer," + "primary key (id));");
				  
			 }
			 
		 }
	 }
	 
	 private void initialiseTimeLinesTable() throws SQLException {
		 if( !hasTimelineTable ) {
			 hasTimelineTable = true;
			 // check for database table
			 Statement TimelineSt = con.createStatement();
			 ResultSet TimeLine = TimelineSt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='TimeLinesTable'");
			                  
			// Statement TimelineSt = con.createStatement();
			// ResultSet TimeLine = TimelineSt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='TimelinesTable'");
			 
			 if( !TimeLine.next()) {
				 // need to build the table
				  Statement state2 = con.createStatement();
				  state2.executeUpdate("create table TimeLinesTable(id integer,"
				    + "TimeLineID integer," + "TimeLineTitle varchar(60)," + "TimeLineDesc varchar(60)," + "TimeLineStart varchar(60)," + "TimeLineEnd varchar(60),"+ "primary key (id));");
			 }
			 
		 }
	 }
         
         
         
          public ResultSet getAllTaskofTheTimeline(long id) throws SQLException, ClassNotFoundException {
		 if(con == null) {
			 // get connection
			 connectToDatabase();
		 }
                 
                 int i=(int) id;
		 Statement state = con.createStatement();
		 ResultSet res = state.executeQuery("select * from TasksTable where TimelineID='"+i+"'");
		 return res;
	 }
          
          
          
          public void cleanTimelineTable() throws ClassNotFoundException, SQLException{
             
               if(con==null){
                   connectToDatabase();
               }
               else{
                   Statement state2 = con.createStatement();
                   state2.executeUpdate("DELETE FROM TimeLinesTable");
               
               }
               
          
          }
          
          
          
          public void cleanTaskTable() throws ClassNotFoundException, SQLException{
             
               if(con==null){
                   connectToDatabase();
               }
               else{
                   Statement state2 = con.createStatement();
                   state2.executeUpdate("DELETE FROM TasksTable");
               
               }
               
          
          }
          
          
         public Connection getConnection() throws Exception{
             if(con!=null)
                return con;
             else
                 throw new Exception("There is no connection to get");
         }
	 
}
