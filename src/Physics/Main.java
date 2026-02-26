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
        scene.setOnMouseClicked(e -> {
            // Check if the left mouse button was clicked
            if (e.getButton().name().equals("PRIMARY")) {
                addCircleUponClick(root, e.getX(), e.getY());
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addCircles(Group root) {
        Random random = new Random();
        // Change this later
        for (int i = 0; i < 100; i++) {
            double randMass = random.nextDouble(0, 100);
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

    private void addCircleUponClick(Group root, double x, double y) {
        Random random = new Random();

        double randMass = random.nextDouble(0, 100);
        double randVX = random.nextDouble(-2, 2);
        double randVY = random.nextDouble(-2, 2);

        Particle p = new Particle(randMass, randVX, randVY, x, y);
        Circle toBeAdded = new Circle(p.getX(), p.getY(), 5);
        Color massColour = Color.color(p.getMass() / 100, 0, 1);
        toBeAdded.setFill(massColour);
        p.setCircle(toBeAdded);
        particles.add(p);

        root.getChildren().add(toBeAdded);

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
        int numParticles = particles.size();
        // Acceleration matrix
        double[][] accelerations = new double[numParticles][2];
        for (int i = 0; i < numParticles; i++) {
            // Find acceleration for every single particle
            accelerations[i] = getAcceleration(particles.get(i));
        }

        for (int i = 0; i < numParticles; i++) {
            Particle p = particles.get(i);
            p.setVx(p.getVx() + accelerations[i][0]);
            p.setVy(p.getVy() + accelerations[i][1]);

            // Bounce off walls
            if (p.getX() + p.getVx() >= 800) {
                p.setVx(-restitution * p.getVx());
                p.setX(800);
            }
            if (p.getX() + p.getVx() <= 0) {
                p.setVx(-restitution * p.getVx());
                p.setX(0);
            }
            if (p.getY() + p.getVy() >= 500) {
                p.setVy(-restitution * p.getVy());
                p.setY(500);
            }
            if (p.getY() + p.getVy() <= 0) {
                p.setVy(-restitution * p.getVy());
                p.setY(0);
            }
            // Prevent collisions between particles, don't count same one twice
            for (int j = i + 1; j < numParticles; j++) {
                Particle other = particles.get(j);
                double distX = other.getX() - p.getX();
                double distY = other.getY() - p.getY();
                double dist = Math.hypot(distX, distY);

                double minDist = 10; // Each ball has radius 5
                if (dist < minDist && dist > 0) {
                    double overlap = 0.5 * (minDist - dist);
                    p.setX(p.getX() - overlap * (distX / dist));
                    p.setY(p.getY() - overlap * (distY / dist));
                    other.setX(other.getX() + overlap * (distX / dist));
                    other.setY(other.getY() + overlap * (distY / dist));
                    double p1NewVx = (p.getVx() * (p.getMass() - other.getMass())
                            + (2 * other.getMass() * other.getVx())) / (p.getMass() + other.getMass());
                    double p1NewVy = (p.getVy() * (p.getMass() - other.getMass())
                            + (2 * other.getMass() * other.getVy())) / (p.getMass() + other.getMass());

                    double p2NewVx = (other.getVx() * (other.getMass() - p.getMass()) + (2 * p.getMass() * p.getVx()))
                            / (p.getMass() + other.getMass());
                    double p2NewVy = (other.getVy() * (other.getMass() - p.getMass()) + (2 * p.getMass() * p.getVy()))
                            / (p.getMass() + other.getMass());

                    p.setVx(p1NewVx * restitution);
                    p.setVy(p1NewVy * restitution);
                    other.setVx(p2NewVx * restitution);
                    other.setVy(p2NewVy * restitution);
                }
            }

            p.setX(p.getX() + p.getVx());
            p.setY(p.getY() + p.getVy());

            p.getCircle().setCenterX(p.getX());
            p.getCircle().setCenterY(p.getY());
        }
    }

}