package phase1;

public interface ClientConnection {

    // ==============================
    // 1. Connection Actions
    // ==============================
    void connect(String username);

    // ==============================
    // 2. Room Actions
    // ==============================
    void sendPlayRequest();
    void sendExitRequest();

    // ==============================
    // 3. Game Actions
    // ==============================
    void sendCellSelection(int cellIndex);
    void sendAnswer(int cellIndex, String answer);
    void sendNewGameRequest();
}