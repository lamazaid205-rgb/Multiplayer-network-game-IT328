package phase1;

public class BroadcastManager {

    // ==============================
    // 1. Broadcast To All Connected Clients
    // ==============================
    public static void broadcast(String message) {
        for (ClientHandler client : MainServer.clients) {
            client.sendMessage(message);
        }
    }

    // ==============================
    // 2. Broadcast Only To Game Players
    // ==============================
    public static void broadcastToGame(String message) {
        for (ClientHandler client : MainServer.clients) {
            if (MainServer.isInGame(client.getUsername())) {
                client.sendMessage(message);
            }
        }
    }
}