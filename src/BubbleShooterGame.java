import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BubbleShooterGame extends JFrame {
    private static final int BOARD_WIDTH = 800;
    private static final int BOARD_HEIGHT = 600;
    private static final int BUBBLE_SIZE = 30;
    private static final int SHOOTER_SIZE = 50;

    private JPanel board;
    private JPanel shooter;
    private int shooterX;
    private boolean isShooting;

    public BubbleShooterGame() {
        super("Bubble Shooter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(BOARD_WIDTH, BOARD_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);

        board = new JPanel();
        board.setBackground(Color.BLACK);
        board.setLayout(null);
        add(board);

        shooter = new JPanel();
        shooter.setBounds(BOARD_WIDTH / 2 - SHOOTER_SIZE / 2, BOARD_HEIGHT - SHOOTER_SIZE, SHOOTER_SIZE, SHOOTER_SIZE);
        shooter.setBackground(Color.YELLOW);
        board.add(shooter);

        shooterX = BOARD_WIDTH / 2 - SHOOTER_SIZE / 2;
        isShooting = false;

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    if (shooterX > 0) {
                        shooterX -= 10;
                        shooter.setBounds(shooterX, BOARD_HEIGHT - SHOOTER_SIZE, SHOOTER_SIZE, SHOOTER_SIZE);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    if (shooterX < BOARD_WIDTH - SHOOTER_SIZE) {
                        shooterX += 10;
                        shooter.setBounds(shooterX, BOARD_HEIGHT - SHOOTER_SIZE, SHOOTER_SIZE, SHOOTER_SIZE);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (!isShooting) {
                        isShooting = true;
                        shootBubble();
                    }
                }
            }
        });

        setFocusable(true);
        requestFocus();
    }

    private void shootBubble() {
        JPanel bubble = new JPanel();
        bubble.setBounds(shooterX, BOARD_HEIGHT - SHOOTER_SIZE - BUBBLE_SIZE, BUBBLE_SIZE, BUBBLE_SIZE);
        bubble.setBackground(Color.BLUE);
        board.add(bubble);

        Timer timer = new Timer(10, new ActionListener() {
            int bubbleY = BOARD_HEIGHT - SHOOTER_SIZE - BUBBLE_SIZE;

            @Override
            public void actionPerformed(ActionEvent e) {
                bubbleY -= 5;
                bubble.setBounds(shooterX, bubbleY, BUBBLE_SIZE, BUBBLE_SIZE);

                if (bubbleY <= 0) {
                    ((Timer) e.getSource()).stop();
                    isShooting = false;
                    board.remove(bubble);
                    board.revalidate();
                    board.repaint();
                }
            }
        });

        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BubbleShooterGame game = new BubbleShooterGame();
            game.setVisible(true);
        });
    }
}
