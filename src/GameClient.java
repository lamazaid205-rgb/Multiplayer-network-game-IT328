package phase1;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameClient implements ClientConnection {

    // ==============================
    // 1. Client Variables
    // ==============================
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private final GameClientGUI gui;

    // ==============================
    // 2. Constructor
    // ==============================
    public GameClient(GameClientGUI gui) {
        this.gui = gui;
        this.gui.setClientConnection(this);
    }

    // ==============================
    // 3. Connect To Server
    // ==============================
    @Override
    public void connect(String username) {

        try {
            socket = new Socket("localhost", 1234);

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println(username);

            gui.onConnectedSuccessfully();

            Thread listenerThread = new Thread(this::listenForMessages);
            listenerThread.start();

        } catch (IOException e) {
            gui.showToast("Could not connect to server.", true);

            JOptionPane.showMessageDialog(
                    gui,
                    "Could not connect to server.",
                    "Connection Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ==============================
    // 4. Play Request
    // ==============================
    @Override
    public void sendPlayRequest() {
        if (out != null) {
            out.println("PLAY");
        }
    }

    // ==============================
    // 5. Exit Request
    // ==============================
    @Override
    public void sendExitRequest() {
        if (out != null) {
            out.println("EXIT");
        }
    }

    // ==============================
    // 6. Cell Selection
    // ==============================
    @Override
    public void sendCellSelection(int cellIndex) {
        if (out != null) {
            out.println("SELECT:" + cellIndex);
        }
    }

    // ==============================
    // 7. Send Answer
    // ==============================
    @Override
    public void sendAnswer(int cellIndex, String answer) {
        if (out != null) {
            out.println("ANSWER:" + cellIndex + ":" + answer);
        }
    }

    // ==============================
    // 8. New Game Request
    // ==============================
    @Override
    public void sendNewGameRequest() {
        if (out != null) {
            out.println("NEW_GAME");
        }
    }

    // ==============================
    // 9. Listen For Messages
    // ==============================
    private void listenForMessages() {

    try {

        String message;

        while ((message = in.readLine()) != null) {

            System.out.println(
                    "Received from server: " + message
            );

            // ==========================
            // Connected Players
            // ==========================
            if (message.startsWith("LIST:")) {

                List<String> players =
                        parseNames(message.substring(5));

                gui.updateConnectedPlayers(players);
            }

            // ==========================
            // Waiting / Joined Players
            // ==========================
            else if (message.startsWith("WAITING:")) {

                List<String> players =
                        parseNames(message.substring(8));

                gui.updateWaitingPlayers(players);
            }

            // ==========================
            // Game Players
            // ==========================
            else if (message.startsWith("GAMEPLAYERS:")) {

                List<String> players =
                        parseNames(message.substring(12));

                gui.updateGamePlayers(players);
            }

            // ==========================
            // Lobby Timer
            // ==========================
            else if (message.startsWith("TIMER:")) {

                String[] parts = message.split(":");

                int seconds =
                        Integer.parseInt(parts[1]);

                int joined =
                        Integer.parseInt(parts[2]);

                int max =
                        Integer.parseInt(parts[3]);

                gui.updateLobbyTimer(
                        seconds,
                        joined,
                        max
                );
            }

            // ==========================
            // Joined Status
            // ==========================
            else if (message.startsWith("JOINED_STATUS:")) {

                String[] parts = message.split(":");

                int joined =
                        Integer.parseInt(parts[1]);

                int max =
                        Integer.parseInt(parts[2]);

                gui.updateJoinedStatus(
                        joined,
                        max
                );
            }

            // ==========================
            // Game Full
            // ==========================
            else if (message.equals("GAME_FULL")) {

                gui.showToast(
                        "اكتمل عدد اللاعبين، لا يمكنك الانضمام الآن.",
                        true
                );

                gui.goBackToLobby();
            }

            // ==========================
            // Game Already Started
            // ==========================
            else if (message.equals("GAME_ALREADY_STARTED")) {

                gui.showToast(
                        "اللعبة بدأت بالفعل، لا يمكنك الانضمام الآن.",
                        true
                );

                gui.goBackToLobby();
            }

            // ==========================
            // Enter Game
            // ==========================
            else if (message.equals("ENTER_GAME")) {

                gui.enterGamePage();
            }

            // ==========================
            // Turn
            // ==========================
            else if (message.startsWith("TURN:")) {

                String currentPlayer =
                        message.substring(5);

                gui.showTurn(currentPlayer);
            }

            // ==========================
            // Question
            // ==========================
            else if (message.startsWith("QUESTION:")) {

                String[] parts =
                        message.split(":", 7);

                int cellIndex =
                        Integer.parseInt(parts[1]);

                String question =
                        parts[2];

                String[] options = {
                        parts[3],
                        parts[4],
                        parts[5],
                        parts[6]
                };

                gui.showQuestion(
                        cellIndex,
                        question,
                        options
                );
            }

            // ==========================
            // Cell Coloring
            // ==========================
            else if (message.startsWith("CELL:")) {

                String[] parts =
                        message.split(":");

                int cellIndex =
                        Integer.parseInt(parts[1]);

                int colorIndex =
                        Integer.parseInt(parts[2]);

                gui.colorCellByIndex(
                        cellIndex,
                        colorIndex
                );
            }

            // ==========================
            // Hide Question
            // ==========================
            else if (message.equals("HIDE_QUESTION")) {

                gui.hideQuestion();
            }

            // ==========================
            // Round Winner
            // ==========================
            else if (message.startsWith("ROUND_WIN:")) {

                gui.showToast(
                        message.substring(10),
                        false
                );
            }

            // ==========================
            // Wrong Message
            // ==========================
            else if (message.startsWith("WRONG:")) {

                gui.showToast(
                        message.substring(6),
                        true
                );
            }

            // ==========================
            // Scores
            // ==========================
            else if (message.startsWith("SCORE:")) {

                gui.updateScoresText(
                        message.substring(6)
                );
            }

            // ==========================
            // Winner
            // ==========================
            else if (message.startsWith("WINNER:")) {

                gui.showWinnerPage(
                        message.substring(7)
                );
            }

            // ==========================
            // No Winner
            // ==========================
            else if (message.equals("NO_WINNER")) {

                gui.showNoWinnerPage();
            }

            // ==========================
            // Reset Game
            // ==========================
            else if (message.equals("RESET_GAME")) {

                gui.resetBoard();
            }

            // ==========================
            // Info Messages
            // ==========================
            else if (message.startsWith("INFO:")) {

                gui.showToast(
                        message.substring(5),
                        false
                );
            }

            // ==========================
            // Default
            // ==========================
            else {

                gui.showToast(message, false);
            }
        }

    } catch (IOException e) {

        gui.showToast(
                "Disconnected from server",
                true
        );
    }
}

    // ==============================
    // 10. Parse Names Helper
    // ==============================
    private List<String> parseNames(String data) {

        List<String> names = new ArrayList<>();
        String[] parts = data.split(",");

        for (String name : parts) {
            if (!name.trim().isEmpty()) {
                names.add(name.trim());
            }
        }

        return names;
    }

    // ==============================
    // 11. Main Method
    // ==============================
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            GameClientGUI gui = new GameClientGUI();

            new GameClient(gui);

            gui.setVisible(true);
        });
    }
}