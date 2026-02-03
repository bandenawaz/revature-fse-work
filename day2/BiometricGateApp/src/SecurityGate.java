public class SecurityGate {

    public static void main(String[] args) {

        //1. Variable Declaration and Initialisation
        String passengerName = "Amelia Barner";
        int passengerAge = 25;
        double passengerBaggageWeight = 18.5;
        boolean passengerHasBoardingPass = true;
        char gender = 'F';

        //lets do the processing
        System.out.println("Processing Passenger: " + passengerName);
        System.out.println("Checking baggage weight " + passengerBaggageWeight + "kg");

        //Output\
        System.out.println("Passenger: " + passengerName + " is allowed to board the plane: "
                +passengerHasBoardingPass);

    }
}
