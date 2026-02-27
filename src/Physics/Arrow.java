package Physics;

import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

/**
 * 
 * @author kn
 */
public class Arrow extends Path{
    private static final double defaultArrowHeadSize = 5.0;
    
    public Arrow(double startX, double startY, double endX, double endY, double arrowHeadSize){
        super();
        updateArrow(startX, startY, endX, endY);
    }

    public void updateArrow(double startX, double startY, double endX, double endY){
        getElements().clear();
        double dx = endX - startX;
        double dy = endY - startY;
        double vectorLength = Math.hypot(dx, dy);
        
        double defaultLength = 70;

        double fixedEndX = startX + (dx/vectorLength)*defaultLength;
        double fixedEndY = startY + (dy/vectorLength)*defaultLength;
        
        //Line
        getElements().add(new MoveTo(startX, startY));
        getElements().add(new LineTo(fixedEndX, fixedEndY));
        
        //ArrowHead
        double angle = Math.atan2((endY - startY), (endX - startX)) - Math.PI / 2.0;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        //point1
        double x1 = (- 1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * defaultArrowHeadSize + fixedEndX;
        double y1 = (- 1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * defaultArrowHeadSize + fixedEndY;
        //point2
        double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * defaultArrowHeadSize + fixedEndX;
        double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * defaultArrowHeadSize + fixedEndY;
        
        getElements().add(new LineTo(x1, y1));
        getElements().add(new LineTo(x2, y2));
        getElements().add(new LineTo(fixedEndX, fixedEndY));
        double intensity = Math.min(1.0, vectorLength/255);
        setStroke(Color.color(intensity, 1-intensity, 0));
    }
    
    public Arrow(double startX, double startY, double endX, double endY){
        this(startX, startY, endX, endY, defaultArrowHeadSize);
    }
}