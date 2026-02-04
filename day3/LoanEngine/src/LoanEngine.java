import java.util.ArrayList;
import java.util.List;

public class LoanEngine {
    public static void main(String[] args) {
        //1. Handling nulls
        //We use Integer because a credit score might not be available yet
        Integer creditScore = null;

        if (creditScore == null){
            System.out.println("Status: Pending. Waiting for the " +
                    "Credit Bureau respones....");
        }

        //2. Utility methods
        String webInput = "75000";

        Integer annualSalary = Integer.parseInt(webInput);

        //3. Collections for loan offers
        //You cannot do this: List<double> loanOffers = new ArrayList<>()
        List<Double> loanOffers = new ArrayList<>();
        loanOffers.add(500000.0); //Autoboxing : double -> Double
        loanOffers.add(10000000.050);
        loanOffers.add(8000000.050);

        System.out.println("Processing Salary ₹: "+annualSalary);
        System.out.println("Available Offers: "+loanOffers.size());

        //Wrapper provide system limits
        System.out.println("Max loan ID possible: "+Integer.MAX_VALUE);

    }
}
