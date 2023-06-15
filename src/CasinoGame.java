import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CasinoGame extends JFrame {
    private JLabel resultLabel;
    private JButton betButton;
    private JButton resetButton;
    private int balance;

    public CasinoGame() {
        super("Casino Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        JLabel balanceLabel = new JLabel("Balance: $0");
        resultLabel = new JLabel("Place your bet!");
        betButton = new JButton("Bet");
        resetButton = new JButton("Reset");

        betButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int betAmount = Integer.parseInt(JOptionPane.showInputDialog("Enter your bet amount:"));
                if (betAmount > balance) {
                    JOptionPane.showMessageDialog(null, "Insufficient balance!");
                } else {
                    int randomNumber = (int) (Math.random() * 10 + 1);
                    if (randomNumber % 2 == 0) {
                        balance += betAmount;
                        resultLabel.setText("You won! New balance: $" + balance);
                    } else {
                        balance -= betAmount;
                        resultLabel.setText("You lost! New balance: $" + balance);
                    }
                    balanceLabel.setText("Balance: $" + balance);
                }
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                balance = 0;
                balanceLabel.setText("Balance: $" + balance);
                resultLabel.setText("Place your bet!");
            }
        });

        add(balanceLabel);
        add(resultLabel);
        add(betButton);
        add(resetButton);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CasinoGame();
        });
    }
}
