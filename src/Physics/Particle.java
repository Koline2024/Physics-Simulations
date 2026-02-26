package Physics;

import javafx.scene.shape.Circle;

public class Particle {
    double mass;
    double vx;
    double vy;
    double x;
    double y;
    Circle circle;

    public Particle(double mass, double vx, double vy, double x, double y){
        this.mass = mass;
        this.vx = vx;
        this.vy = vy;
        this.x = x;
        this.y = y;
    }

    // Getters and setters

    public Circle getCircle(){
        return circle;
    }

    public void setCircle(Circle c){
        this.circle = c;
    }

    public double getMass(){
        return mass;
    }
    
    public void setMass(double m){
        mass = m;
    }

    public double getVy(){
        return vy;
    }

    public void setVy(double vy){
        this.vy = vy;
    }

    public double getVx(){
        return vx;
    }

    public void setVx(double vx){
        this.vx = vx;
    }

    public double getX(){
        return x;
    }
    
    public void setX(double x){
        this.x = x;
    }

    public double getY(){
        return y;
    }

    public void setY(double y){
        this.y = y;
    }

}
