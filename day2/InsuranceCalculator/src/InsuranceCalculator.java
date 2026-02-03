import java.util.Scanner;

public class InsuranceCalculator {

    public static void main(String[] args) {
        //Input data: This data comes from database
        //But i will be using it from user input
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Driver Age");
        int driverAge = scanner.nextInt();

        System.out.println("Enter Car type");
        String carType = scanner.next();

        System.out.println("Enter the base premium");
        double basePremium = scanner.nextDouble();

        System.out.println("Enter final premium");
        double finalPremium = scanner.nextDouble();

        /*
        PART 1: IF-ELSE (RANGE BASE LOGIC
        WHY? : Because age is a range(e.g: 0-25), not a fixed number
         */
        if (driverAge < 25){
            System.out.println("Risk Category: Young Driver (High Risk)");
            finalPremium = basePremium + 100.00;

        }else if (driverAge >= 25 && driverAge <= 60){
            System.out.println("Risk Category: Standard (Low Risk)");
            finalPremium = basePremium;
        }else {
            System.out.println("Risk Category: Senior river (Moderate Risk");
            finalPremium = basePremium + 50.00;
        }

        /* PART 2: SWITCH CASE (EQUALITY BASE LOGIC)
        WHY? : Because car type is a fixed value(e.g: Sedan, SUV), not a range
         */
        switch (carType){
            case "LUXURY":
                System.out.println("Adding luxury vehicle surcharge");
                finalPremium += 150.00; //Extra 150$
                break;

            case "SEDAN":
                System.out.println("Adding Standard Sedan rates");
                finalPremium += 10.00;
                break;
            case "SUV":
                System.out.println("Adding SUV safety fee");
                finalPremium += 50.00;
                break;
            default:
                System.out.println("Unknown car type, Applying generic fee");
                finalPremium += 25.00;
                break;
        }

                /*
                PART 3: Final premium calculation

                 */
            System.out.println("------------------------------------");
            System.out.println("Final Premium: " + finalPremium);
            System.out.println("------------------------------------");
        }



}
