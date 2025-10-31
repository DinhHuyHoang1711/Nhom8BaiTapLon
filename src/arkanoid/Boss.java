package arkanoid;

public class Boss extends arkanoid.GameObject {
    private static final int BOSS_WIDTH = 160;
    private static final int BOSS_HEIGHT = 160;
    private static final int PLAYFRAME_WIDTH = 800;
    private static final int PLAYFRAME_HEIGHT = 700;

    // Đường dẫn ảnh mặc định cho từng nguyên tố
    public static final String FIRE_IMG = "img/Boss/FireBoss3.png";
    public static final String WATER_IMG = "img/Boss/WaterBoss.png";
    public static final String WIND_IMG = "img/Boss/WindBoss.png";
    public static final String EARTH_IMG = "img/Boss/EarthBoss.png";

    private final String element;
    private int maxHp = 500;
    private int hp;


    /**
     * Tạo Boss theo element.
     */
    public Boss(String element, int maxHp) {
        super(0, 0, BOSS_WIDTH, BOSS_HEIGHT, 0, 0, pickImage(element));
        this.element = (element == null ? "fire" : element.toLowerCase());
        this.maxHp = maxHp;
        this.hp = maxHp;
    }

    /**
     * Tạo Boss theo ảnh.
     */
    public Boss(String element, String spritePath) {
        super(0, 0, BOSS_WIDTH, BOSS_HEIGHT, 0, 0, spritePath);
        this.element = (element == null ? "fire" : element.toLowerCase());
        this.maxHp = 5000;
        this.hp = maxHp;
    }

    /**
     * Chọn ảnh theo element.
     */
    private static String pickImage(String element) {
        String e = (element == null ? "fire" : element.toLowerCase());
        switch (e) {
            case "water":
                return WATER_IMG;
            case "wind":
                return WIND_IMG;
            case "earth":
                return EARTH_IMG;
        }
        return FIRE_IMG; // mặc định
    }

    // ==== HP API ====
    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int v) {
        maxHp = Math.max(1, v);
        hp = Math.min(hp, maxHp);
    }

    private int iFrames = 0;

    public void tick() {
        if (iFrames > 0) iFrames--;
    }

    /**
     * Gây sát thương dương. Trả về true nếu đã chết sau khi trừ.
     */
    public boolean takeDamage(int dmg) {
        if (iFrames > 0) return false;
        hp = Math.max(0, hp - Math.max(1, dmg));
        iFrames = 6;
        return hp == 0;
    }

    /**
     * Tiện cho win condition.
     */
    public boolean isDead() {
        return hp <= 0;
    }

    // ==== FIRE RAIN ====
    private static final int FB_SIZE = 100;         // Kích thước Fire Ball
    private static final int FB_VY_BASE = 5;        // Vận tốc rơi
    private static final int COLS = 12;             // Số cột chia ngang màn để spawn ball
    private static final int SPAWN_INTERVAL = 40;   // Số tick giữa 2 quả liên tiếp trong 1 đợt
    private static final int RAIN_WAVES = 3;        // Số đợt mưa trong một lần kích hoạt skill
    private static final int RAIN_GAP = 30;         // Nghỉ giữa 2 đợt
    private static final int COOLDOWN = 240;        // Hồi chiêu sau khi kết thúc toàn bộ đợt
    private static final int MAX_ACTIVE = 3;        // Giới hạn tối đa số Fire Ball đồng thời tồn tại trên màn hình

    private final java.util.List<Fireball> fireballs = new java.util.ArrayList<>();
    private final java.util.Random rng = new java.util.Random();
    private boolean skillOn = false;
    private int waveLeft = 0;
    private int gap = 0;
    private int tickInWave = 0;
    private int spawnIdx = 0;
    private int cooldown = 0;
    private int[] colOrder = null;

    public void activateFireRain() {
        if (!"fire".equals(element)) return;
        if (skillOn || cooldown > 0) return;
        skillOn = true;
        waveLeft = RAIN_WAVES;
        gap = 0;
        tickInWave = 0;
        spawnIdx = 0;
        colOrder = shuffledCols();
    }

    public void tickSkill(int playW, int playH) {
        // luôn cập nhật đạn đang tồn tại
        for (int i = 0; i < fireballs.size(); ) {
            Fireball f = fireballs.get(i);
            f.y += f.vy;                  // rơi thẳng, không có vx
            if (f.y > playH) {            // swap-remove
                fireballs.set(i, fireballs.get(fireballs.size() - 1));
                fireballs.remove(fireballs.size() - 1);
            } else i++;
        }

        // giảm cooldown
        if (cooldown > 0) cooldown--;

        // sinh đạn chỉ khi đang bật skill
        if (!skillOn) return;

        if (spawnIdx < COLS) {
            if (tickInWave % SPAWN_INTERVAL == 0 && fireballs.size() < MAX_ACTIVE) {
                int colW = Math.max(1, (playW - FB_SIZE) / COLS);
                int col = colOrder[spawnIdx++];
                int baseX = col * colW + (colW - FB_SIZE) / 2;
                int jitter = rng.nextInt(Math.max(1, colW / 3)) - colW / 6;
                int fx = clamp(baseX + jitter, 0, playW - FB_SIZE);
                int fy = -rng.nextInt(playH);            // cao hơn để rơi lâu
                int vy = FB_VY_BASE + rng.nextInt(2);
                fireballs.add(new Fireball(fx, fy, vy));
            }
            tickInWave++;
        } else {
            // hết một đợt -> nghỉ rồi đợt mới
            if (gap == 0) {
                waveLeft--;
                if (waveLeft <= 0) {
                    skillOn = false;
                    cooldown = COOLDOWN;
                } else {
                    gap = RAIN_GAP;
                    tickInWave = 0;
                    spawnIdx = 0;
                    colOrder = shuffledCols();
                }
            } else gap--;
        }
    }

    private int[] shuffledCols() {
        int[] a = new int[COLS];
        for (int i = 0; i < COLS; i++) a[i] = i;
        for (int i = COLS - 1; i > 0; i--) {
            int j = rng.nextInt(i + 1);
            int t = a[i];
            a[i] = a[j];
            a[j] = t;
        }
        return a;
    }

    private static int clamp(int v, int lo, int hi) {
        return v < lo ? lo : (v > hi ? hi : v);
    }

    private static final class Fireball extends arkanoid.GameObject {
        final int vy;

        Fireball(int x, int y, int vy) {
            super(x, y, FB_SIZE, FB_SIZE, 0, vy, "img/Boss/FireBall.png");
            this.vy = vy; // chỉ dùng vy, không trôi ngang
        }
    }

    public java.util.List<? extends arkanoid.GameObject> getFireballs() {
        return fireballs;
    }

    public String getElement() {
        return element;
    }

    protected static Boss makeBossForLevel(int level) {
        switch (level) {
            case 1:
                return new Boss("fire", 5000);
            case 5:
                return new Boss("water", 5000);
            case 8:
                return new Boss("wind", 5000);
            case 12:
                return new Boss("earth", 5000);
            default:
                return null; // các level khác không có boss
        }
    }
}
