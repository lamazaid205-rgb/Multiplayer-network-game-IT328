package phase1;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    // ==============================
    // 1. Client Data
    // ==============================
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public String getUsername() {
        return username;
    }

    // ==============================
    // 2. Main Client Thread
    // ==============================
    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            username = in.readLine();

            if (username != null && !username.isBlank()) {
                MainServer.clients.add(this);
                GameManager.initPlayer(username);

                MainServer.updateAllClients();
                MainServer.updateWaitingRoom();
                MainServer.updateGamePlayers();

                BroadcastManager.broadcast("INFO:" + username + " connected.");
            }

            String message;

            while ((message = in.readLine()) != null) {

                if (message.equals("PLAY")) {
                    MainServer.handlePlayRequest(this);

                } else if (message.equals("EXIT")) {
                    MainServer.handleExitRequest(this);

                } else if (message.equals("NEW_GAME")) {
                    MainServer.handleNewGameRequest();

                } else if (message.startsWith("SELECT:")) {
                    handleSelect(message);

                } else if (message.startsWith("ANSWER:")) {
                    handleAnswer(message);

                } else {
                    BroadcastManager.broadcast("INFO:" + username + ": " + message);
                }
            }

        } catch (IOException e) {
            System.out.println("Connection lost with: " + username);
        } finally {
            closeConnection();
        }
    }

    // ==============================
    // 3. Cell Selection Handler
    // ==============================
    private void handleSelect(String message) {
        try {
            int cellIndex = Integer.parseInt(message.substring(7));
            MainServer.handleCellSelection(this, cellIndex);

        } catch (Exception e) {
            sendMessage("WRONG:Invalid cell selection.");
        }
    }

    // ==============================
    // 4. Answer Handler
    // ==============================
    private void handleAnswer(String message) {
        try {
            String[] parts = message.split(":", 3);

            int cellIndex = Integer.parseInt(parts[1]);
            String answer = parts[2];

            MainServer.handleAnswer(this, cellIndex, answer);

        } catch (Exception e) {
            sendMessage("WRONG:Invalid answer format.");
        }
    }

    // ==============================
    // 5. Send Message To This Client
    // ==============================
    public void sendMessage(String msg) {
        if (out != null) {
            out.println(msg);
        }
    }

    // ==============================
    // 6. Close Connection
    // ==============================
    private void closeConnection() {
        try {
            MainServer.clients.remove(this);
            MainServer.removeDisconnectedPlayer(username);

            if (socket != null) {
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}