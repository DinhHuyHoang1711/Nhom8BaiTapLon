import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import arkanoid.Sound;

import static arkanoid.GameObject.GAME_HEIGHT;
import static arkanoid.GameObject.GAME_WIDTH;

public class Menu extends JFrame {
    private JButton startButton;
    private JButton tutorialButton;
    private JButton exitButton;
    private JButton bagButton;
    private JButton gachaButton;

    private Image backgroundImage;

    private Sound click;
    private Sound clickBag;
    private Sound backgroundMusic;

    // Ảnh mặc định và hover
    private ImageIcon startIcon, startHoverIcon;
    private ImageIcon tutorialIcon, tutorialHoverIcon;
    private ImageIcon exitIcon, exitHoverIcon;
    private ImageIcon bagCloseIcon, bagOpenIcon;
    private ImageIcon gachaIcon, gachaHoverIcon;

    public Menu() {
        setTitle("ARKANOID");
        setSize(GAME_WIDTH, GAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // sound
        click = new Sound("sound/click.wav");
        clickBag = new Sound("sound/bag.wav");
        backgroundMusic = new Sound("sound/backgroundMusic.wav");
        backgroundMusic.loop();

        // background
        backgroundImage = new ImageIcon("img/background.jpg").getImage();

        // img button
        startIcon = new ImageIcon("img/start_button.png");
        startHoverIcon = new ImageIcon("img/start_hover.png");

        exitIcon = new ImageIcon("img/exit_button.png");
        exitHoverIcon = new ImageIcon("img/exit_hover.png");

        bagCloseIcon = new ImageIcon("img/bag_close.png");
        bagOpenIcon = new ImageIcon("img/bag_open.png");

        tutorialIcon = new ImageIcon("img/tutorial_button.png");
        tutorialHoverIcon = new ImageIcon("img/tutorial_hover.png");

        gachaIcon = new ImageIcon("img/gacha_button.png");
        gachaHoverIcon = new ImageIcon("img/gacha_hover.png");

        // panel
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(null);

        // button
        startButton = new JButton(startIcon);
        exitButton = new JButton(exitIcon);
        bagButton = new JButton(bagCloseIcon);
        tutorialButton = new JButton(tutorialIcon);
        gachaButton = new JButton(gachaIcon);

        // xoa vien va nen
        for (JButton btn : new JButton[]{startButton, exitButton, bagButton, tutorialButton, gachaButton}) {
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setFocusPainted(false);
            btn.setOpaque(false);
        }

        // toa do nut
        startButton.setBounds(500, 180, 200, 110);
        exitButton.setBounds(500, 420, 200, 110);
        bagButton.setBounds(1050, 640, 100, 120);
        tutorialButton.setBounds(1050, 20, 100, 100);
        gachaButton.setBounds(1050, 500, 100, 100);

        // su kien Click
        startButton.addActionListener(this::startGame);
        exitButton.addActionListener(this::exitGame);
        bagButton.addActionListener(this::openBag);
        tutorialButton.addActionListener(this::showTutorial);
        gachaButton.addActionListener(this::openGacha);

        // Hover Start
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                startButton.setIcon(startHoverIcon);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                startButton.setIcon(startIcon);
            }
        });

        // Hover Exit
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                exitButton.setIcon(exitHoverIcon);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                exitButton.setIcon(exitIcon);
            }
        });

        // Hover Bag
        bagButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                bagButton.setIcon(bagOpenIcon);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                bagButton.setIcon(bagCloseIcon);
            }
        });

        // Hover Tutorial
        tutorialButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                tutorialButton.setIcon(tutorialHoverIcon);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                tutorialButton.setIcon(tutorialIcon);
            }
        });

        //Hover Gacha
        gachaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                gachaButton.setIcon(gachaHoverIcon);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                gachaButton.setIcon(gachaIcon);
            }
        });

        // them nut vao pannel
        backgroundPanel.add(startButton);
        backgroundPanel.add(tutorialButton);
        backgroundPanel.add(exitButton);
        backgroundPanel.add(bagButton);
        backgroundPanel.add(gachaButton);

        // gan pannel vao frame
        setContentPane(backgroundPanel);
        setVisible(true);
    }

    // Click Start
    private void startGame(ActionEvent e) {
        click.play();
        JOptionPane.showMessageDialog(this, "Comming Soon");
    }

    // Click Exit
    private void exitGame(ActionEvent e) {
        backgroundMusic.stop();
        System.exit(0);
    }

    // Click Tutorial
    private void showTutorial(ActionEvent e) {
        click.play();
        String tutorialText = """
                * Hướng dẫn sử dụng :
                    + Nhấn SPACE để bắt đầu
                    + Sử dụng phím A/D hoặc trái/phải để di chuyển sang trái hoặc phải
                    + Khi thanh Energy sẵn sàng có thể nhấn "Q" để kích hoạt Artifact
                                   
                * Nhiệm vụ
                    + Diểu khiển thanh Paddle linh hoạt để đỡ bóng
                    + Phá hủy toàn bộ Bricks để qua màn
                    + Đỡ thất bại hoặc chịu sát thương quá số lượng cho phép, nhiệm vụ thất bại
                """
                ;
        JOptionPane.showMessageDialog(this, tutorialText, "Hướng Dẫn",
                                        JOptionPane.INFORMATION_MESSAGE);
    }

    // Click Bag
    private void openBag(ActionEvent e) {
        clickBag.play();
        JOptionPane.showMessageDialog(this, "Comming Soon");
    }

    // Click Gacha
    private void openGacha(ActionEvent e) {
        click.play();
        JOptionPane.showMessageDialog(this, "Comming Soon");
    }

    // Render
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Menu::new);
    }
}
