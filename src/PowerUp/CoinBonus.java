package PowerUp;

import MoneyCollected.Coin;

public class CoinBonus extends PowerUp {
    private static final int COIN_BONUS = 10;
    private final Coin amount;

    public CoinBonus(int x, int y, Coin amount) {
        super(x, y, 40, 40, "img/coin.png", PowerUpType.COIN, 0);
        this.amount = amount;
    }

    @Override
    public void applyEffect() {
        amount.add(COIN_BONUS);
    }

    @Override
    public void removeEffect() {
        //hello.
    }
}
