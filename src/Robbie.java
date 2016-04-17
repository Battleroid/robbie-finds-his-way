import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

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
                }
            }
        });
    }
}
