import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;

public class LuckySpinGame extends JFrame {
    private SpinCanvas spinCanvas;
    private JButton spinButton;
    private JLabel resultLabel;

    public LuckySpinGame() {
        super("Lucky Spin Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        spinCanvas = new SpinCanvas();
        resultLabel = new JLabel("Click Spin to play!");
        spinButton = new JButton("Spin");

        spinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spinCanvas.startSpin();
            }
        });

        JPanel controlPanel = new JPanel();
        controlPanel.add(resultLabel);
        controlPanel.add(spinButton);

        mainPanel.add(spinCanvas, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private class SpinCanvas extends JPanel {
        private double startAngle = 0;
        private double rotationSpeed = 2;
        private double currentAngle = 0;
        private boolean spinning = false;

        public SpinCanvas() {
            setPreferredSize(new Dimension(300, 300));
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g.create();
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            int radius = Math.min(centerX, centerY) - 10;

            // Draw the wheel
            g2d.setStroke(new BasicStroke(3));
            g2d.setColor(Color.BLUE);
            g2d.draw(new Arc2D.Double(centerX - radius, centerY - radius, radius * 2, radius * 2, startAngle, 120, Arc2D.PIE));
            g2d.setColor(Color.RED);
            g2d.draw(new Arc2D.Double(centerX - radius, centerY - radius, radius * 2, radius * 2, startAngle + 120, 120, Arc2D.PIE));
            g2d.setColor(Color.GREEN);
            g2d.draw(new Arc2D.Double(centerX - radius, centerY - radius, radius * 2, radius * 2, startAngle + 240, 120, Arc2D.PIE));

            // Draw the indicator line
            int indicatorLength = radius + 10;
            double indicatorAngle = Math.toRadians(currentAngle);
            int indicatorX = centerX + (int) (Math.cos(indicatorAngle) * indicatorLength);
            int indicatorY = centerY + (int) (Math.sin(indicatorAngle) * indicatorLength);
            g2d.setColor(Color.BLACK);
            g2d.drawLine(centerX, centerY, indicatorX, indicatorY);

            g2d.dispose();
        }

        public void startSpin() {
            if (!spinning) {
                spinning = true;
                spinButton.setEnabled(false);
                new SpinThread().start();
            }
        }

        private class SpinThread extends Thread {
            @Override
            public void run() {
                try {
                    // Spin for a random number of iterations
                    int iterations = (int) (Math.random() * 10) + 20;
                    for (int i = 0; i < iterations; i++) {
                        currentAngle += rotationSpeed;
                        repaint();
                        Thread.sleep(100);
                    }

                    // Determine the result
                    String[] options = {"Lucky", "Unlucky"};
                    int randomIndex = (int) (Math.random() * options.length);
                    String result = options[randomIndex];
                    resultLabel.setText(result + "! Spin again!");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    spinning = false;
                    spinButton.setEnabled(true);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LuckySpinGame();
        });
    }
}
