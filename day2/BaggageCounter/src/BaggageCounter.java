public class BaggageCounter {
    public static void main(String[] args) {

        //Data Input
        double baggageWeight = 25.13;
        double baggageWeightLimit = 23.0;
        double baggageFeePerKG = 1000.0;

        boolean hasPriorityStatus = false;
        boolean isSecurityCleared = true;

        //1. Arithmetic Operation: Calculate excess weight
        double excessWeight = baggageWeight - baggageWeightLimit;

        //2. Relational Operator: Check if baggage is overweight
        boolean isOverweight = baggageWeight > baggageWeightLimit;

        //3. Assignment and Arithmetic Operation
        double totalExtraFee = 0.0;
        if(isOverweight) {
            totalExtraFee = excessWeight * baggageFeePerKG;
        }

        //4. Logical Operator: Final Clearance
        /*
        Rule: Can only fly if(Not overweight or has priority) AND is security cleared
         */
        boolean canFly = (!isOverweight || hasPriorityStatus) && isSecurityCleared;

        //Output
        System.out.println("Excess Weight: "+excessWeight+ "kg");
        System.out.println("Baggage Fee: "+totalExtraFee);
        System.out.println("Boarding Status: "+canFly);

    }
}
