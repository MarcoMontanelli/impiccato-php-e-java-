import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
// gestisce la partita con la logica e parola da indovinare (stato)
public class GameSession {
    private StringBuilder wordToGuess;
    private Set<String> players = new HashSet<>();
    private int attemptsLeft;
    private boolean infiniteAttempts;
    private StringBuilder currentGuessState; 
    private String gameCode;
    private GameState gameState;

    // Enum to represent the state of the game
    private enum GameState {
        ONGOING, WON, LOST
    }

    public GameSession(String playerName, boolean infiniteAttempts, int attempts, String wordToGuess, String gameCode) {
        this.wordToGuess = new StringBuilder(wordToGuess);
        this.attemptsLeft = attempts;
        this.infiniteAttempts = infiniteAttempts;
        this.gameCode = gameCode;
        this.players = new HashSet<>();
        this.currentGuessState = new StringBuilder("_".repeat(wordToGuess.length()));

        
        addPlayer(playerName); // Adds the game creator as the first player
    }

    public synchronized boolean addPlayer(String playerName) {
        return players.add(playerName);
    }

    public synchronized boolean attemptGuess(String playerName, char guess) {
        if (gameState != GameState.ONGOING) {
            return false; // No more guesses allowed if the game has ended
        }

        guess = Character.toLowerCase(guess); // Normalizes the guess to lowercase
        boolean isCorrect = false;
        for (int i = 0; i < wordToGuess.length(); i++) {
            if (wordToGuess.charAt(i) == guess && currentGuessState.charAt(i) == '_') {
                currentGuessState.setCharAt(i, guess);
                isCorrect = true;
            }
        }

        if (!isCorrect && !infiniteAttempts) {
            attemptsLeft--;
            if (attemptsLeft <= 0) {
                gameState = GameState.LOST;
            }
        }

        // Check if the game is won
        if (!currentGuessState.toString().contains("_")) {
            gameState = GameState.WON;
        }

        return isCorrect;
    }

    public synchronized GameState getGameState() {
        return gameState;
    }

    public synchronized String getCurrentGuessState() {
        return currentGuessState.toString();
    }
}
