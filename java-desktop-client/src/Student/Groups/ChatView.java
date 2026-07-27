package Student.Groups;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ChatView extends JPanel {

    private final Color PRIMARY_BLUE = new Color(37, 99, 235);    // #2563EB
    private final Color PAGE_BG = new Color(243, 244, 246);        // #F3F4F6
    private final Color BORDER_COLOR = new Color(229, 231, 235);   // #E5E7EB
    private final Color DARK_TEXT = new Color(31, 41, 55);         // #1F2937
    private final Color MUTED_TEXT = new Color(107, 114, 128);     // #6B7280

    private String authToken;
    private String currentUserName;
    private int groupId;
    private Integer topicId;
    private JPanel messageListPanel;
    private JTextField messageInputField;

    public ChatView(int groupId, Integer topicId, String groupName, String subTitle, String authToken, String currentUserName, Runnable onBackClicked, Runnable onSwitchToTopics) {
        this.groupId = groupId;
        this.topicId = topicId;
        this.authToken = authToken;
        this.currentUserName = (currentUserName != null && !currentUserName.trim().isEmpty()) ? currentUserName : "User";

        setLayout(new BorderLayout());
        setBackground(PAGE_BG);

        // --- TOP HEADER BAR ---
        JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setBackground(Color.WHITE);
        topHeader.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
            new EmptyBorder(16, 24, 16, 24)
        ));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);

        JLabel headingLbl = new JLabel(groupName);
        headingLbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        headingLbl.setForeground(DARK_TEXT);
        titlePanel.add(headingLbl);

        JLabel subLbl = new JLabel(subTitle);
        subLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subLbl.setForeground(MUTED_TEXT);
        titlePanel.add(subLbl);

        topHeader.add(titlePanel, BorderLayout.WEST);

        // Action Buttons (General Chat, Topic Discussions, Back)
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionsPanel.setBackground(Color.WHITE);

        JButton generalChatBtn = new JButton("General Chat");
        generalChatBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        generalChatBtn.setForeground(Color.WHITE);
        generalChatBtn.setBackground(PRIMARY_BLUE);
        generalChatBtn.setFocusPainted(false);
        generalChatBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JButton topicDiscussionsBtn = new JButton("Topic Discussions");
        topicDiscussionsBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        topicDiscussionsBtn.setForeground(DARK_TEXT);
        topicDiscussionsBtn.setBackground(Color.WHITE);
        topicDiscussionsBtn.setFocusPainted(false);
        topicDiscussionsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        topicDiscussionsBtn.addActionListener(e -> {
            if (onSwitchToTopics != null) {
                onSwitchToTopics.run();
            }
        });

        JButton backBtn = new JButton("← Back");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        backBtn.setForeground(DARK_TEXT);
        backBtn.setBackground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            if (onBackClicked != null) onBackClicked.run();
        });

        actionsPanel.add(generalChatBtn);
        actionsPanel.add(topicDiscussionsBtn);
        actionsPanel.add(backBtn);

        topHeader.add(actionsPanel, BorderLayout.EAST);
        add(topHeader, BorderLayout.NORTH);

        // --- MESSAGE STREAM CONTAINER ---
        messageListPanel = new JPanel();
        messageListPanel.setLayout(new BoxLayout(messageListPanel, BoxLayout.Y_AXIS));
        messageListPanel.setBackground(PAGE_BG);
        messageListPanel.setBorder(new EmptyBorder(16, 24, 16, 24));

        JScrollPane scrollPane = new JScrollPane(messageListPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // --- BOTTOM TYPING & SEND PANEL ---
        JPanel bottomBar = new JPanel(new BorderLayout(12, 0));
        bottomBar.setBackground(Color.WHITE);
        bottomBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR),
            new EmptyBorder(16, 24, 16, 24)
        ));

        messageInputField = new JTextField();
        messageInputField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageInputField.addActionListener(e -> sendCurrentMessage());
        bottomBar.add(messageInputField, BorderLayout.CENTER);

        JButton sendBtn = new JButton("Send");
        sendBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        sendBtn.setForeground(Color.WHITE);
        sendBtn.setBackground(PRIMARY_BLUE);
        sendBtn.setFocusPainted(false);
        sendBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendBtn.addActionListener(e -> sendCurrentMessage());
        bottomBar.add(sendBtn, BorderLayout.EAST);

        add(bottomBar, BorderLayout.SOUTH);
    }

    public void addMessageRow(String senderName, String messageText, String timestamp) {
        // Dynamically checks if the sender's name matches the logged-in user's name
        boolean isMe = senderName.equalsIgnoreCase(currentUserName);

        JPanel rowWrapper = new JPanel(new BorderLayout());
        rowWrapper.setOpaque(false);
        rowWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        rowWrapper.setBorder(new EmptyBorder(4, 0, 4, 0));

        JPanel bubble = new JPanel();
        bubble.setLayout(new BoxLayout(bubble, BoxLayout.Y_AXIS));
        bubble.setOpaque(true);
        
        if (isMe) {
            bubble.setBackground(PRIMARY_BLUE);
            bubble.setBorder(new EmptyBorder(10, 14, 10, 14));
            rowWrapper.add(bubble, BorderLayout.EAST);
        } else {
            bubble.setBackground(Color.WHITE);
            bubble.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(10, 14, 10, 14)
            ));
            rowWrapper.add(bubble, BorderLayout.WEST);
        }

        JLabel senderLbl = new JLabel(senderName);
        senderLbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        senderLbl.setForeground(isMe ? new Color(219, 234, 254) : MUTED_TEXT);
        bubble.add(senderLbl);

        bubble.add(Box.createRigidArea(new Dimension(0, 4)));

        JLabel textLbl = new JLabel(messageText);
        textLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        textLbl.setForeground(isMe ? Color.WHITE : DARK_TEXT);
        bubble.add(textLbl);

        bubble.add(Box.createRigidArea(new Dimension(0, 4)));

        JLabel timeLbl = new JLabel(timestamp);
        timeLbl.setFont(new Font("SansSerif", Font.ITALIC, 9));
        timeLbl.setForeground(isMe ? new Color(191, 219, 254) : MUTED_TEXT);
        bubble.add(timeLbl);

        messageListPanel.add(rowWrapper);
        messageListPanel.revalidate();
        messageListPanel.repaint();
    }

    private void sendCurrentMessage() {
        String text = messageInputField.getText().trim();
        if (text.isEmpty()) return;

        // Renders your message locally using the logged-in user's name
        addMessageRow(currentUserName, text, "Just now");
        messageInputField.setText("");

        // Send payload via background thread to your Laravel API endpoint
        new Thread(() -> {
            try {
                URL url = new URL("http://127.0.0.1:8000/api/groups/" + groupId + "/messages");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", "Bearer " + authToken);
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                String jsonInputString = "{\"msg_txt\": \"" + text + "\"}";
                if (topicId != null) {
                    jsonInputString = "{\"msg_txt\": \"" + text + "\", \"topic_id\": " + topicId + "}";
                }

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int responseCode = conn.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_CREATED) {
                    System.err.println("Failed to post message to API. Response code: " + responseCode);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }
}