package timelineManager.helpClasses;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import timelineManager.model.Timeline;

/**
 * A class for creating the timeline Rectangle.
 * It adds the timeline to a AnchorPane with rectangle with text of title of the timeline in top of it.
 */
public class TimelineRectangle extends AnchorPane
{
    private Timeline timeline;
    private Rectangle rectangle;
    private Label text;
    private boolean leftCutoff = false;  // if timeline stretches outside the screen the cutoff booleans is true.
    private boolean rightCutoff = false;
    
    /**
     * Constructor for creating the timeline Rectangle
     * @param inputTimeline the timeline to be created as rectangle
     */
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
        text.setTextFill(Color.rgb(255, 255, 255));
        text.setAlignment(Pos.TOP_CENTER);
        text.setLayoutY(20);
        
        getChildren().add(rectangle);
        getChildren().add(text);
    }
    
    // Getters and Setters
    
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
    
    public AnchorPane getPane()
    {
        return this;
    }
}
