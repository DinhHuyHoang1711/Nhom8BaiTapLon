package MoneyCollected;

public class Coin {
    private int amount;

    // Tài sản
    public Coin() {
        amount = 2200;
    }

    // Copy
    public Coin(Coin another) {
        this.amount = another.getAmount();
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
