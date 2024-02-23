
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.nio.file.Files;
import java.nio.file.Paths;

class GameSession {
    String wordToGuess;
    int attemptsLeft;
    Set<Character> guessedLetters = Collections.synchronizedSet(new HashSet<>());
    boolean guesserJoined = false;
    PrintWriter chooserWriter; 
    PrintWriter guesserWriter; 
    public GameSession(String word, int attempts, PrintWriter chooserWriter) { // Passes the PrintWriter to the GameSession in order to comunicate the progress of the guesser
        this.wordToGuess = word;
        this.attemptsLeft = attempts;
        this.chooserWriter = chooserWriter; // Save chooser's writer
    }
    //methos to notify the chooser and the guesser
    public synchronized String guessLetter(char letter) {
        boolean letterFound = wordToGuess.indexOf(letter) >= 0;
        if (guessedLetters.add(letter) && !letterFound) {
            attemptsLeft--;
        }
        String progress = generateCurrentProgress();
        
        if (chooserWriter != null) {
            chooserWriter.println("Guesser guessed: " + letter + ". " + "Current Progress: " + progress + " | Attempts Left: " + attemptsLeft);
            chooserWriter.flush();
        }
        if (guesserWriter != null) {
            guesserWriter.println("You guessed: " + letter + ". " + "Current Progress: " + progress + " | Attempts Left: " + attemptsLeft);
            guesserWriter.flush();
        }
        return progress;
    }
    public void setGuesserWriter(PrintWriter guesserWriter) {
        this.guesserWriter = guesserWriter;
    }
    public void setChooserWriter(PrintWriter chooserWriter) {
        this.chooserWriter = chooserWriter;
    }
    public synchronized void broadcastMessage(String message) {
        if (chooserWriter != null) {
            chooserWriter.println(message);
            chooserWriter.flush();
        }
        if (guesserWriter != null) {
            guesserWriter.println(message);
            guesserWriter.flush();
        }
    }
    public boolean checkWinCondition() {
        for (char letter : wordToGuess.toCharArray()) {
            if (!guessedLetters.contains(letter)) {
                return false;
            }
        }
        return true;
    }

    public boolean isGameOver() {
        return attemptsLeft <= 0 || checkWinCondition();
    }

    public String generateCurrentProgress() {
        StringBuilder progress = new StringBuilder();
        for (char letter : wordToGuess.toCharArray()) {
            progress.append(guessedLetters.contains(letter) ? letter : "_");
            progress.append(" ");
        }
        return progress.toString().trim();
    }

    public synchronized void waitForGuesser() {
        while (!guesserJoined) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public synchronized void guesserHasJoined() {
        guesserJoined = true;
        notifyAll();
    }
}

public class HangmanServer {
    private final int port;
    private final Map<String, GameSession> sessions = new ConcurrentHashMap<>();
    private LeaderboardManager leaderboardManager = new LeaderboardManager(); 

    public HangmanServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket, this, leaderboardManager).start(); // Passes leaderboardManager to ClientHandler
            }
        }
    }

    public String createGameSession(String word, int attempts, PrintWriter chooserWriter) {
        String code = UUID.randomUUID().toString().substring(0, 8);
        sessions.put(code, new GameSession(word, attempts, chooserWriter)); // Passes the PrintWriter to the GameSession in order to comunicate the progress of the guesser
        return code;
    }

    public GameSession getGameSession(String code) {
        return sessions.get(code);
    }

    public static void main(String[] args) throws IOException {
        HangmanServer server = new HangmanServer(12345);
        server.start();
    }
}

class ClientHandler extends Thread {
    private final Socket clientSocket;
    private final HangmanServer server;
    private final LeaderboardManager leaderboardManager; 
    private BufferedReader in;
    private PrintWriter out;
    private String playerName;

    public ClientHandler(Socket socket, HangmanServer server, LeaderboardManager leaderboardManager) {
        this.clientSocket = socket;
        this.server = server;
        this.leaderboardManager = leaderboardManager;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            
            out.println("Enter your name:");
            playerName = in.readLine();
            out.println("Welcome, " + playerName + "!");
             
            out.println("Choose role: 1 for Chooser, 2 for Guesser");
            String role = in.readLine();
            

            if ("1".equals(role)) {
                handleChooser();
            } else if ("2".equals(role)) {
                handleGuesser();
            }
            String command = in.readLine();
            if ("VIEW_LEADERBOARD".equalsIgnoreCase(command)) {
                sendUpdatedLeaderboard();
            }
            
        } catch (IOException e) {
            System.err.println("Exception in client handler: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing a client socket: " + e.getMessage());
            }
        }
    }

    private void handleChooser() throws IOException {
        out.println("Enter word to guess ('random' for a random word):");
        String word = in.readLine();
        if ("random".equalsIgnoreCase(word)) {
            // Loads words from file and selects a random one
            List<String> words = Files.readAllLines(Paths.get("1000_parole_italiane_comuni.txt"));
            if (words.isEmpty()) {
                out.println("Word list is empty. Using a default word.");
                word = "example"; // Falls back to a default word
            } else {
                Random rand = new Random();
                word = words.get(rand.nextInt(words.size()));
            }
        }

        out.println("Enter number of attempts (or 'i' for infinite):");
        String attemptsInput = in.readLine();
        int attempts = "i".equalsIgnoreCase(attemptsInput) ? Integer.MAX_VALUE : Integer.parseInt(attemptsInput);

        String gameCode = server.createGameSession(word, attempts, out);
        out.println("Waiting for a guesser to join the game. Game code: " + gameCode);
        GameSession session = server.getGameSession(gameCode);
        session.waitForGuesser(); // Wait for guesser to join
        session.setChooserWriter(out);
        out.println("Guesser has joined. You can now watch the game progress.");
    }

    private void handleGuesser() throws IOException {
        out.println("Enter game code:");
    String gameCode = in.readLine();
    GameSession session = server.getGameSession(gameCode);

    if (session == null) {
        out.println("Invalid game code.");
        return;
    }

    session.setGuesserWriter(out); // Sets the guesser's PrintWriter in the session
    session.guesserHasJoined(); // Notifyies the session that the guesser has joined
    playGame(session); // Proceeds with the handling of the game
    }

    private void playGame(GameSession session) throws IOException {
        boolean gameWon = false;
        while (!session.isGameOver()) {
            out.println("Guess a letter:");
            char letter = in.readLine().charAt(0);
            String progress = session.guessLetter(letter);
            out.println(progress);

            if (session.checkWinCondition()) {
                out.println("Congratulations, you've won!");
                gameWon = true;
                int score = session.wordToGuess.length() * session.attemptsLeft;
            leaderboardManager.updateLeaderboard(playerName, score);
            out.println("Your score of " + score + " has been added to the leaderboard.");
                
            }

            if (session.isGameOver()) {
                out.println("Game over. The word was: " + session.wordToGuess);
                
            }
        }
        out.println("Type 'view leaderboard' to see the leaderboard or 'exit' to quit:");
        handlePostGameCommands();
        
    }
    private void handlePostGameCommands() throws IOException {
        String command;
        while((command = in.readLine()) != null) {
            if ("view leaderboard".equalsIgnoreCase(command)) {
                sendUpdatedLeaderboard();
                break; 
            }
        }
    }
    private void sendUpdatedLeaderboard() {
        out.println("Leaderboard:\n" + leaderboardManager.getFormattedLeaderboard());
        out.println("END_LEADERBOARD"); // Marks the end of leaderboard data
    }
}
