package phase1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class GameClientGUI extends JFrame {

    // ==============================
    // 1. Theme Colors
    // ==============================
    private static final Color PURPLE_DARK = new Color(45, 24, 105);
    private static final Color PURPLE = new Color(78, 39, 150);
    private static final Color PURPLE_LIGHT = new Color(115, 65, 190);
    private static final Color GOLD = new Color(255, 205, 65);
    private static final Color RED = new Color(235, 70, 65);
    private static final Color BLUE = new Color(75, 195, 220);
    private static final Color WHITE = new Color(250, 248, 255);
    private static final Color CARD_DARK = new Color(40, 36, 70, 230);

    private static final Color[] PLAYER_COLORS = {
            RED, BLUE, GOLD, PURPLE_LIGHT
    };

    // ==============================
    // 2. Board Data
    // ==============================
    private static final String[] ARABIC_LETTERS = {
            "ن","م","ك","ي","ت",
            "ط","ز","ق","ث","ض",
            "خ","م","ج","ر","أ",
            "ح","س","غ","ذ","ه",
            "ف","ل","ب","ش","ع"
    };

    private static final int[] ROW_SIZES = {5, 5, 5, 5, 5};

    // ==============================
    // 3. Cards
    // ==============================
    private static final String CARD_LOGIN = "LOGIN";
    private static final String CARD_LOBBY = "LOBBY";
    private static final String CARD_WAITING = "WAITING";
    private static final String CARD_GAME = "GAME";
    private static final String CARD_RESULT = "RESULT";
    

    // ==============================
    // 4. Main Components
    // ==============================
    private String myPlayerName = "";
    private String currentTurnPlayer = "";
    private JPanel resultPlayersPanel;
    private String winnerName = "";
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private ClientConnection clientConnection;

    private JTextField nameField;
    private JButton connectButton;

    private DefaultListModel<String> connectedModel = new DefaultListModel<>();
    private DefaultListModel<String> waitingModel = new DefaultListModel<>();

    private JList<String> connectedList;
    private JList<String> waitingList;

    private HexBoardPanel hexBoard;
    private JPanel playerBadgesPanel;
    private JLabel turnLabel;

    private JPanel questionPanel;
    private JLabel questionLabel;
    private JButton[] optionButtons = new JButton[4];
    private int currentQuestionCell = -1;

    private JLabel winnerTitleLabel;
    private JLabel resultScoreLabel;
    private JLabel joinedCountLabel;
    private JLabel timerLabel;
    private JLabel resultTitleLabel;

    private List<String> currentGamePlayers = new ArrayList<>();
    private Map<String, Integer> playerScores = new HashMap<>();
    private Map<String, Color> playerColorMap = new HashMap<>();

    // ==============================
    // 5. Constructor
    // ==============================
    public GameClientGUI() {
        setTitle("حروف مع ITers");
        setSize(1200, 760);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(buildLoginPage(), CARD_LOGIN);
        mainPanel.add(buildLobbyPage(), CARD_LOBBY);
        mainPanel.add(buildWaitingPage(), CARD_WAITING);
        mainPanel.add(buildGamePage(), CARD_GAME);
        mainPanel.add(buildResultPage(), CARD_RESULT);

        setContentPane(mainPanel);
        cardLayout.show(mainPanel, CARD_LOGIN);
    }

    // ==============================
    // 6. Login Page
    // ==============================
    private JPanel buildLoginPage() {
        JPanel root = backgroundPanel();
        root.setLayout(new GridBagLayout());

        JPanel card = glassCard(500, 540);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(35, 50, 35, 50));

        JLabel logo = buildLogo(300, 230);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(Box.createVerticalStrut(10));
        card.add(logo);
        card.add(Box.createVerticalStrut(30));

        JLabel nameLabel = label("اسم اللاعب", 18, Font.BOLD, WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(nameLabel);
        card.add(Box.createVerticalStrut(12));

        nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(320, 48));
        nameField.setHorizontalAlignment(JTextField.CENTER);
        nameField.setFont(arabicFont(16, Font.BOLD));
        card.add(nameField);
        card.add(Box.createVerticalStrut(28));

        connectButton = gradientButton("ابدأ اللعب", 260, 52);
        connectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        connectButton.addActionListener(e -> doConnect());
        card.add(connectButton);

        root.add(card);
        return root;
    }

    private void doConnect() {

    String name =
            nameField.getText().trim();

    if (name.isEmpty()) {

        showToast(
                "Please enter your name",
                true
        );

        return;
    }

    myPlayerName = name;

    if (clientConnection != null) {

        clientConnection.connect(name);
    }
}

    // ==============================
    // 7. Lobby Page
    // ==============================
    private JPanel buildLobbyPage() {
    JPanel root = backgroundPanel();
    root.setLayout(new BorderLayout());

    JPanel center = new JPanel(new GridBagLayout());
    center.setOpaque(false);

    JPanel card = glassCard(570, 560);
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
    card.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

    JLabel logo = buildLogo(210, 155);
    logo.setAlignmentX(Component.CENTER_ALIGNMENT);
    card.add(logo);
    card.add(Box.createVerticalStrut(18));

    JLabel title = label("اللاعبون المتصلون", 22, Font.BOLD, GOLD);
    title.setAlignmentX(Component.CENTER_ALIGNMENT);
    card.add(title);
    card.add(Box.createVerticalStrut(18));

    connectedList = new JList<>(connectedModel);
    styleDiamondList(connectedList);

    JScrollPane scroll = listScroll(connectedList);
    scroll.setPreferredSize(new Dimension(440, 250));
    scroll.setMaximumSize(new Dimension(440, 250));
    card.add(scroll);

    card.add(Box.createVerticalStrut(25));

    JButton playBtn = gradientButton("بدء اللعبة", 250, 52);
    playBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

    playBtn.addActionListener(e -> {
        if (clientConnection != null) {
            clientConnection.sendPlayRequest();
        }

        goTo(CARD_WAITING);
    });

    card.add(playBtn);

    center.add(card);
    root.add(center, BorderLayout.CENTER);

    return root;
}
    public void goBackToLobby() {

    SwingUtilities.invokeLater(() -> {
        goTo(CARD_LOBBY);
    });
}
    // ==============================
    // 8. Waiting Page
    // ==============================
    private JPanel buildWaitingPage() {
    JPanel root = backgroundPanel();
    root.setLayout(new BorderLayout());

    JPanel center = new JPanel(new GridBagLayout());
    center.setOpaque(false);

    JPanel card = glassCard(570, 620);
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
    card.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

    JLabel logo = buildLogo(210, 155);
    logo.setAlignmentX(Component.CENTER_ALIGNMENT);
    card.add(logo);
    card.add(Box.createVerticalStrut(14));

    JLabel title = label("اللاعبون المنضمون", 22, Font.BOLD, GOLD);
    title.setAlignmentX(Component.CENTER_ALIGNMENT);
    card.add(title);
    card.add(Box.createVerticalStrut(14));

    waitingList = new JList<>(waitingModel);
    styleDiamondList(waitingList);

    JScrollPane scroll = listScroll(waitingList);
    scroll.setPreferredSize(new Dimension(440, 210));
    scroll.setMaximumSize(new Dimension(440, 210));
    card.add(scroll);

    card.add(Box.createVerticalStrut(18));

    JPanel timerCard = glassCard(440, 95);
    timerCard.setLayout(new BoxLayout(timerCard, BoxLayout.Y_AXIS));
    timerCard.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
    timerCard.setAlignmentX(Component.CENTER_ALIGNMENT);

    joinedCountLabel = label("اللاعبون المنضمون: 0 / 4", 18, Font.BOLD, WHITE);
    joinedCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    timerLabel = label("بانتظار لاعب آخر...", 18, Font.BOLD, GOLD);
    timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    timerCard.add(joinedCountLabel);
    timerCard.add(Box.createVerticalStrut(8));
    timerCard.add(timerLabel);

    card.add(timerCard);
    card.add(Box.createVerticalStrut(20));

    JButton exitBtn = outlineButton("العودة", 180, 48);
    exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    exitBtn.addActionListener(e -> {
        if (clientConnection != null) {
            clientConnection.sendExitRequest();
        }
        goTo(CARD_LOBBY);
    });

    card.add(exitBtn);

    center.add(card);
    root.add(center, BorderLayout.CENTER);

    return root;
}

    // ==============================
    // 9. Game Page
    // ==============================
    private JPanel buildGamePage() {
        JPanel root = backgroundPanel();
        root.setLayout(new BorderLayout());

        JPanel left = new JPanel(new BorderLayout());
        left.setOpaque(false);

        turnLabel = label("Waiting for players...", 21, Font.BOLD, GOLD);
        turnLabel.setBorder(BorderFactory.createEmptyBorder(18, 0, 8, 0));
        left.add(turnLabel, BorderLayout.NORTH);

        hexBoard = new HexBoardPanel();
        JPanel boardWrap = new JPanel(new BorderLayout());
        boardWrap.setOpaque(false);
        boardWrap.setBorder(BorderFactory.createEmptyBorder(5, 25, 5, 15));
        boardWrap.add(hexBoard, BorderLayout.CENTER);
        left.add(boardWrap, BorderLayout.CENTER);

        questionPanel = buildQuestionPanel();
        questionPanel.setVisible(false);
        left.add(questionPanel, BorderLayout.SOUTH);

        root.add(left, BorderLayout.CENTER);

        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setPreferredSize(new Dimension(320, 0));
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBorder(BorderFactory.createEmptyBorder(25, 18, 25, 18));

        JLabel logo = buildLogo(230, 170);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        right.add(logo);
        right.add(Box.createVerticalStrut(25));

        playerBadgesPanel = new JPanel();
        playerBadgesPanel.setOpaque(false);
        playerBadgesPanel.setLayout(new BoxLayout(playerBadgesPanel, BoxLayout.Y_AXIS));
        right.add(playerBadgesPanel);

        right.add(Box.createVerticalGlue());

        JButton exitBtn = outlineButton("Exit Game", 190, 50);
        exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitBtn.addActionListener(e -> {
            if (clientConnection != null) {
                clientConnection.sendExitRequest();
            }
            goTo(CARD_LOBBY);
        });
        right.add(exitBtn);

        root.add(right, BorderLayout.EAST);
        return root;
    }

    // ==============================
    // 10. Question Panel
    // ==============================
    private JPanel buildQuestionPanel() {
        JPanel panel = glassCard(0, 190);
        panel.setLayout(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 25, 16, 25));

        questionLabel = label("Question", 19, Font.BOLD, WHITE);
        panel.add(questionLabel, BorderLayout.NORTH);

        JPanel options = new JPanel(new GridLayout(2, 2, 12, 10));
        options.setOpaque(false);

        for (int i = 0; i < 4; i++) {
            JButton btn = outlineButton("", 220, 46);
            final int index = i;

            btn.addActionListener(e -> {
                if (clientConnection != null && currentQuestionCell != -1) {
                    clientConnection.sendAnswer(
                            currentQuestionCell,
                            optionButtons[index].getText()
                    );
                }
                questionPanel.setVisible(false);
            });

            optionButtons[i] = btn;
            options.add(btn);
        }

        panel.add(options, BorderLayout.CENTER);
        return panel;
    }

    // ==============================
    // 11. Result Page
    // ==============================
    private JPanel buildResultPage() {

    JPanel root = backgroundPanel();
    root.setLayout(new GridBagLayout());

    JPanel card = glassCard(980, 600);
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
    card.setBorder(BorderFactory.createEmptyBorder(18, 35, 22, 35));

    JLabel logo = buildLogo(190, 135);
    logo.setAlignmentX(Component.CENTER_ALIGNMENT);
    card.add(logo);

    card.add(Box.createVerticalStrut(5));

    resultTitleLabel = label("نتائج اللعبة", 30, Font.BOLD, WHITE);
resultTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
card.add(resultTitleLabel);

    card.add(Box.createVerticalStrut(15));

    resultPlayersPanel = new JPanel();
    resultPlayersPanel.setOpaque(false);
    resultPlayersPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 0));
    resultPlayersPanel.setMaximumSize(new Dimension(930, 235));
    resultPlayersPanel.setPreferredSize(new Dimension(930, 235));

    card.add(resultPlayersPanel);

    card.add(Box.createVerticalStrut(18));

    JButton newGameBtn = gradientButton("لعبة جديدة", 280, 55);
    newGameBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    newGameBtn.addActionListener(e -> {
        if (clientConnection != null) {
            clientConnection.sendNewGameRequest();
        }
    });

    card.add(newGameBtn);

    root.add(card);
    return root;
}

    // ==============================
    // 12. Hex Board Panel
    // ==============================
    class HexBoardPanel extends JPanel {

        private static final int R = 65;
        private Color[] owned = new Color[ARABIC_LETTERS.length];

        HexBoardPanel() {
    setOpaque(false);

    addMouseListener(new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {

            if (currentTurnPlayer == null || currentTurnPlayer.isBlank()) {
                showToast("انتظري بدء الدور", true);
                return;
            }

            if (!myPlayerName.equals(currentTurnPlayer)) {
                showToast("ليس دورك الآن، الدور على " + currentTurnPlayer, true);
                return;
            }

            int index = getClickedCell(e.getX(), e.getY());

            if (index != -1 && clientConnection != null) {
                clientConnection.sendCellSelection(index);
            }
        }
    });
}

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            double hexW = Math.sqrt(3) * R;
            double hexH = 2.0 * R;
            double stepX = hexW;
            double stepY = hexH * 0.75;

            int rows = ROW_SIZES.length;
            double totalH = (rows - 1) * stepY + hexH;

            int maxCols = 0;
            for (int rowSize : ROW_SIZES) {
                maxCols = Math.max(maxCols, rowSize);
            }

            double totalW = maxCols * stepX + stepX * 0.5;
            double originX = (getWidth() - totalW) / 2.0 + hexW / 2.0;
            double originY = (getHeight() - totalH) / 2.0 + R;

            int idx = 0;

            for (int row = 0; row < rows; row++) {
                int cols = ROW_SIZES[row];
                double offsetX = (row % 2 == 1) ? stepX * 0.5 : 0;
                double cy = originY + row * stepY;

                for (int col = 0; col < cols; col++) {
                    double cx = originX + offsetX + col * stepX;
                    drawHex(g2, cx, cy, idx);
                    idx++;
                }
            }
        }

        private void drawHex(Graphics2D g2, double cx, double cy, int idx) {
            Polygon hx = poly(cx, cy, R);

            g2.setColor(owned[idx] != null ? owned[idx] : WHITE);
            g2.fill(hx);

            g2.setColor(PURPLE_DARK);
            g2.setStroke(new BasicStroke(3f));
            g2.draw(hx);

            String letter = ARABIC_LETTERS[idx];

            Font f = arabicFont(25, Font.BOLD);
            g2.setFont(f);
            g2.setColor(owned[idx] != null ? Color.WHITE : PURPLE_DARK);

            FontMetrics fm = g2.getFontMetrics(f);

            g2.drawString(
                    letter,
                    (int) (cx - fm.stringWidth(letter) / 2.0),
                    (int) (cy + fm.getAscent() / 2.0 - fm.getDescent() / 2.0)
            );
        }

        private Polygon poly(double cx, double cy, int r) {
            Polygon p = new Polygon();

            for (int i = 0; i < 6; i++) {
                double angle = Math.toRadians(60 * i - 30);

                p.addPoint(
                        (int) (cx + r * Math.cos(angle)),
                        (int) (cy + r * Math.sin(angle))
                );
            }

            return p;
        }

        private int getClickedCell(int x, int y) {
            double hexW = Math.sqrt(3) * R;
            double hexH = 2.0 * R;
            double stepX = hexW;
            double stepY = hexH * 0.75;

            int rows = ROW_SIZES.length;
            double totalH = (rows - 1) * stepY + hexH;

            int maxCols = 0;
            for (int rowSize : ROW_SIZES) {
                maxCols = Math.max(maxCols, rowSize);
            }

            double totalW = maxCols * stepX + stepX * 0.5;
            double originX = (getWidth() - totalW) / 2.0 + hexW / 2.0;
            double originY = (getHeight() - totalH) / 2.0 + R;

            int idx = 0;

            for (int row = 0; row < rows; row++) {
                int cols = ROW_SIZES[row];
                double offsetX = (row % 2 == 1) ? stepX * 0.5 : 0;
                double cy = originY + row * stepY;

                for (int col = 0; col < cols; col++) {
                    double cx = originX + offsetX + col * stepX;
                    Polygon p = poly(cx, cy, R);

                    if (p.contains(x, y)) {
                        return idx;
                    }

                    idx++;
                }
            }

            return -1;
        }

        void colorCell(int idx, Color c) {
            if (idx >= 0 && idx < owned.length) {
                owned[idx] = c;
                repaint();
            }
        }

        void resetCells() {
            Arrays.fill(owned, null);
            repaint();
        }
    }

    // ==============================
    // 13. Public API
    // ==============================
    
    public void hideQuestion() {
    SwingUtilities.invokeLater(() -> {
        currentQuestionCell = -1;
        questionPanel.setVisible(false);
        questionPanel.revalidate();
        questionPanel.repaint();
    });
}
    public void setClientConnection(ClientConnection c) {
        this.clientConnection = c;
    }

    public void onConnectedSuccessfully() {
        SwingUtilities.invokeLater(() -> {
            goTo(CARD_LOBBY);
            showToast("Connected successfully", false);
        });
    }

    public void updateConnectedPlayers(List<String> players) {
        SwingUtilities.invokeLater(() -> {
            connectedModel.clear();

            for (String p : players) {
                if (p != null && !p.isBlank()) {
                    connectedModel.addElement(p);
                }
            }
        });
    }

    public void updateWaitingPlayers(List<String> players) {
    SwingUtilities.invokeLater(() -> {
        waitingModel.clear();

        for (String p : players) {
            if (p != null && !p.isBlank()) {
                waitingModel.addElement(p);
            }
        }
    });
}

    public void updateGamePlayers(List<String> players) {

    SwingUtilities.invokeLater(() -> {

        currentGamePlayers.clear();
        currentGamePlayers.addAll(players);

        waitingModel.clear();

        playerColorMap.clear();

        int index = 0;

        for (String player : currentGamePlayers) {

            if (player != null && !player.isBlank()) {
                waitingModel.addElement(player);
            }

            playerScores.putIfAbsent(player, 0);

            Color c =
                    PLAYER_COLORS[
                            index % PLAYER_COLORS.length
                    ];

            playerColorMap.put(player, c);

            index++;
        }

        updateJoinedStatus(currentGamePlayers.size(), 4);
        refreshPlayerBadges();
    });
}

    public void updateJoinedStatus(int joined, int max) {
    SwingUtilities.invokeLater(() -> {
        if (joinedCountLabel != null) {
            joinedCountLabel.setText(
                    "اللاعبون المنضمون: " + joined + " / " + max
            );
        }

        if (timerLabel != null) {
            if (joined < 2) {
                timerLabel.setText("بانتظار لاعب آخر...");
            } else if (joined < max) {
                timerLabel.setText("سيبدأ العد التنازلي عند اكتمال الشرط");
            } else {
                timerLabel.setText("اكتمل العدد! تبدأ اللعبة الآن");
            }
        }
    });
}

public void updateLobbyTimer(int seconds, int joined, int max) {
    SwingUtilities.invokeLater(() -> {
        if (joinedCountLabel != null) {
            joinedCountLabel.setText(
                    "اللاعبون المنضمون: " + joined + " / " + max
            );
        }

        if (timerLabel != null) {
            if (seconds <= 0) {
                timerLabel.setText("تبدأ اللعبة الآن");
            } else {
                timerLabel.setText(
                        "تبدأ اللعبة خلال: " + seconds + " ثانية"
                );
            }
        }
    });
}

    public void enterGamePage() {
        SwingUtilities.invokeLater(() -> goTo(CARD_GAME));
    }

    public void enterWaitingPage() {
        SwingUtilities.invokeLater(() -> goTo(CARD_WAITING));
    }

    public void showQuestion(int cellIndex, String question, String[] options) {
        SwingUtilities.invokeLater(() -> {
            currentQuestionCell = cellIndex;
            questionLabel.setText(question);

            for (int i = 0; i < 4; i++) {
                optionButtons[i].setText(options[i]);
            }

            questionPanel.setVisible(true);
            questionPanel.revalidate();
            questionPanel.repaint();
        });
    }
public void showTurn(String player) {
    SwingUtilities.invokeLater(() -> {
        currentTurnPlayer = player;

        turnLabel.setText("الدور الآن على: " + player);

        showToast(
                "الدور الآن على " + player + " لاختيار حرف",
                false
        );
    });
}


    public void colorCellByIndex(int cellIndex, int colorIndex) {
        if (hexBoard != null &&
                colorIndex >= 0 &&
                colorIndex < PLAYER_COLORS.length) {

            hexBoard.colorCell(cellIndex, PLAYER_COLORS[colorIndex]);
        }
    }

    public void updateScoresText(String scoreData) {
        SwingUtilities.invokeLater(() -> {
            String[] parts = scoreData.split(",");

            for (String part : parts) {
                if (part.contains("=")) {
                    String[] pair = part.split("=");

                    if (pair.length == 2) {
                        try {
                            playerScores.put(pair[0].trim(), Integer.parseInt(pair[1].trim()));
                        } catch (Exception ignored) {}
                    }
                }
            }

            refreshPlayerBadges();
            updateResultScores();
        });
    }

    public void showWinnerPage(String winner) {

    SwingUtilities.invokeLater(() -> {

        winnerName = winner;

        if (resultTitleLabel != null) {
            resultTitleLabel.setText("نتائج اللعبة");
        }

        updateResultScores();

        goTo(CARD_RESULT);
    });
}

    public void showNoWinnerPage() {
    SwingUtilities.invokeLater(() -> {
        winnerName = "";

        if (resultTitleLabel != null) {
            resultTitleLabel.setText("انتهت اللعبة بدون فائز!");
        }

        updateResultScores();
        goTo(CARD_RESULT);
    });
}

    public void resetBoard() {
        SwingUtilities.invokeLater(() -> {
            if (hexBoard != null) {
                hexBoard.resetCells();
            }

            currentQuestionCell = -1;
            questionPanel.setVisible(false);

            for (String player : currentGamePlayers) {
                playerScores.put(player, 0);
            }

            refreshPlayerBadges();
            goTo(CARD_GAME);
        });
    }

    public void showToast(String message, boolean isError) {

    SwingUtilities.invokeLater(() -> {

        JDialog dialog = new JDialog(this);

        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 0));
        dialog.setAlwaysOnTop(true);

        JPanel panel = new JPanel(new BorderLayout()) {

            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                Color bg;

if (message.contains("incorrectly")) {
    bg = new Color(235, 70, 65); // أحمر للخطأ
}
else if (message.contains("correctly")) {
    bg = new Color(46, 204, 113); // أخضر للصح
}
else if (isError) {
    bg = RED;
}
else {
    bg = PURPLE_LIGHT;
}

                g2.setColor(bg);

                g2.fillRoundRect(
                        0,
                        0,
                        getWidth(),
                        getHeight(),
                        28,
                        28
                );

                g2.dispose();
            }
        };

        panel.setOpaque(false);

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        14,
                        30,
                        14,
                        30
                )
        );

        JLabel label = new JLabel(
                message,
                SwingConstants.CENTER
        );

        label.setFont(
                arabicFont(16, Font.BOLD)
        );

        label.setForeground(Color.WHITE);

        panel.add(label, BorderLayout.CENTER);

        dialog.setContentPane(panel);

        dialog.setSize(430, 62);

        int x =
                getX()
                + (getWidth() - dialog.getWidth()) / 2;

        int y =
                getY() + 65;

        dialog.setLocation(x, y);

        dialog.setVisible(true);

        javax.swing.Timer timer =
                new javax.swing.Timer(2500, e -> {

                    dialog.dispose();

                    ((javax.swing.Timer)
                            e.getSource()).stop();
                });

        timer.setRepeats(false);

        timer.start();
    });
}

    // ==============================
    // 14. Player Badges
    // ==============================
    private void refreshPlayerBadges() {
        if (playerBadgesPanel == null) return;

        playerBadgesPanel.removeAll();

        int count = 0;

        for (String player : currentGamePlayers) {
            if (player == null || player.isBlank()) continue;

            Color color =playerColorMap.getOrDefault(player,PLAYER_COLORS[count % PLAYER_COLORS.length]);
            int score = playerScores.getOrDefault(player, 0);

            playerBadgesPanel.add(createPlayerBadge(player, color, score));
            playerBadgesPanel.add(Box.createVerticalStrut(14));

            count++;
        }

        playerBadgesPanel.revalidate();
        playerBadgesPanel.repaint();
    }

    private JPanel createPlayerBadge(String playerName, Color badgeColor, int score) {
        JPanel row = new JPanel(new BorderLayout(10, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(CARD_DARK);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);

                g2.setColor(badgeColor);
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 28, 28);

                g2.dispose();
            }
        };

        row.setOpaque(false);
        row.setMaximumSize(new Dimension(270, 66));
        row.setPreferredSize(new Dimension(270, 60));
        row.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 12));

        JLabel name = new JLabel(playerName);
        name.setFont(arabicFont(18, Font.BOLD));
        name.setForeground(badgeColor);

        JComponent hexScore = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int cx = getWidth() / 2;
                int cy = getHeight() / 2;
                int r = Math.min(getWidth(), getHeight()) / 2 - 4;

                Polygon p = new Polygon();

                for (int i = 0; i < 6; i++) {
                    double a = Math.toRadians(60 * i - 30);
                    p.addPoint(
                            (int) (cx + r * Math.cos(a)),
                            (int) (cy + r * Math.sin(a))
                    );
                }

                g2.setColor(badgeColor);
                g2.fillPolygon(p);

                g2.setColor(GOLD);
                g2.setStroke(new BasicStroke(2f));
                g2.drawPolygon(p);

                String s = String.valueOf(score);
                g2.setFont(new Font("Arial Black", Font.BOLD, 18));
                g2.setColor(Color.WHITE);

                FontMetrics fm = g2.getFontMetrics();

                g2.drawString(
                        s,
                        cx - fm.stringWidth(s) / 2,
                        cy + fm.getAscent() / 2 - fm.getDescent() / 2
                );

                g2.dispose();
            }
        };

        hexScore.setPreferredSize(new Dimension(48, 48));

        row.add(name, BorderLayout.CENTER);
        row.add(hexScore, BorderLayout.EAST);

        return row;
    }

    private void updateResultScores() {

    if (resultPlayersPanel == null) return;

    resultPlayersPanel.removeAll();

    List<String> sortedPlayers =
            new ArrayList<>(currentGamePlayers);

    sortedPlayers.sort((p1, p2) ->
            Integer.compare(
                    playerScores.getOrDefault(p2, 0),
                    playerScores.getOrDefault(p1, 0)
            )
    );

    int place = 1;

    for (String player : sortedPlayers) {

        int score =
                playerScores.getOrDefault(player, 0);

        boolean isWinner =
                player.equals(winnerName);

        resultPlayersPanel.add(
                createResultPlayerCard(
                        player,
                        score,
                        place,
                        isWinner
                )
        );

        place++;
    }

    resultPlayersPanel.revalidate();
    resultPlayersPanel.repaint();
}
private JPanel createResultPlayerCard(
        String playerName,
        int score,
        int place,
        boolean isWinner
) {
    Color color = playerColorMap.getOrDefault(playerName, GOLD);

    JPanel card = new JPanel(null);
    card.setOpaque(false);
    card.setPreferredSize(new Dimension(200, 225));
    card.setMaximumSize(new Dimension(200, 225));

    JComponent scoreHex = createScoreHex(score, color);
    scoreHex.setBounds(45, 28, 110, 110);
    card.add(scoreHex);

    if (isWinner) {
        JLabel trophy = buildTrophy(95, 95);

        // يخلي الكأس فوق المعين وواضح
        trophy.setBounds(5, -15, 110, 110);
        card.add(trophy);
        card.setComponentZOrder(trophy, 0);
    }

    JPanel nameBox = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(new Color(30, 28, 55, 235));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 26, 26);

            g2.setColor(color);
            g2.setStroke(new BasicStroke(3f));
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 26, 26);

            g2.dispose();
        }
    };

    nameBox.setOpaque(false);
    nameBox.setLayout(new BoxLayout(nameBox, BoxLayout.Y_AXIS));
    nameBox.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    nameBox.setBounds(10, 138, 180, 72);

    JLabel nameLabel = label(playerName, 19, Font.BOLD, color);
    nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel scoreLabel = label("الإجابات الصحيحة: " + score, 15, Font.BOLD, color);
    scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    nameBox.add(nameLabel);
    nameBox.add(Box.createVerticalStrut(3));
    nameBox.add(scoreLabel);

    card.add(nameBox);

    return card;
}

private JComponent createScoreHex(int score, Color color) {

    return new JComponent() {

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int w = getWidth();
            int h = getHeight();

            Polygon hex = new Polygon();

            hex.addPoint(w / 2, 0);
            hex.addPoint(w - 10, h / 4);
            hex.addPoint(w - 10, h * 3 / 4);
            hex.addPoint(w / 2, h);
            hex.addPoint(10, h * 3 / 4);
            hex.addPoint(10, h / 4);

            g2.setColor(color);
            g2.fillPolygon(hex);

            g2.setColor(GOLD);
            g2.setStroke(new BasicStroke(4f));
            g2.drawPolygon(hex);

            String text = String.valueOf(score);

            g2.setFont(new Font("Arial Black", Font.BOLD, 48));
            g2.setColor(Color.WHITE);

            FontMetrics fm = g2.getFontMetrics();

            g2.drawString(
                    text,
                    w / 2 - fm.stringWidth(text) / 2,
                    h / 2 + fm.getAscent() / 3
            );

            g2.dispose();
        }
    };
}
private JLabel buildTrophy(
        int width,
        int height
) {

    try {

        ImageIcon icon =
                new ImageIcon(
                        getClass().getResource(
                                "/trophy.png"
                        )
                );

        Image scaled =
                icon.getImage().getScaledInstance(
                        width,
                        height,
                        Image.SCALE_SMOOTH
                );

        return new JLabel(
                new ImageIcon(scaled)
        );

    } catch (Exception e) {

        return label(
                "🏆",
                45,
                Font.BOLD,
                GOLD
        );
    }
}
    // ==============================
    // 15. UI Helpers
    // ==============================
    private void goTo(String card) {
        cardLayout.show(mainPanel, card);
    }

    private JLabel buildLogo(int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/logo.PNG"));

            Image scaled = icon.getImage().getScaledInstance(
                    width,
                    height,
                    Image.SCALE_SMOOTH
            );

            return new JLabel(new ImageIcon(scaled));

        } catch (Exception e) {
            return label("حروف مع ITers", 28, Font.BOLD, GOLD);
        }
    }

    private JPanel backgroundPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                GradientPaint gp = new GradientPaint(
                        0, 0, PURPLE_LIGHT,
                        w, h, PURPLE_DARK
                );

                g2.setPaint(gp);
                g2.fillRect(0, 0, w, h);

                g2.setColor(new Color(255, 255, 255, 18));
                g2.fillOval(-120, -80, 380, 280);

                g2.setColor(new Color(255, 255, 255, 12));
                g2.fillOval(w - 260, h - 260, 420, 360);

                g2.setColor(new Color(0, 0, 0, 35));
                g2.fillRect(0, 0, w, h);
            }
        };
    }

    private JPanel glassCard(int w, int h) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(45, 30, 95, 210));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);

                g2.setColor(new Color(255, 255, 255, 35));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 35, 35);

                g2.dispose();
            }
        };

        card.setOpaque(false);

        if (w > 0 && h > 0) {
            card.setPreferredSize(new Dimension(w, h));
            card.setMaximumSize(new Dimension(w, h));
        }

        return card;
    }

    private void styleDiamondList(JList<String> list) {
        list.setOpaque(false);
        list.setFixedCellHeight(58);
        list.setSelectionBackground(new Color(0, 0, 0, 0));
        list.setCellRenderer((l, value, index, selected, focus) -> {
            Color color = PLAYER_COLORS[index % PLAYER_COLORS.length];
            return createListBadge(value, color);
        });
    }

    private JPanel createListBadge(String name, Color color) {
        JPanel row = new JPanel(new BorderLayout(10, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(CARD_DARK);
                g2.fillRoundRect(5, 4, getWidth() - 10, getHeight() - 8, 26, 26);

                g2.setColor(color);
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(6, 5, getWidth() - 12, getHeight() - 10, 26, 26);

                g2.dispose();
            }
        };

        row.setOpaque(false);
        row.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));

        JLabel label = new JLabel(name);
        label.setFont(arabicFont(16, Font.BOLD));
        label.setForeground(color);

        JLabel hex = new JLabel("⬢");
        hex.setFont(new Font("SansSerif", Font.BOLD, 28));
        hex.setForeground(color);

        row.add(label, BorderLayout.CENTER);
        row.add(hex, BorderLayout.EAST);

        return row;
    }

    private JScrollPane listScroll(JList<String> list) {
        JScrollPane sc = new JScrollPane(list);
        sc.setBorder(null);
        sc.setOpaque(false);
        sc.getViewport().setOpaque(false);
        return sc;
    }

    private JButton gradientButton(String text, int w, int h) {
        JButton b = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(
                        0, 0, GOLD,
                        getWidth(), getHeight(), new Color(255, 165, 40)
                );

                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

                g2.dispose();

                super.paintComponent(g);
            }
        };

        return styleButton(b, w, h, PURPLE_DARK);
    }

    private JButton outlineButton(String text, int w, int h) {
        JButton b = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(35, 25, 75, 200));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

                g2.setColor(PURPLE_LIGHT);
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 18, 18);

                g2.dispose();

                super.paintComponent(g);
            }
        };

        return styleButton(b, w, h, WHITE);
    }

    private JButton styleButton(JButton b, int w, int h, Color textColor) {
        b.setPreferredSize(new Dimension(w, h));
        b.setMaximumSize(new Dimension(w, h));
        b.setForeground(textColor);
        b.setFont(arabicFont(15, Font.BOLD));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JLabel label(String text, int size, int style, Color color) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(arabicFont(size, style));
        l.setForeground(color);
        return l;
    }

    private Font arabicFont(int size, int style) {
        return new Font("Tahoma", style, size);
    }
}