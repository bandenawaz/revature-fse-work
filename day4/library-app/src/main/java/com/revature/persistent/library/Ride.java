package com.revature.persistent.library;

//1. ABSTRACTION: The Generic Concept
abstract class Ride {

    private double distance;  //ENCAPSULATION: Hidden data

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {

        if (distance > 0) {
            this.distance = distance;
        }
    }

    //Abstract Method
    public abstract double calculateFare();

    //3. Overloading: Same name, different inputs
    public void requestRide(String destination){
        System.out.println("Finding ride to "+destination);
    }

    public void requestRide(String destination, int passengers){
        System.out.println("Finding ride for "+passengers+" people to "+destination);
    }
}
