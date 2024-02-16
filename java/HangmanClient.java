import java.io.*;
import java.net.*;
//gestisce la parte client dell'applicativo (collegamento con server)
public class HangmanClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public HangmanClient(String address, int port) throws IOException {
        socket = new Socket(address, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        listenForServerMessages(); 
    }

    public void createGame(String playerName, boolean infiniteAttempts, int attempts, String wordToGuess, boolean randomWord) {
        // Constructs the joining message 
        System.out.println("accesso ad una partita...");//debugging
        String message = String.format("CREATE %s %b %d %s %b", playerName, infiniteAttempts, attempts, wordToGuess, randomWord);
        // Sends the message to the server
        send(message);
    }
    public void joinGame(String playerName, String gameCode) {
        // Constructs the joining message 
        System.out.println("accesso ad una partita...");//debugging
        String message = String.format("JOIN %s %s", playerName, gameCode);
        // Sends the message to the server
        send(message);
    }
    
    private void send(String message) {
        System.out.println("sto inviando il messaggio: " + message); //debugging
        out.println(message);
        out.flush();
    }

    // Method that listens for messages from the server
    private void listenForServerMessages() {
        new Thread(() -> {
            String fromServer;
            try {
                while ((fromServer = in.readLine()) != null) {
                    if (fromServer.startsWith("GAME_CREATED")) {
                        String gameCode = fromServer.split(" ")[1];
                        System.out.println("partita inizializzata con codice: " + gameCode);//qui devo decidere cosa fare
                    } else if (fromServer.startsWith("JOINED_GAME")) {
                        String gameCode = fromServer.split(" ")[1];
                        System.out.println("mi sono unito a partita: " + gameCode);//qui devo decidere cosa fare
                    } else if (fromServer.startsWith("ERROR")) {
                        System.err.println(fromServer);//qui devo decidere cosa fare
                    }
                    
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}