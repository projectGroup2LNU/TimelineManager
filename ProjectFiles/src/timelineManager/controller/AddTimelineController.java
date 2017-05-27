package timelineManager.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.stage.Modality;
import javafx.stage.Stage;
import timelineManager.helpClasses.DateViewer;
import timelineManager.helpClasses.TimelineViewer;
import timelineManager.model.Timeline;

/**
 * This is a controller class for the AddTimelineView.
 * it holds the variables and logics needed to create a new timeline from GUI.
 * @author beysimeryalmaz
 */
public class AddTimelineController extends AbstractController{
	
	@FXML
	private JFXTextField titleField;
	
	@FXML
	private JFXDatePicker startDate;
	
	@FXML
	private JFXDatePicker endDate;
	
	@FXML
	private JFXTextArea descriptionField;
	
	@FXML
	private JFXButton saveButton;
	
	@FXML
	private JFXButton cancelButton;
	
	private boolean isTestMode = false; // Used for Junit tests
	
	String title;
	LocalDate start,end;
	String desc = "";
	
	/**
	 * Constructor that sets the connections to model and important parts from main
	 * window to be same as the abstract controller.
	 * @param modelAccess The model access of the Timeline Manager
	 * @param timelineViewer The timelineViewer that shows all Timelines and tasks on the main window
	 * @param timelineTable The timelineTable which shows all timelines as a list on the main window
	 * @param dateViewer The dateviewer that shows all dates in the main window
	 */
	public AddTimelineController(ModelAccess modelAccess, TimelineViewer timelineViewer, DateViewer dateViewer, TableView<Timeline> timelineTable) {
		super(modelAccess, timelineViewer, timelineTable, dateViewer);
	}
	
	/**
	 *
	 * @param e
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws Exception
	 */
	public void addTheTimeline(ActionEvent e) throws ClassNotFoundException, SQLException, Exception {
		title = titleField.getText();
		desc = descriptionField.getText();
		start = startDate.getValue();
		end = endDate.getValue();
		
		if(!errorCheck()) { // Checks if there's any missing or invalid information in the fields.
			Timeline timeline = new Timeline(title,desc,start,end);
			getModelAccess().setSelectedTimeline(timeline);
			getModelAccess().timelineModel.addTimelineToList(timeline);
			
			// If check is needed for JUnit tests
			if(!isTestMode) {
				super.getTimelineViewer().update(getModelAccess().timelineModel);
				int id = (int) timeline.getId();
				getDatabaseConnection();
				getModelAccess().database.addTimeLine(id, title, desc, start.toString(), end.toString());
				//closes the connection
				getModelAccess().database.getConnection().close();
				getTimelineViewer().update(timeline.getStartTime(), getModelAccess().timelineModel);
				getDateViewer().showDates(timeline.getStartTime());
				
				// Window closes itself after user clicks the Save button
				final Node source = (Node) e.getSource();
				final Stage stage = (Stage) saveButton.getScene().getWindow();
				stage.close();
			}
		}
	}
	
	public void cancelTimeline(){
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}
	
	// Setters
	public void setTitle(String title) {
		titleField.setText(title);
	}
	
	public void setStartDate(LocalDate startDate) {
		this.startDate.setValue(startDate);
	}
	
	public void setEndDate(LocalDate endDate) {
		this.endDate.setValue(endDate);
	}
	
	public void setDescription(String description) {
		descriptionField.setText(description);
	}
	
	public void setTestMode(boolean isTestMode){
		this.isTestMode = isTestMode;
	}
	
	// Getters
	public JFXTextField getTitleField() {
		return titleField;
	}
	
	public JFXTextArea getDescField() {
		return descriptionField;
	}
	
	public JFXDatePicker getStartDate() {
		return startDate;
	}
	
	public JFXDatePicker getEndDate() {
		return endDate;
	}
	
	
	// Private methods
	
	/**
	 * Checking for invalid inputs and give the user feedback on what
	 * was wrong
	 * @return Boolean, true if errors are found.
	 */
	private boolean errorCheck() {
		boolean errorFound = true;
		
		if(title.trim().isEmpty()) {
			Tooltip tooltip = new Tooltip("Please insert title");
			setTooltipStyle(tooltip);
			titleField.setStyle("-fx-border-color: orangered;"+"-fx-border-width: 3;");
			titleField.setTooltip(tooltip);
		} else if ((!title.isEmpty()) && (start == null)) {
			titleField.setStyle("-fx-border-color: transparent;");
			Tooltip tooltip = new Tooltip("Select start date");
			setTooltipStyle(tooltip);
			startDate.setStyle("-fx-border-color: orangered;"+"-fx-border-width: 3;");
			startDate.setTooltip(tooltip);
		} else if ((!title.isEmpty()) && (start != null) && (end == null)) {
			titleField.setStyle("-fx-border-color: transparent;");
			startDate.setStyle("-fx-border-color: transparent;");
			Tooltip tooltip = new Tooltip("Select end date");
			setTooltipStyle(tooltip);
			endDate.setStyle("-fx-border-color: orangered;"+"-fx-border-width: 3;");
			endDate.setTooltip(tooltip);
		} else if((!title.isEmpty()) && (start != null) && (end!=null) && (end.isBefore(start))) {
			titleField.setStyle("-fx-border-color: transparent;");
			startDate.setStyle("-fx-border-color: transparent;");
			endDate.setStyle("-fx-border-color: transparent;");
			Tooltip tooltip = new Tooltip("End date cannot be before start date");
			setTooltipStyle(tooltip);
			endDate.setStyle("-fx-border-color: orangered;"+"-fx-border-width: 3;");
			endDate.setTooltip(tooltip);
			
		} else {
			errorFound = false;
		}
		
		return errorFound;
	}
	
	/**
	 * This method set style to red (error message) of the input tooltip
	 * @param tooltip the Tooltip to be edited
	 */
	private void setTooltipStyle(Tooltip tooltip)
	{
		tooltip.setStyle("-fx-background: rgb(30,30,30);" +
				"-fx-background-color: rgba(255,0,0,0.3);" +
				"-fx-text-fill: orange;" +
				"-fx-background-radius: 6px;" +
				"-fx-background-insets: 0;" +
				"-fx-padding: 0.667em 0.75em 0.667em 0.75em;" +
				"-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.5) , 10, 0.0 , 0 , 3 );" +
				"-fx-font-size: 0.85em;");
	}
}
