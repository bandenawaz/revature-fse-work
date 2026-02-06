import java.util.Iterator;
import java.util.LinkedList;

public class LinkedListDemo {
    static void main(String[] args) {

        LinkedList<String> fruits = new LinkedList<>();
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Orange");


        //lets insert a new fruit a first position
        fruits.add(0, "Watermelon");

        //lets iterate the linked list with iterator
        Iterator<String> fruitsIterator = fruits.iterator();
        while (fruitsIterator.hasNext()) {
            String fruit = fruitsIterator.next();
            System.out.println(fruit);
        }
    }
}
