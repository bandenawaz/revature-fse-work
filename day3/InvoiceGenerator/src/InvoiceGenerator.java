public class InvoiceGenerator {

    public static void main(String[] args) {

        StringBuilder invoice = new StringBuilder("---- Online Store Invoice ----\n");

        //.append: adds to the end of the existing buffer
        invoice.append("Item: Laptop    | Price: ₹50000\n");
        invoice.append("Item: Mouse     | Price: ₹1500\n");

        //.insert: aloows you to add data at a specific position
        invoice.insert(0, "CONFIDENTIAL");

        //.replace: change a specific range
        int start = invoice.indexOf("Laptop");
        invoice.replace(start, start + 6, "Macbook Pro");

        //Lets convert the builder to string
        String finalInvoice = invoice.toString();
        System.out.println(finalInvoice);
    }
}
