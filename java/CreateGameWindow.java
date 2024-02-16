import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//finestra creazione della partita
public class CreateGameWindow extends JFrame {
    private JTextField attemptsField;
    private JCheckBox infiniteAttemptsBox;
    private JCheckBox randomWordBox;
    private JTextField wordToGuessField;
    private JButton creaPartita;
    private String playerName;
    private HangmanClient hangmanClient;
    public CreateGameWindow(String playerName, HangmanClient client) {
        this.playerName = playerName;
        this.hangmanClient = client;
        initializeCreateGameGUI();
    }

    private void initializeCreateGameGUI() {
        setTitle("Crea Nuova Partita");
        setSize(350, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(0, 1)); 

        add(new JLabel("Numero di tentativi:"));
        attemptsField = new JTextField();
        add(attemptsField);

        infiniteAttemptsBox = new JCheckBox("Tentativi infiniti");
        add(infiniteAttemptsBox);

        randomWordBox = new JCheckBox("Genera parola casuale");
        add(randomWordBox);

        add(new JLabel("Parola da indovinare:"));
        wordToGuessField = new JTextField();
        add(wordToGuessField);

        creaPartita = new JButton("Crea Partita");
        add(creaPartita);

        // Add action listener to the create button
        creaPartita.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createGame();
            }
        });

        // Add action listener to the infinite attempts checkbox
        infiniteAttemptsBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attemptsField.setEnabled(!infiniteAttemptsBox.isSelected());
            }
        });

        setVisible(true);
    }

    private void createGame() {
        boolean infiniteAttempts = infiniteAttemptsBox.isSelected();
        boolean randomWord = randomWordBox.isSelected();
    
        String attemptsStr = attemptsField.getText();
        int attempts = 0;
        if (!infiniteAttempts) {
            try {
                attempts = Integer.parseInt(attemptsStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "inserisci un numero", "errore input", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    
        String wordToGuess = wordToGuessField.getText();
        if (!randomWord && wordToGuess.isEmpty()) {
            JOptionPane.showMessageDialog(this, "inserisci la parola.", "errore input", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // Send the create game command to the server through the HangmanClient
        hangmanClient.createGame(playerName, infiniteAttempts, attempts, wordToGuess, randomWord);
    
       
    }
}
