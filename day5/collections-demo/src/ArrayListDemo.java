import java.util.ArrayList;

public class ArrayListDemo {
    static void main(String[] args) {

        //Lets create ArrayList of type String
        ArrayList<String> sports = new ArrayList<>();

        //Adding new elements to the list
        sports.add("Cricket");
        sports.add("Football");
        sports.add("Hockey");



        //insert new sport at index
        sports.add(1, "Basketball");
        //lets print the list
        System.out.println("List of sports played in Revature are: \n");
        for (String sport: sports) {
            System.out.println(sport);
        }

    }


}
