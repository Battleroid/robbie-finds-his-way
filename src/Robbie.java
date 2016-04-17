import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Created by casey on 2016-04-16.
 */
public class Robbie extends Application {
    final static int cellSize = 15;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Logic logic = new Logic(cellSize);
        Scene scene = new Scene(logic, cellSize * 19, cellSize * 21);
        stage.setScene(scene);
        stage.show();

        Duration duration = new Duration(1000 / 5);
        KeyFrame frame = new KeyFrame(duration,
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (logic.tick()) logic.reset();
                    }
                }
        );
        Timeline timer = new Timeline();
        timer.getKeyFrames().add(frame);
        timer.setCycleCount(Animation.INDEFINITE);
        timer.setDelay(duration);
//        timer.play();

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case SPACE:
                        logic.tick();
                        break;
                    case W:
                        logic.robot.moveY(Logic.Direction.N.dy * logic.cellSize);
                        break;
                    case D:
                        logic.robot.moveX(Logic.Direction.E.dx * logic.cellSize);
                        break;
                    case S:
                        logic.robot.moveY(Logic.Direction.S.dy * logic.cellSize);
                        break;
                    case A:
                        logic.robot.moveX(Logic.Direction.W.dx * logic.cellSize);
                        break;
                    case N:
                        logic.reset();
                        break;
                    case P:
                        if (timer.getCurrentRate() == 0.0)
                            timer.play();
                        else
                            timer.pause();
                        break;
                }
            }
        });
    }
}
