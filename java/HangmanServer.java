import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
// gestisce la parte server dell'applicativo ed in particolare le sessioni di gioco
public class HangmanServer {
    private int port;
    private ExecutorService executor; // For handling client threads
    private ConcurrentHashMap<String, GameSession> sessions; //collection for game sessions

    public HangmanServer(int port) {
        this.port = port;
        this.executor = Executors.newCachedThreadPool(); // Initialize the thread pool for client handlers
        this.sessions = new ConcurrentHashMap<>(); // Initialize the sessions map
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("server inizializzato su porta: " + port);

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept(); // Accept a client connection
                    ClientHandler handler = new ClientHandler(clientSocket, this); // Create a handler for the client
                    executor.execute(handler); // Execute the handler in its own thread
                } catch (IOException e) {
                    System.err.println("errore nella richiesta: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("porta occupata " + port + ": " + e.getMessage());
        }
    }

    public ConcurrentHashMap<String, GameSession> getSessions() {
        return sessions;
    }

    public static void main(String[] args) {
        int port = 4444; 
        HangmanServer server = new HangmanServer(port);
        server.start();
    }
}