import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//finestra unione alla partita
public class JoinGameWindow extends JFrame {
    private JTextField codicePartita;
    private JButton uniscitiPartita;
    private String playerName;
    private HangmanClient hangmanClient;
    public JoinGameWindow(String playerName, HangmanClient client) {
        this.playerName = playerName;
        this.hangmanClient = client;
        initializeJoinGameGUI();
    }

    private void initializeJoinGameGUI() {
        setTitle("Unisciti a Partita Esistente");
        setSize(300, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(0, 1));

        add(new JLabel("Inserisci il codice della partita:"));
        codicePartita = new JTextField();
        add(codicePartita);

        uniscitiPartita = new JButton("Unisciti alla Partita");
        add(uniscitiPartita);

        // Add action listener to join game button
        uniscitiPartita.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                joinGame();
            }
        });

        setVisible(true);
    }

    private void joinGame() {
        String gameCode = codicePartita.getText().trim();
    
        if (gameCode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "inserisci un codice", "errore input", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // Uses the HangmanClient to send the join game request to the server
        hangmanClient.joinGame(playerName, gameCode);
    
        // disables the button to prevent multiple requests
        uniscitiPartita.setEnabled(false);
    }
}