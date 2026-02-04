public class FlightSeating {
    public static void main(String[] args) {
        //1. Create a grid of seats 3 rows and 2 columns
        String[][] seatingChart = new String[3][2];

        //2.Lets fill the seats with rows and columns
        seatingChart[0][0] = "VIP: Mr. Smith"; //Row 1, Seat A
        seatingChart[0][1] = "VIP: Mrs. Smith"; //Row 1, Seat B
        seatingChart[1][0] = "Empty"; //Row 2, Seat A
        seatingChart[1][1] = "Capt. Rogers"; //Row 2, Seat B
        seatingChart[2][0] = "Dr. Strange"; //Row 3, Seat A
        seatingChart[2][1] = "Empty"; //Row 3, Seat B

        //3. Find and exact passenger (Row 3, Seat A)
        System.out.println("\n Seat 3A belongs to : "+seatingChart[2][0]);

        //4: Process the whole plane, print all the passengers in the list
        for (int row = 0; row < seatingChart.length; row++){
            for (int col = 0; col < seatingChart[row].length; col++){
                System.out.println("Row " + (row+1) + ", Seat " + (col+1) + ": "
                        + seatingChart[row][col]);

            }
        }

    }
}
