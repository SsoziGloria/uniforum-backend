package GeneralUser.views.Groups;

import GeneralUser.api.ApiClient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatView extends JPanel {

    // --- Styling Palette ---
    private final Color PRIMARY_BLUE = new Color(37, 99, 235);    // #2563EB
    private final Color PAGE_BG = new Color(248, 250, 252);       // #F8FAFC
    private final Color BORDER_COLOR = new Color(226, 232, 240);  // #E2E8F0
    private final Color DARK_TEXT = new Color(15, 23, 42);        // #0F172A
    private final Color MUTED_TEXT = new Color(100, 116, 139);    // #64748B
    private final Color CHAT_BG_OTHER = new Color(241, 245, 249);  // #F1F5F9

    private final String authToken;
    private final String currentUserName;
    private final int groupId;
    private final Integer topicId;
    private final Consumer<Integer> onTopicSelectedCallback;

    private JPanel messageListPanel;
    private JTextArea messageInputField;
    private JCheckBox restrictCheckBox;
    private JPanel excludeMembersPanel;
    private JPanel membersCheckboxContainer;
    private JLabel connectionStatusLbl;

    private final List<MemberModel> groupMembers = new ArrayList<>();
    private final List<MessageModel> loadedMessages = new ArrayList<>();
    private javax.swing.Timer pollingTimer;
    private boolean isOnline = true;

    public ChatView(int groupId, Integer topicId, String groupName, String subTitle, String authToken, String currentUserName, Runnable onBackClicked, Consumer<Integer> onTopicSelectedCallback) {
        this.groupId = groupId;
        this.topicId = topicId;
        this.authToken = authToken;
        this.currentUserName = (currentUserName != null && !currentUserName.trim().isEmpty()) ? currentUserName : "User";
        this.onTopicSelectedCallback = onTopicSelectedCallback;

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

        JButton backLink = new JButton("← Back to Group");
        backLink.setFont(new Font("SansSerif", Font.PLAIN, 12));
        backLink.setForeground(PRIMARY_BLUE);
        backLink.setBorderPainted(false);
        backLink.setContentAreaFilled(false);
        backLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLink.setAlignmentX(Component.LEFT_ALIGNMENT);
        backLink.addActionListener(e -> {
            stopPolling();
            if (onBackClicked != null) onBackClicked.run();
        });
        titlePanel.add(backLink);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 4)));

        JLabel headingLbl = new JLabel(groupName + " Discussion Group Chat");
        headingLbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        headingLbl.setForeground(DARK_TEXT);
        headingLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        titlePanel.add(headingLbl);

        JLabel subLbl = new JLabel(subTitle != null ? subTitle : "Communicate with members of this group.");
        subLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subLbl.setForeground(MUTED_TEXT);
        subLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        titlePanel.add(subLbl);

        topHeader.add(titlePanel, BorderLayout.WEST);

        // Connection status indicator + Topic switch
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionsPanel.setBackground(Color.WHITE);

        connectionStatusLbl = new JLabel("● Online");
        connectionStatusLbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        connectionStatusLbl.setForeground(new Color(16, 185, 129));
        actionsPanel.add(connectionStatusLbl);

        JButton topicDiscussionsBtn = new JButton("Topic Discussions");
        topicDiscussionsBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        topicDiscussionsBtn.setForeground(DARK_TEXT);
        topicDiscussionsBtn.setBackground(Color.WHITE);
        topicDiscussionsBtn.setFocusPainted(false);
        topicDiscussionsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        topicDiscussionsBtn.addActionListener(e -> {
            stopPolling();
            loadGroupTopics(selectedTopicId -> {
                if (onTopicSelectedCallback != null) {
                    onTopicSelectedCallback.accept(selectedTopicId);
                }
            });
        });

        actionsPanel.add(topicDiscussionsBtn);
        topHeader.add(actionsPanel, BorderLayout.EAST);
        add(topHeader, BorderLayout.NORTH);

        // --- CENTER MAIN CONTENT (MESSAGES + INPUT) ---
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(PAGE_BG);
        mainContent.setBorder(new EmptyBorder(16, 24, 16, 24));

        // MESSAGES STREAM
        messageListPanel = new JPanel();
        messageListPanel.setLayout(new BoxLayout(messageListPanel, BoxLayout.Y_AXIS));
        messageListPanel.setBackground(Color.WHITE);
        messageListPanel.setBorder(new EmptyBorder(16, 16, 16, 16));

        JScrollPane scrollPane = new JScrollPane(
            messageListPanel,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.setPreferredSize(new Dimension(800, 400));
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainContent.add(scrollPane);
        mainContent.add(Box.createRigidArea(new Dimension(0, 16)));

        // MESSAGE INPUT CARD
        JPanel inputCard = new JPanel();
        inputCard.setLayout(new BoxLayout(inputCard, BoxLayout.Y_AXIS));
        inputCard.setBackground(Color.WHITE);
        inputCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(16, 16, 16, 16)
        ));

        messageInputField = new JTextArea(3, 20);
        messageInputField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        messageInputField.setLineWrap(true);
        messageInputField.setWrapStyleWord(true);
        JScrollPane textScroll = new JScrollPane(messageInputField);
        textScroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));

        inputCard.add(textScroll);
        inputCard.add(Box.createRigidArea(new Dimension(0, 12)));

        // VISIBILITY CONTROLS (Restricted / Exclude Members)
        JPanel visibilityPanel = new JPanel();
        visibilityPanel.setLayout(new BoxLayout(visibilityPanel, BoxLayout.Y_AXIS));
        visibilityPanel.setBackground(Color.WHITE);
        visibilityPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel visTitle = new JLabel("Message Visibility");
        visTitle.setFont(new Font("SansSerif", Font.BOLD, 13));
        visTitle.setForeground(DARK_TEXT);
        visibilityPanel.add(visTitle);

        restrictCheckBox = new JCheckBox("Select members who should NOT see this message");
        restrictCheckBox.setFont(new Font("SansSerif", Font.PLAIN, 12));
        restrictCheckBox.setBackground(Color.WHITE);
        restrictCheckBox.setForeground(DARK_TEXT);
        restrictCheckBox.addActionListener(e -> excludeMembersPanel.setVisible(restrictCheckBox.isSelected()));
        visibilityPanel.add(restrictCheckBox);

        excludeMembersPanel = new JPanel();
        excludeMembersPanel.setLayout(new BoxLayout(excludeMembersPanel, BoxLayout.Y_AXIS));
        excludeMembersPanel.setBackground(new Color(248, 250, 252));
        excludeMembersPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(10, 12, 10, 12)
        ));
        excludeMembersPanel.setVisible(false);

        JLabel exTitle = new JLabel("Exclude Members:");
        exTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        exTitle.setForeground(DARK_TEXT);
        excludeMembersPanel.add(exTitle);
        excludeMembersPanel.add(Box.createRigidArea(new Dimension(0, 6)));

        membersCheckboxContainer = new JPanel();
        membersCheckboxContainer.setLayout(new BoxLayout(membersCheckboxContainer, BoxLayout.Y_AXIS));
        membersCheckboxContainer.setBackground(new Color(248, 250, 252));
        excludeMembersPanel.add(membersCheckboxContainer);

        visibilityPanel.add(excludeMembersPanel);
        inputCard.add(visibilityPanel);
        inputCard.add(Box.createRigidArea(new Dimension(0, 12)));

        // SEND BUTTON
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonRow.setBackground(Color.WHITE);
        JButton sendBtn = new JButton("Send Message");
        sendBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        sendBtn.setForeground(Color.WHITE);
        sendBtn.setBackground(PRIMARY_BLUE);
        sendBtn.setFocusPainted(false);
        sendBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendBtn.addActionListener(e -> sendCurrentMessage());
        buttonRow.add(sendBtn);

        inputCard.add(buttonRow);
        mainContent.add(inputCard);

        add(mainContent, BorderLayout.CENTER);

        // Load cached messages first, sync offline queue, then poll backend
        loadLocalCacheAndMembers();
        syncPendingOfflineMessages();
        startPolling();
    }

    // --- DATA MODELLING ---
    private static class MessageModel {
        int id;
        int senderId;
        String senderName;
        String text;
        String timestamp;
        boolean isMe;

        MessageModel(int id, int senderId, String senderName, String text, String timestamp, boolean isMe) {
            this.id = id;
            this.senderId = senderId;
            this.senderName = senderName;
            this.text = text;
            this.timestamp = timestamp;
            this.isMe = isMe;
        }
    }

    private static class MemberModel {
        int userId;
        String name;
        String role;
        JCheckBox checkBox;

        MemberModel(int userId, String name, String role) {
            this.userId = userId;
            this.name = name;
            this.role = role;
        }
    }

    // --- RENDER MESSAGES ---
    private void renderMessages() {
        messageListPanel.removeAll();

        if (loadedMessages.isEmpty()) {
            JPanel emptyPanel = new JPanel();
            emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
            emptyPanel.setBackground(Color.WHITE);
            emptyPanel.setBorder(new EmptyBorder(60, 0, 60, 0));

            JLabel iconLbl = new JLabel("💬");
            iconLbl.setFont(new Font("SansSerif", Font.PLAIN, 36));
            iconLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel titleLbl = new JLabel("No messages yet");
            titleLbl.setFont(new Font("SansSerif", Font.BOLD, 16));
            titleLbl.setForeground(DARK_TEXT);
            titleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel subLbl = new JLabel("Start the conversation with your group members.");
            subLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
            subLbl.setForeground(MUTED_TEXT);
            subLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

            emptyPanel.add(iconLbl);
            emptyPanel.add(Box.createRigidArea(new Dimension(0, 8)));
            emptyPanel.add(titleLbl);
            emptyPanel.add(Box.createRigidArea(new Dimension(0, 4)));
            emptyPanel.add(subLbl);

            messageListPanel.add(emptyPanel);
        } else {
            for (MessageModel msg : loadedMessages) {
                messageListPanel.add(createSingleMessageRow(msg));
                messageListPanel.add(Box.createRigidArea(new Dimension(0, 12)));
            }
        }

        messageListPanel.revalidate();
        messageListPanel.repaint();
    }

    private JPanel createSingleMessageRow(MessageModel msg) {
        JPanel rowWrapper = new JPanel(new BorderLayout());
        rowWrapper.setOpaque(false);
        rowWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (msg.isMe) {
            // OWN MESSAGE (RIGHT SIDE)
            JPanel rightContainer = new JPanel();
            rightContainer.setLayout(new BoxLayout(rightContainer, BoxLayout.Y_AXIS));
            rightContainer.setOpaque(false);

            JPanel bubble = new JPanel(new BorderLayout());
            bubble.setBackground(PRIMARY_BLUE);
            bubble.setBorder(new EmptyBorder(12, 14, 12, 14));

            JLabel textLbl = new JLabel("<html><body style='width: 320px; color: white;'>" + escapeHtml(msg.text) + "</body></html>");
            textLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
            bubble.add(textLbl, BorderLayout.CENTER);

            JPanel subRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 2));
            subRow.setOpaque(false);

            JLabel timeLbl = new JLabel(msg.timestamp);
            timeLbl.setFont(new Font("SansSerif", Font.PLAIN, 10));
            timeLbl.setForeground(new Color(226, 232, 240));

            JButton deleteBtn = new JButton("Delete");
            deleteBtn.setFont(new Font("SansSerif", Font.PLAIN, 10));
            deleteBtn.setForeground(new Color(254, 202, 202));
            deleteBtn.setBorderPainted(false);
            deleteBtn.setContentAreaFilled(false);
            deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            deleteBtn.addActionListener(e -> deleteMessage(msg));

            subRow.add(timeLbl);
            subRow.add(deleteBtn);

            rightContainer.add(bubble);
            rightContainer.add(subRow);

            rowWrapper.add(rightContainer, BorderLayout.EAST);
        } else {
            // OTHER MEMBER MESSAGE (LEFT SIDE)
            JPanel leftContainer = new JPanel(new BorderLayout(10, 0));
            leftContainer.setOpaque(false);

            // Avatar initial circle
            String initial = msg.senderName != null && !msg.senderName.isEmpty() ? msg.senderName.substring(0, 1).toUpperCase() : "U";
            JLabel avatarLbl = new JLabel(initial, SwingConstants.CENTER);
            avatarLbl.setPreferredSize(new Dimension(36, 36));
            avatarLbl.setOpaque(true);
            avatarLbl.setBackground(PRIMARY_BLUE);
            avatarLbl.setForeground(Color.WHITE);
            avatarLbl.setFont(new Font("SansSerif", Font.BOLD, 14));

            JPanel messageBody = new JPanel();
            messageBody.setLayout(new BoxLayout(messageBody, BoxLayout.Y_AXIS));
            messageBody.setOpaque(false);

            JPanel senderMeta = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
            senderMeta.setOpaque(false);

            JLabel nameLbl = new JLabel(msg.senderName);
            nameLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
            nameLbl.setForeground(DARK_TEXT);

            JLabel timeLbl = new JLabel(msg.timestamp);
            timeLbl.setFont(new Font("SansSerif", Font.PLAIN, 10));
            timeLbl.setForeground(MUTED_TEXT);

            senderMeta.add(nameLbl);
            senderMeta.add(timeLbl);

            JPanel bubble = new JPanel(new BorderLayout());
            bubble.setBackground(CHAT_BG_OTHER);
            bubble.setBorder(new EmptyBorder(12, 14, 12, 14));

            JLabel textLbl = new JLabel("<html><body style='width: 320px; color: #0F172A;'>" + escapeHtml(msg.text) + "</body></html>");
            textLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
            bubble.add(textLbl, BorderLayout.CENTER);

            messageBody.add(senderMeta);
            messageBody.add(Box.createRigidArea(new Dimension(0, 4)));
            messageBody.add(bubble);

            leftContainer.add(avatarLbl, BorderLayout.WEST);
            leftContainer.add(messageBody, BorderLayout.CENTER);

            rowWrapper.add(leftContainer, BorderLayout.WEST);
        }

        return rowWrapper;
    }

    // --- SEND MESSAGE & EXCLUDE LOGIC ---
    private void sendCurrentMessage() {
        String text = messageInputField.getText().trim();
        if (text.isEmpty()) return;

        List<Integer> excludedIds = new ArrayList<>();
        if (restrictCheckBox.isSelected()) {
            for (MemberModel m : groupMembers) {
                if (m.checkBox != null && m.checkBox.isSelected()) {
                    excludedIds.add(m.userId);
                }
            }
        }

        MessageModel newMsg = new MessageModel(
            (int) (System.currentTimeMillis() % 1000000),
            -1,
            currentUserName,
            text,
            "Just now",
            true
        );

        loadedMessages.add(newMsg);
        renderMessages();
        messageInputField.setText("");
        restrictCheckBox.setSelected(false);
        excludeMembersPanel.setVisible(false);

        saveLocalCache();

        // Dispatch via Thread
        new Thread(() -> {
            try {
                URL url = new URL("http://127.0.0.1:8000/api/groups/" + groupId + "/messages");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", "Bearer " + authToken);
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                StringBuilder json = new StringBuilder();
                json.append("{");
                json.append("\"msg_txt\":\"").append(escapeJson(text)).append("\"");
                if (topicId != null) {
                    json.append(",\"topic_id\":").append(topicId);
                }
                if (!excludedIds.isEmpty()) {
                    json.append(",\"is_restricted\":1");
                    json.append(",\"excluded_user_ids\":").append(excludedIds.toString());
                }
                json.append("}");

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(json.toString().getBytes(StandardCharsets.UTF_8));
                }

                int code = conn.getResponseCode();
                if (code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_CREATED) {
                    updateConnectionStatus(true);
                } else {
                    queuePendingOfflineMessage(text, excludedIds);
                    updateConnectionStatus(false);
                }
            } catch (Exception ex) {
                queuePendingOfflineMessage(text, excludedIds);
                updateConnectionStatus(false);
            }
        }).start();
    }

    private void deleteMessage(MessageModel msg) {
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this message?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        loadedMessages.remove(msg);
        renderMessages();
        saveLocalCache();

        new Thread(() -> {
            try {
                ApiClient.post("/groups/" + groupId + "/messages/" + msg.id + "/delete", "{}", authToken);
            } catch (Exception ignored) {}
        }).start();
    }

    // --- REALTIME POLLING & NETWORK SYNC ---
    private void startPolling() {
        pollingTimer = new javax.swing.Timer(4000, e -> fetchRemoteMessages());
        pollingTimer.start();
    }

    private void stopPolling() {
        if (pollingTimer != null) {
            pollingTimer.stop();
        }
    }

    private void fetchRemoteMessages() {
        new Thread(() -> {
            try {
                String endpoint = "/groups/" + groupId + "/messages" + (topicId != null ? "?topic_id=" + topicId : "");
                String json = ApiClient.get(endpoint, authToken);

                if (json != null && json.startsWith("{")) {
                    parseAndMergeMessages(json);
                    updateConnectionStatus(true);
                    syncPendingOfflineMessages();
                } else {
                    updateConnectionStatus(false);
                }
            } catch (Exception e) {
                updateConnectionStatus(false);
            }
        }).start();
    }

    private void parseAndMergeMessages(String json) {
        Pattern pattern = Pattern.compile("\"msg_id\":\\s*(\\d+).*?\"msg_txt\":\\s*\"([^\"]+)\".*?\"sender_name\":\\s*\"([^\"]+)\"", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(json);

        boolean updated = false;
        while (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            String text = matcher.group(2);
            String sender = matcher.group(3);

            boolean exists = false;
            for (MessageModel m : loadedMessages) {
                if (m.id == id) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                loadedMessages.add(new MessageModel(id, -1, sender, text, "Recently", sender.equalsIgnoreCase(currentUserName)));
                updated = true;
            }
        }

        if (updated) {
            saveLocalCache();
            SwingUtilities.invokeLater(this::renderMessages);
        }
    }

    // --- OFFLINE QUEUE & CACHE SYSTEM ---
    private String getCacheFilename() {
        return "cached_chat_group_" + groupId + ".json";
    }

    private String getPendingQueueFilename() {
        return "pending_messages_group_" + groupId + ".json";
    }

    private void saveLocalCache() {
        try (PrintWriter out = new PrintWriter(new FileWriter(getCacheFilename()))) {
            out.print("[");
            for (int i = 0; i < loadedMessages.size(); i++) {
                MessageModel m = loadedMessages.get(i);
                out.print("{\"id\":" + m.id + ",\"sender\":\"" + escapeJson(m.senderName) + "\",\"text\":\"" + escapeJson(m.text) + "\",\"time\":\"" + escapeJson(m.timestamp) + "\",\"isMe\":" + m.isMe + "}");
                if (i < loadedMessages.size() - 1) out.print(",");
            }
            out.print("]");
        } catch (Exception ignored) {}
    }

    private void loadLocalCacheAndMembers() {
        // 1. Members checklist
        fetchGroupMembers();

        // 2. Chat history from disk
        File cacheFile = new File(getCacheFilename());
        if (cacheFile.exists()) {
            try {
                String content = new String(Files.readAllBytes(Paths.get(getCacheFilename())));
                Pattern pattern = Pattern.compile("\\{\"id\":(\\d+),\"sender\":\"([^\"]+)\",\"text\":\"([^\"]+)\",\"time\":\"([^\"]+)\",\"isMe\":(true|false)\\}");
                Matcher matcher = pattern.matcher(content);

                loadedMessages.clear();
                while (matcher.find()) {
                    loadedMessages.add(new MessageModel(
                        Integer.parseInt(matcher.group(1)),
                        -1,
                        matcher.group(2),
                        matcher.group(3),
                        matcher.group(4),
                        Boolean.parseBoolean(matcher.group(5))
                    ));
                }
                renderMessages();
            } catch (Exception ignored) {}
        }
    }

    private void fetchGroupMembers() {
        new Thread(() -> {
            try {
                String response = ApiClient.get("/groups/" + groupId + "/members", authToken);
                if (response != null && response.contains("members")) {
                    Pattern p = Pattern.compile("\"id\":(\\d+).*?\"name\":\"([^\"]+)\".*?\"group_role\":\"([^\"]+)\"");
                    Matcher m = p.matcher(response);

                    groupMembers.clear();
                    while (m.find()) {
                        groupMembers.add(new MemberModel(Integer.parseInt(m.group(1)), m.group(2), m.group(3)));
                    }

                    SwingUtilities.invokeLater(() -> {
                        membersCheckboxContainer.removeAll();
                        for (MemberModel mem : groupMembers) {
                            if (!mem.name.equalsIgnoreCase(currentUserName)) {
                                mem.checkBox = new JCheckBox(mem.name + " (" + mem.role + ")");
                                mem.checkBox.setFont(new Font("SansSerif", Font.PLAIN, 12));
                                mem.checkBox.setBackground(new Color(248, 250, 252));
                                membersCheckboxContainer.add(mem.checkBox);
                            }
                        }
                        membersCheckboxContainer.revalidate();
                        membersCheckboxContainer.repaint();
                    });
                }
            } catch (Exception ignored) {}
        }).start();
    }

    private void queuePendingOfflineMessage(String text, List<Integer> excludedIds) {
        try (PrintWriter out = new PrintWriter(new FileWriter(getPendingQueueFilename(), true))) {
            out.println(escapeJson(text) + "||" + excludedIds.toString());
        } catch (Exception ignored) {}
    }

    private void syncPendingOfflineMessages() {
        File pendingFile = new File(getPendingQueueFilename());
        if (!pendingFile.exists()) return;

        new Thread(() -> {
            try (BufferedReader br = new BufferedReader(new FileReader(pendingFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("\\|\\|");
                    if (parts.length > 0) {
                        String text = parts[0];
                        URL url = new URL("http://127.0.0.1:8000/api/groups/" + groupId + "/messages");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Authorization", "Bearer " + authToken);
                        conn.setRequestProperty("Content-Type", "application/json; utf-8");
                        conn.setDoOutput(true);

                        String json = "{\"msg_txt\":\"" + text + "\"}";
                        try (OutputStream os = conn.getOutputStream()) {
                            os.write(json.getBytes(StandardCharsets.UTF_8));
                        }
                        conn.getResponseCode();
                    }
                }
                pendingFile.delete(); // Clear queue after sync
            } catch (Exception ignored) {}
        }).start();
    }

    private void updateConnectionStatus(boolean online) {
        this.isOnline = online;
        SwingUtilities.invokeLater(() -> {
            if (online) {
                connectionStatusLbl.setText("● Online");
                connectionStatusLbl.setForeground(new Color(16, 185, 129));
            } else {
                connectionStatusLbl.setText("● Offline (Cached)");
                connectionStatusLbl.setForeground(new Color(239, 68, 68));
            }
        });
    }

    // --- TOPIC DISCUSSIONS FALLBACK ---
    public void loadGroupTopics(Consumer<Integer> onTopicSelected) {
        messageListPanel.removeAll();
        messageListPanel.revalidate();
        messageListPanel.repaint();

        new Thread(() -> {
            try {
                String response = ApiClient.get("/groups/" + groupId + "/topics", authToken);
                SwingUtilities.invokeLater(() -> {
                    messageListPanel.removeAll();
                    if (response != null && response.contains("title")) {
                        Pattern p = Pattern.compile("\"id\":(\\d+).*?\"title\":\"([^\"]+)\".*?\"description\":\"([^\"]+)\"");
                        Matcher m = p.matcher(response);

                        while (m.find()) {
                            int tId = Integer.parseInt(m.group(1));
                            String title = m.group(2);
                            String desc = m.group(3);

                            JPanel row = new JPanel(new BorderLayout());
                            row.setBackground(Color.WHITE);
                            row.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                                new EmptyBorder(12, 14, 12, 14)
                            ));
                            row.setCursor(new Cursor(Cursor.HAND_CURSOR));

                            JLabel titleLbl = new JLabel(title);
                            titleLbl.setFont(new Font("SansSerif", Font.BOLD, 13));
                            titleLbl.setForeground(PRIMARY_BLUE);

                            JLabel descLbl = new JLabel(desc);
                            descLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
                            descLbl.setForeground(MUTED_TEXT);

                            row.add(titleLbl, BorderLayout.NORTH);
                            row.add(descLbl, BorderLayout.CENTER);

                            row.addMouseListener(new java.awt.event.MouseAdapter() {
                                @Override
                                public void mouseClicked(java.awt.event.MouseEvent e) {
                                    if (onTopicSelected != null) onTopicSelected.accept(tId);
                                }
                            });

                            messageListPanel.add(row);
                            messageListPanel.add(Box.createRigidArea(new Dimension(0, 8)));
                        }
                    }
                    messageListPanel.revalidate();
                    messageListPanel.repaint();
                });
            } catch (Exception ignored) {}
        }).start();
    }

    // --- HELPER UTILITIES ---
    private String escapeHtml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\n", "<br/>");
    }

    private String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }
}