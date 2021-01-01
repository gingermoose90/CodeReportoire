import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class BallMultithreadAnimation extends Application {
  private final double radius = 20;
  private double x = radius, y = radius;
  private volatile double dx = 1, dy = 1;
  private volatile long speed = 10;
  private Circle circle = new Circle(x, y, radius);
  private Label speedLabel = new Label();
  private Pane pane;
  private Thread moveBall;
  
  /**
   * This program uses multithreading to animate a ball moving across a window and rebounding off the borders of
   * the window. The user can control the speed up the ball's movement by pressing the UP or DOWN arrow keys.
   * 
   */

  @Override
  public void start(Stage primaryStage) {
    pane = new Pane();
    circle.setFill(Color.GREEN);
    pane.getChildren().add(circle);
    speedLabel = new Label("Speed is " + speed);
    pane.getChildren().add(speedLabel);

    /**
     * This thread is responsible for updating the position of the ball with each frame. First,
     * boundary conditions are tested to check if the ball has reached the margin of the <CODE>pane
     * </CODE>. If so, then the direction of the next x and/or y coords are multiplied by -1 to
     * reverse their direction. The <CODE>dx</CODE> variable represents the distance the circle
     * object will move in the next frame along the x-axis. Likewise for the <CODE>dy</CODE>
     * variable.
     */
    moveBall =
        new Thread(
            () -> {
              try {
                while (true) {
                  if (x < radius || x > pane.getWidth() - radius) {
                    dx *= -1;
                  }
                  if (y < radius || y > pane.getHeight() - radius) {
                    dy *= -1;
                  }
                  x += dx;
                  y += dy;

                  Platform.runLater(() -> circle.setCenterX(x));
                  Platform.runLater(() -> circle.setCenterY(y));

                  Thread.sleep(1);
                }
              } catch (InterruptedException ex) {
              }
            });
    moveBall.start();

    /**
     * The UP and DOWN keys control the movement speed of the ball This is implemented by doubling
     * or halving the distance the ball moves with each frame with the <CODE>dx</CODE> and <CODE>dy
     * </CODE> variables
     */
    pane.setOnKeyPressed(
        e -> {
          new Thread(
                  () -> {
                    Platform.runLater(
                        () -> {
                          if (e.getCode() == KeyCode.UP) {
                            dx *= 2;
                            dy *= 2;
                            speed++;
                            ;
                          } else if (e.getCode() == KeyCode.DOWN && dx != 1 && dy != 1) {
                            dx /= 2;
                            dy /= 2;
                            speed--;
                          }
                          speedLabel.setText("Speed is " + speed);
                        });
                  })
              .start();
        });

    Scene scene = new Scene(pane, 200, 300);
    primaryStage.setTitle("Ball Multithread Animation");
    primaryStage.setScene(scene);
    primaryStage.show();
    pane.requestFocus();
  }

  public static void main(String[] args) {
    Application.launch(args);
  }
}
