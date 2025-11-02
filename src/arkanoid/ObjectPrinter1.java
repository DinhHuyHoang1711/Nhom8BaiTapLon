package arkanoid;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import arkanoid.GameObject;

public class ObjectPrinter1 extends JPanel {

    private BufferedImage image;   // lưu ảnh trong bộ nhớ
    private Image scaledImage; //Anh co the khong fit voi khung hcn nen can scale
    private BufferedImage scaledARGB; // ảnh đã crop + scale, còn alpha
    private BufferedImage tintedCache; // cache ảnh đỏ để tránh tạo lại mỗi frame
    private String imagePath;      // đường dẫn file ảnh
    private int x, y, width, height; //cac thong so de ve 1 object;

    //nháy đỏ
    private boolean flashRed = false;
    private long flashStart = 0;


    //Constructor
    public ObjectPrinter1() {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
        this.imagePath = null;
    }

    public ObjectPrinter1(GameObject obj) {
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

            // Xác định vùng có nội dung thực (bỏ nền trắng / trong suốt)
            Rectangle cropRect = getNonTransparentArea(image);

            //  Cắt ảnh theo vùng đó
            BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = scaled.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.drawImage(
                    image,
                    0, 0, width, height,
                    cropRect.x, cropRect.y, cropRect.x + cropRect.width, cropRect.y + cropRect.height,
                    null
            );
            g2.dispose();

            scaledARGB = scaled;
            tintedCache = null;   // ảnh thay đổi -> xoá cache tint

        } catch (IOException e) {
            System.err.println("Không thể tải ảnh: " + imagePath);
            image = null;
            scaledARGB = null;
            tintedCache = null;
            ;
        }
    }

    // Xác định vùng không trong suốt / không trắng
    private Rectangle getNonTransparentArea(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();

        int minX = w, minY = h, maxX = 0, maxY = 0;
        boolean found = false;

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int pixel = image.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xff;
                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = (pixel) & 0xff;

                // Ngưỡng bỏ qua pixel trắng hoặc trong suốt
                if (alpha > 0 && !(r > 240 && g > 240 && b > 240)) {
                    found = true;
                    if (x < minX) minX = x;
                    if (y < minY) minY = y;
                    if (x > maxX) maxX = x;
                    if (y > maxY) maxY = y;
                }
            }
        }

        if (!found) {
            // Nếu ảnh toàn trắng hoặc trong suốt
            return new Rectangle(0, 0, w, h);
        }

        return new Rectangle(minX, minY, maxX - minX + 1, maxY - minY + 1);
    }

    // Hàm vẽ ảnh (Swing tự gọi khi cần)
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        updateFlash();

        if (scaledARGB == null) return;

        BufferedImage toDraw;
        if (flashRed) {
            //vẽ ảnh hơi đỏ tức là đang nháy đỏ đấy
            if (tintedCache == null) tintedCache = tintRedOverAlpha(scaledARGB, 0.40f);
            toDraw = tintedCache;
        } else {
            //binh thuong thi ve binh thuong
            toDraw = scaledARGB;
        }
        g.drawImage(toDraw, this.x, this.y, null);
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

    //cho 1 vat nhay mau do
    public void startFlash() {
        flashRed = true;
        flashStart = System.currentTimeMillis();
    }

    private void updateFlash() {
        if (flashRed && System.currentTimeMillis() - flashStart > 100) {
            flashRed = false;
        }
    }

    private static BufferedImage tintRedOverAlpha(BufferedImage src, float alpha) {
        BufferedImage out = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = out.createGraphics();
        g2.drawImage(src, 0, 0, null);                               // nền: chính ảnh
        g2.setComposite(AlphaComposite.SrcAtop.derive(alpha));        // chỉ phủ nơi ảnh có alpha
        g2.setColor(Color.RED);
        g2.fillRect(0, 0, src.getWidth(), src.getHeight());          // phủ đỏ theo mask alpha
        g2.dispose();
        return out;
    }
}
