public class AverageFinder {
    public static void main(String[] args) {

        System.out.println("Average Finder v0.1");
        double average = findAverage(args);
        System.out.println("The average is: "+average);

    }

    private static double findAverage(String[] input){

        double result = 0;

        for(String str : input){
            result += Integer.parseInt(str);
        }
        return result/input.length;
    }
}
