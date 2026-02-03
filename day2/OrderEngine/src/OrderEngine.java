public class OrderEngine {
    public static void main(String[] args) {
        
        // --- 1. THE SECURITY GATE (do-while) ---
        // Why: We must ask for the PIN at least once. 
        int systemPin = 1234;
        int enteredPin = 1234; // Simulated input
        int attempts = 0;

        do {
            System.out.println("System: Please enter PIN to start dispatch...");
            attempts++;
            // Logic: System starts if PIN matches
        } while (enteredPin != systemPin && attempts < 3);

        System.out.println(">>> Access Granted. System Online.\n");


        // --- 2. THE DYNAMIC TRUCK UNLOADER (while) ---
        // Why: We don't know if the truck has 0 or 100 boxes. 
        // We stop only when 'boxesInTruck' reaches zero.
        int boxesInTruck = 3; 

        while (boxesInTruck > 0) {
            System.out.println("Unloading Box #" + boxesInTruck + " from truck...");
            boxesInTruck--; // CRITICAL: This is the "Update" step to avoid infinite loops
        }
        System.out.println(">>> Truck is empty. Moving to Order Processing.\n");


        // --- 3. THE CUSTOMER ORDER BATCH (for-each) ---
        // Why: We have a fixed list (Array) of customers. 
        // We want to touch every single name without worrying about index numbers.
        String[] customerOrders = {"Alice", "Bob", "Charlie", "David", "Eve", "Frank"};

        for (String customer : customerOrders) {
            System.out.println("Processing shipping label for: " + customer);
        }
        System.out.println(">>> All " + customerOrders.length + " labels printed.\n");


        // --- 4. THE PRIORITY SUMMARY (for) ---
        // Why: We only want to print a report for the TOP 3 priority customers.
        // We use 'for' because we have a specific start (0) and a specific end (3).
        System.out.println("--- TOP 3 PRIORITY REPORT ---");
        for (int i = 0; i < 3; i++) {
            System.out.println("Priority Slot " + (i + 1) + ": " + customerOrders[i]);
        }
        
        System.out.println("\n*** All tasks completed for the day. ***");
    }
}