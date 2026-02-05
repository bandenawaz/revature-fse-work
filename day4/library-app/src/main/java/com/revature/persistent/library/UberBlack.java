package com.revature.persistent.library;

public class UberBlack extends Ride{
    @Override
    public double calculateFare() {
        return getDistance() * 4.0;  //Premium fare + luxury fee
    }
}
