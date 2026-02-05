package com.revature.persistent.library;
// INHERITANCE & OVERRIDING
public class UberX extends Ride{

    @Override
    public double calculateFare() {
        return getDistance() * 1.5;  //Standard fare
    }
}
