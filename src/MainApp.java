/**
 * Created by Bryn Murrell on 4/27/2017.
 */
import processing.core.PApplet;
import processing.core.PVector;
import java.util.ArrayList;

public class MainApp extends PApplet{

    private ArrayList<Vehicle> vehicles;
    private ArrayList<PVector> food;
    private ArrayList<PVector> poison;
    private int NUM_VEHICLES, NUM_FOOD, NUM_POISON;


    public static void main(String[] args) {
        PApplet.main("MainApp", args);
    }
    public void settings() {
        size(800,800);
    }

    public void setup() {
        NUM_VEHICLES = 500;
        NUM_FOOD = 500;
        NUM_POISON = 200;
       background(51);
        vehicles = new ArrayList<>(NUM_VEHICLES);
        for (int i = 0; i < NUM_VEHICLES; i++) {
            Vehicle tmp = new Vehicle(this,random(width),random(height),null);
            vehicles.add(i, tmp);
        }
        food = new ArrayList<>(NUM_FOOD);
        for (int i = 0; i < NUM_FOOD; i++) {
            PVector tmp = new PVector(random(width), random(height));
            food.add(i, tmp);
        }
        poison = new ArrayList<>(NUM_POISON);

        for (int i = 0; i < NUM_POISON; i++) {
            PVector tmp = new PVector(random(width), random(height));
            poison.add(i, tmp);
        }
    }

    public void draw() {
        fill(51,10);
        rect(0,0,width,height);
        if (random(1) < 0.1) {
            PVector tmp = new PVector(random(width), random(height));
            food.add(tmp);
        }

        if (random(1) < 0.1) {
            PVector tmp = new PVector(random(width), random(height));
            poison.add(tmp);
        }

        /*
        for (PVector item : poison) {

            fill(255,0,0);
            noStroke();
            ellipse(item.x, item.y, 4, 4);
        }
        for (PVector item : food) {
            fill(0,255,0);
            noStroke();
            ellipse(item.x, item.y, 4, 4);
        }
        */
        for (int i = vehicles.size() - 1; i >= 0; i--) {
            vehicles.get(i).boundaries();
            vehicles.get(i).behaviors(food, poison);
            vehicles.get(i).update();
            //vehicles.get(i).display();
            vehicles.get(i).display2();


            Vehicle newVehicle = vehicles.get(i).reproduce();
            if (newVehicle != null) {
                vehicles.add(newVehicle);
            }

            if (vehicles.get(i).dead()) {
                PVector tmp = new PVector(vehicles.get(i).position.x, vehicles.get(i).position.y);
                food.add(tmp);
                vehicles.remove(i);
            }
        }
    }
    public void mousePressed() {
        //vehicles.add(new Vehicle(this, mouseX, mouseY, null));
        saveFrame("name-#####.png");
        println("Frame saved!");
    }

    public void keyPressed() {
        if (key == 'p') {
            saveFrame("name-#####.png");
            println("Frame saved!");
        }
    }
}
