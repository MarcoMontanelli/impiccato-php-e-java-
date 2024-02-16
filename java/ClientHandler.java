import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;
//gestisce le richieste dei client e l'interconnesione tra partite
public class ClientHandler implements Runnable {
    private Socket socket;
    private Map<String, GameSession> sessions;
    private BufferedReader in;
    private PrintWriter out;
    private String playerName;
    private HangmanServer server;
    public ClientHandler(Socket socket, HangmanServer server) {
        this.socket = socket;
        this.server = server;
        this.sessions = server.getSessions(); // Directly access the server's sessions map
    }
    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine); // For debugging
                handleClientInput(inputLine);
            }
        } catch (IOException e) {
            System.out.println("errore nell'handler del client" + playerName + ": " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // hanldeClientInput method, it is responsible for parsing the client's input and taking the appropriate action.
    private void handleClientInput(String input) {
        // Split by spaces considering quotes for multi-word parameters like wordToGuess
        System.out.println("Server received: " + input);
        String[] tokens = input.split(" (?=([^\"]*\"[^\"]*\")*[^\"]*$)");

        if (tokens.length < 2) {
            out.println("ERROR formato comandi invalido");
            return;
        }

        String command = tokens[0].toUpperCase();
        switch (command) {
            case "CREATE":
                if (tokens.length >= 5) handleCreateCommand(tokens);
                else out.println("ERROR parametri insufficienti per CREATE");
                break;
            case "JOIN":
                if (tokens.length >= 3) handleJoinCommand(tokens);
                else out.println("ERROR parametri insufficienti per JOIN");
                break;
            default:
                out.println("ERROR comando sconosciuto");
        }
    }

    private void handleCreateCommand(String[] tokens) {
        playerName = tokens[1].replace("\"", "");
        boolean infiniteAttempts = Boolean.parseBoolean(tokens[2]);
        int attempts;
        try {
            attempts = Integer.parseInt(tokens[3]);
        } catch (NumberFormatException e) {
            out.println("ERROR numero di tentativi non valido");
            return;
        }
        String wordToGuess = tokens.length > 4 ? tokens[4].replace("\"", "") : ""; // Supports an empty wordToGuess

        String gameCode = generateGameCode();
        GameSession newSession = new GameSession(playerName, infiniteAttempts, attempts, wordToGuess, gameCode);
        sessions.put(gameCode, newSession);

        out.println("GAME_CREATED " + gameCode);
    }

    private String generateGameCode() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    private void handleJoinCommand(String[] tokens) {
        playerName = tokens[1].replace("\"", "");
        String gameCode = tokens[2];

        GameSession sessionToJoin = sessions.get(gameCode);
        if (sessionToJoin != null && sessionToJoin.addPlayer(playerName)) {
            out.println("JOINED_GAME " + gameCode);
        } else {
            out.println("ERROR codice non valido o partita inesistente");
        }
    }
    
    
}
