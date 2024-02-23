import java.io.*;
import java.nio.file.*;
import java.util.*;

class PlayerScore implements Comparable<PlayerScore> {
    private String name;
    private int score;

    public PlayerScore(String name, int score) {
        this.name = name;
        this.score = score;
    }

    @Override
    public int compareTo(PlayerScore other) {
        return Integer.compare(other.score, this.score);
    }

    @Override
    public String toString() {
        return String.format("%-20s | %d", name, score);
    }
    public String getName() {
        return name;
    }
    public int getScore() {
        return score;
    }
}

class LeaderboardManager {
    private static final String LEADERBOARD_FILE = "leaderboard.txt";
    private List<PlayerScore> leaderboard = new ArrayList<>();

    public LeaderboardManager() {
        loadLeaderboard();
    }

    private void loadLeaderboard() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(LEADERBOARD_FILE));
            for (String line : lines) {
                String[] parts = line.split(",");
                leaderboard.add(new PlayerScore(parts[0], Integer.parseInt(parts[1].trim())));
            }
            Collections.sort(leaderboard);
        } catch (IOException e) {
            System.out.println("Error loading leaderboard: " + e.getMessage());
        }
    }

    public synchronized void updateLeaderboard(String playerName, int score) {
        leaderboard.add(new PlayerScore(playerName, score));
        Collections.sort(leaderboard);
        if (leaderboard.size() > 10) { // Keeps only the top 10 scores
            leaderboard = leaderboard.subList(0, 10);
        }
        saveLeaderboard();
    }

    private void saveLeaderboard() {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(LEADERBOARD_FILE))) {
            for (PlayerScore playerScore : leaderboard) {
                bw.write(playerScore.getName() + "," + playerScore.getScore());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving leaderboard: " + e.getMessage());
        }
    }

    public String getFormattedLeaderboard() {
        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════╗\n");
        sb.append("║         Leaderboard          ║\n");
        sb.append("╠═════════════════╤════════════╣\n");
        sb.append("║ Player          │ Score      ║\n");
        sb.append("╠═════════════════╧════════════╣\n");
        leaderboard.forEach(playerScore -> sb.append("║ ").append(playerScore).append(" ║\n"));
        sb.append("╚══════════════════════════════╝\n");
        return sb.toString();
    }
}
