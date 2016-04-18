import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;

/**
 * Created by casey on 2016-04-16.
 */
public class Logic extends Pane {
    public static double cellSize;

    // robot related
    public Robot robot;

    // shapes
    public Shape end;
    public Polyline path;
    public ArrayList<Shape> obstacles = new ArrayList<>();
    int[][] board;
    Text[][] boardText;

    public Logic(double cellSize) {
        this.cellSize = cellSize;

        buildBoard();
        buildGrid();
        addObstacles();
        addPath();
        addRobot();
        addSE();
    }

    public void reset() {
        getChildren().clear();
        buildBoard();
        buildGrid();
        addObstacles();
        addPath();
        addRobot();
        addSE();
    }

    public boolean tick() {
        System.out.println("Tick - " + robot.getX() / cellSize + ", " + robot.getY() / cellSize);

        // are we at the goal already?
        if (robot.hits(end)) return true;

        // check if we see the goal yet
        if (found()) return true;

        // follow
        followBehavior();

        // set values in board and visualize the encounters
        showNeighboring();

        return false;
    }

    private void followBehavior() {
        if (isSensorBlocked(Sensor.X1) && !isSensorBlocked(Sensor.X2))
            robot.moveD(Direction.E, cellSize);
        else if (isSensorBlocked(Sensor.X2) && !isSensorBlocked(Sensor.X3))
            robot.moveD(Direction.S, cellSize);
        else if (isSensorBlocked(Sensor.X3) && !isSensorBlocked(Sensor.X4))
            robot.moveD(Direction.W, cellSize);
        else if (isSensorBlocked(Sensor.X4) && !isSensorBlocked(Sensor.X1))
            robot.moveD(Direction.N, cellSize);
        else
            robot.moveD(Direction.N, cellSize);
    }

    private void showNeighboring() {
        for (Direction d : Direction.values()) {
            int x = (int) ((robot.getX() / cellSize) + d.dx);
            int y = (int) ((robot.getY() / cellSize) + d.dy);
            if (!inbounds(x, y)) continue;

            if (board[x][y] == -1) {
                Rectangle rect = new Rectangle(cellSize, cellSize);
                rect.setOpacity(0.5);
                if (isDirectionBlocked(d)) {
                    rect.setFill(Color.RED);
                    board[x][y] = 1;
                    boardText[x][y].setStyle("-fx-fill: white; -fx-stroke: red; -fx-stroke-width: 1px;");
                } else {
                    rect.setFill(Color.LIGHTCYAN);
                    board[x][y] = 0;
                }
                boardText[x][y].setText("" + board[x][y]);
                boardText[x][y].toFront();
                rect.setTranslateX(robot.getX() + (d.dx * cellSize));
                rect.setTranslateY(robot.getY() + (d.dy * cellSize));
                getChildren().add(rect);
            }
        }
        robot.getShape().toFront();
    }

    private boolean inbounds(int x, int y) {
        return (x < board.length && x >= 0) && (y < board[0].length && y >= 0);
    }

    public boolean found() {
        Direction[] nwse = new Direction[]{
                Direction.N,
                Direction.E,
                Direction.S,
                Direction.W
        };

        double bx = robot.getX();
        double by = robot.getY();

        boolean foundEnd = false;
        for (Direction d : nwse) {
            robot.moveD(d, cellSize);
            foundEnd = robot.hits(end);
            if (foundEnd)
                break;
            robot.setXY(bx, by);
        }

        return foundEnd;
    }

    private boolean isDirectionBlocked(Direction d) {
        double bx = robot.getX();
        double by = robot.getY();

        robot.moveX(d.dx * cellSize);
        robot.moveY(d.dy * cellSize);

        boolean isBlocked = false;
        for (Shape s : obstacles) {
            if (robot.hits(s)) {
                isBlocked = true;
                break;
            }
        }

        robot.setXY(bx, by);

        return isBlocked;
    }

    private boolean isSensorBlocked(Sensor sensor) {
        boolean isBlocked = false;

        for (Direction d : sensor.toArray()) {
            if (isDirectionBlocked(d)) {
                isBlocked = true;
                break;
            }
        }

        return isBlocked;
    }

    private void buildBoard() {
        board = new int[19][21];
        boardText = new Text[19][21];
        double pad = cellSize * 0.25;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = -1;
                boardText[i][j] = new Text((i * cellSize) + pad, ((j + 1) * cellSize) - pad, "?");
                boardText[i][j].setOpacity(0.5);
                boardText[i][j].setFont(new Font("Consolas", 9));
                getChildren().add(boardText[i][j]);
            }
        }
    }

    private void addSE() {
        // just for visual cue on where we started
        Rectangle start = new Rectangle(10.0 * cellSize, 20 * cellSize, cellSize, cellSize);
        start.setFill(Color.GOLD);
        start.setOpacity(0.25);
        Text st = new Text(10.0 * cellSize, 21 * cellSize, "S");

        // ending goal with visual marker
        end = new Rectangle(16.0 * cellSize, 0, cellSize, cellSize);
        end.setFill(Color.VIOLET);
        end.setOpacity(0.25);
        Text et = new Text(16.0 * cellSize, 1 * cellSize, "E");

        st.setOpacity(0.5);
        et.setOpacity(0.5);

        getChildren().addAll(start, end, st, et);
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
        obstacles.add(path);
    }

    private void addObstacles() {
        Rectangle a = new Rectangle(2 * cellSize, 16 * cellSize, 9 * cellSize, 3 * cellSize);
        Rectangle b = new Rectangle(3 * cellSize, 8 * cellSize + (0.5 * cellSize), 6 * cellSize, 3 * cellSize + (0.5 * cellSize));
        Ellipse c = new Ellipse(14.35 * cellSize, 7 * cellSize, 3.5 * cellSize, 3 * cellSize);
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
        X1(Direction.N, Direction.NE),
        X2(Direction.E, Direction.SE),
        X3(Direction.S, Direction.SW),
        X4(Direction.W, Direction.NW);

        public final Direction a;
        public final Direction b;

        Sensor(Direction a, Direction b) {
            this.a = a;
            this.b = b;
        }

        public Direction[] toArray() {
            return new Direction[]{a, b};
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
