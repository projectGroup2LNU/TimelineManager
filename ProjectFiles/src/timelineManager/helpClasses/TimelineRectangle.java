package timelineManager.helpClasses;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import timelineManager.TimelineManagerMain;
import timelineManager.controller.MainWindowController;
import timelineManager.model.Task;
import timelineManager.model.Timeline;

/**
 * A class for printing a Timeline in a grid pane.
 */
public class TimelineRectangle extends StackPane
{
    
    private Timeline timeline;
    private Rectangle rectangle;
    private Label text;
    private boolean leftCutoff = false;  // if timeline stretches outside the screen the cutoff booleans is true.
    private boolean rightCutoff = false;
    

    public TimelineRectangle(Timeline inputTimeline)
    {
        timeline = inputTimeline;
        rectangle = new Rectangle(200,10);
        rectangle.setFill(timeline.getColor());
        rectangle.setOpacity(1);
        rectangle.setArcWidth(10);
        rectangle.setArcHeight(10);
        text = new Label("");
        text.setFont(new Font(10));
        text.setText(timeline.getTitle());
        //text.setTextAlignment(TextAlignment.CENTER);
        text.setTextFill(Color.rgb(255, 255, 255));
        setAlignment(Pos.TOP_CENTER);
        text.setAlignment(Pos.TOP_CENTER);
        
        text.setLayoutY(20);
        
        getChildren().add(rectangle);
        getChildren().add(text);
       
    }
    
    public Label getText()
    {
        return text;
    }
    
    public void setTextString(String text)
    {
        this.text.setText(text);
    }
    
    public boolean isLeftCutoff()
    {
        return leftCutoff;
    }
    
    public void setLeftCutoff(boolean leftCutoff)
    {
        this.leftCutoff = leftCutoff;
    }
    
    public boolean isRightCutoff()
    {
        return rightCutoff;
    }
    
    public void setRightCutoff(boolean rightCutoff)
    {
        this.rightCutoff = rightCutoff;
    }
    
    public Timeline getTimeline()
    {
        return timeline;
    }
    
    public void setTimeline(Timeline timeline)
    {
        this.timeline = timeline;
    }
    
    public Rectangle getRectangle()
    {
        return rectangle;
    }
    
    public StackPane getStackPane()
    {
        return this;
    }
}
