package timelineManager.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
* This class has been designed for the project course in computer science at the Linnaeus University year 2017.
* This class represents the connection between the program and the database located into
* main root of application. Using JDBC or Java Database Connectivity is necessary.
* Note: This Database class, tables and it's controllers made for a specific goals, because of that tables and other prerequisite data can be seen in this class.
* If the user needs to use the database for other purposes rather than the current tables and columns, I strongly suggest a new design.
* @author Ahmadreza Vakilalroayayi
*/

public class ApplicationDB {
	 private static Connection con;
	 private boolean hasTimelineTable = false;
         private boolean hasTaskTable = false;
	 
	
	/**
         * This void method use JDBC(Java Database Connectivity) to create a connection with the database.
         * By using this method if there will be no DB file,a new connection to database contains of two tables will be made. 
         * If there is a connection, the DB file or tables will not be replaced.
         * @exception SQLException SQL query is not correct or the JDBC class can not be found
	 * @throws ClassNotFoundException JDBC class not exist
         */
	 public void connectToDatabase() throws ClassNotFoundException, SQLException {
		 
		  Class.forName("org.sqlite.JDBC");
		  con = DriverManager.getConnection("jdbc:sqlite:TimeLineManagerDatabase.db");
		  initialiseTimeLinesTable();
		  initialiseTasksTable();
	 }
	 
	
	/**
	  * Find Timeline tasks and delete them by Timeline id.
	  * deleteAllTaskByID(); will delete all tasks WHERE Tasks id refer to it's own Timeline id.
	  * @param id Specified Timeline id number to delete the tasks with the same id.
          */
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
         
	
	 /**
	  * Find tasks and delete it by it's unique id.
	  * deleteTaskByTaskID(); will delete the unique task WHERE Task id==id.
	  * @param id Specified Task unique id number to delete the task with the id.
          */
          public void deleteTaskByTaskID(int id) {
	         String sql = "DELETE FROM TasksTable WHERE TaskUniqueID = '"+id+"'";
	       
            
	        try {
                   
                    PreparedStatement preparedStatement = con.prepareStatement(sql);
                    preparedStatement.executeUpdate();
	 
	        } catch (SQLException e) {
                     System.out.println("test delete task");   
	            System.out.println(e.getMessage());
	        }
	    } 
	 
	
	/**
 	  * Find Timelines and delete it by it's unique id.
 	  * By deleting a Timeline data(row) the tasks that belong to that Timeline will also be gone.
 	  * deleteTimeLineByID(int id); will delete the unique Timeline WHERE Timeline id is equal to id parameter.
 	  * @param id Specified Timeline unique id.
          */
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
	 
	 
	/**
 	  * Adding data(Task) as a row to the database into TasksTable.
 	  * @param UniqueName used to assign a unique number for the task.
 	  * @param Title used to assign a title for the task.
 	  * @param TaskDescription used to assign a related description for the task.
 	  * @param TaskStartDate used to assign the start date of the task.
 	  * @param TaskEndDate used to assign the end date of the task.
 	  * @param TimelineID id number of the Timeline that this task should belongs to.
 	  * @exception SQLException SQL query is not correct or the JDBC class can not be found
	  * @throws ClassNotFoundException JDBC class not exist
          */ 
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
	
	
	/**
	  * Adding data(Timeline) as a row to the database and table TimeLinesTable.
	  * All the parameters in this method will be used with an index number for the database tables columns.
	  * This method will create a row into the TimeLinesTable table. Each row has different columns to categorize the details of the Timeline.
	  * Values of the rows(?), TimeLinesTable needs 6 values to be full filled.
	  * If the connection is not null, a new  PreparedStatement will be created with SQL commands.
	  * All the values will be assigned into the specified index number and the PreparedStatement will be executed and the last.
	  * @param TimeLineID id number of the Timeline.
	  * @param TimeLineTitle used to assign a title for the Timeline.
	  * @param TimelineDescription used to assign a related description for the Timeline.
	  * @param TimelineStartDate used to assign the start date of the Timeline.
	  * @param TimeLineEndDate used to assign the end date of the Timeline.
	  * @exception SQLException SQL query is not correct or the JDBC class can not be found
	  * @throws ClassNotFoundException JDBC class not exist
          */ 
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
	 
	
	/**
	  * Create a SQL statement and execute the query as a ResultSet from the TasksTable.
	  * If the connection is equal to null a new connection will be created into database and tables.
	  * SQL Query description in this method:
	  * SELECT: Find the data.
	  * *: Means all the data.
	  * from: From target table.
	  * It is possible to change the method to SELECT a specified column name in the table.
	  * @exception SQLException SQL query is not correct or the JDBC class can not be found
	  * @throws ClassNotFoundException JDBC class not exist
	  * @return A set of all values based on the column names by SELECT TaskUniqueID, TTitle, TaskDesc, TaskStart, TaskEnd, TimeLineID from TasksTable 
	  */
	 public ResultSet displaySetOfAllTasks() throws SQLException, ClassNotFoundException {
		 if(con == null) {
			 // get connection
			 connectToDatabase();
		 }
		 Statement state = con.createStatement();
		 ResultSet res = state.executeQuery("select TaskUniqueID, TTitle, TaskDesc, TaskStart, TaskEnd, TimeLineID from TasksTable");
		 return res;
	 }
	 
	
	 /**
	  * Create a SQL statement and execute the query as a ResultSet from the TimeLinesTable.
	  * If the connection is equal to null a new connection will be created into database and tables.
	  * SQL Query description in this method:
	  * SELECT: Find the data.
	  * *: Means all the data.
	  * from: From target table.
	  * It is possible to change the method to SELECT a specified column name in the table.
	  * @exception SQLException SQL query is not correct or the JDBC class can not be found
	  * @throws ClassNotFoundException JDBC class not exist
	  * @return A set of all values based on the column names by SELECT TimeLineID, TimeLineTitle, TimeLineDesc, TimeLineStart, TimeLineEnd from TimeLinesTable.
	  */
	 public ResultSet displaySetOfAllTimeLines() throws SQLException, ClassNotFoundException {
		 if(con == null) {
			 // get connection
			 connectToDatabase();
		 }
		 Statement state = con.createStatement();
		 ResultSet res = state.executeQuery("select TimeLineID, TimeLineTitle, TimeLineDesc, TimeLineStart, TimeLineEnd from TimeLinesTable");
		 return res;
	 }
	 
	
	 /**
	  * Print all the tasks by creating ResultSet.
	  * ResultSet might return incomparable column name if the tables are designed with different column names.
	  * while the ResultSet.next()!=false , ResultSet.getString("column name") will return the value of that column based on the current .next() pointer.
	  */
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
	 
	
	
	 /**
	  * Print all the Timelines by iterating through the database Timeline table.
          */
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
	 
	
	

	/**
	  * Initialize table if not exist.
	  * @exception SQLException SQL query is not correct or the JDBC class can not be found
	  * @throws ClassNotFoundException JDBC class not exist
	  * Create a new type of 'table' into the database with the name of 'TasksTable'.
	  * Type of the columns can be assigned with "NAME integer". Here integer is the type of the variable NAME.
	  */
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
	 
	
	
	/**
	  * Initialize table if not exist.
	  * @exception SQLException SQL query is not correct or the JDBC class can not be found
	  * @throws ClassNotFoundException JDBC class not exist
	  * Create a new type of 'table' into the database with the name of 'TimeLinesTable'.
	  * Type of the columns can be assigned with "NAME integer". Here integer is the type of the variable NAME.
          */
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
         
         
         /**
	  * Return all the Tasks that belongs to a Timeline by long id number.
	  * @exception SQLException SQL query is not correct or the JDBC class can not be found
	  * @throws ClassNotFoundException JDBC class not exist
	  * @param id The id number of the Timeline
	  * @return ResultSet A set of all data in the TaskTable by executing a SQL query
          */
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
          
          
          /**
     	  * Remove all the data from TimeLinesTable.
     	  * @exception SQLException SQL query is not correct or the JDBC class can not be found
          * @throws ClassNotFoundException JDBC class not exist
          */ 
          public void cleanTimelineTable() throws ClassNotFoundException, SQLException{
             
               if(con==null){
                   connectToDatabase();
               }
               else{
                   Statement state2 = con.createStatement();
                   state2.executeUpdate("DELETE FROM TimeLinesTable");
               
               }
               
          
          }
          
          
          /**
      	  * Remove all the data from TasksTable.
      	  * @exception SQLException SQL query is not correct or the JDBC class can not be found
          * @throws ClassNotFoundException JDBC class not exist
         */
          public void cleanTaskTable() throws ClassNotFoundException, SQLException{
             
               if(con==null){
                   connectToDatabase();
               }
               else{
                   Statement state2 = con.createStatement();
                   state2.executeUpdate("DELETE FROM TasksTable");
               
               }
               
          
          }
          
          /**
       	  * @return Connection if not null.
       	  * @exception SQLException Statement query should be correct at the time of executing and connection variable should be implemented correctly.
       	  * @throws Exception There is no connection to get
          */
         public Connection getConnection() throws Exception{
             if(con!=null)
                return con;
             else
                 throw new Exception("There is no connection to get");
         }
	 
}
