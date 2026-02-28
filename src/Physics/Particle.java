package Physics;

import javafx.scene.shape.Circle;

public class Particle {
    double mass;
    double vx;
    double vy;
    double x;
    double y;
    double rad;
    Circle circle;
    Arrow netForceVector;

    public Particle(double mass, double vx, double vy, double x, double y, double rad){
        this.mass = mass;
        this.vx = vx;
        this.vy = vy;
        this.x = x;
        this.y = y;
        this.rad = rad;
    }

    // Getters and setters

    public Circle getCircle(){
        return circle;
    }

    public void setCircle(Circle c){
        this.circle = c;
    }

    public Arrow getForceVector(){
        return netForceVector;
    }

    public void setForceVector(Arrow v){
        netForceVector = v;
    }

    public double getRadius(){
        return rad;
    }

    public void setRadius(double r){
        rad = r;
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
