package Student.Participation;

import GeneralUser.api.ApiClient;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.HttpURLConnection;

public class ParticipationResultsView extends JPanel {
    private final Color PRIMARY_BLUE = new Color(37, 99, 235);     // text-blue-600
    private final Color PAGE_BG = new Color(248, 250, 252);        // bg-slate-50
    private final Color CARD_BG = Color.WHITE;
    private final Color BORDER_COLOR = new Color(226, 232, 240);    // border-slate-200
    private final Color DIVIDER_COLOR = new Color(241, 245, 249);   // divide-slate-100
    private final Color DARK_TEXT = new Color(30, 41, 59);         // text-slate-800
    private final Color MUTED_TEXT = new Color(100, 116, 139);     // text-slate-500
    private final Color BODY_TEXT = new Color(71, 85, 105);        // text-slate-600

    private final int groupId;
    private final String authToken;
    private final JPanel contentContainer;

    public ParticipationResultsView(int groupId, String authToken, Runnable onBackClicked) {
        this.groupId = groupId;
        this.authToken = authToken;

        setLayout(new BorderLayout());
        setBackground(PAGE_BG);

        // Scrollable content wrapper
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

        // Fetch data from backend
        loadParticipationResults(onBackClicked);
    }

    private void loadParticipationResults(Runnable onBackClicked) {
        new Thread(() -> {
            try {
                // GET request to fetch participation scores for this group
                String endpoint = "/groups/" + groupId + "/participation-results";
                String response = ApiClient.get(endpoint, authToken);

                int splitIndex = response.indexOf(":");
                final String responseStr = (splitIndex != -1) ? response.substring(splitIndex + 1) : response;

                SwingUtilities.invokeLater(() -> {
                    contentContainer.removeAll();

                    // Parse JSON payload or fallback to defaults
                    String groupName = extractJsonValue(responseStr, "group_name", "Study Group");
                    String totalScore = extractJsonValue(responseStr, "total_score", "0");
                    String rank = extractJsonValue(responseStr, "rank", "1");
                    String topicsCreated = extractJsonValue(responseStr, "topics_created", "0");
                    String messagesSent = extractJsonValue(responseStr, "messages_sent", "0");

                    // ----------------------------------------------------
                    // 1. Header Card
                    // ----------------------------------------------------
                    JPanel headerCard = createCardPanel();
                    headerCard.setLayout(new BoxLayout(headerCard, BoxLayout.Y_AXIS));

                    JButton backBtn = new JButton("← Back to Group");
                    backBtn.setFont(new Font("SansSerif", Font.PLAIN, 13));
                    backBtn.setForeground(PRIMARY_BLUE);
                    backBtn.setBorderPainted(false);
                    backBtn.setContentAreaFilled(false);
                    backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
                    backBtn.addActionListener(e -> {
                        if (onBackClicked != null) onBackClicked.run();
                    });
                    headerCard.add(backBtn);
                    headerCard.add(Box.createRigidArea(new Dimension(0, 16)));

                    JLabel pageTitle = new JLabel("Participation Results");
                    pageTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
                    pageTitle.setForeground(DARK_TEXT);
                    pageTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
                    headerCard.add(pageTitle);

                    JLabel subTitle = new JLabel("View your contribution and participation performance in this group.");
                    subTitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
                    subTitle.setForeground(MUTED_TEXT);
                    subTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
                    headerCard.add(Box.createRigidArea(new Dimension(0, 8)));
                    headerCard.add(subTitle);

                    contentContainer.add(headerCard);
                    contentContainer.add(Box.createRigidArea(new Dimension(0, 24)));

                    // ----------------------------------------------------
                    // 2. Metrics Grid (3 Stat Cards)
                    // ----------------------------------------------------
                    JPanel gridPanel = new JPanel(new GridLayout(1, 3, 20, 0));
                    gridPanel.setBackground(PAGE_BG);
                    gridPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    gridPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

                    // Card 1: Total Score
                    gridPanel.add(createStatCard("Total Participation Score", totalScore, PRIMARY_BLUE, 30));
                    
                    // Card 2: Current Rank
                    gridPanel.add(createStatCard("Current Rank", "#" + rank, PRIMARY_BLUE, 30));
                    
                    // Card 3: Group Name
                    gridPanel.add(createStatCard("Group", groupName, DARK_TEXT, 20));

                    contentContainer.add(gridPanel);
                    contentContainer.add(Box.createRigidArea(new Dimension(0, 24)));

                    // ----------------------------------------------------
                    // 3. Criteria Breakdown Card
                    // ----------------------------------------------------
                    JPanel breakdownCard = createCardPanel();
                    breakdownCard.setLayout(new BoxLayout(breakdownCard, BoxLayout.Y_AXIS));

                    // Breakdown Header
                    JLabel breakdownTitle = new JLabel("Participation Breakdown");
                    breakdownTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
                    breakdownTitle.setForeground(DARK_TEXT);
                    breakdownTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
                    
                    JPanel breakdownHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                    breakdownHeaderPanel.setBackground(CARD_BG);
                    breakdownHeaderPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    breakdownHeaderPanel.add(breakdownTitle);
                    
                    breakdownCard.add(breakdownHeaderPanel);
                    breakdownCard.add(Box.createRigidArea(new Dimension(0, 16)));

                    // Divider
                    JSeparator sep1 = new JSeparator();
                    sep1.setForeground(DIVIDER_COLOR);
                    sep1.setAlignmentX(Component.LEFT_ALIGNMENT);
                    breakdownCard.add(sep1);
                    breakdownCard.add(Box.createRigidArea(new Dimension(0, 16)));

                    // Row 1: Discussions created
                    breakdownCard.add(createBreakdownRow("Discussions created", topicsCreated));
                    
                    // Divider
                    JSeparator sep2 = new JSeparator();
                    sep2.setForeground(DIVIDER_COLOR);
                    sep2.setAlignmentX(Component.LEFT_ALIGNMENT);
                    breakdownCard.add(Box.createRigidArea(new Dimension(0, 12)));
                    breakdownCard.add(sep2);
                    breakdownCard.add(Box.createRigidArea(new Dimension(0, 12)));

                    // Row 2: Messages sent
                    breakdownCard.add(createBreakdownRow("Messages sent", messagesSent));

                    contentContainer.add(breakdownCard);

                    contentContainer.revalidate();
                    contentContainer.repaint();
                });

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Error loading participation results: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    private JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_BG);
        panel.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(24, 24, 24, 24)
        ));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    private JPanel createStatCard(String labelText, String valueText, Color valueColor, int fontSize) {
        JPanel card = createCardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        label.setForeground(MUTED_TEXT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel value = new JLabel(valueText);
        value.setFont(new Font("SansSerif", Font.BOLD, fontSize));
        value.setForeground(valueColor);
        value.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(label);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(value);

        return card;
    }

    private JPanel createBreakdownRow(String labelText, String valueText) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(CARD_BG);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setForeground(BODY_TEXT);

        JLabel value = new JLabel(valueText);
        value.setFont(new Font("SansSerif", Font.BOLD, 14));
        value.setForeground(DARK_TEXT);

        row.add(label, BorderLayout.WEST);
        row.add(value, BorderLayout.EAST);

        return row;
    }

    private String extractJsonValue(String source, String key, String fallback) {
        try {
            int keyIdx = source.indexOf("\"" + key + "\":");
            if (keyIdx != -1) {
                int start = source.indexOf(":", keyIdx) + 1;
                // Trim leading whitespace/quotes
                while (start < source.length() && (source.charAt(start) == ' ' || source.charAt(start) == '"')) {
                    start++;
                }
                int end = start;
                while (end < source.length() && source.charAt(end) != '"' && source.charAt(end) != ',' && source.charAt(end) != '}') {
                    end++;
                }
                return source.substring(start, end).trim();
            }
        } catch (Exception ignored) {}
        return fallback;
    }
}