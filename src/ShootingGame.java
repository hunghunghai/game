import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ShootingGame extends JFrame {
    private static final int BOARD_WIDTH = 400;
    private static final int BOARD_HEIGHT = 600;
    private static final int PLAYER_WIDTH = 50;
    private static final int PLAYER_HEIGHT = 50;
    private static final int ENEMY_WIDTH = 30;
    private static final int ENEMY_HEIGHT = 30;
    private static final int BULLET_WIDTH = 5;
    private static final int BULLET_HEIGHT = 10;
    private static final int PLAYER_SPEED = 5;
    private static final int ENEMY_SPEED = 3;
    private static final int BULLET_SPEED = 7;

    private JPanel board;
    private JPanel player;
    private JPanel[] enemies;
    private JPanel[] bullets;
    private int playerX;
    private int playerY;
    private boolean[] enemyAlive;
    private int[] enemyX;
    private int[] enemyY;
    private boolean[] bulletActive;
    private int[] bulletX;
    private int[] bulletY;
    private Timer timer;

    public ShootingGame() {
        super("Shooting Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(BOARD_WIDTH, BOARD_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);

        board = new JPanel();
        board.setBackground(Color.BLACK);
        board.setLayout(null);
        add(board);

        player = new JPanel();
        player.setBounds((BOARD_WIDTH - PLAYER_WIDTH) / 2, BOARD_HEIGHT - PLAYER_HEIGHT - 10, PLAYER_WIDTH, PLAYER_HEIGHT);
        player.setBackground(Color.BLUE);
        board.add(player);

        enemies = new JPanel[5];
        enemyAlive = new boolean[5];
        enemyX = new int[5];
        enemyY = new int[5];

        for (int i = 0; i < enemies.length; i++) {
            enemies[i] = new JPanel();
            enemies[i].setBounds(i * (ENEMY_WIDTH + 10), 10, ENEMY_WIDTH, ENEMY_HEIGHT);
            enemies[i].setBackground(Color.RED);
            board.add(enemies[i]);
            enemyAlive[i] = true;
            enemyX[i] = i * (ENEMY_WIDTH + 10);
            enemyY[i] = 10;
        }

        bullets = new JPanel[5];
        bulletActive = new boolean[5];
        bulletX = new int[5];
        bulletY = new int[5];

        for (int i = 0; i < bullets.length; i++) {
            bullets[i] = new JPanel();
            bullets[i].setBounds(0, 0, BULLET_WIDTH, BULLET_HEIGHT);
            bullets[i].setBackground(Color.YELLOW);
            bullets[i].setVisible(false);
            board.add(bullets[i]);
            bulletActive[i] = false;
            bulletX[i] = 0;
            bulletY[i] = 0;
        }

        playerX = (BOARD_WIDTH - PLAYER_WIDTH) / 2;
        playerY = BOARD_HEIGHT - PLAYER_HEIGHT - 10;

        timer = new Timer(10, new GameUpdateListener());
        timer.start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_LEFT) {
                    if (playerX > 0) {
                        playerX -= PLAYER_SPEED;
                    }
                } else if (keyCode == KeyEvent.VK_RIGHT) {
                    if (playerX < BOARD_WIDTH - PLAYER_WIDTH) {
                        playerX += PLAYER_SPEED;
                    }
                } else if (keyCode == KeyEvent.VK_SPACE) {
                    for (int i = 0; i < bullets.length; i++) {
                        if (!bulletActive[i]) {
                            bulletActive[i] = true;
                            bulletX[i] = playerX + (PLAYER_WIDTH - BULLET_WIDTH) / 2;
                            bulletY[i] = playerY - BULLET_HEIGHT;
                            bullets[i].setBounds(bulletX[i], bulletY[i], BULLET_WIDTH, BULLET_HEIGHT);
                            bullets[i].setVisible(true);
                            break;
                        }
                    }
                }
            }
        });

        setFocusable(true);
        requestFocus();
    }

    private void updateEnemies() {
        for (int i = 0; i < enemies.length; i++) {
            if (enemyAlive[i]) {
                if (enemyY[i] < BOARD_HEIGHT) {
                    enemyY[i] += ENEMY_SPEED;
                    enemies[i].setBounds(enemyX[i], enemyY[i], ENEMY_WIDTH, ENEMY_HEIGHT);
                    checkCollision(i);
                } else {
                    enemyAlive[i] = false;
                    enemies[i].setVisible(false);
                }
            }
        }
    }

    private void updateBullets() {
        for (int i = 0; i < bullets.length; i++) {
            if (bulletActive[i]) {
                if (bulletY[i] > 0) {
                    bulletY[i] -= BULLET_SPEED;
                    bullets[i].setBounds(bulletX[i], bulletY[i], BULLET_WIDTH, BULLET_HEIGHT);
                    checkHit(i);
                } else {
                    bulletActive[i] = false;
                    bullets[i].setVisible(false);
                }
            }
        }
    }

    private void checkCollision(int enemyIndex) {
        Rectangle enemyBounds = new Rectangle(enemyX[enemyIndex], enemyY[enemyIndex], ENEMY_WIDTH, ENEMY_HEIGHT);
        Rectangle playerBounds = new Rectangle(playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT);

        if (enemyBounds.intersects(playerBounds)) {
            gameOver();
        }
    }

    private void checkHit(int bulletIndex) {
        Rectangle bulletBounds = new Rectangle(bulletX[bulletIndex], bulletY[bulletIndex], BULLET_WIDTH, BULLET_HEIGHT);

        for (int i = 0; i < enemies.length; i++) {
            if (enemyAlive[i]) {
                Rectangle enemyBounds = new Rectangle(enemyX[i], enemyY[i], ENEMY_WIDTH, ENEMY_HEIGHT);
                if (bulletBounds.intersects(enemyBounds)) {
                    bulletActive[bulletIndex] = false;
                    bullets[bulletIndex].setVisible(false);
                    enemyAlive[i] = false;
                    enemies[i].setVisible(false);
                }
            }
        }
    }

    private void gameOver() {
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over!", "Shooting Game", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    private class GameUpdateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            updateEnemies();
            updateBullets();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ShootingGame game = new ShootingGame();
            game.setVisible(true);
        });
    }
}
