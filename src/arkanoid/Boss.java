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
        if (invulnerable) return false;
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

        // cập nhật sóng
        for (int i = waves.size() - 1; i >= 0; i--) {
            GameObject w = waves.get(i);
            w.setY(w.getY() + 3);                  // tốc độ rơi
            if (w.getY() > playH) waves.remove(i);
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

    // ==== WATER WAVE ====
    private int waveCooldown = 0;                      // tick
    private static final int WAVE_COOLDOWN_TICKS = 120; // ~2s nếu TICK_MS=16
    private static final int MAX_WAVES = 10;           // trần số wave đang sống
    private static final int WAVE_GAP = 20;            // khoảng cách tối thiểu theo trục X

    private final java.util.ArrayList<GameObject> waves = new java.util.ArrayList<>();
    private final java.util.Random rnd = new java.util.Random();

    public java.util.List<? extends GameObject> getWaves() {
        return waves;
    }

    // Gọi mỗi tick khi boss element = "water"
    public void maybeActivateWaveRain(int playWidth, int brickW, int brickH) {
        if (waveCooldown > 0) {
            waveCooldown--;
            return;
        }
        if (waves.size() >= MAX_WAVES) return;

        // batch nhỏ: 2–3 viên, có spacing + stagger
        int batch = 2 + rnd.nextInt(2);
        spawnWaveBatchSafe(batch, playWidth, brickW, brickH);
        waveCooldown = WAVE_COOLDOWN_TICKS;
    }

    private void spawnWaveBatchSafe(int count, int playWidth, int brickW, int brickH) {
        // snapshot X để tránh dính nhau
        int n = waves.size();
        int[] xs = new int[n];
        for (int i = 0; i < n; i++) xs[i] = waves.get(i).getX();

        int added = 0, attempts = 0;
        while (added < count && attempts < 50) {
            attempts++;
            int x = rnd.nextInt(Math.max(1, playWidth - brickW));
            boolean ok = true;
            for (int xi : xs) {
                if (Math.abs(x - xi) < brickW + WAVE_GAP) {
                    ok = false;
                    break;
                }
            }
            if (!ok) continue;

            GameObject w = new GameObject(
                    x,                 // x
                    -brickH - 8,       // y spawn trên đỉnh
                    brickW,            // width
                    brickH,            // height
                    0,                 // dx
                    3,                 // dy rơi
                    "img/brick/WaveWater.png"  // imagePath (không bắt buộc nếu vẽ bằng layer)
            );

            w.setY(w.getY() - 6 * added); // stagger dọc
            waves.add(w);
            xs = java.util.Arrays.copyOf(xs, xs.length + 1);
            xs[xs.length - 1] = x;
            added++;
            if (waves.size() >= MAX_WAVES) break;
        }
    }

    /**
     * Cập nhật vị trí và dọn wave. Gọi mỗi tick khi element="water".
     */
    public void tickWaves(int playWidth, int playHeight) {
        for (int i = waves.size() - 1; i >= 0; i--) {
            GameObject w = waves.get(i);
            w.setY(w.getY() + w.getDy());   // dùng dy đã set = 3
            if (w.getY() > playHeight) {
                waves.set(i, waves.get(waves.size() - 1));
                waves.remove(waves.size() - 1);
            }
        }
    }

    // ==== EARTH BULWARK (spawn stone bricks, boss bất tử đến khi phá hết) ====
    private boolean invulnerable = false;        // chặn sát thương khi earth wave còn sống
    private int earthWaveId = 0;                 // id của đợt đang active
    private int earthCooldown = 90;               // đếm lùi hồi chiêu
    private boolean earthActive = false;         // đang có đợt earth đang sống

    // Thông số điều chỉnh
    private static final int EARTH_COOLDOWN_TICKS = 11 * 60; // ~11s nếu ~60 FPS
    private static final int EARTH_MIN = 4;                   // số gạch mỗi đợt
    private static final int EARTH_MAX = 6;                   // số gạch mỗi đợt
    private static final int EARTH_HP  = 200;                // HP mỗi gạch

    public boolean isInvulnerable() { return invulnerable; }

    // Gọi để thử kích hoạt: chỉ có tác dụng khi element="earth", hết cooldown, và chưa có wave sống
    public void maybeActivateEarthBulwark(java.util.List<arkanoid.Brick> bricks,
                                          int playWidth, int brickW, int brickH) {
        if (!"earth".equals(element)) return;
        if (earthActive) return;
        if (earthCooldown > 0) return;

        int count = EARTH_MIN + rng.nextInt(EARTH_MAX - EARTH_MIN + 1);
        int spawned = spawnEarthBricks(bricks, count, playWidth, brickW, brickH);
        if (spawned > 0) {
            earthActive = true;
            invulnerable = true;           // bất tử đến khi phá hết
            earthWaveId++;
            // earthCooldown = EARTH_COOLDOWN_TICKS;  // đặt hồi chiêu ngay từ lúc spawn
        }
    }

    // Gọi mỗi tick khi element="earth": giảm cooldown và theo dõi còn brick nào của wave không
    public void tickEarth(java.util.List<arkanoid.Brick> bricks) {
        if (!"earth".equals(element)) return;

        if (earthCooldown > 0) earthCooldown--;

        if (!earthActive) return;

        // Kiểm tra còn viên earth nào thuộc wave hiện tại không
        int alive = 0;
        for (arkanoid.Brick b : bricks) {
            if (b != null
                    && "earth".equals(b.getElement())
                    && b.isSummonedByBoss()
                    && b.getSummonWaveId() == earthWaveId
                    && b.getHitPoints() > 0) {
                alive++;
                if (alive > 0) break;
            }
        }

        if (alive == 0) {
            earthActive = false;
            invulnerable = false;   // hết bất tử khi đã phá hết
            earthCooldown = EARTH_COOLDOWN_TICKS;
        }
    }

    // Spawn count viên, căn theo lưới, tránh overlap sơ bộ
    private int spawnEarthBricks(java.util.List<arkanoid.Brick> bricks, int count,
                                 int playWidth, int brickW, int brickH) {
        java.util.Random rnd = rng;  // dùng RNG có sẵn
        int spawned = 0, attempts = 0;

        // vùng spawn: hàng trên giữa màn (y ∈ [140, 300]), căn theo lưới
        while (spawned < count && attempts < 80) {
            attempts++;
            int x = rnd.nextInt(Math.max(1, playWidth - brickW));
            int y = 140 + rnd.nextInt(Math.max(1, 300 - 140 + 1));

            x = (x / brickW) * brickW;
            y = (y / brickH) * brickH;

            // tránh chồng lên brick hiện có
            java.awt.Rectangle rect = new java.awt.Rectangle(x, y, brickW, brickH);
            boolean overlap = false;
            for (arkanoid.Brick b : bricks) {
                if (b == null) continue;
                java.awt.Rectangle r2 = new java.awt.Rectangle(b.getX(), b.getY(), b.getWidth(), b.getHeight());
                if (rect.intersects(r2)) { overlap = true; break; }
            }
            if (overlap) continue;

            // tạo viên earth skill: HP=EARTH_HP, sprite trạng thái 1
            arkanoid.Brick eb = new arkanoid.Brick(x, y, brickW, brickH,
                    EARTH_HP, "earth", "img/Boss/Stone1.png");
            eb.setSummonedByBoss(true);
            eb.setSummonWaveId(earthWaveId + 1);   // wave mới

            bricks.add(eb);
            spawned++;
        }
        return spawned;
    }

    // ===== WIND SHURIKEN =====
    private final java.util.List<ShurikenWind> shurikens = new java.util.ArrayList<>();

    public java.util.List<ShurikenWind> getShurikens() {
        return shurikens;
    }

    public void activateWindSeek(Paddle paddle) {
        if (!"wind".equals(element)) return;

        java.util.concurrent.ThreadLocalRandom r = java.util.concurrent.ThreadLocalRandom.current();
        int n = 1; // số viên

        for (int i = 0; i < n; i++) {
            int bossCenterX = getX() + getWidth() / 2;
            int bossBottom  = getY() + getHeight();

            double init = Math.atan2(
                    paddle.getCenterY() - bossBottom,
                    paddle.getCenterX() - bossCenterX
            );
            init += r.nextDouble(-0.35, 0.35);

            ShurikenWind s = new ShurikenWind(bossCenterX - 12, bossBottom, 32, init);
            shurikens.add(s);
        }
    }



    public void tickWindShurikens(int playW, int playH, Paddle paddle) {
        for (int i = 0; i < shurikens.size(); ) {
            ShurikenWind s = shurikens.get(i);

            // chết => swap-remove
            if (s.isDead()) {
                shurikens.set(i, shurikens.get(shurikens.size() - 1));
                shurikens.remove(shurikens.size() - 1);
                continue;
            }

            // ra khỏi đáy màn (dùng mép dưới)
            if (s.getY() + s.getHeight() >= playH) {
                s.markDead();
                shurikens.set(i, shurikens.get(shurikens.size() - 1));
                shurikens.remove(shurikens.size() - 1);
                continue;
            }

            // còn trong màn -> dẫn đường + bước
            s.seekTowards(paddle.getCenterX(), paddle.getCenterY());
            s.step(playW, playH);
            i++;
        }
    }


    public String getElement() {
        return element;
    }

    protected static Boss makeBossForLevel(int level) {
        switch (level) {
            case 15:
                return new Boss("fire", 5000);
            case 6:
                return new Boss("water", 5000);
            case 12:
                return new Boss("wind", 5000);
            case 9:
                return new Boss("earth", 5000);
            default:
                return null; // các level khác không có boss
        }
    }
}
