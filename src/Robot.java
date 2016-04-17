import javafx.geometry.Point2D;
import javafx.scene.shape.Shape;

/**
 * Created by casey on 2016-04-16.
 */
public class Robot {
    private double x, y;
    private Shape shape;

    public Robot(Shape shape) {
        this.shape = shape;
    }

    public Robot(double x, double y, Shape shape) {
        this.x = x;
        this.y = y;
        this.shape = shape;
    }

    public void moveD(Logic.Direction d, int scale) {
        moveX(d.dx * scale);
        moveY(d.dy * scale);
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
