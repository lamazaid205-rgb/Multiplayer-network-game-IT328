package phase1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class MainServer {

    public static Vector<ClientHandler> clients = new Vector<>();
    public static Vector<String> waitingPlayers = new Vector<>();
    public static Vector<String> gamePlayers = new Vector<>();

    private static final int MAX_GAME_PLAYERS = 4;
    private static final int MIN_GAME_PLAYERS = 2;

    private static int port = 1234;
    private static boolean gameStarted = false;
    private static boolean gameEnded = false;
    private static int currentTurnIndex = 0;
    private static boolean startTimerRunning = false;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Server is running on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New connection attempt...");

                ClientHandler clientThread = new ClientHandler(socket);
                new Thread(clientThread).start();
            }

        } catch (IOException e) {
            System.out.println("Server Error: " + e.getMessage());
        }
    }

    public static void updateAllClients() {
        StringBuilder list = new StringBuilder("LIST:");

        for (ClientHandler c : clients) {
            if (c.getUsername() != null) {
                list.append(c.getUsername()).append(",");
            }
        }

        BroadcastManager.broadcast(list.toString());
    }

    public static void updateWaitingRoom() {
        BroadcastManager.broadcast("WAITING:");
    }

    public static void updateGamePlayers() {
        StringBuilder list = new StringBuilder("GAMEPLAYERS:");

        for (String name : gamePlayers) {
            list.append(name).append(",");
        }

        BroadcastManager.broadcast(list.toString());
    }

    private static void sendJoinedStatus() {
        BroadcastManager.broadcast(
                "JOINED_STATUS:" + gamePlayers.size() + ":" + MAX_GAME_PLAYERS
        );
    }

    public static boolean isInGame(String username) {
        return username != null && gamePlayers.contains(username);
    }

    public static ClientHandler findClientByUsername(String username) {
        for (ClientHandler client : clients) {
            if (username != null && username.equals(client.getUsername())) {
                return client;
            }
        }
        return null;
    }

    public static synchronized void handlePlayRequest(ClientHandler client) {
        String username = client.getUsername();

        if (username == null || username.isBlank()) return;

        if (gameStarted && !gameEnded) {
            client.sendMessage("GAME_ALREADY_STARTED");
            return;
        }

        if (gamePlayers.contains(username)) {
            client.sendMessage("INFO:You are already joined.");
            sendJoinedStatus();
            return;
        }

        if (gamePlayers.size() >= MAX_GAME_PLAYERS) {
            client.sendMessage("GAME_FULL");
            return;
        }

        gamePlayers.add(username);
        GameManager.initPlayer(username);

        BroadcastManager.broadcast(
                "INFO:" + username + " joined the game players."
        );

        updateGamePlayers();
        sendJoinedStatus();
        checkStartCondition();
    }

    private static synchronized void checkStartCondition() {
        if (gameStarted) return;

        if (gamePlayers.size() >= MAX_GAME_PLAYERS) {
            BroadcastManager.broadcast(
                    "TIMER:0:" + gamePlayers.size() + ":" + MAX_GAME_PLAYERS
            );
            startGameNow();
            return;
        }

        if (gamePlayers.size() >= MIN_GAME_PLAYERS && !startTimerRunning) {
            startTimerRunning = true;

            new Thread(() -> {
                for (int seconds = 30; seconds >= 0; seconds--) {

                    synchronized (MainServer.class) {
                        if (gameStarted || gamePlayers.size() < MIN_GAME_PLAYERS) {
                            startTimerRunning = false;
                            sendJoinedStatus();
                            return;
                        }

                        BroadcastManager.broadcast(
                                "TIMER:" + seconds + ":" +
                                        gamePlayers.size() + ":" +
                                        MAX_GAME_PLAYERS
                        );

                        if (gamePlayers.size() >= MAX_GAME_PLAYERS) {
                            startTimerRunning = false;
                            startGameNow();
                            return;
                        }
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                synchronized (MainServer.class) {
                    startTimerRunning = false;

                    if (!gameStarted && gamePlayers.size() >= MIN_GAME_PLAYERS) {
                        startGameNow();
                    }
                }

            }).start();
        }
    }

    private static synchronized void startGameNow() {
        if (gameStarted || gamePlayers.size() < MIN_GAME_PLAYERS) return;

        gameStarted = true;
        gameEnded = false;
        startTimerRunning = false;
        currentTurnIndex = 0;

        GameManager.resetGame();

        for (String player : gamePlayers) {
            ClientHandler c = findClientByUsername(player);

            if (c != null) {
                c.sendMessage("ENTER_GAME");
            }
        }

        BroadcastManager.broadcastToGame("INFO:Game started for all players.");
        BroadcastManager.broadcastToGame(GameManager.getScoreMessage());
        broadcastCurrentTurn();
    }

    private static synchronized String getCurrentTurnPlayer() {
        if (gamePlayers.isEmpty()) return null;

        if (currentTurnIndex < 0 || currentTurnIndex >= gamePlayers.size()) {
            currentTurnIndex = 0;
        }

        return gamePlayers.get(currentTurnIndex);
    }

    private static void moveToNextTurn() {
        new Thread(() -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (MainServer.class) {
                if (gamePlayers.isEmpty() || gameEnded) return;

                currentTurnIndex = (currentTurnIndex + 1) % gamePlayers.size();
                broadcastCurrentTurn();
            }
        }).start();
    }

    private static synchronized void broadcastCurrentTurn() {
        if (!gameStarted || gameEnded || gamePlayers.isEmpty()) return;

        String currentPlayer = getCurrentTurnPlayer();

        if (currentPlayer != null) {
            BroadcastManager.broadcastToGame("TURN:" + currentPlayer);
        }
    }

    public static synchronized void handleCellSelection(ClientHandler client, int cellIndex) {
        String username = client.getUsername();

        if (!gameStarted) {
            client.sendMessage("WRONG:Game has not started yet.");
            return;
        }

        if (gameEnded) {
            client.sendMessage("WRONG:The game has ended.");
            return;
        }

        if (!gamePlayers.contains(username)) {
            client.sendMessage("WRONG:You are not in the game.");
            return;
        }

        String currentPlayer = getCurrentTurnPlayer();

        if (!username.equals(currentPlayer)) {
            client.sendMessage("WRONG:ليس دورك الآن، الدور على " + currentPlayer);
            return;
        }

        if (!GameManager.isCellAvailable(cellIndex)) {
            client.sendMessage("WRONG:This cell is already owned.");
            return;
        }

        Question q = GameManager.startQuestionForCell(cellIndex);

        if (q == null) {
            client.sendMessage("WRONG:Invalid cell selection.");
            return;
        }

        String[] options = q.getOptions();

        BroadcastManager.broadcastToGame(
                "QUESTION:" + cellIndex + ":" +
                        q.getQuestion() + ":" +
                        options[0] + ":" +
                        options[1] + ":" +
                        options[2] + ":" +
                        options[3]
        );

        BroadcastManager.broadcastToGame(
                "INFO:" + username + " selected letter " + GameManager.getLetter(cellIndex)
        );
    }

    public static synchronized void handleAnswer(
            ClientHandler client,
            int cellIndex,
            String answer
    ) {
        String username = client.getUsername();

        if (!gameStarted || gameEnded) {
            client.sendMessage("WRONG:The game is not active.");
            return;
        }

        if (!gamePlayers.contains(username)) {
            client.sendMessage("WRONG:You are not in the game.");
            return;
        }

        GameManager.AnswerResult result =
                GameManager.submitFirstAnswer(username, cellIndex, answer);

        if (!result.accepted) {
            client.sendMessage("WRONG:" + result.message);
            return;
        }

        if (result.correct) {
            int colorIndex = gamePlayers.indexOf(username);

            BroadcastManager.broadcastToGame("CELL:" + cellIndex + ":" + colorIndex);
            BroadcastManager.broadcastToGame(
                    "ROUND_WIN:" + username + " answered correctly ✅"
            );
            BroadcastManager.broadcastToGame("HIDE_QUESTION");
            BroadcastManager.broadcastToGame(GameManager.getScoreMessage());

            if (GameManager.hasWinner(username)) {
                gameEnded = true;
                BroadcastManager.broadcastToGame("WINNER:" + username);
                return;
            }

            if (GameManager.isBoardFull()) {
                gameEnded = true;
                BroadcastManager.broadcastToGame("NO_WINNER");
                return;
            }

            moveToNextTurn();

        } else {
            BroadcastManager.broadcastToGame(
                    "WRONG:" + username + " answered incorrectly ❌"
            );

            BroadcastManager.broadcastToGame("HIDE_QUESTION");
            moveToNextTurn();
        }
    }

    public static synchronized void handleExitRequest(ClientHandler client) {
        String username = client.getUsername();

        if (username == null || username.isBlank()) return;

        boolean removedFromGame = gamePlayers.remove(username);

        if (removedFromGame) {
            BroadcastManager.broadcast("INFO:" + username + " left the game.");

            if (currentTurnIndex >= gamePlayers.size()) {
                currentTurnIndex = 0;
            }

            if (gamePlayers.size() < MIN_GAME_PLAYERS && gameStarted && !gameEnded) {
                gameEnded = true;
                BroadcastManager.broadcastToGame("NO_WINNER");
            } else {
                broadcastCurrentTurn();
            }
        }

        updateWaitingRoom();
        updateGamePlayers();
        sendJoinedStatus();
        BroadcastManager.broadcastToGame(GameManager.getScoreMessage());
    }

    public static synchronized void removeDisconnectedPlayer(String username) {
        if (username == null || username.isBlank()) return;

        boolean removedFromGame = gamePlayers.remove(username);

        if (removedFromGame) {
            BroadcastManager.broadcast("INFO:" + username + " disconnected and left the game.");
        } else {
            BroadcastManager.broadcast("INFO:" + username + " disconnected.");
        }

        updateAllClients();
        updateWaitingRoom();
        updateGamePlayers();
        sendJoinedStatus();

        if (gamePlayers.size() < MIN_GAME_PLAYERS && gameStarted && !gameEnded) {
            gameEnded = true;
            BroadcastManager.broadcastToGame("NO_WINNER");
        } else if (gameStarted && !gameEnded) {
            broadcastCurrentTurn();
        }
    }

    public static synchronized void handleNewGameRequest() {
        if (gamePlayers.size() >= MIN_GAME_PLAYERS) {
            gameStarted = true;
            gameEnded = false;
            currentTurnIndex = 0;

            GameManager.resetGame();

            BroadcastManager.broadcastToGame("RESET_GAME");
            BroadcastManager.broadcastToGame(GameManager.getScoreMessage());
            BroadcastManager.broadcastToGame("INFO:New game started.");
            broadcastCurrentTurn();
        }
    }
}