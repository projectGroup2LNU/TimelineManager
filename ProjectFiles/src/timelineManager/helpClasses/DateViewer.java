package timelineManager.helpClasses;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.time.LocalDate;

/**
 * A class for showing dates in a gridPane
 * First row is months and year, and second row is the dates and days.
 */
public class DateViewer
{
    
    private final int DAY_PIXEL_SIZE = 59;
    
    private Text[] dateClaendarText;
    private Text[] dayOfWeek;
    private Rectangle[] dayRectangles;
    private Pane[] dayPanes;
    private Rectangle monthRect1;
    private Rectangle monthRect2;
    private Pane monthPane1;
    private Pane monthPane2;
    private Text month1Text;
    private Text month2Text;
    private GridPane grid;
    private LocalDate currentDate;
    private int daysInView = 17;
    private Text year1Text;
    private Text year2Text;
    private Color backColor = Color.rgb(180, 180, 180);
    private Color strokeColor = Color.rgb(123, 123, 123);
    private Color todayColor = Color.rgb(242, 128, 38);
    private Color dayColor = Color.rgb(123, 123, 123);
    private Color weekendColor = Color.rgb(180,180,180);
    private Color fontColor = Color.WHITE;
    /**
     * The constructor sets up the grid for viewing the monthes.
     * It instanciates all the object needed for later change the variables.
     * @param currentDate the constructor needs to know on which date to start the drawing.
     */
    public DateViewer(LocalDate currentDate)
    {
        this.currentDate = currentDate;
    }
    
    /**
     * initializes the elements needed for showing dates, months and year.
     */
    public void initializeCalendarView(GridPane inGrid)
    {
        grid = inGrid;
        grid.getColumnConstraints().setAll(new ColumnConstraints(DAY_PIXEL_SIZE,DAY_PIXEL_SIZE,DAY_PIXEL_SIZE));
        grid.getRowConstraints().setAll(new RowConstraints(20,20,20));
        dateClaendarText = new Text[daysInView];
        dayOfWeek = new Text[daysInView];
        dayRectangles = new Rectangle[daysInView];
        for(int i = 0; i < daysInView; i++)
        {
            Pane pane = new Pane();
            pane.setMinSize(DAY_PIXEL_SIZE, 20);
            pane.setMaxSize(DAY_PIXEL_SIZE, 20);
            
            Rectangle dateRect = new Rectangle(DAY_PIXEL_SIZE, 20);
            Text text = new Text("");
            Text dayName = new Text("");
            dateRect.setStroke(strokeColor);
            text.setFont(Font.font(15));
            dayName.setFont(Font.font(8));
            dayName.setFill(fontColor);
            pane.getChildren().add(dateRect);
            pane.getChildren().add(text);
            pane.getChildren().add(dayName);
            text.setLayoutY(15);
            text.setLayoutX(25);
            dayName.setLayoutY(9);
            dayName.setLayoutX(3);
            dateClaendarText[i] = text;
            dayOfWeek[i] = dayName;
            dayRectangles[i] = dateRect;
            grid.add(pane, i, 1);
        }
        
        monthPane1 = new Pane();
        monthPane2 = new Pane();
        
        monthRect1 = new Rectangle(DAY_PIXEL_SIZE, 40);
        monthRect2 = new Rectangle(DAY_PIXEL_SIZE, 40);
        month1Text = new Text("");
        month2Text = new Text("");
        month1Text.setFont(new Font(20));
        month2Text.setFont(new Font(20));
        year1Text = new Text("");
        year2Text = new Text("");
        year1Text.setFont(new Font (12));
        year2Text.setFont(new Font (12));
        
        
        monthPane1.getChildren().add(monthRect1);
        monthPane2.getChildren().add(monthRect2);
        monthPane1.getChildren().add(month1Text);
        monthPane2.getChildren().add(month2Text);
        monthPane1.getChildren().add(year1Text);
        monthPane2.getChildren().add(year2Text);
        
        month1Text.setLayoutX(30);
        month1Text.setLayoutY(30);
        month2Text.setLayoutX(30);
        month2Text.setLayoutY(30);
        year1Text.setLayoutX(90);
        year1Text.setLayoutY(30);
        year2Text.setLayoutX(95);
        year2Text.setLayoutY(30);
        monthRect1.setFill(backColor);
        monthRect1.setStroke(backColor);
        monthRect2.setFill(backColor);
        monthRect2.setStroke(backColor);
        
        grid.add(monthPane1, 0,0, 1,1);
        grid.add(monthPane2, 1, 0, 1,1 );
        showDates(currentDate);
    }
    
    /**
     * Updates the dates, starts 4 days before input date and will show in total 17 days.
     * @param inputDate LocalDate of the date to show.
     */
    public void showDates(LocalDate inputDate)
    {
        inputDate= inputDate.minusDays(4);
        for(int i = 0; i < 17; i++)
        {
            dateClaendarText[i].setText("" + inputDate.plusDays(i).getDayOfMonth());
            dateClaendarText[i].setFill(fontColor);
            dayOfWeek[i].setText(inputDate.plusDays(i).getDayOfWeek().toString().substring(0, 3));
            
            if(inputDate.plusDays(i).isEqual(LocalDate.now()))              // today
            {
                dayRectangles[i].setFill(todayColor);//orange
            } else if(inputDate.plusDays(i).getDayOfWeek().getValue() >= 6)   // weekend
            {
                dayRectangles[i].setFill(weekendColor);//grey
            } else
            {
                dayRectangles[i].setFill(dayColor);//black
            }
            
        }
        
        int month1Width = inputDate.lengthOfMonth() - inputDate.getDayOfMonth() + 1;
        int month2Width = 17 - month1Width;      // 17 is the total number of visible days
        
        if(month2Width < 1)
        {
            monthPane2.setVisible(false);
            month1Width = daysInView;
            monthRect1.setWidth(month1Width * DAY_PIXEL_SIZE);
            month1Text.setText(inputDate.getMonth().toString());
            year1Text.setText(inputDate.getYear()+ "");
            year1Text.setLayoutX(yearPlacementRetriver(inputDate));
            monthRect1.setWidth(month1Width * DAY_PIXEL_SIZE);
            
        }
        else
        {
            monthPane2.setVisible(true);
            monthRect1.setWidth(month1Width*DAY_PIXEL_SIZE);
            grid.setColumnSpan(monthPane1,month1Width);
            monthRect2.setWidth(month2Width * DAY_PIXEL_SIZE);
            month1Text.setText(inputDate.getMonth().toString());
            month2Text.setText(inputDate.plusMonths(1).getMonth().toString());
            year1Text.setText(inputDate.getYear()+ "");
            year2Text.setText(inputDate.plusMonths(1).getYear() + "");
            year2Text.setLayoutX(yearPlacementRetriver(inputDate.plusMonths(1)));
            year1Text.setLayoutX(yearPlacementRetriver(inputDate));
            grid.getChildren().remove(monthPane2);
            grid.add(monthPane2,month1Width,0,month2Width,1);
        }
        
        // A filler to fill a otherwise black hole right corner of the date rectangles
        Rectangle rightFiller = new Rectangle(2,60);
        rightFiller.setFill(backColor);
        grid.add(rightFiller,17,0,1,2);
        
    }
    
    /*
        private help method to calculate the distance the year after the name of the month.
        this could probobly be replaced by using a HBox instead of the Pane as of now.
     */
    private int  yearPlacementRetriver(LocalDate currentDate)
    {
        int month = currentDate.getMonthValue();
        switch(month)
        {
            case 1: return 130;
            case 2: return 145;
            case 3: return 115;
            case 4: return 100;
            case 5: return 85;
            case 6: return 95;
            case 7: return 95;
            case 8: return 125;
            case 9: return 163;
            case 10: return 140;
            case 11: return 155;
            case 12: return 155;
        }
        return 0;
    }
    
}
