package PowerUp;

import MoneyCollected.Coin;

public class CoinBonus extends PowerUp {
    private static final int COIN_BONUS = 50;      // Số xu nhận được mỗi lần nhặt
    private final Coin amount;  // Tham chiếu đến tài sản

    // Constructor
    public CoinBonus(int x, int y, Coin amount) {
        super(x, y, 40, 40, "img/coin.png", PowerUpType.COIN, 0);
        this.amount = amount;
    }

    @Override
    public void applyEffect() {
        amount.add(COIN_BONUS); // Cộng xu khi nhặt
    }

    @Override
    public void removeEffect() {
        //hello.
    }
}
