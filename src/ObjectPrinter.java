package arkanoid;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import arkanoid.GameObject;

public class ObjectPrinter extends JPanel {

    private BufferedImage image;   // lưu ảnh trong bộ nhớ
    private String imagePath;      // đường dẫn file ảnh
    private int x, y, width, height; //cac thong so de ve 1 object;

    //Constructor
    public ObjectPrinter() {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
        this.imagePath = null;
    }
    public ObjectPrinter(GameObject obj) {
        this.imagePath = obj.getImagePath();
        this.x = obj.getX();
        this.y = obj.getY();
        this.width = obj.getWidth();
        this.height = obj.getHeight();
        loadImage();
    }

    // ✅ Hàm đọc ảnh từ đường dẫn
    private void loadImage() {
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            System.err.println("Không thể tải ảnh: " + imagePath);
            image = null;
        }
    }

    // ✅ Hàm vẽ ảnh (Swing tự gọi khi cần)
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, this.x, this.y, this.width, this.height, null);
    }

    // Setter đổi ảnh khác (tự load và vẽ lại)
    /* Tot nhat ko nen dung ham nay. Khi muon ve 1 object moi:
    1. Xoa bo nho di
    2. Su dung ham paintObject
     */
    public void setImage(String newPath) {
        this.imagePath = newPath;
        loadImage();
        /* repaint(); Dang ko cho ve lai nhe, muon ve tu add cai panel moi vao frame
        nho giai phong bo nho
         */
    }

    // Hàm giải phóng bộ nhớ khi không dùng nữa
    public void clearImage() {
        image = null;
        repaint();
        System.gc(); // Gợi ý dọn rác (tùy chọn)
    }
    //Thay the cai game object ma object printer muon in ra;
    public void setGameObject(GameObject obj) {
        clearImage();
        this.imagePath = obj.getImagePath();
        this.x = obj.getX();
        this.y = obj.getY();
        this.width = obj.getWidth();
        this.height = obj.getHeight();
        loadImage();
    }
}
