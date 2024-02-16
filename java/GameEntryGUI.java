import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
//finestra di accesso al gioco
public class GameEntryGUI extends JFrame {
    private JTextField nome;
    private JRadioButton creaPartita, uniscitiPartita;
    private ButtonGroup opzioniPanel;
    private JButton procedi;
    private String serverAddress = "localhost"; 
    private int serverPort = 4444;
    public GameEntryGUI() {
        initializeGUI();
    }

    private void initializeGUI() {
        // Frame configuration
        setTitle("impiccato - Accedi al gioco");
        setSize(300, 200);
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Name input
        nome = new JTextField(20);
        add(new JLabel("Inserisci il tuo nome:"));
        add(nome);

        // Radio buttons for game options
        creaPartita = new JRadioButton("Crea partita", true); 
        uniscitiPartita = new JRadioButton("Unisciti a partita esistente");
        opzioniPanel = new ButtonGroup();
        opzioniPanel.add(creaPartita);
        opzioniPanel.add(uniscitiPartita);
        add(creaPartita);
        add(uniscitiPartita);

        // Proceed button
        procedi = new JButton("Procedi");
        add(procedi);
        procedi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                proceedWithOption();
            }
        });

        setVisible(true);
    }

    private void proceedWithOption() {
        String playerName = nome.getText().trim();
        if (playerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your name.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

         try {
            HangmanClient hangmanClient = new HangmanClient(serverAddress, serverPort);

            boolean isCreatingGame = creaPartita.isSelected();
            if (isCreatingGame) {
                // Pass both playerName and hangmanClient to CreateGameWindow
                EventQueue.invokeLater(() -> new CreateGameWindow(playerName, hangmanClient));
            } else {
                // Pass both playerName and hangmanClient to JoinGameWindow
                EventQueue.invokeLater(() -> new JoinGameWindow(playerName, hangmanClient));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "errore connessione al server: " + ex.getMessage(), "errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameEntryGUI());
    }
}