package GeneralUser.views.Groups.Discussions;

import GeneralUser.api.ApiClient;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.HttpURLConnection;

public class NewDiscussionView extends JFrame {
    private final Color PRIMARY_BLUE = new Color(37, 99, 235);
    private final Color PAGE_BG = new Color(248, 250, 252);
    private final Color CARD_BG = Color.WHITE;
    private final Color BORDER_COLOR = new Color(226, 232, 240);
    private final Color DARK_TEXT = new Color(15, 23, 42);
    private final Color MUTED_TEXT = new Color(100, 116, 139);
    private final Color HINT_TEXT = new Color(148, 163, 184);
    private final Color AI_BOX_BG = new Color(239, 246, 255);
    private final Color AI_BOX_TEXT = new Color(29, 78, 216);

    private JTextField titleField;
    private JComboBox<String> categoryComboBox;
    private JTextArea descriptionArea;

    private final int groupId;
    private final String authToken;

    public NewDiscussionView(int groupId, String authToken) {
        this.groupId = groupId;
        this.authToken = authToken;

        setTitle("Create Discussion - UniForum");
        setSize(700, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(PAGE_BG);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JButton backBtn = new JButton("← Back to Discussions");
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        backBtn.setForeground(PRIMARY_BLUE);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> dispose());
        mainPanel.add(backBtn);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel headerLabel = new JLabel("Start a New Discussion");
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        headerLabel.setForeground(DARK_TEXT);
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(headerLabel);

        JLabel subHeader = new JLabel("Ask questions, share ideas, and start conversations with your university community.");
        subHeader.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subHeader.setForeground(MUTED_TEXT);
        subHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(subHeader);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(CARD_BG);
        cardPanel.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(32, 32, 32, 32)
        ));
        cardPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        cardPanel.add(createFieldLabel("Discussion Title"));
        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        titleField = new JTextField();
        styleTextField(titleField);
        cardPanel.add(titleField);

        JLabel titleHint = new JLabel("Use a clear title that describes your topic.");
        titleHint.setFont(new Font("SansSerif", Font.PLAIN, 11));
        titleHint.setForeground(HINT_TEXT);
        titleHint.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        cardPanel.add(titleHint);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        cardPanel.add(createFieldLabel("Category"));
        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        String[] categories = {
            "Select category", 
            "Software Engineering", 
            "Artificial Intelligence", 
            "Database Systems", 
            "Web Development"
        };
        categoryComboBox = new JComboBox<>(categories);
        categoryComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        categoryComboBox.setFont(new Font("SansSerif", Font.PLAIN, 13));
        categoryComboBox.setBackground(CARD_BG);
        categoryComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardPanel.add(categoryComboBox);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        cardPanel.add(createFieldLabel("Description"));
        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        descriptionArea = new JTextArea(7, 20);
        descriptionArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        descScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardPanel.add(descScroll);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel aiNoticePanel = new JPanel();
        aiNoticePanel.setLayout(new BorderLayout(10, 0));
        aiNoticePanel.setBackground(AI_BOX_BG);
        aiNoticePanel.setBorder(new EmptyBorder(16, 16, 16, 16));
        aiNoticePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        aiNoticePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JLabel aiIcon = new JLabel("⚡");
        aiIcon.setFont(new Font("SansSerif", Font.PLAIN, 18));

        JLabel aiText = new JLabel("<html><b>UniForum AI</b> will automatically classify your discussion and recommend it to students with similar interests based on their previous engagement.</html>");
        aiText.setFont(new Font("SansSerif", Font.PLAIN, 12));
        aiText.setForeground(AI_BOX_TEXT);

        aiNoticePanel.add(aiIcon, BorderLayout.WEST);
        aiNoticePanel.add(aiText, BorderLayout.CENTER);

        cardPanel.add(aiNoticePanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 24)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttonPanel.setBackground(CARD_BG);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        cancelBtn.setBackground(new Color(241, 245, 249));
        cancelBtn.setForeground(new Color(51, 65, 85));
        cancelBtn.setFocusPainted(false);
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.setBorder(new EmptyBorder(10, 20, 10, 20));
        cancelBtn.addActionListener(e -> dispose());

        JButton postBtn = new JButton("Post Discussion");
        postBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        postBtn.setBackground(PRIMARY_BLUE);
        postBtn.setForeground(Color.WHITE);
        postBtn.setFocusPainted(false);
        postBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        postBtn.setBorder(new EmptyBorder(10, 20, 10, 20));
        postBtn.addActionListener(e -> submitDiscussion());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(postBtn);
        cardPanel.add(buttonPanel);

        mainPanel.add(cardPanel);

        JScrollPane containerScroll = new JScrollPane(mainPanel);
        containerScroll.setBorder(null);
        containerScroll.getVerticalScrollBar().setUnitIncrement(16);
        add(containerScroll);
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(new Color(51, 65, 85));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void styleTextField(JTextField field) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setPreferredSize(new Dimension(0, 40));
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void submitDiscussion() {
        String title = titleField.getText().trim();
        String category = (String) categoryComboBox.getSelectedItem();
        String description = descriptionArea.getText().trim();

        if (title.isEmpty() || "Select category".equals(category) || description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        new Thread(() -> {
            try {
                String safeTitle = title.replace("\\", "\\\\").replace("\"", "\\\"");
                String safeCategory = category.replace("\\", "\\\\").replace("\"", "\\\"");
                String safeDescription = description.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");

                String jsonPayload = String.format(
                    "{\"title\":\"%s\",\"ml_category\":\"%s\",\"description\":\"%s\",\"group_id\":%d}",
                    safeTitle, safeCategory, safeDescription, groupId
                );

                String endpoint = "/groups/" + groupId + "/topics";
                String response = ApiClient.post(endpoint, jsonPayload, authToken);

                int responseCode = 500;
                if (response != null && response.contains(":")) {
                    try {
                        responseCode = Integer.parseInt(response.split(":", 2)[0]);
                    } catch (NumberFormatException ignored) {}
                }

                final int code = responseCode;
                SwingUtilities.invokeLater(() -> {
                    if (code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_CREATED) {
                        JOptionPane.showMessageDialog(this, "Discussion posted successfully!");
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to post discussion. Response Code: " + code, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Network error: " + ex.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }
}