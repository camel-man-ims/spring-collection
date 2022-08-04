package hello.singleton;

public class StatefulService {
    private int price;

    public void order(String name, int price){
        System.out.println("name = " + name + " price " + price);
        // 문제 발생
        this.price=price;
    }

    public int getPrice(){
        return price;
    }
}
