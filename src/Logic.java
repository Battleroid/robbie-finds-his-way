import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.ArrayList;

/**
 * Created by casey on 2016-04-16.
 */
public class Logic extends Pane {
    public static int cellSize;

    // robot related
    public Robot robot;
    // shapes
    public Shape end;
    public Polyline path;
    public ArrayList<Shape> obstacles = new ArrayList<>();
    int[][] board;

    public Logic(int cellSize) {
        this.cellSize = cellSize;

        buildBoard();
        buildGrid();
        addObstacles();
        addPath();
        addRobot();
        addSE();
    }

    public void tick() {
        System.out.println("Ticked, " + robot.getX() + ", " + robot.getY());
        if (robot.hits(end)) return; // we're at the goal, no point in continuing

        // check for path and avoid obstacles
        for (Sensor s : Sensor.values()) {
            System.out.println(s.toString() + ": " + blocked(s));
        }
    }

    private boolean blocked(Sensor sensor) {
        boolean isBlocked = false;

        for (Direction d : new Direction[]{sensor.a, sensor.b}) {
            double bx = robot.getX();
            double by = robot.getY();

            robot.moveX(d.dx * cellSize);
            robot.moveY(d.dy * cellSize);

            for (Shape s : obstacles) {
                if (robot.hits(s)) {
                    isBlocked = true;
                    break;
                }
            }

            robot.setXY(bx, by);

            if (isBlocked) return isBlocked;
        }

        return false;
    }

    private void buildBoard() {
        board = new int[19][21];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = 0;
            }
        }
    }

    private void addSE() {
        // just for visual cue on where we started
        Rectangle start = new Rectangle(10.0 * cellSize, 20 * cellSize, cellSize, cellSize);
        start.setFill(Color.GOLD);
        start.setOpacity(0.25);

        // ending goal with visual marker
        end = new Rectangle(16.0 * cellSize, 0, cellSize, cellSize);
        end.setFill(Color.VIOLET);
        end.setOpacity(0.25);

        getChildren().addAll(start, end);
    }

    private void addRobot() {
        Rectangle rect = new Rectangle(cellSize, cellSize);
        rect.setFill(Color.GREEN);
        robot = new Robot(rect);
        getChildren().add(robot.getShape());
        robot.setXY(10.0 * cellSize, 20 * cellSize);
    }

    private void addPath() {
        path = new Polyline();
        path.getPoints().addAll(new Double[]{
                8.9 * cellSize, 21.0 * cellSize,
                9.1 * cellSize, 19.0 * cellSize,
                4.5 * cellSize, 16.0 * cellSize,
                3.75 * cellSize, 14.0 * cellSize,
                4.0 * cellSize, 13.0 * cellSize,
                5.0 * cellSize, 12.0 * cellSize,
                6.8 * cellSize, 8.5 * cellSize,
                6.6 * cellSize, 8.0 * cellSize,
                6.0 * cellSize, 7.25 * cellSize,
                5.0 * cellSize, 7.0 * cellSize,
                4.0 * cellSize, 6.85 * cellSize,
                2.5 * cellSize, 6.5 * cellSize,
                2.0 * cellSize, 6.0 * cellSize,
                1.8 * cellSize, 5.0 * cellSize,
                2.0 * cellSize, 4.0 * cellSize,
                3.0 * cellSize, 3.2 * cellSize,
                4.0 * cellSize, 2.9 * cellSize,
                5.0 * cellSize, 3.2 * cellSize,
                6.0 * cellSize, 3.5 * cellSize,
                10.0 * cellSize, 6.8 * cellSize,
                11.0 * cellSize, 7.0 * cellSize,
                14.75 * cellSize, 4.0 * cellSize,
                15.0 * cellSize, 3.5 * cellSize,
                16.0 * cellSize, 2.0 * cellSize,
                16.5 * cellSize, 0.0
        });
        path.setStrokeWidth(1);
        path.setStroke(Color.BLUE);
        getChildren().add(path);
        path.toBack();
    }

    private void addObstacles() {
        Rectangle a = new Rectangle(2 * cellSize, 16 * cellSize, 9 * cellSize, 3 * cellSize);
        Rectangle b = new Rectangle(3 * cellSize, 8 * cellSize + (0.5 * cellSize), 6 * cellSize, 3 * cellSize + (0.5 * cellSize));
        Ellipse c = new Ellipse(14.5 * cellSize, 7 * cellSize, 3.5 * cellSize, 3 * cellSize);
        obstacles.add(a);
        obstacles.add(b);
        obstacles.add(c);
        getChildren().addAll(a, b, c);
    }

    private void buildGrid() {
        // vertical
        for (int i = 0; i <= 19; i++) {
            Line line = new Line(i * cellSize, 0, i * cellSize, 21 * cellSize);
            line.setStroke(Color.BLACK);
            line.setOpacity(0.25);
            line.setStrokeWidth(1.5);
            getChildren().add(line);
        }

        // horizontal
        for (int j = 0; j <= 21; j++) {
            Line line = new Line(0, j * cellSize, 19 * cellSize, j * cellSize);
            line.setStroke(Color.BLACK);
            line.setOpacity(0.25);
            line.setStrokeWidth(1.5);
            getChildren().add(line);
        }
    }

    public enum Sensor {
        A(Direction.NW, Direction.N),
        B(Direction.NE, Direction.E),
        C(Direction.SE, Direction.S),
        D(Direction.SW, Direction.W);

        public final Direction a;
        public final Direction b;

        Sensor(Direction a, Direction b) {
            this.a = a;
            this.b = b;
        }
    }

    public enum Direction {
        NW(-1, -1),
        N(0, -1),
        NE(1, -1),
        E(1, 0),
        SE(1, 1),
        S(0, 1),
        SW(-1, 1),
        W(-1, 0);

        public final double dx;
        public final double dy;

        Direction(double dx, double dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }
}
