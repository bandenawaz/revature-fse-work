public class ShopApp {
    public static void main(String[] args) {

        //Creating objects
        Product laptop = new Product("Macbook Air",
                940000.00,50);
        Product phone = new Product("iPhone 17",
                150000.00,20);

        Product someOtherProduct = laptop;

        //Lets Access the state and behaviour
        System.out.println("Processing order for "+laptop.productName);
        laptop.reduceStock(5);
        someOtherProduct.productPrice = 195000.00;

        System.out.println("Stock of laptop is :"+laptop.stockQuantity);
        System.out.println("Stock of phones is :"+phone.stockQuantity);
        System.out.println(someOtherProduct.stockQuantity);
        System.out.println(laptop.productPrice);
    }
}
