package Lecturer.Groups;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StatisticsView extends JPanel {

    private final Color PRIMARY_BLUE = new Color(37, 99, 235);
    private final Color PAGE_BG = new Color(248, 250, 252);
    private final Color BORDER_COLOR = new Color(226, 232, 240);
    private final Color DARK_TEXT = new Color(15, 23, 42);
    private final Color MUTED_TEXT = new Color(100, 116, 139);
    private final Color ORANGE_ACCENT = new Color(234, 88, 12);
    private final Color RED_ACCENT = new Color(220, 38, 38);

    private String authToken;

    public StatisticsView(String token) {
        this.authToken = token;
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
            JOptionPane.showMessageDialog(this, "Navigating back to group view...");
        });
        headerCard.add(backLink);

        headerCard.add(Box.createRigidArea(new Dimension(0, 8)));

        JLabel titleLbl = new JLabel("BSSE Year II Group Statistics");
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

        overviewGrid.add(createMetricCard("Total Members", "--", PRIMARY_BLUE));
        overviewGrid.add(createMetricCard("Discussions", "--", PRIMARY_BLUE));
        overviewGrid.add(createMetricCard("Total Messages", "--", PRIMARY_BLUE));
        overviewGrid.add(createMetricCard("Active Today", "--", PRIMARY_BLUE));

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

        partGrid.add(createSubMetricPanel("Participation Rate", "-- %"));
        partGrid.add(createSubMetricPanel("Questions Answered", "--"));

        participationCard.add(partGrid);
        mainContent.add(participationCard);
        mainContent.add(Box.createRigidArea(new Dimension(0, 24)));

        // 4. MOST ACTIVE MEMBERS SECTION
        JPanel activeMembersCard = createWhiteCard();
        activeMembersCard.setLayout(new BoxLayout(activeMembersCard, BoxLayout.Y_AXIS));
        activeMembersCard.setMaximumSize(new Dimension(1200, 170));
        activeMembersCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel membersHeader = new JLabel("Most Active Members");
        membersHeader.setFont(new Font("SansSerif", Font.BOLD, 15));
        membersHeader.setForeground(DARK_TEXT);
        membersHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        activeMembersCard.add(membersHeader);

        activeMembersCard.add(Box.createRigidArea(new Dimension(0, 12)));
        activeMembersCard.add(createMemberRow("Sarah Namukasa", "-- posts"));
        activeMembersCard.add(createMemberRow("Peter Okello", "-- posts"));

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

        JPanel modGrid = new JPanel(new GridLayout(1, 3, 20, 0));
        modGrid.setBackground(Color.WHITE);
        modGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        modGrid.add(createColoredMetricCard("Warnings Issued", "--", ORANGE_ACCENT));
        modGrid.add(createColoredMetricCard("Currently Blacklisted", "--", RED_ACCENT));
        modGrid.add(createColoredMetricCard("Moderation Actions", "--", PRIMARY_BLUE));

        moderationCard.add(modGrid);
        mainContent.add(moderationCard);
        mainContent.add(Box.createRigidArea(new Dimension(0, 24)));

        // 6. POPULAR DISCUSSIONS SECTION
        JPanel discussionsCard = createWhiteCard();
        discussionsCard.setLayout(new BoxLayout(discussionsCard, BoxLayout.Y_AXIS));
        discussionsCard.setMaximumSize(new Dimension(1200, 220));
        discussionsCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel discHeader = new JLabel("Popular Discussions");
        discHeader.setFont(new Font("SansSerif", Font.BOLD, 15));
        discHeader.setForeground(DARK_TEXT);
        discHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        discussionsCard.add(discHeader);

        discussionsCard.add(Box.createRigidArea(new Dimension(0, 14)));
        discussionsCard.add(createDiscussionRow("Laravel Authentication Problem"));
        discussionsCard.add(Box.createRigidArea(new Dimension(0, 8)));
        discussionsCard.add(createDiscussionRow("Database Design Questions"));
        discussionsCard.add(Box.createRigidArea(new Dimension(0, 8)));
        discussionsCard.add(createDiscussionRow("AI Project Ideas"));

        mainContent.add(discussionsCard);

        // Wrap everything inside a scroll pane
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(PAGE_BG);

        add(scrollPane, BorderLayout.CENTER);
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

    private JPanel createMetricCard(String label, String value, Color valueColor) {
        JPanel card = createWhiteCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setForeground(MUTED_TEXT);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lbl);

        card.add(Box.createRigidArea(new Dimension(0, 6)));

        JLabel val = new JLabel(value);
        val.setFont(new Font("SansSerif", Font.BOLD, 24));
        val.setForeground(valueColor);
        val.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(val);

        return card;
    }

    private JPanel createColoredMetricCard(String label, String value, Color valueColor) {
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

        JLabel val = new JLabel(value);
        val.setFont(new Font("SansSerif", Font.BOLD, 22));
        val.setForeground(valueColor);
        val.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(val);

        return panel;
    }

    private JPanel createSubMetricPanel(String label, String value) {
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

        JLabel val = new JLabel(value);
        val.setFont(new Font("SansSerif", Font.BOLD, 20));
        val.setForeground(PRIMARY_BLUE);
        val.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(val);

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

    private JPanel createDiscussionRow(String topic) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(new Color(248, 250, 252));
        row.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        row.setMaximumSize(new Dimension(1200, 42));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel topicLbl = new JLabel(topic);
        topicLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        topicLbl.setForeground(DARK_TEXT);
        row.add(topicLbl, BorderLayout.WEST);

        return row;
    }
}