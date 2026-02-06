import java.util.Iterator;
import java.util.TreeSet;

public class TreeSetDemo {
    static void main(String[] args) {
        TreeSet<String> treeSet = new TreeSet();
        treeSet.add("Saman");
        treeSet.add("Salman");
        treeSet.add("Sachin");
        treeSet.add("Aaron");

        Iterator<String> iterator = treeSet.iterator();
        while (iterator.hasNext()) System.out.println(iterator.next());
    }
}
