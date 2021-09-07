package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;

public class Main extends Application {
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;
    private static ArrayList<OrbitingBody> solarSystem = new ArrayList<OrbitingBody>(100);
    private static GraphicsContext gc;
    private static double t = 0;
    private static HashMap<String, CartVector> posMap = new HashMap<String, CartVector>();
    private static int focus = 0;
    private static int zoom = 50;
    private static double timeStep = 0.00001;
    private static boolean showLines = true;
    private static CircleArray tracers;
    private static boolean showTracers = false;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        StackPane root = new StackPane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        tracers = new CircleArray(solarSystem.size() * 700);

        canvas.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override public void handle(ScrollEvent event) { handleScroll(event); }
        });

        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            handleKeyEvent(key.getCode());
        });

        new AnimationTimer() {
            @Override
            public void handle(long now) {
              drawPlanets();
              t += timeStep;
            }
        }.start();

        root.getChildren().add(canvas);
        primaryStage.setTitle("Orbit Sim");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Main Animation loop that draws planets and labels to the screen
     */
    private void drawPlanets() {
        gc.setFill(Color.web("#1f2f3f"));
        gc.fillRect(0, 0, WIDTH, HEIGHT);
        gc.setFill(Color.WHITESMOKE);
        gc.setStroke(Color.WHITESMOKE);

        // update each planet's position and put it in the hashmap
        for (OrbitingBody planet : solarSystem) {
            if (planet.getParent() != null) {
                planet.updatePosition(t);
                CartVector pos = planet.getPosition();
                CartVector parentPos = posMap.get(planet.getParent().getName());
                posMap.put(planet.getName(), pos.addTo(parentPos));
            }
            else {
                posMap.put(planet.getName(), planet.getPosition());
            }
        }

        OrbitingBody focusBody = solarSystem.get(focus);
        CartVector focusPos = posMap.get(focusBody.getName());
        int focusDepth = focusBody.getDepth();

        // plot each planet to the screen
        for (OrbitingBody planet : solarSystem) {   
            CartVector pos = posMap.get(planet.getName());
            pos = pos.subtract(focusPos);
            pos = pos.scale(zoom);
            pos = pos.addTo(new CartVector(WIDTH / 2, HEIGHT / 2));
            gc.fillOval(pos.getX(), pos.getY(), 10, 10);

            // add current position to the tracers array
            tracers.add(new CartVector(pos.getX() + 5, pos.getY() + 5));

            // only plot the moon labels if we're focused on their parent or lower
            int relativeDepth = planet.getDepth() - focusDepth;
            if ( relativeDepth <= 1)
                gc.fillText(planet.getName(), pos.getX(), pos.getY());
            
            // plot parent lines
            if (showLines && planet.getParent() != null) {
                drawLines(pos, focusPos, planet);
            }
        }

        // draw the tracers to the screen if we need to
        if (showTracers)
            tracers.draw(gc);
    }

    /**
     * Draws lines connecting planets to their parent bodies.
     * @param pos is the position of the current planet on the screen
     * @param focusPos is the position of the planet of focus
     * @param planet is the current planet object
     */
    private void drawLines(CartVector pos, CartVector focusPos, OrbitingBody planet) {
        CartVector parentPos = posMap.get(planet.getParent().getName());
        parentPos = parentPos.subtract(focusPos);
        parentPos = parentPos.scale(zoom);
        parentPos = parentPos.addTo(new CartVector(WIDTH / 2, HEIGHT / 2));

        gc.strokeLine(parentPos.getX() + 5, parentPos.getY() + 5, pos.getX() + 5, pos.getY() + 5);
    }

    /**
     * Handler for key presses. Modifies focus and timeStep depending on the key press
     * @param key is the current key being pressed. KeyCode Enum type from JavaFX
     */
    private void handleKeyEvent(KeyCode key) {
        switch(key) {
            case LEFT:
                focus--;
                if (focus < 0)
                    focus = solarSystem.size() - 1;
                tracers.clear();
                break;
            case RIGHT:
                focus++;
                if (focus >= solarSystem.size()) 
                    focus = 0;
                tracers.clear();
                break;
            case UP:
                timeStep *= 10;
                break;
            case DOWN:
                timeStep /= 10;
                break;
            case SPACE:
                showLines = !showLines;
                break;
            case ENTER:
                showTracers = !showTracers;
                break;
        }
    }

    /**
     * Method that smoothes out the scroll and handles the exponential nature of planetary distance
     * @param event is the scrollEvent being fired
     */
    private void handleScroll(ScrollEvent event) {
        if (zoom > 1000) {
            zoom += 70 * event.getDeltaY();
        } else if (zoom < 20) {
            zoom += 0.1 * event.getDeltaY();
        } else {
            zoom += event.getDeltaY();
        }
        if (zoom < 0) {
            zoom = 0;
        }
        tracers.clear();
    }

    public static void main(String[] args) {
        FileManager.LoadSystem("Planets.txt", solarSystem);
        launch(args);
    }
}
