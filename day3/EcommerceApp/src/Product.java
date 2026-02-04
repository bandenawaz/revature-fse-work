public class Product {

    //1. Fields
    String productName;
    double productPrice;
    int stockQuantity;

    //2. Constructor:
    public Product(String productName,double productPrice, int stockQuantity){
        this.productName = productName;
        this.productPrice = productPrice;
        this.stockQuantity = stockQuantity;
    }

    //3. Method
    public void reduceStock(int quantity){
        this.stockQuantity -= quantity;
        System.out.println("Inventory Updated for "+productName
        +". New Stock :"+stockQuantity);
    }
}
