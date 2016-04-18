import javafx.geometry.Point2D;
import javafx.scene.shape.Shape;

/**
 * Created by casey on 2016-04-16.
 */
public class Robot {
    private double x, y;
    private Shape shape;
    private double movementScale;

    public Robot(Shape shape) {
        this.shape = shape;
    }

    public Robot(double x, double y, double movementScale, Shape shape) {
        this.x = x;
        this.y = y;
        this.movementScale = movementScale;
        this.shape = shape;
    }

    public void moveD(Logic.Direction d) {
        moveX(d.dx * movementScale);
        moveY(d.dy * movementScale);
    }

    public void moveX(double dx) {
        x += dx;
        shape.setTranslateX(x);
    }

    public void moveY(double dy) {
        y += dy;
        shape.setTranslateY(y);
    }

    public void setXY(double x, double y) {
        this.x = x;
        this.y = y;
        shape.setTranslateX(x);
        shape.setTranslateY(y);
    }

    public boolean hits(Shape shape) {
        Shape xsect = Shape.intersect(this.shape, shape);
        return xsect.getBoundsInLocal().getWidth() != -1;
    }

    public double getMovementScale() {
        return movementScale;
    }

    public void setMovementScale(double movementScale) {
        this.movementScale = movementScale;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Point2D getPt() {
        return new Point2D(x, y);
    }

    public Shape getShape() {
        return shape;
    }
}
