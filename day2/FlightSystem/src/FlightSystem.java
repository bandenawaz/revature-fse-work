public class FlightSystem {
    public static void main(String[] args) {

        //Step 1: Initialise the array with pasenger names
        String[] passengers = {"Abhishek", "Ganesh","Varshini","Nikhil",
        "Rajat"};

        //Step2: Access the array size using length
        System.out.println("Total seats: "+passengers.length);

        //step 3: Iterate through the array (Industry standards)
        for (int i = 0; i < passengers.length; i++){
            System.out.println("Seat "+ (i+1) + " : "+passengers[i]);
        }

        //Step 4: Updating the element
        System.out.println("\nUpdate: Nikhil cancelled. Adding Nawaz");
        passengers[3] = "Nawaz";
        System.out.println("New Passenger at Seat 4: "+passengers[3]);

    }
}
