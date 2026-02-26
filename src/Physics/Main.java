package Physics;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.shape.Circle;
import java.util.*;
import javafx.scene.paint.Color;

public class Main extends Application {

    ArrayList<Particle> particles = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, 800, 500, Color.BLACK);

        addCircles(root);
        animate(root);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addCircles(Group root) {
        Random random = new Random();
        // Change this later
        for (int i = 0; i < 3; i++) {
            double randMass = random.nextDouble(100);
            double randVX = random.nextDouble(-2, 2);
            double randVY = random.nextDouble(-2, 2);
            double randX = random.nextDouble(800);
            double randY = random.nextDouble(500);

            Particle p = new Particle(randMass, randVX, randVY, randX, randY);
            Circle toBeAdded = new Circle(p.getX(), p.getY(), 5);
            Color massColour = Color.color(p.getMass() / 100, 0, 1);
            toBeAdded.setFill(massColour);
            p.setCircle(toBeAdded);
            particles.add(p);

            root.getChildren().add(toBeAdded);
        }

    }

    private double[] getAcceleration(Particle self) {
        double ax = 0;
        double ay = 0;
        double G = 5;
        double SOFTENING = 100;

        for (Particle p : particles) {
            if (p.equals(self)) {
                continue;
            }

            double dx = p.getX() - self.getX();
            double dy = p.getY() - self.getY();
            double rSq = dx * dx + dy * dy + SOFTENING;
            double r = Math.sqrt(rSq);

            // F = gmM/r^2, a = F/m = gM/r^2
            double forceMag = G * p.getMass() / rSq;
            // Split into components
            ax += forceMag * (dx / r); // In x dir
            ay += forceMag * (dy / r); // In y dir

        }

        return new double[] { ax, ay };
    }

    private void animate(Group root) {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateParticles();
            }
        }.start();
    }

    private void updateParticles() {
        double restitution = 0.8;
        for (Particle p : particles) {
            double[] acc = getAcceleration(p);

            p.setVx(p.getVx() + acc[0]);
            p.setVy(p.getVy() + acc[1]);

            // Bounce off walls
            if (p.getX() + p.getVx() >= 800) {
                p.setVx(-restitution*p.getVx());
                p.setX(800);
            }
            if (p.getX() + p.getVx() <= 0) {
                p.setVx(-restitution*p.getVx());
                p.setX(0);
            }
            if (p.getY() + p.getVy() >= 500) {
                p.setVy(-restitution*p.getVy());
                p.setY(500);
            }
            if (p.getY() + p.getVy() <= 0) {
                p.setVy(-restitution*p.getVy());
                p.setY(0);
            }



            p.setX(p.getX() + p.getVx());
            p.setY(p.getY() + p.getVy());

            p.getCircle().setCenterX(p.getX());
            p.getCircle().setCenterY(p.getY());
        }
    }

}