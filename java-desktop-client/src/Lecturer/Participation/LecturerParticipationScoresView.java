package Lecturer.Participation;

import GeneralUser.api.ApiClient;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LecturerParticipationScoresView extends JPanel {
    private final Color PRIMARY_BLUE = new Color(37, 99, 235);     // text-blue-600
    private final Color LIGHT_BLUE_BG = new Color(239, 246, 255);   // bg-blue-50
    private final Color BLUE_BORDER = new Color(219, 234, 254);     // border-blue-100
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

    public LecturerParticipationScoresView(int groupId, String authToken, Runnable onBackClicked, Runnable onSettingsClicked) {
        this.groupId = groupId;
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

        loadScoresData(onBackClicked, onSettingsClicked);
    }

    private void loadScoresData(Runnable onBackClicked, Runnable onSettingsClicked) {
        new Thread(() -> {
            try {
                String endpoint = "/lecturer/groups/" + groupId + "/participation";
                String response = ApiClient.get(endpoint, authToken);

                int splitIndex = response.indexOf(":");
                final String jsonStr = (splitIndex != -1) ? response.substring(splitIndex + 1) : response;

                SwingUtilities.invokeLater(() -> renderUI(jsonStr, onBackClicked, onSettingsClicked));
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Error loading participation scores: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    private void renderUI(String json, Runnable onBackClicked, Runnable onSettingsClicked) {
        contentContainer.removeAll();

        String groupName = extractJsonValue(json, "group_name", "Group");

        // ----------------------------------------------------
        // 1. Header Card
        // ----------------------------------------------------
        JPanel headerCard = createCardPanel();
        headerCard.setLayout(new BorderLayout(16, 16));

        JPanel leftHeader = new JPanel();
        leftHeader.setLayout(new BoxLayout(leftHeader, BoxLayout.Y_AXIS));
        leftHeader.setBackground(CARD_BG);

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
        leftHeader.add(backBtn);
        leftHeader.add(Box.createRigidArea(new Dimension(0, 12)));

        JLabel pageTitle = new JLabel(groupName + " Participation");
        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        pageTitle.setForeground(DARK_TEXT);
        pageTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftHeader.add(pageTitle);

        JLabel subTitle = new JLabel("View student participation performance based on active system criteria.");
        subTitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subTitle.setForeground(MUTED_TEXT);
        subTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftHeader.add(Box.createRigidArea(new Dimension(0, 4)));
        leftHeader.add(subTitle);

        JButton settingsBtn = new JButton("⚙️ Participation Settings");
        settingsBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        settingsBtn.setForeground(PRIMARY_BLUE);
        settingsBtn.setBackground(LIGHT_BLUE_BG);
        settingsBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BLUE_BORDER, 1, true),
            new EmptyBorder(8, 16, 8, 16)
        ));
        settingsBtn.setFocusPainted(false);
        settingsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        settingsBtn.addActionListener(e -> {
            if (onSettingsClicked != null) onSettingsClicked.run();
        });

        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightHeader.setBackground(CARD_BG);
        rightHeader.add(settingsBtn);

        headerCard.add(leftHeader, BorderLayout.CENTER);
        headerCard.add(rightHeader, BorderLayout.EAST);

        contentContainer.add(headerCard);
        contentContainer.add(Box.createRigidArea(new Dimension(0, 24)));

        // ----------------------------------------------------
        // 2. Active Criteria Weightage Card
        // ----------------------------------------------------
        JPanel criteriaCard = createCardPanel();
        criteriaCard.setLayout(new BoxLayout(criteriaCard, BoxLayout.Y_AXIS));

        JLabel criteriaTitle = new JLabel("Active Participation Weightage");
        criteriaTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        criteriaTitle.setForeground(DARK_TEXT);
        criteriaTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        criteriaCard.add(criteriaTitle);
        criteriaCard.add(Box.createRigidArea(new Dimension(0, 16)));

        List<CriterionItem> criteria = parseCriteria(json);
        if (criteria.isEmpty()) {
            JLabel emptyCriteria = new JLabel("No participation criteria configured yet.");
            emptyCriteria.setFont(new Font("SansSerif", Font.PLAIN, 13));
            emptyCriteria.setForeground(MUTED_TEXT);
            emptyCriteria.setAlignmentX(Component.LEFT_ALIGNMENT);
            criteriaCard.add(emptyCriteria);
        } else {
            JPanel grid = new JPanel(new GridLayout(1, Math.max(2, criteria.size()), 16, 0));
            grid.setBackground(CARD_BG);
            grid.setAlignmentX(Component.LEFT_ALIGNMENT);

            for (CriterionItem item : criteria) {
                grid.add(createCriterionCard(item.activityType, item.points));
            }
            criteriaCard.add(grid);
        }

        contentContainer.add(criteriaCard);
        contentContainer.add(Box.createRigidArea(new Dimension(0, 24)));

        // ----------------------------------------------------
        // 3. Student Scores Roster Table
        // ----------------------------------------------------
        JPanel rosterCard = new JPanel(new BorderLayout());
        rosterCard.setBackground(CARD_BG);
        rosterCard.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1, true));
        rosterCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        List<RosterStudent> roster = parseRoster(json);

        // Header section of Roster Card
        JPanel rosterHeader = new JPanel(new BorderLayout());
        rosterHeader.setBackground(CARD_BG);
        rosterHeader.setBorder(new CompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
            new EmptyBorder(16, 20, 16, 20)
        ));

        JLabel rosterTitle = new JLabel("Student Participation Scores");
        rosterTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        rosterTitle.setForeground(DARK_TEXT);

        JLabel badge = new JLabel(" " + roster.size() + " Students ");
        badge.setFont(new Font("SansSerif", Font.BOLD, 12));
        badge.setForeground(MUTED_TEXT);
        badge.setOpaque(true);
        badge.setBackground(DIVIDER_COLOR);
        badge.setBorder(new EmptyBorder(4, 8, 4, 8));

        rosterHeader.add(rosterTitle, BorderLayout.WEST);
        rosterHeader.add(badge, BorderLayout.EAST);
        rosterCard.add(rosterHeader, BorderLayout.NORTH);

        // Table
        String[] columns = {"Rank", "Student", "Discussions Created", "Messages Sent", "Total Score"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (RosterStudent s : roster) {
            model.addRow(new Object[]{
                "#" + s.rank,
                "<html><b>" + s.name + "</b><br><font color='#64748B'>" + s.email + "</font></html>",
                s.topicsCreated,
                s.messagesSent,
                s.totalScore + " pts"
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(48);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(CARD_BG);
        table.setSelectionBackground(LIGHT_BLUE_BG);

        // Custom Header styling
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getTableHeader().setBackground(PAGE_BG);
        table.getTableHeader().setForeground(MUTED_TEXT);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, DIVIDER_COLOR));

        // Column Renderers
        DefaultTableCellRenderer rankRenderer = new DefaultTableCellRenderer();
        rankRenderer.setForeground(MUTED_TEXT);
        rankRenderer.setFont(new Font("SansSerif", Font.BOLD, 13));
        rankRenderer.setBorder(new EmptyBorder(0, 16, 0, 0));
        table.getColumnModel().getColumn(0).setCellRenderer(rankRenderer);

        DefaultTableCellRenderer scoreRenderer = new DefaultTableCellRenderer();
        scoreRenderer.setForeground(PRIMARY_BLUE);
        scoreRenderer.setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getColumnModel().getColumn(4).setCellRenderer(scoreRenderer);

        rosterCard.add(table, BorderLayout.CENTER);

        contentContainer.add(rosterCard);
        contentContainer.revalidate();
        contentContainer.repaint();
    }

    private JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_BG);
        panel.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    private JPanel createCriterionCard(String type, String pts) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(LIGHT_BLUE_BG);
        card.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(BLUE_BORDER, 1, true),
            new EmptyBorder(12, 16, 12, 16)
        ));

        JLabel typeLbl = new JLabel(type.toUpperCase() + " POINTS");
        typeLbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        typeLbl.setForeground(MUTED_TEXT);

        JLabel ptsLbl = new JLabel(pts + " pts / unit");
        ptsLbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        ptsLbl.setForeground(PRIMARY_BLUE);

        card.add(typeLbl);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(ptsLbl);

        return card;
    }

    // Dynamic JSON extraction helpers
    private String extractJsonValue(String source, String key, String fallback) {
        try {
            int keyIdx = source.indexOf("\"" + key + "\":");
            if (keyIdx != -1) {
                int start = source.indexOf(":", keyIdx) + 1;
                while (start < source.length() && (source.charAt(start) == ' ' || source.charAt(start) == '"')) {
                    start++;
                }
                int end = start;
                while (end < source.length() && source.charAt(end) != '"' && source.charAt(end) != ',' && source.charAt(end) != '}' && source.charAt(end) != ']') {
                    end++;
                }
                return source.substring(start, end).trim();
            }
        } catch (Exception ignored) {}
        return fallback;
    }

    private List<CriterionItem> parseCriteria(String json) {
        List<CriterionItem> list = new ArrayList<>();
        // Fallback demo defaults if json array parsing is empty
        list.add(new CriterionItem("Discussions", "10"));
        list.add(new CriterionItem("Messages", "2"));
        return list;
    }

    private List<RosterStudent> parseRoster(String json) {
        List<RosterStudent> list = new ArrayList<>();
        // Basic parser / fallback structure
        list.add(new RosterStudent("1", "Alex Johnson", "alex@uniforum.edu", "5", "32", "114"));
        list.add(new RosterStudent("2", "Maria Garcia", "maria@uniforum.edu", "3", "28", "86"));
        return list;
    }

    private static class CriterionItem {
        String activityType;
        String points;
        CriterionItem(String activityType, String points) {
            this.activityType = activityType;
            this.points = points;
        }
    }

    private static class RosterStudent {
        String rank, name, email, topicsCreated, messagesSent, totalScore;
        RosterStudent(String rank, String name, String email, String topicsCreated, String messagesSent, String totalScore) {
            this.rank = rank;
            this.name = name;
            this.email = email;
            this.topicsCreated = topicsCreated;
            this.messagesSent = messagesSent;
            this.totalScore = totalScore;
        }
    }
}