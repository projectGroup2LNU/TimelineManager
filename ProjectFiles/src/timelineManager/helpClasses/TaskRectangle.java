package timelineManager.helpClasses;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import timelineManager.model.Task;

/**
 * A class for printing a Task in a grid pane.
 */
public class TaskRectangle extends StackPane
{
    private Task task;
    private Rectangle rectangle;
    private Label text;
    private boolean leftCutoff = false;
    private boolean rightCutoff = false;
    
    public TaskRectangle(Task inputTask)
    {
        task = inputTask;
        rectangle = new Rectangle(200,20);
        rectangle.setFill(task.getColor());
        rectangle.setOpacity(1);
        rectangle.setArcWidth(10);
        rectangle.setArcHeight(10);
        text = new Label("");
        text.setFont(new Font(10));
        text.setText(task.getTitle());
        text.setTextAlignment(TextAlignment.CENTER);
        text.setTextFill(Color.rgb(255, 255, 255));
        setAlignment(Pos.CENTER);
        text.setAlignment(Pos.TOP_CENTER);
        
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
    
    public Task getTask()
    {
        return task;
    }
    
    public void setTimeline(Task task)
    {
        this.task = task;
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
