import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class HangmanGame extends JFrame {
    private String secretWord;
    private StringBuilder guessedWord;
    private int remainingGuesses;
    private JLabel guessedWordLabel;
    private JLabel remainingGuessesLabel;
    private JTextField inputField;
    private JButton guessButton;
    private JButton restartButton;

    public HangmanGame() {
        super("Hangman Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLayout(new BorderLayout());

        secretWord = generateSecretWord();
        guessedWord = new StringBuilder();
        for (int i = 0; i < secretWord.length(); i++) {
            guessedWord.append("_");
        }
        remainingGuesses = 6;

        JLabel titleLabel = new JLabel("Hangman Game", SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 1));

        JPanel wordPanel = new JPanel();
        wordPanel.setLayout(new FlowLayout());
        guessedWordLabel = new JLabel(guessedWord.toString(), SwingConstants.CENTER);
        wordPanel.add(guessedWordLabel);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout());
        remainingGuessesLabel = new JLabel("Remaining Guesses: " + remainingGuesses, SwingConstants.CENTER);
        infoPanel.add(remainingGuessesLabel);

        mainPanel.add(wordPanel);
        mainPanel.add(infoPanel);

        add(mainPanel, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputField = new JTextField(10);
        guessButton = new JButton("Guess");
        guessButton.addActionListener(new GuessButtonClickListener());
        inputPanel.add(new JLabel("Enter your guess: "));
        inputPanel.add(inputField);
        inputPanel.add(guessButton);

        add(inputPanel, BorderLayout.SOUTH);

        restartButton = new JButton("Restart");
        restartButton.addActionListener(new RestartButtonClickListener());
        add(restartButton, BorderLayout.SOUTH);
    }

    private String generateSecretWord() {
        String[] words = {"hangman", "computer", "java", "programming", "game"};
        int randomIndex = (int) (Math.random() * words.length);
        return words[randomIndex];
    }

    private void checkGuess(char guess) {
        boolean correctGuess = false;
        for (int i = 0; i < secretWord.length(); i++) {
            if (secretWord.charAt(i) == guess) {
                guessedWord.setCharAt(i, guess);
                correctGuess = true;
            }
        }

        if (!correctGuess) {
            remainingGuesses--;
            remainingGuessesLabel.setText("Remaining Guesses: " + remainingGuesses);
        }

        guessedWordLabel.setText(guessedWord.toString());

        if (guessedWord.toString().equals(secretWord)) {
            JOptionPane.showMessageDialog(this, "Congratulations! You guessed the word.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            guessButton.setEnabled(false);
        }

        if (remainingGuesses == 0) {
            JOptionPane.showMessageDialog(this, "Sorry, you ran out of guesses. The word was: " + secretWord, "Game Over", JOptionPane.INFORMATION_MESSAGE);
            guessButton.setEnabled(false);
        }
    }

    private void restartGame() {
        secretWord = generateSecretWord();
        guessedWord = new StringBuilder();
        for (int i = 0; i < secretWord.length(); i++) {
            guessedWord.append("_");
        }
        remainingGuesses = 6;
        guessedWordLabel.setText(guessedWord.toString());
        remainingGuessesLabel.setText("Remaining Guesses: " + remainingGuesses);
        inputField.setText("");
        guessButton.setEnabled(true);
    }

    private class GuessButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String input = inputField.getText();
            if (input.length() == 1) {
                char guess = input.charAt(0);
                checkGuess(guess);
                inputField.setText("");
                inputField.requestFocus();
            } else {
                JOptionPane.showMessageDialog(HangmanGame.this, "Please enter a single letter.", "Error", JOptionPane.ERROR_MESSAGE);
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
            HangmanGame game = new HangmanGame();
            game.setVisible(true);
        });
    }
}
