package GeneralUser.views.Groups.Discussions;

import GeneralUser.api.ApiClient;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TopicDetailView extends JPanel {
    private final Color PRIMARY_BLUE = new Color(37, 99, 235);
    private final Color BLUE_BG = new Color(239, 246, 255);
    private final Color BLUE_TEXT = new Color(29, 78, 216);
    private final Color PAGE_BG = new Color(248, 250, 252);
    private final Color CARD_BG = Color.WHITE;
    private final Color BORDER_COLOR = new Color(226, 232, 240);
    private final Color DARK_TEXT = new Color(15, 23, 42);
    private final Color MUTED_TEXT = new Color(100, 116, 139);
    private final Color DANGER_BG = new Color(254, 242, 242);
    private final Color DANGER_TEXT = new Color(220, 38, 38);

    private final int groupId;
    private final int topicId;
    private final String authToken;
    private final JPanel contentContainer;
    private JTextArea askQuestionTextArea;

    public TopicDetailView(int groupId, int topicId, String authToken, Runnable onBackClicked) {
        this.groupId = groupId;
        this.topicId = topicId;
        this.authToken = authToken;

        setLayout(new BorderLayout());
        setBackground(PAGE_BG);

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

        loadTopicDetails(onBackClicked);
    }

    private void loadTopicDetails(Runnable onBackClicked) {
        new Thread(() -> {
            try {
                String response = ApiClient.get("/groups/" + groupId + "/topics/" + topicId, authToken);
                
                int splitIndex = response.indexOf(":");
                final String responseStr = (splitIndex != -1) ? response.substring(splitIndex + 1) : response;
                
                SwingUtilities.invokeLater(() -> {
                    contentContainer.removeAll();

                    JButton backBtn = new JButton("← Back to Discussions");
                    backBtn.setFont(new Font("SansSerif", Font.PLAIN, 13));
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

                    String title = extractJsonValue(responseStr, "title", "Discussion Topic");
                    String category = extractJsonValue(responseStr, "ml_category", "General");
                    String description = extractJsonValue(responseStr, "description", "No discussion description provided.");
                    String creatorName = extractJsonValue(responseStr, "creator_name", "Author");
                    String createdAt = extractJsonValue(responseStr, "created_at", "Recently");

                    JLabel categoryBadge = new JLabel(" " + category + " ");
                    categoryBadge.setFont(new Font("SansSerif", Font.BOLD, 11));
                    categoryBadge.setForeground(BLUE_TEXT);
                    categoryBadge.setBackground(BLUE_BG);
                    categoryBadge.setOpaque(true);
                    categoryBadge.setBorder(new EmptyBorder(4, 8, 4, 8));
                    categoryBadge.setAlignmentX(Component.LEFT_ALIGNMENT);
                    contentContainer.add(categoryBadge);
                    contentContainer.add(Box.createRigidArea(new Dimension(0, 12)));

                    JLabel titleLbl = new JLabel(title);
                    titleLbl.setFont(new Font("SansSerif", Font.BOLD, 26));
                    titleLbl.setForeground(DARK_TEXT);
                    titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
                    contentContainer.add(titleLbl);
                    contentContainer.add(Box.createRigidArea(new Dimension(0, 8)));

                    JLabel metaLbl = new JLabel("Started by " + creatorName + " • " + createdAt);
                    metaLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
                    metaLbl.setForeground(MUTED_TEXT);
                    metaLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
                    contentContainer.add(metaLbl);
                    contentContainer.add(Box.createRigidArea(new Dimension(0, 16)));

                    JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
                    actionRow.setBackground(PAGE_BG);
                    actionRow.setAlignmentX(Component.LEFT_ALIGNMENT);

                    JButton shareBtn = new JButton("Share");
                    styleButton(shareBtn, new Color(241, 245, 249), DARK_TEXT);
                    shareBtn.addActionListener(e -> showShareMenu(shareBtn));

                    JButton exportPdfBtn = new JButton("Export PDF");
                    styleButton(exportPdfBtn, BLUE_BG, PRIMARY_BLUE);
                    exportPdfBtn.addActionListener(e -> exportPdf());

                    JButton deleteTopicBtn = new JButton("Delete Discussion");
                    styleButton(deleteTopicBtn, DANGER_BG, DANGER_TEXT);
                    deleteTopicBtn.addActionListener(e -> deleteTopic());

                    actionRow.add(shareBtn);
                    actionRow.add(exportPdfBtn);
                    actionRow.add(deleteTopicBtn);
                    contentContainer.add(actionRow);
                    contentContainer.add(Box.createRigidArea(new Dimension(0, 24)));

                    JPanel descCard = new JPanel();
                    descCard.setLayout(new BoxLayout(descCard, BoxLayout.Y_AXIS));
                    descCard.setBackground(CARD_BG);
                    descCard.setBorder(new CompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                        new EmptyBorder(24, 24, 24, 24)
                    ));
                    descCard.setAlignmentX(Component.LEFT_ALIGNMENT);

                    JLabel descTitle = new JLabel("Discussion Description");
                    descTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
                    descTitle.setForeground(DARK_TEXT);
                    descTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
                    descCard.add(descTitle);
                    descCard.add(Box.createRigidArea(new Dimension(0, 12)));

                    JTextArea descText = new JTextArea(description);
                    descText.setFont(new Font("SansSerif", Font.PLAIN, 13));
                    descText.setForeground(new Color(51, 65, 85));
                    descText.setLineWrap(true);
                    descText.setWrapStyleWord(true);
                    descText.setEditable(false);
                    descText.setBackground(CARD_BG);
                    descText.setAlignmentX(Component.LEFT_ALIGNMENT);
                    descCard.add(descText);

                    contentContainer.add(descCard);
                    contentContainer.add(Box.createRigidArea(new Dimension(0, 24)));

                    JPanel qaCard = new JPanel();
                    qaCard.setLayout(new BoxLayout(qaCard, BoxLayout.Y_AXIS));
                    qaCard.setBackground(CARD_BG);
                    qaCard.setBorder(new CompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                        new EmptyBorder(24, 24, 24, 24)
                    ));
                    qaCard.setAlignmentX(Component.LEFT_ALIGNMENT);

                    JLabel qaTitle = new JLabel("Questions & Answers");
                    qaTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
                    qaTitle.setForeground(DARK_TEXT);
                    qaTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
                    qaCard.add(qaTitle);
                    qaCard.add(Box.createRigidArea(new Dimension(0, 20)));

                    qaCard.add(createQuestionItem(
                        101,
                        "Sarah Jenkins",
                        "2 hours ago",
                        "Does anyone have the recommended reading list or study materials for the ML classification section?",
                        5
                    ));

                    contentContainer.add(qaCard);
                    contentContainer.add(Box.createRigidArea(new Dimension(0, 24)));

                    JPanel askCard = new JPanel();
                    askCard.setLayout(new BoxLayout(askCard, BoxLayout.Y_AXIS));
                    askCard.setBackground(CARD_BG);
                    askCard.setBorder(new CompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                        new EmptyBorder(24, 24, 24, 24)
                    ));
                    askCard.setAlignmentX(Component.LEFT_ALIGNMENT);

                    JLabel askTitle = new JLabel("Ask a Question or Prompt Students");
                    askTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
                    askTitle.setForeground(DARK_TEXT);
                    askTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
                    askCard.add(askTitle);
                    askCard.add(Box.createRigidArea(new Dimension(0, 12)));

                    askQuestionTextArea = new JTextArea(5, 20);
                    askQuestionTextArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
                    askQuestionTextArea.setLineWrap(true);
                    askQuestionTextArea.setWrapStyleWord(true);
                    JScrollPane askScroll = new JScrollPane(askQuestionTextArea);
                    askScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
                    askCard.add(askScroll);
                    askCard.add(Box.createRigidArea(new Dimension(0, 16)));

                    JButton postQuestionBtn = new JButton("Post Question");
                    postQuestionBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
                    postQuestionBtn.setBackground(PRIMARY_BLUE);
                    postQuestionBtn.setForeground(Color.WHITE);
                    postQuestionBtn.setFocusPainted(false);
                    postQuestionBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    postQuestionBtn.addActionListener(e -> submitQuestion());
                    
                    JPanel btnWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                    btnWrapper.setBackground(CARD_BG);
                    btnWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
                    btnWrapper.add(postQuestionBtn);
                    askCard.add(btnWrapper);

                    contentContainer.add(askCard);

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

    private JPanel createQuestionItem(int msgId, String senderName, String timeAgo, String messageText, int votesCount) {
        JPanel questionBox = new JPanel();
        questionBox.setLayout(new BoxLayout(questionBox, BoxLayout.Y_AXIS));
        questionBox.setBackground(CARD_BG);
        questionBox.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));
        questionBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel headPanel = new JPanel(new BorderLayout());
        headPanel.setBackground(CARD_BG);

        JLabel tag = new JLabel("QUESTION");
        tag.setFont(new Font("SansSerif", Font.BOLD, 10));
        tag.setForeground(PRIMARY_BLUE);

        JLabel nameLbl = new JLabel(senderName);
        nameLbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        nameLbl.setForeground(DARK_TEXT);

        JPanel leftMeta = new JPanel();
        leftMeta.setLayout(new BoxLayout(leftMeta, BoxLayout.Y_AXIS));
        leftMeta.setBackground(CARD_BG);
        leftMeta.add(tag);
        leftMeta.add(Box.createRigidArea(new Dimension(0, 2)));
        leftMeta.add(nameLbl);

        JLabel timeLbl = new JLabel(timeAgo);
        timeLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        timeLbl.setForeground(MUTED_TEXT);

        headPanel.add(leftMeta, BorderLayout.WEST);
        headPanel.add(timeLbl, BorderLayout.EAST);
        questionBox.add(headPanel);
        questionBox.add(Box.createRigidArea(new Dimension(0, 12)));

        JTextArea msgBody = new JTextArea(messageText);
        msgBody.setFont(new Font("SansSerif", Font.PLAIN, 13));
        msgBody.setForeground(DARK_TEXT);
        msgBody.setLineWrap(true);
        msgBody.setWrapStyleWord(true);
        msgBody.setEditable(false);
        msgBody.setBackground(CARD_BG);
        msgBody.setAlignmentX(Component.LEFT_ALIGNMENT);
        questionBox.add(msgBody);
        questionBox.add(Box.createRigidArea(new Dimension(0, 12)));

        JPanel questionActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        questionActions.setBackground(CARD_BG);
        questionActions.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton upvoteBtn = new JButton("▲ " + votesCount);
        styleButton(upvoteBtn, BLUE_BG, PRIMARY_BLUE);
        upvoteBtn.addActionListener(e -> upvoteQuestion(msgId));

        JButton deleteMsgBtn = new JButton("Delete Question");
        styleButton(deleteMsgBtn, DANGER_BG, DANGER_TEXT);
        deleteMsgBtn.addActionListener(e -> deleteQuestion(msgId));

        questionActions.add(upvoteBtn);
        questionActions.add(deleteMsgBtn);
        questionBox.add(questionActions);
        questionBox.add(Box.createRigidArea(new Dimension(0, 16)));

        JTextArea replyInput = new JTextArea(3, 20);
        replyInput.setFont(new Font("SansSerif", Font.PLAIN, 12));
        replyInput.setLineWrap(true);
        replyInput.setWrapStyleWord(true);
        JScrollPane replyScroll = new JScrollPane(replyInput);
        replyScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        questionBox.add(replyScroll);
        questionBox.add(Box.createRigidArea(new Dimension(0, 8)));

        JButton postAnswerBtn = new JButton("Post Response");
        postAnswerBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        postAnswerBtn.setBackground(PRIMARY_BLUE);
        postAnswerBtn.setForeground(Color.WHITE);
        postAnswerBtn.setFocusPainted(false);
        postAnswerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        postAnswerBtn.addActionListener(e -> submitReplyToQuestion(msgId, replyInput.getText().trim()));

        JPanel replyBtnWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        replyBtnWrapper.setBackground(CARD_BG);
        replyBtnWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        replyBtnWrapper.add(postAnswerBtn);
        questionBox.add(replyBtnWrapper);

        return questionBox;
    }

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(6, 12, 6, 12));
    }

    private void showShareMenu(Component invoker) {
        JPopupMenu shareMenu = new JPopupMenu();
        JMenuItem copyLink = new JMenuItem("Copy Link");
        copyLink.addActionListener(e -> JOptionPane.showMessageDialog(this, "Link copied to clipboard!"));
        shareMenu.add(copyLink);
        shareMenu.show(invoker, 0, invoker.getHeight());
    }

    private void exportPdf() {
        JOptionPane.showMessageDialog(this, "Exporting discussion to PDF...", "Export", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteTopic() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Delete this entire discussion topic along with all questions and answers?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            new Thread(() -> {
                String endpoint = "/groups/" + groupId + "/topics/" + topicId;
                ApiClient.post(endpoint, "{\"_method\":\"DELETE\"}", authToken);
                
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Discussion topic deleted.");
                });
            }).start();
        }
    }

    private void upvoteQuestion(int msgId) {
        new Thread(() -> {
            String endpoint = "/groups/" + groupId + "/messages/" + msgId + "/upvote";
            ApiClient.post(endpoint, "{}", authToken);
            SwingUtilities.invokeLater(() -> loadTopicDetails(null));
        }).start();
    }

    private void deleteQuestion(int msgId) {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Delete this question?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            new Thread(() -> {
                String endpoint = "/groups/" + groupId + "/topics/" + topicId + "/messages/" + msgId;
                ApiClient.post(endpoint, "{\"_method\":\"DELETE\"}", authToken);
                SwingUtilities.invokeLater(() -> loadTopicDetails(null));
            }).start();
        }
    }

    private void submitReplyToQuestion(int questionMsgId, String replyText) {
        if (replyText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Answer cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        new Thread(() -> {
            String safeText = replyText.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
            String jsonPayload = String.format("{\"msg_txt\":\"%s\"}", safeText);

            String endpoint = "/groups/" + groupId + "/topics/" + topicId + "/messages/" + questionMsgId + "/reply";
            ApiClient.post(endpoint, jsonPayload, authToken);

            SwingUtilities.invokeLater(() -> loadTopicDetails(null));
        }).start();
    }

    private void submitQuestion() {
        String questionText = askQuestionTextArea.getText().trim();
        if (questionText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Question cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        new Thread(() -> {
            String safeText = questionText.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
            String jsonPayload = String.format("{\"msg_txt\":\"%s\"}", safeText);

            String endpoint = "/groups/" + groupId + "/topics/" + topicId + "/messages";
            ApiClient.post(endpoint, jsonPayload, authToken);

            SwingUtilities.invokeLater(() -> {
                askQuestionTextArea.setText("");
                loadTopicDetails(null);
            });
        }).start();
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