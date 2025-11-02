package MoneyCollected;

public class Coin {
    private static int totalAmount = 0;

    public Coin() {

    }

    // add money
    public void add(int money){
        totalAmount += money;
    }

    // getter
    public int getAmount() {
        return totalAmount;
    }

    // when use money for buying
    public boolean spend(int cost) {
        if(cost <= totalAmount){
            totalAmount -= cost;
            return true;
        }else{
            return false;
        }
    }
}
