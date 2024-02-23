import java.io.*;
import java.net.*;

public class HangmanClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Waiting for the server to request the player name...");
            String serverMessage = in.readLine(); // Expecting "Enter your name:"
            if (serverMessage != null) {
                System.out.println(serverMessage); // Displays server's prompt to the player
                String name = userInput.readLine(); // Reads the player's name from console
                out.println(name); // Sends the player's name to the server
            }

       boolean gameInProgress = true;
       while (gameInProgress) {
            serverMessage = in.readLine();
           if (serverMessage == null) {
               continue; // Waits for the next message
           }
           System.out.println(serverMessage);

              // Handles user input for specific prompts
           if ("Enter command:".equalsIgnoreCase(serverMessage)) {
            String command = userInput.readLine();
            out.println(command);
            if ("view leaderboard".equalsIgnoreCase(command)) {
                // Displays the leaderboard once received from the server
                continue; // Skips the next part of the loop
            }
        }

           // Handles user input for specific prompts that regulate the game flow
           if ("Choose role: 1 for Chooser, 2 for Guesser".equals(serverMessage) ||
               "Guess a letter:".equals(serverMessage) ||
               "Enter word to guess ('random' for a random word):".equals(serverMessage) ||
               "Enter number of attempts (or 'i' for infinite):".equals(serverMessage) ||
               "Enter game code:".equals(serverMessage)) {
               String input = userInput.readLine();
               out.println(input);
           } 
           if (serverMessage.equals("Type 'view leaderboard' to see the leaderboard or 'exit' to quit:")) {
            // This prompt comes from the server after the game concludes
            System.out.println("Enter your choice:");
            String choice = userInput.readLine(); // Reads the player's choice
            out.println(choice); // Sends it back to the server
            
            if ("view leaderboard".equalsIgnoreCase(choice)) {
                // Displays the leaderboard once received from the server
                while (!(serverMessage = in.readLine()).equals("END_LEADERBOARD")) {
                    System.out.println(serverMessage);
                }
            }
            gameInProgress = false; // Stops the client loop after handling the post-game action
        }
           
           
       }

   } catch (IOException e) {
       System.err.println("Client error: " + e.getMessage());
   }
    }
}