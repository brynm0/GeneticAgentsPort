
/**
 * Created by Bryn Murrell on 4/27/2017.
 */

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

import static java.lang.Float.POSITIVE_INFINITY;

public class Vehicle {
    private PApplet app;
    private PVector acceleration;
    private PVector velocity;
    PVector position;
    private int r;
    private float maxspeed;
    private float maxforce;
    private float health;
    private float[] dna;

    Vehicle(PApplet _app, float _x, float _y, float[] _dna) {
        app = _app;
        float mutationRate = 0.01f;
        acceleration = new PVector(0,0);
        velocity =  new PVector(0,0);
        position = new PVector(_x, _y);
        r = 4;
        maxspeed = 5f;
        maxforce = 0.5f;
        health = 1f;
        dna = _dna;

        if (dna == null) {
            dna = new float[4];
            for (int i = 0; i < 2; i++) {
                dna[i] = app.random(-2,2);
            }
            for (int i = 2; i < 4; i++) {
                dna[i] = app.random(50,300);
            }
        } else {
            for (int i = 0; i < 2; i++) {
                dna[i] = _dna[i];
                if (app.random(1) < mutationRate) {
                    dna[i] += app.random(-0.1f, 1.0f);
                }
            }
            for (int i = 2; i < 4; i++) {
                dna[i] = _dna[i];
                if (app.random(1) < mutationRate) {
                    dna[i] += app.random(-10, 10);
                }
            }

        }
    }

    void update() {
        health -= 0.005f;
        velocity.add(acceleration);
        velocity.limit(maxspeed);
        position.add(velocity);
        acceleration.mult(0);
    }

    private void applyForce(PVector _force) {
        acceleration.add(_force);
    }

    void behaviors(ArrayList<PVector> _good, ArrayList<PVector> _bad) {
        PVector steerG = eat(_good, 0.2f, dna[2]);
        PVector steerB = eat(_bad, -1, dna[3]);
        steerG.mult(dna[0]);
        steerB.mult(dna[1]);
        applyForce(steerG);
        applyForce(steerB);
    }

    Vehicle reproduce() {
        if (app.random(1) < 0.002f) {
            return new Vehicle(app, position.x + app.random(-50,50), position.y + app.random(-50, 50), dna);
        } else {
            return null;
        }
    }

    private PVector eat(ArrayList<PVector> _list, float _nutrition, float _perception) {
        float record = POSITIVE_INFINITY;
        PVector closest = null;
        for (int i = _list.size() - 1; i >= 0; i--) {
            float d = position.dist(_list.get(i));
            if (d < maxspeed) {
                _list.remove(i);
                health += _nutrition;
            } else if (d < record && d < _perception) {
                record = d;
                closest = _list.get(i);
            }
        }
        if (closest != null && closest.x != 0 && closest.y != 0) {
            return seek(closest);
        } else {
            return wander();
        }
    }
    PVector wander() {
        PVector v = new PVector(app.random(-5,5), app.random(-5,5));
    //    v.limit(maxforce);
        if (app.random(1) > 0.9) {
            return v;
        } else {
            return new PVector();
        }
    }
    private PVector seek (PVector _target) {
        PVector desired = PVector.sub(_target, position);
        desired.setMag(maxspeed);

        //steering = desired - velocity
        PVector steer = PVector.sub(desired, velocity);
        steer.limit(maxforce);

        return steer;
    }

    boolean dead () {
        return (health <= 0);
    }

    void display() {

        float angle = velocity.heading() + 3.14159f / 2;
        app.pushMatrix();



        app.translate(this.position.x, this.position.y);
        //translate(50,50);
        app.rotate(angle);

        int gr = app.color(0,255,0);
        int rd = app.color(255,0,0);
        int col = app.lerpColor(rd,gr,health);
        app.fill(col);
        app.stroke(col);
        app.strokeWeight(1);
        app.beginShape();
        app.vertex(0, -this.r * 2);
        app.vertex(-this.r, this.r * 2);
        app. vertex(this.r, this.r * 2);
        app.endShape(app.CLOSE);
        app.popMatrix();

    }
    void display2() {
        app.fill(255);
        app.stroke(255);
        app.strokeWeight(0);
        app.ellipse(position.x,position.y,1,1);
    }
    void boundaries() {
        if (position.x < 0) {
            position.x = app.width + position.x;
        }
        if (position.x > app.width) {
            position.x = 0 + (app.width - position.x);
        }
        if (position.y < 0) {
            position.y = app.height + position.y;
        }
        if (position.y > app.height) {
            position.y = 0 + (app.height - position.y);
        }

    }


}
