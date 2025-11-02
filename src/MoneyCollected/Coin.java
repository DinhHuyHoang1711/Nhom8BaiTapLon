package MoneyCollected;

public class Coin {
    private int amount; // Số tiền hiện có

<<<<<<< HEAD
    // Tài sản
=======
    // Constructor mặc định, gán tiền ban đầu = 2200
>>>>>>> 3df1daac4675939fcddf088ad4827cd6b09283a8
    public Coin() {
        amount = 2200;
    }

<<<<<<< HEAD
    // Copy
=======
    // Constructor sao chép (copy constructor)
>>>>>>> 3df1daac4675939fcddf088ad4827cd6b09283a8
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
