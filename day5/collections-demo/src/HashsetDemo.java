import java.util.HashSet;
import java.util.Iterator;

public class HashsetDemo {
    static void main(String[] args) {
        HashSet<String> names =  new HashSet<>();

        names.add("Ganesh");
        names.add("Sai Pallavi");
        names.add("Sam");
        names.add("Mahesh Babu");
        names.add("Saman");

        //lets iterate over the set
        Iterator<String > namesIterator = names.iterator();
        while (namesIterator.hasNext()) {
            String name = namesIterator.next();
            System.out.println(name);
        }
    }
}
