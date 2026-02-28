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

    Random random = new Random();

    ArrayList<Particle> particles = new ArrayList<>();
    int width = 1200;
    int height = 600;
    int massLower = 1;
    int massUpper = 100;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, width, height, Color.BLACK);

        addParticles(root);
        animate(root);
        scene.setOnMouseClicked(e -> {
            // Check if the left mouse button was clicked
            if (e.getButton().name().equals("PRIMARY")) {
                addCircleUponClick(root, e.getX(), e.getY());
            }

            if (e.getButton().name().equals("SECONDARY")) {
                addMassUponClick(root, e.getX(), e.getY());
            }

        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Spawns a new particle
     * 
     * @param root
     * @param mass
     * @param vx
     * @param vy
     * @param x
     * @param y
     * @param r
     */
    private void spawnParticle(Group root, double mass, double vx, double vy, double x, double y, double r) {
        Particle p = new Particle(mass, vx, vy, x, y, r);
        Circle circle = new Circle(x, y, r);
        Arrow vect = new Arrow(0, 0, 0, 0);
        p.setForceVector(vect);
        p.setCircle(circle);
        double intensity = Math.min(1.0, Math.abs(mass) / 100.0);
        if (mass > 0) {
            circle.setFill(Color.color(intensity, 0, 1));
        } else {
            circle.setFill(Color.color(1, intensity, 1));
        }
        particles.add(p);
        root.getChildren().addAll(circle, vect);
    }

    private void addParticles(Group root) {

        // Change this later
        for (int i = 0; i < 30; i++) {
            double randMass = random.nextDouble(massLower, massUpper);
            double randVX = random.nextDouble(-2, 2);
            double randVY = random.nextDouble(-2, 2);
            double randX = random.nextDouble(width);
            double randY = random.nextDouble(height);
            double randR = random.nextDouble(0, 15);
            spawnParticle(root, randMass, randVX, randVY, randX, randY, randR);
        }
    }

    private void addMassUponClick(Group root, double x, double y) {
        double randMass = random.nextDouble(500, 10000);
        spawnParticle(root, randMass, 0, 0, x, y, 30);
    }

    private void addCircleUponClick(Group root, double x, double y) {

        double randMass = random.nextDouble(massLower, massUpper);
        double randVX = random.nextDouble(-5, 5);
        double randVY = random.nextDouble(-5, 5);
        spawnParticle(root, randMass, randVX, randVY, x, y, 5);

    }

    private double[] getAcceleration(Particle self) {
        double ax = 0;
        double ay = 0;
        double G = 1;
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

        if (self.getForceVector() != null) {
            double scale = 500; // Adjust scale
            self.getForceVector().updateArrow(
                    self.getX(),
                    self.getY(),
                    self.getX() + (ax * scale),
                    self.getY() + (ay * scale));
        }

        return new double[] { ax, ay };
    }

    private void animate(Group root) {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateParticles(root);
            }
        }.start();
    }

    private void updateParticles(Group root) {
        ArrayList<Particle> toRemove = new ArrayList<>();
        ArrayList<Particle> toMerge = new ArrayList<>();

        double restitution = 0.2;
        int numParticles = particles.size();
        
        // Acceleration matrix
        double[][] accelerations = new double[numParticles][2];
        for (int i = 0; i < numParticles; i++) {
            accelerations[i] = getAcceleration(particles.get(i));
        }

        for (int i = 0; i < numParticles; i++) {
            Particle p = particles.get(i);
            p.setVx(p.getVx() + accelerations[i][0]);
            p.setVy(p.getVy() + accelerations[i][1]);

            // Bounce off walls
            if (p.getX() + p.getVx() >= width) {
                p.setVx(-restitution * p.getVx());
                p.setX(width);
            }
            if (p.getX() + p.getVx() <= 0) {
                p.setVx(-restitution * p.getVx());
                p.setX(0);
            }
            if (p.getY() + p.getVy() >= height) {
                p.setVy(-restitution * p.getVy());
                p.setY(height);
            }
            if (p.getY() + p.getVy() <= 0) {
                p.setVy(-restitution * p.getVy());
                p.setY(0);
            }
            
            // Prevent collisions and handle merging
            for (int j = i + 1; j < numParticles; j++) {
                Particle other = particles.get(j);
                double distX = other.getX() - p.getX();
                double distY = other.getY() - p.getY();
                double dist = Math.hypot(distX, distY);

                double minDist = other.getRadius() + p.getRadius(); 

                if (dist < minDist && !toRemove.contains(p) && !toRemove.contains(other)) {
                    double newMass = p.getMass() + other.getMass();

                    // Merged particle centre of mass
                    double newX = (p.getX() * p.getMass() + other.getX() * other.getMass()) / newMass;
                    double newY = (p.getY() * p.getMass() + other.getY() * other.getMass()) / newMass;

                    // Conservation of momentum
                    double newVx = (p.getVx() * p.getMass() + other.getVx() * other.getMass()) / newMass;
                    double newVy = (p.getVy() * p.getMass() + other.getVy() * other.getMass()) / newMass;

                    Particle merged = new Particle(newMass, newVx, newVy, newX, newY,
                            Math.hypot(p.getRadius(), other.getRadius()));

                    toMerge.add(merged);
                    toRemove.add(p);
                    toRemove.add(other);
                }
            } 

            p.setX(p.getX() + p.getVx());
            p.setY(p.getY() + p.getVy());

            p.getCircle().setCenterX(p.getX());
            p.getCircle().setCenterY(p.getY());
            
        } 

        modifyParticles(root, toMerge, toRemove);
    }

    private void modifyParticles(Group root, ArrayList<Particle> toMerge, ArrayList<Particle> toRemove) {
        for (Particle pRemove : toRemove) {
            particles.remove(pRemove);
            root.getChildren().removeAll(pRemove.getCircle(), pRemove.getForceVector());
        }
        for (Particle p : toMerge) {
            spawnParticle(root, p.getMass(), p.getVx(), p.getVy(), p.getX(), p.getY(), p.getRadius());
        }
    }
}