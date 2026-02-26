package Physics;

import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
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
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Region simulate() {
        VBox results = new VBox(setParticles());
        results.setAlignment(Pos.CENTER);
        return results;
    }

    private Group setParticles() {
        Group root = new Group();
        for (Particle p : particles) {
            Circle toBeAdded = new Circle(p.getX(), p.getY(), 10);
            Color massColour = Color.color(p.getMass()/100, 0.2, p.getMass()/200);
            toBeAdded.setFill(massColour);
            root.getChildren().add(toBeAdded);
        }
        return root;
    }

    private void addCircles(Group root) {
        Random random = new Random();
        // Change this later
        for (int i = 0; i < 100; i++) {
            double randMass = random.nextDouble(100);
            double randVX = random.nextDouble(10);
            double randVY = random.nextDouble(10);
            double randX = random.nextDouble(800);
            double randY = random.nextDouble(500);

            Particle p = new Particle(randMass, randVX, randVY, randX, randY);
            Circle toBeAdded = new Circle(p.getX(), p.getY(), 5);
            Color massColour = Color.color(p.getMass()/100, 0, 0);
            toBeAdded.setFill(massColour);
            p.setCircle(toBeAdded);
            particles.add(p);
            
            root.getChildren().add(toBeAdded);
        }

    }

    private Vector<Double> getForces(Particle self){
        Vector<Double> netForce = new Vector<>();
        
        for (Particle p : particles){
            if (p.equals(self)){
                continue;
            }
            Vector<Double> rVect = new Vector<>();
            rVect.add(p.getX()-self.getX());
            rVect.add(p.getY()-self.getY());
            double r = Math.hypot(p.getX()-self.getX(), p.getY()-self.getY());
            // F = mM/r^3 * r vector
            double gravForceConst = p.getMass()*self.getMass()/(Math.pow(r, 3));
            
            
        }
    }

    private void animate(Group root){
        new AnimationTimer() {
            @Override
            public void handle(long arg0) {
                for(Particle p : particles){

                }
                
            }
        };
    }

}