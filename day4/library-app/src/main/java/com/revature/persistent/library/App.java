package com.revature.persistent.library;

public class App 
{
    public static void main( String[] args )
    {
        //PLOYMORPHISM
        Ride myRide;
        boolean isPremium = true;

        if (isPremium){
            myRide = new UberBlack(); //OneForm
        }else {
            myRide = new UberX(); // Second form
        }

        //lets set the distance
        myRide.setDistance(10.5);

        //Overriding in Action
        System.out.println("Your total is $"+myRide.calculateFare());

        //Overloading in Action
        myRide.requestRide("Airport");
        myRide.requestRide("Airport", 6);

    }
}
