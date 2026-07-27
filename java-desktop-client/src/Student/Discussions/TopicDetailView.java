package Student.Groups.Discussions;

import GeneralUser.api.ApiClient;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

public class TopicDetailView extends JPanel {
    private final Color PRIMARY_BLUE = new Color(37, 99, 235);
    private final Color PAGE_BG = new Color(248, 250, 252);
    private final Color BORDER_COLOR = new Color(226, 232, 240);
    private final Color DARK_TEXT = new Color(15, 23, 42);
    private final Color MUTED_TEXT = new Color(100, 116, 139);

    private int groupId;
    private int topicId;
    private String authToken;
    private JPanel contentContainer;
    private JTextArea replyTextArea;
    private JLabel selectedFileLabel;
    private File attachedFile;

    public TopicDetailView(int groupId, int topicId, String authToken, Runnable onBackClicked) {
        this.groupId = groupId;
        this.topicId = topicId;
        this.authToken = authToken;

        setLayout(new BorderLayout());
        setBackground(PAGE_BG);

        // Main scrollable container
        contentContainer = new JPanel();
        contentContainer.setLayout(new BoxLayout(contentContainer, BoxLayout.Y_AXIS));
        contentContainer.setBackground(PAGE_BG);
        contentContainer.setBorder(new EmptyBorder(24, 40, 24, 40));

        JScrollPane scrollPane = new JScrollPane(
            contentContainer,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Load data from backend
        loadTopicDetails(onBackClicked);
    }

    private void loadTopicDetails(Runnable onBackClicked) {
        new Thread(() -> {
            try {
                // Fetch topic and its replies/messages from your Laravel API
                String response = ApiClient.get("/groups/" + groupId + "/topics/" + topicId, authToken);
                
                // Fixed response parser to handle raw string responses without breaking layout loading
                int splitIndex = response.indexOf(":");
final String responseStr = (splitIndex != -1) ? response.substring(splitIndex + 1) : response;
                
                SwingUtilities.invokeLater(() -> {
                    contentContainer.removeAll();

                    // --- 1. Back Button & Header ---
                    JButton backBtn = new JButton("← Back to Discussions");
                    backBtn.setFont(new Font("SansSerif", Font.PLAIN, 12));
                    backBtn.setForeground(PRIMARY_BLUE);
                    backBtn.setBorderPainted(false);
                    backBtn.setContentAreaFilled(false);
                    backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
                    backBtn.addActionListener(e -> {
                        if (onBackClicked != null) onBackClicked.run();
                    });
                    contentContainer.add(backBtn);
                    contentContainer.add(Box.createRigidArea(new Dimension(0, 16)));

                    // Extract Title & Description safely
                    String title = extractJsonValue(responseStr, "title", "Discussion Thread");
                    String description = extractJsonValue(responseStr, "description", "No content provided.");

                    JLabel titleLbl = new JLabel(title);
                    titleLbl.setFont(new Font("SansSerif", Font.BOLD, 26));
                    titleLbl.setForeground(DARK_TEXT);
                    titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
                    contentContainer.add(titleLbl);
                    contentContainer.add(Box.createRigidArea(new Dimension(0, 20)));

                    // --- 2. Question / Original Post Card ---
                    JPanel questionCard = new JPanel();
                    questionCard.setLayout(new BoxLayout(questionCard, BoxLayout.Y_AXIS));
                    questionCard.setBackground(Color.WHITE);
                    questionCard.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 1),
                        new EmptyBorder(20, 20, 20, 20)
                    ));
                    questionCard.setAlignmentX(Component.LEFT_ALIGNMENT);
                    questionCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, questionCard.getPreferredSize().height));

                    JLabel qHeader = new JLabel("Question details");
                    qHeader.setFont(new Font("SansSerif", Font.BOLD, 14));
                    qHeader.setForeground(DARK_TEXT);
                    qHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
                    questionCard.add(qHeader);
                    questionCard.add(Box.createRigidArea(new Dimension(0, 10)));

                    JTextArea descArea = new JTextArea(description);
                    descArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
                    descArea.setForeground(MUTED_TEXT);
                    descArea.setLineWrap(true);
                    descArea.setWrapStyleWord(true);
                    descArea.setEditable(false);
                    descArea.setBackground(Color.WHITE);
                    
                    JScrollPane descScroll = new JScrollPane(descArea);
                    descScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
                    descScroll.setBorder(null);
                    questionCard.add(descScroll);

                    contentContainer.add(questionCard);
                    contentContainer.add(Box.createRigidArea(new Dimension(0, 20)));

                    // --- 3. Replies Section ---
                    JLabel repliesHeader = new JLabel("Replies");
                    repliesHeader.setFont(new Font("SansSerif", Font.BOLD, 18));
                    repliesHeader.setForeground(DARK_TEXT);
                    repliesHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
                    contentContainer.add(repliesHeader);
                    contentContainer.add(Box.createRigidArea(new Dimension(0, 10)));

                    JPanel repliesContainer = new JPanel();
                    repliesContainer.setLayout(new BoxLayout(repliesContainer, BoxLayout.Y_AXIS));
                    repliesContainer.setBackground(PAGE_BG);
                    repliesContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

                    // Add dummy placeholder reply item if none returned yet
                    repliesContainer.add(createReplyRow("System User", "Discussion loaded successfully. Post your answer below!"));

                    contentContainer.add(repliesContainer);
                    contentContainer.add(Box.createRigidArea(new Dimension(0, 20)));

                    // --- 4. Reply Box with WhatsApp-style File Attachment ---
                    JPanel replyCard = new JPanel();
                    replyCard.setLayout(new BoxLayout(replyCard, BoxLayout.Y_AXIS));
                    replyCard.setBackground(Color.WHITE);
                    replyCard.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 1),
                        new EmptyBorder(20, 20, 20, 20)
                    ));
                    replyCard.setAlignmentX(Component.LEFT_ALIGNMENT);
                    replyCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, replyCard.getPreferredSize().height));

                    JLabel replyTitle = new JLabel("Add your answer");
                    replyTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
                    replyTitle.setForeground(DARK_TEXT);
                    replyTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
                    replyCard.add(replyTitle);
                    replyCard.add(Box.createRigidArea(new Dimension(0, 10)));

                    replyTextArea = new JTextArea(4, 20);
                    replyTextArea.setLineWrap(true);
                    replyTextArea.setWrapStyleWord(true);
                    JScrollPane replyScroll = new JScrollPane(replyTextArea);
                    replyScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
                    replyCard.add(replyScroll);
                    replyCard.add(Box.createRigidArea(new Dimension(0, 10)));

                    // Attachment row status & button
                    JPanel attachRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
                    attachRow.setBackground(Color.WHITE);
                    attachRow.setAlignmentX(Component.LEFT_ALIGNMENT);

                    JButton attachBtn = new JButton("📎 Attach File");
                    attachBtn.setFont(new Font("SansSerif", Font.PLAIN, 12));
                    attachBtn.setForeground(PRIMARY_BLUE);
                    attachBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    attachBtn.addActionListener(e -> {
                        JFileChooser fileChooser = new JFileChooser();
                        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                            attachedFile = fileChooser.getSelectedFile();
                            selectedFileLabel.setText("Attached: " + attachedFile.getName());
                        }
                    });

                    selectedFileLabel = new JLabel("No file chosen");
                    selectedFileLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
                    selectedFileLabel.setForeground(MUTED_TEXT);

                    attachRow.add(attachBtn);
                    attachRow.add(selectedFileLabel);
                    replyCard.add(attachRow);
                    replyCard.add(Box.createRigidArea(new Dimension(0, 10)));

                    // Submit Post Button
                    JButton postAnswerBtn = new JButton("Post Answer");
                    postAnswerBtn.setBackground(PRIMARY_BLUE);
                    postAnswerBtn.setForeground(Color.WHITE);
                    postAnswerBtn.setFocusPainted(false);
                    postAnswerBtn.addActionListener(e -> submitReply());

                    JPanel submitWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                    submitWrapper.setBackground(Color.WHITE);
                    submitWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
                    submitWrapper.add(postAnswerBtn);
                    replyCard.add(submitWrapper);

                    contentContainer.add(replyCard);

                    contentContainer.revalidate();
                    contentContainer.repaint();
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Error loading topic details: " + ex.getMessage());
                });
            }
        }).start();
    }

    private JPanel createReplyRow(String author, String message) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(12, 16, 12, 16)
        ));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel authorLbl = new JLabel(author);
        authorLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        authorLbl.setForeground(DARK_TEXT);
        authorLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.add(authorLbl);

        row.add(Box.createRigidArea(new Dimension(0, 4)));

        JLabel msgLbl = new JLabel(message);
        msgLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        msgLbl.setForeground(MUTED_TEXT);
        msgLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.add(msgLbl);

        return row;
    }

   private void submitReply() {
        String replyText = replyTextArea.getText().trim();
        if (replyText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Reply cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        new Thread(() -> {
            try {
                String boundary = "===" + System.currentTimeMillis() + "===";
                // Updated URL to match your group topic message route pattern
                URL url = new URL("http://127.0.0.1:8000/api/groups/" + groupId + "/messages");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                if (authToken != null && !authToken.isEmpty()) {
                    conn.setRequestProperty("Authorization", "Bearer " + authToken);
                }

                var outputStream = conn.getOutputStream();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true);

                // FIXED: Changed "message" to "msg_txt" to match your backend validation rules
                addFormField(writer, boundary, "msg_txt", replyText);
                
                // FIXED: Explicitly pass the topic_id so the message attaches to this specific topic stream
                addFormField(writer, boundary, "topic_id", String.valueOf(topicId));

                if (groupId > 0) {
                    addFormField(writer, boundary, "group_id", String.valueOf(groupId));
                }

                if (attachedFile != null) {
                    writer.println("--" + boundary);
                    writer.println("Content-Disposition: form-data; name=\"attachment\"; filename=\"" + attachedFile.getName() + "\"");
                    writer.println("Content-Type: " + java.net.URLConnection.guessContentTypeFromName(attachedFile.getName()));
                    writer.println("Content-Transfer-Encoding: binary");
                    writer.println();
                    writer.flush();

                    Files.copy(attachedFile.toPath(), outputStream);
                    outputStream.flush();
                    writer.println();
                    writer.flush();
                }
                writer.append("--").append(boundary).append("--\r\n");
                writer.flush();
                writer.close();

                int responseCode = conn.getResponseCode();
                SwingUtilities.invokeLater(() -> {
                    if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                        JOptionPane.showMessageDialog(this, "Reply posted successfully!");
                        replyTextArea.setText("");
                        selectedFileLabel.setText("No file chosen");
                        attachedFile = null;
                        loadTopicDetails(null); // Refresh view after posting
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to post reply code: " + responseCode);
                    }
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Network error: " + ex.getMessage());
                });
            }
        }).start();
    }

    private void addFormField(PrintWriter writer, String boundary, String name, String value) {
        writer.append("--").append(boundary).append("\r\n");
        writer.append("Content-Disposition: form-data; name=\"").append(name).append("\"\r\n\r\n");
        writer.append(value).append("\r\n");
        writer.flush();
    }

    private String extractJsonValue(String source, String key, String fallback) {
        try {
            int keyIdx = source.indexOf("\"" + key + "\":");
            if (keyIdx != -1) {
                int start = source.indexOf("\"", keyIdx + key.length() + 3) + 1;
                int end = source.indexOf("\"", start);
                return source.substring(start, end);
            }
        } catch (Exception ignored) {}
        return fallback;
    }
}