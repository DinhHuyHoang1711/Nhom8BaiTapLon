package MoneyCollected;

public class Coin {
    private int amount; // Số tiền hiện có


    public Coin() {
        amount = 2200;
    }


    // Constructor sao chép (copy constructor)

    public Coin(Coin another) {
        this.amount = another.getAmount();
    }

    // Thêm tiền
    public void add(int money) {
        amount += money;
    }

    // Lấy số tiền hiện tại
    public int getAmount() {
        return amount;
    }

    // Trừ tiền khi mua, trả về true nếu đủ tiền
    public boolean spend(int cost) {
        if (cost <= amount) {
            amount -= cost;
            return true;
        } else {
            return false;
        }
    }
}
