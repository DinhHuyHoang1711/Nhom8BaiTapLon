package arkanoid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.WindowListener;

import GachaMachine.Item;
import arkanoid.*;

import static arkanoid.GameObject.GAME_HEIGHT;
import static arkanoid.GameObject.GAME_WIDTH;

/**
 * Giao diện BaloUI quản lý các vật phẩm mà người chơi đang sở hữu.
 * Bao gồm bóng và artifacts, cho phép mở các danh sách chi tiết và quay lại menu chính.
 */
public class BaloUI extends JFrame {

    /**
     * Âm thanh khi nhấn nút
     */
    private Sound clickSound;

    /**
     * Hình nền balo
     */
    private Image baloImg;

    /**
     * Quản lý các vật phẩm sở hữu
     */
    private OwnedManager ownedManager;

    /**
     * Tham chiếu đến menu cha MapMenu
     */
    private MapMenu parentMenu;

    /**
     * Constructor tạo giao diện BaloUI với menu cha và quản lý vật phẩm.
     *
     * @param parentMenu   menu cha MapMenu
     * @param ownedManager quản lý các vật phẩm sở hữu
     */
    public BaloUI(MapMenu parentMenu, OwnedManager ownedManager) {

        this.parentMenu = parentMenu;
        this.ownedManager = ownedManager;

        setTitle("Vật phầm đang sở hữu");
        setSize(GAME_WIDTH, GAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        clickSound = new Sound("sound/click.wav");
        baloImg = new ImageIcon("img/balo.jpg").getImage();

        // Panel chính
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(baloImg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null);

        // Nút mở danh sách bóng
        panel.add(ButtonManager.createImageButton("img/balls.png", "img/balls_hover.png",
                600, 200, clickSound, e -> openBallList()));

        // Nút mở danh sách artifacts
        panel.add(ButtonManager.createImageButton("img/artifacts.png", "img/artifacts_hover.png",
                320, 500, clickSound, e -> openArtifactList()));

        // Nút Back
        JButton back = ButtonManager.createImageButton(
                "img/back_button.png", "img/back_hover.png",
                10, 550, clickSound, e -> {
                    this.dispose();
                    parentMenu.setVisible(true);
                });
        panel.add(back);

        setContentPane(panel);
        setVisible(true);
    }

    /**
     * Constructor BaloUI chỉ với OwnedManager, không có menu cha.
     *
     * @param ownedManager quản lý các vật phẩm sở hữu
     */
    public BaloUI(OwnedManager ownedManager) {
        this(null, ownedManager);
    }

    /**
     * Mở giao diện danh sách bóng.
     * Đóng giao diện hiện tại và mở BallUI.
     */
    private void openBallList() {
        this.dispose();
        new BallUI(this, ownedManager, ownedManager.getBalls());
    }

    /**
     * Mở giao diện danh sách artifacts.
     * Đóng giao diện hiện tại và mở ItemUI với các vật phẩm sở hữu.
     */
    private void openArtifactList() {
        this.dispose();
        Item[] ownedItems = ownedManager.getOwnedItemsArray();
        new ItemUI(this, ownedManager, ownedItems);
    }

    /**
     * Hàm main để chạy thử BaloUI.
     *
     * @param args tham số dòng lệnh
     */
    public static void main(String[] args) {
        OwnedManager ownedManager = new OwnedManager();

        new BaloUI(ownedManager);
    }

}
