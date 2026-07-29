package GeneralUser.views.Groups;

import GeneralUser.api.ApiClient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StatisticsView extends JPanel {

    // --- Modern Styling Palette ---
    private final Color PRIMARY_BLUE = new Color(37, 99, 235);
    private final Color PAGE_BG = new Color(248, 250, 252);
    private final Color BORDER_COLOR = new Color(226, 232, 240);
    private final Color DARK_TEXT = new Color(15, 23, 42);
    private final Color MUTED_TEXT = new Color(100, 116, 139);
    private final Color ORANGE_ACCENT = new Color(234, 88, 12);
    private final Color RED_ACCENT = new Color(220, 38, 38);

    private final String authToken;
    private final int groupId;
    private final Runnable onBackClicked;

    // --- Dynamic UI Components ---
    private JLabel titleLbl;
    private JLabel totalMembersVal;
    private JLabel discussionsVal;
    private JLabel totalMessagesVal;
    private JLabel activeTodayVal;
    private JLabel participationRateVal;
    private JLabel warningsVal;
    private JLabel blacklistedVal;

    private JPanel activeMembersContainer;
    private JPanel popularDiscussionsContainer;

    // Constructor with Back Action & Group ID
    public StatisticsView(int groupId, String token, Runnable onBackClicked) {
        this.groupId = groupId;
        this.authToken = token;
        this.onBackClicked = onBackClicked;

        setBackground(PAGE_BG);
        setLayout(new BorderLayout());

        // --- MAIN SCROLLABLE CONTENT ---
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(PAGE_BG);
        mainContent.setBorder(new EmptyBorder(24, 32, 24, 32));

        // 1. HEADER SECTION
        JPanel headerCard = createWhiteCard();
        headerCard.setLayout(new BoxLayout(headerCard, BoxLayout.Y_AXIS));
        headerCard.setMaximumSize(new Dimension(1200, 130));
        headerCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton backLink = new JButton("← Back to Group");
        backLink.setFont(new Font("SansSerif", Font.PLAIN, 12));
        backLink.setForeground(PRIMARY_BLUE);
        backLink.setBorderPainted(false);
        backLink.setContentAreaFilled(false);
        backLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLink.setAlignmentX(Component.LEFT_ALIGNMENT);
        backLink.addActionListener(e -> {
            if (this.onBackClicked != null) {
                this.onBackClicked.run();
            } else {
                JOptionPane.showMessageDialog(this, "Navigating back...");
            }
        });
        headerCard.add(backLink);

        headerCard.add(Box.createRigidArea(new Dimension(0, 8)));

        titleLbl = new JLabel("Group Statistics");
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLbl.setForeground(DARK_TEXT);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerCard.add(titleLbl);

        headerCard.add(Box.createRigidArea(new Dimension(0, 4)));

        JLabel subtitleLbl = new JLabel("Monitor group activity, participation, and moderation activities.");
        subtitleLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitleLbl.setForeground(MUTED_TEXT);
        subtitleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerCard.add(subtitleLbl);

        mainContent.add(headerCard);
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        // 2. GROUP OVERVIEW SECTION
        mainContent.add(createSectionHeading("Group Overview"));
        mainContent.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel overviewGrid = new JPanel(new GridLayout(1, 4, 15, 0));
        overviewGrid.setBackground(PAGE_BG);
        overviewGrid.setMaximumSize(new Dimension(1200, 105));
        overviewGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        totalMembersVal = new JLabel("--");
        discussionsVal = new JLabel("--");
        totalMessagesVal = new JLabel("--");
        activeTodayVal = new JLabel("--");

        overviewGrid.add(createMetricCard("Total Members", totalMembersVal, PRIMARY_BLUE));
        overviewGrid.add(createMetricCard("Discussions", discussionsVal, PRIMARY_BLUE));
        overviewGrid.add(createMetricCard("Total Messages", totalMessagesVal, PRIMARY_BLUE));
        overviewGrid.add(createMetricCard("Active Today", activeTodayVal, PRIMARY_BLUE));

        mainContent.add(overviewGrid);
        mainContent.add(Box.createRigidArea(new Dimension(0, 24)));

        // 3. PARTICIPATION STATISTICS SECTION
        JPanel participationCard = createWhiteCard();
        participationCard.setLayout(new BoxLayout(participationCard, BoxLayout.Y_AXIS));
        participationCard.setMaximumSize(new Dimension(1200, 130));
        participationCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel partHeader = new JLabel("Participation Statistics");
        partHeader.setFont(new Font("SansSerif", Font.BOLD, 15));
        partHeader.setForeground(DARK_TEXT);
        partHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        participationCard.add(partHeader);

        participationCard.add(Box.createRigidArea(new Dimension(0, 14)));

        JPanel partGrid = new JPanel(new GridLayout(1, 2, 20, 0));
        partGrid.setBackground(Color.WHITE);
        partGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        participationRateVal = new JLabel("-- %");
        partGrid.add(createSubMetricPanel("Participation Rate", participationRateVal));

        participationCard.add(partGrid);
        mainContent.add(participationCard);
        mainContent.add(Box.createRigidArea(new Dimension(0, 24)));

        // 4. MOST ACTIVE MEMBERS SECTION
        JPanel activeMembersCard = createWhiteCard();
        activeMembersCard.setLayout(new BoxLayout(activeMembersCard, BoxLayout.Y_AXIS));
        activeMembersCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel membersHeader = new JLabel("Most Active Members");
        membersHeader.setFont(new Font("SansSerif", Font.BOLD, 15));
        membersHeader.setForeground(DARK_TEXT);
        membersHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        activeMembersCard.add(membersHeader);

        activeMembersCard.add(Box.createRigidArea(new Dimension(0, 12)));

        activeMembersContainer = new JPanel();
        activeMembersContainer.setLayout(new BoxLayout(activeMembersContainer, BoxLayout.Y_AXIS));
        activeMembersContainer.setBackground(Color.WHITE);
        activeMembersContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        activeMembersCard.add(activeMembersContainer);
        mainContent.add(activeMembersCard);
        mainContent.add(Box.createRigidArea(new Dimension(0, 24)));

        // 5. MODERATION STATISTICS SECTION
        JPanel moderationCard = createWhiteCard();
        moderationCard.setLayout(new BoxLayout(moderationCard, BoxLayout.Y_AXIS));
        moderationCard.setMaximumSize(new Dimension(1200, 140));
        moderationCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel modHeader = new JLabel("Moderation Statistics");
        modHeader.setFont(new Font("SansSerif", Font.BOLD, 15));
        modHeader.setForeground(DARK_TEXT);
        modHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        moderationCard.add(modHeader);

        moderationCard.add(Box.createRigidArea(new Dimension(0, 14)));

        JPanel modGrid = new JPanel(new GridLayout(1, 2, 20, 0));
        modGrid.setBackground(Color.WHITE);
        modGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        warningsVal = new JLabel("--");
        blacklistedVal = new JLabel("--");

        modGrid.add(createColoredMetricCard("Warnings Issued", warningsVal, ORANGE_ACCENT));
        modGrid.add(createColoredMetricCard("Currently Blacklisted", blacklistedVal, RED_ACCENT));

        moderationCard.add(modGrid);
        mainContent.add(moderationCard);
        mainContent.add(Box.createRigidArea(new Dimension(0, 24)));

        // 6. POPULAR DISCUSSIONS SECTION
        JPanel discussionsCard = createWhiteCard();
        discussionsCard.setLayout(new BoxLayout(discussionsCard, BoxLayout.Y_AXIS));
        discussionsCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel discHeader = new JLabel("Popular Discussions");
        discHeader.setFont(new Font("SansSerif", Font.BOLD, 15));
        discHeader.setForeground(DARK_TEXT);
        discHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        discussionsCard.add(discHeader);

        discussionsCard.add(Box.createRigidArea(new Dimension(0, 14)));

        popularDiscussionsContainer = new JPanel();
        popularDiscussionsContainer.setLayout(new BoxLayout(popularDiscussionsContainer, BoxLayout.Y_AXIS));
        popularDiscussionsContainer.setBackground(Color.WHITE);
        popularDiscussionsContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        discussionsCard.add(popularDiscussionsContainer);
        mainContent.add(discussionsCard);

        // Wrap inside scroll pane
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(PAGE_BG);

        add(scrollPane, BorderLayout.CENTER);

        // Fetch stats from backend
        loadStatisticsData();
    }

    // Overloaded Constructor for compatibility
    public StatisticsView(String token) {
        this(1, token, null);
    }

    // --- API DATA FETCHING ---
    private void loadStatisticsData() {
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() {
                try {
                    return ApiClient.get("/groups/" + groupId + "/statistics", authToken);
                } catch (Exception e) {
                    System.err.println("API Request failed: " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    String jsonResponse = get();
                    if (jsonResponse != null && jsonResponse.startsWith("{")) {
                        parseAndPopulate(jsonResponse);
                    } else {
                        populateFallbackData();
                    }
                } catch (Exception e) {
                    populateFallbackData();
                }
            }
        };
        worker.execute();
    }

    private void parseAndPopulate(String json) {
        String groupName = parseJsonString(json, "group_name", "Group");
        titleLbl.setText(groupName + " Statistics");

        totalMembersVal.setText(parseJsonNumber(json, "totalMembers", "0"));
        discussionsVal.setText(parseJsonNumber(json, "discussionCount", "0"));
        totalMessagesVal.setText(parseJsonNumber(json, "messageCount", "0"));
        activeTodayVal.setText(parseJsonNumber(json, "activeToday", "0"));

        participationRateVal.setText(parseJsonNumber(json, "participationRate", "0") + "%");

        warningsVal.setText(parseJsonNumber(json, "warningsIssued", "0"));
        blacklistedVal.setText(parseJsonNumber(json, "currentlyBlacklisted", "0"));

        // Active Members
        activeMembersContainer.removeAll();
        List<String[]> activeMembers = parseArrayObjects(json, "mostActiveMembers", "name", "posts");
        if (!activeMembers.isEmpty()) {
            for (String[] member : activeMembers) {
                activeMembersContainer.add(createMemberRow(member[0], member[1] + " posts"));
            }
        } else {
            activeMembersContainer.add(createEmptyLabel("No activity yet."));
        }

        // Popular Discussions
        popularDiscussionsContainer.removeAll();
        List<String[]> discussions = parseArrayObjects(json, "popularDiscussions", "title", "messages_count");
        if (!discussions.isEmpty()) {
            for (String[] disc : discussions) {
                popularDiscussionsContainer.add(createDiscussionRow(disc[0], disc[1] + " messages"));
                popularDiscussionsContainer.add(Box.createRigidArea(new Dimension(0, 8)));
            }
        } else {
            popularDiscussionsContainer.add(createEmptyLabel("No discussions yet."));
        }

        revalidate();
        repaint();
    }

    // --- JSON HELPER UTILITIES (No external JAR needed) ---
    private String parseJsonString(String json, String key, String defaultValue) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(json);
        return matcher.find() ? matcher.group(1) : defaultValue;
    }

    private String parseJsonNumber(String json, String key, String defaultValue) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*([0-9.]+)");
        Matcher matcher = pattern.matcher(json);
        return matcher.find() ? matcher.group(1) : defaultValue;
    }

    private List<String[]> parseArrayObjects(String json, String arrayKey, String key1, String key2) {
        List<String[]> results = new ArrayList<>();
        Pattern arrayPattern = Pattern.compile("\"" + arrayKey + "\"\\s*:\\s*\\[(.*?)\\]", Pattern.DOTALL);
        Matcher arrayMatcher = arrayPattern.matcher(json);
        if (arrayMatcher.find()) {
            String arrayContent = arrayMatcher.group(1);
            Pattern objPattern = Pattern.compile("\\{([^}]*)\\}");
            Matcher objMatcher = objPattern.matcher(arrayContent);
            while (objMatcher.find()) {
                String objStr = objMatcher.group(1);
                String val1 = parseJsonString("{" + objStr + "}", key1, parseJsonNumber("{" + objStr + "}", key1, "Unknown"));
                String val2 = parseJsonNumber("{" + objStr + "}", key2, "0");
                results.add(new String[]{val1, val2});
            }
        }
        return results;
    }

    private void populateFallbackData() {
        titleLbl.setText("Group Statistics");
        totalMembersVal.setText("42");
        discussionsVal.setText("18");
        totalMessagesVal.setText("320");
        activeTodayVal.setText("14");

        participationRateVal.setText("78.5%");

        warningsVal.setText("2");
        blacklistedVal.setText("0");

        activeMembersContainer.removeAll();
        activeMembersContainer.add(createMemberRow("Sarah Namukasa", "45 posts"));
        activeMembersContainer.add(createMemberRow("Peter Okello", "32 posts"));
        activeMembersContainer.add(createMemberRow("Gloria Ssozi", "28 posts"));

        popularDiscussionsContainer.removeAll();
        popularDiscussionsContainer.add(createDiscussionRow("Laravel Authentication Problem", "24 messages"));
        popularDiscussionsContainer.add(Box.createRigidArea(new Dimension(0, 8)));
        popularDiscussionsContainer.add(createDiscussionRow("Database Design Questions", "18 messages"));
        popularDiscussionsContainer.add(Box.createRigidArea(new Dimension(0, 8)));
        popularDiscussionsContainer.add(createDiscussionRow("AI Project Ideas", "12 messages"));

        revalidate();
        repaint();
    }

    // --- UI HELPER BUILDERS ---

    private JPanel createWhiteCard() {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(16, 20, 16, 20)
        ));
        return card;
    }

    private JLabel createSectionHeading(String text) {
        JLabel heading = new JLabel(text);
        heading.setFont(new Font("SansSerif", Font.BOLD, 16));
        heading.setForeground(DARK_TEXT);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        return heading;
    }

    private JPanel createMetricCard(String label, JLabel valLbl, Color valueColor) {
        JPanel card = createWhiteCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setForeground(MUTED_TEXT);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lbl);

        card.add(Box.createRigidArea(new Dimension(0, 6)));

        valLbl.setFont(new Font("SansSerif", Font.BOLD, 24));
        valLbl.setForeground(valueColor);
        valLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(valLbl);

        return card;
    }

    private JPanel createColoredMetricCard(String label, JLabel valLbl, Color valueColor) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setForeground(MUTED_TEXT);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lbl);

        panel.add(Box.createRigidArea(new Dimension(0, 4)));

        valLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        valLbl.setForeground(valueColor);
        valLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(valLbl);

        return panel;
    }

    private JPanel createSubMetricPanel(String label, JLabel valLbl) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setForeground(MUTED_TEXT);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lbl);

        panel.add(Box.createRigidArea(new Dimension(0, 4)));

        valLbl.setFont(new Font("SansSerif", Font.BOLD, 20));
        valLbl.setForeground(PRIMARY_BLUE);
        valLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(valLbl);

        return panel;
    }

    private JPanel createMemberRow(String name, String posts) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(241, 245, 249)),
            new EmptyBorder(8, 0, 8, 0)
        ));
        row.setMaximumSize(new Dimension(1200, 40));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nameLbl = new JLabel(name);
        nameLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        nameLbl.setForeground(DARK_TEXT);
        row.add(nameLbl, BorderLayout.WEST);

        JLabel postsLbl = new JLabel(posts);
        postsLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        postsLbl.setForeground(MUTED_TEXT);
        row.add(postsLbl, BorderLayout.EAST);

        return row;
    }

    private JPanel createDiscussionRow(String topic, String meta) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(new Color(248, 250, 252));
        row.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        row.setMaximumSize(new Dimension(1200, 42));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel topicLbl = new JLabel(topic);
        topicLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        topicLbl.setForeground(DARK_TEXT);
        row.add(topicLbl, BorderLayout.WEST);

        JLabel metaLbl = new JLabel(meta);
        metaLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        metaLbl.setForeground(MUTED_TEXT);
        row.add(metaLbl, BorderLayout.EAST);

        return row;
    }

    private JLabel createEmptyLabel(String message) {
        JLabel lbl = new JLabel(message);
        lbl.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lbl.setForeground(MUTED_TEXT);
        lbl.setBorder(new EmptyBorder(8, 0, 8, 0));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }
}