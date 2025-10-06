package level;

class Level {
    protected String elementName;
    protected ArrayList <Brick> bricks = new ArrayList <> ();
    protected Boss boss;
    
    public void set_elementName(String elementName);
    public void set_bricks(ArrayList <Brick> b);
    public void set_boss(Boss boss);
    public void applyMechanics(Ball ball);
}

class FireLevel extends Level {
    //Nhung co che gi cua Hoa quoc thi them vao day
}

class WindLevel extends Level {
    //Nhung co che gi cua Phong quoc thi them vao day
}

class WaterLevel extends Level {
    //Nhung co che gi cua Thuy quoc thi them vao day
}

class EarthLevel extends Level {
    //Nhung co che gi cua Tho quoc thi them vao day
}
// lam them nhieu loai lv khac, eg: thon tan thu, dau truong dung si, ...