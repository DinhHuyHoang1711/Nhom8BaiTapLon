package MoneyCollected;

public class Coin {
    private int amount;

    public Coin() {
        amount = 500;
    }

    // add money
    public void add(int money){
        amount += money;
    }

    // getter
    public int getAmount() {
        return amount;
    }

    // when use money for buying
    public boolean spend(int cost) {
        if(cost <= amount){
            amount -= cost;
            return true;
        }else{
            return false;
        }
    }
}
