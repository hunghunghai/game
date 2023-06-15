import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GuessTheNumberGame extends JFrame {
    private int targetNumber;
    private JTextField inputField;
    private JTextArea outputArea;
    private JButton guessButton;
    private JButton restartButton;

    public GuessTheNumberGame() {
        super("Guess the Number Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLayout(new BorderLayout());

        targetNumber = generateRandomNumber();

        JLabel titleLabel = new JLabel("Guess the Number", SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 1));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputField = new JTextField(10);
        guessButton = new JButton("Guess");
        guessButton.addActionListener(new GuessButtonClickListener());
        inputPanel.add(new JLabel("Enter your guess: "));
        inputPanel.add(inputField);
        inputPanel.add(guessButton);

        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new FlowLayout());
        outputArea = new JTextArea(5, 20);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        outputPanel.add(scrollPane);

        mainPanel.add(inputPanel);
        mainPanel.add(outputPanel);

        add(mainPanel, BorderLayout.CENTER);

        restartButton = new JButton("Restart");
        restartButton.addActionListener(new RestartButtonClickListener());
        add(restartButton, BorderLayout.SOUTH);
    }

    private int generateRandomNumber() {
        return (int) (Math.random() * 100) + 1;
    }

    private void checkGuess(int guess) {
        if (guess < targetNumber) {
            outputArea.append(guess + " is too low.\n");
        } else if (guess > targetNumber) {
            outputArea.append(guess + " is too high.\n");
        } else {
            outputArea.append("Congratulations! You guessed the number " + guess + ".\n");
            guessButton.setEnabled(false);
        }
    }

    private void restartGame() {
        targetNumber = generateRandomNumber();
        outputArea.setText("");
        guessButton.setEnabled(true);
    }

    private class GuessButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int guess = Integer.parseInt(inputField.getText());
                checkGuess(guess);
                inputField.setText("");
                inputField.requestFocus();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(GuessTheNumberGame.this, "Invalid input. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class RestartButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            restartGame();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GuessTheNumberGame game = new GuessTheNumberGame();
            game.setVisible(true);
        });
    }
}
