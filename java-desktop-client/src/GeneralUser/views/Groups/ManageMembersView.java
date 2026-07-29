package GeneralUser.views.Groups;

import GeneralUser.api.ApiClient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManageMembersView extends JPanel {

    // --- Modern Palette ---
    private final Color PRIMARY_BLUE = new Color(37, 99, 235);
    private final Color PAGE_BG = new Color(248, 250, 252);
    private final Color BORDER_COLOR = new Color(226, 232, 240);
    private final Color DARK_TEXT = new Color(15, 23, 42);
    private final Color MUTED_TEXT = new Color(100, 116, 139);

    private final String authToken;
    private final int groupId;
    private final Runnable onBackClicked;

    private JLabel titleLbl;
    private JTable membersTable;
    private DefaultTableModel tableModel;
    private List<MemberData> currentMembersList = new ArrayList<>();
    private int currentUserId = -1; // To identify self
    private int createdByUserId = -1; // To identify owner

    public ManageMembersView(int groupId, String token, Runnable onBackClicked) {
        this.groupId = groupId;
        this.authToken = token;
        this.onBackClicked = onBackClicked;

        setBackground(PAGE_BG);
        setLayout(new BorderLayout());

        // --- MAIN SCROLLABLE CONTAINER ---
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(PAGE_BG);
        mainContent.setBorder(new EmptyBorder(24, 32, 24, 32));

        // 1. HEADER CARD
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
            }
        });
        headerCard.add(backLink);

        headerCard.add(Box.createRigidArea(new Dimension(0, 8)));

        titleLbl = new JLabel("Discussion Group Members");
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLbl.setForeground(DARK_TEXT);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerCard.add(titleLbl);

        headerCard.add(Box.createRigidArea(new Dimension(0, 4)));

        JLabel subtitleLbl = new JLabel("Manage group members, administrator privileges, warnings, and blacklisted users.");
        subtitleLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitleLbl.setForeground(MUTED_TEXT);
        subtitleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerCard.add(subtitleLbl);

        mainContent.add(headerCard);
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        // 2. MEMBERS TABLE
        JPanel tableCard = createWhiteCard();
        tableCard.setLayout(new BorderLayout());
        tableCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sectionTitle = new JLabel("Group Members");
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        sectionTitle.setForeground(DARK_TEXT);
        sectionTitle.setBorder(new EmptyBorder(0, 0, 16, 0));
        tableCard.add(sectionTitle, BorderLayout.NORTH);

        String[] columns = {"Name", "User Role", "Group Role", "Status", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Only Actions column allows interaction
            }
        };

        membersTable = new JTable(tableModel);
        membersTable.setRowHeight(48);
        membersTable.setIntercellSpacing(new Dimension(10, 10));
        membersTable.setShowGrid(false);
        membersTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        membersTable.getTableHeader().setBackground(new Color(241, 245, 249));
        membersTable.getTableHeader().setForeground(DARK_TEXT);

        // Center render status/roles
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.LEFT);
        membersTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        membersTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        // Custom Action Buttons
        membersTable.getColumnModel().getColumn(4).setCellRenderer(new ActionPanelRenderer());
        membersTable.getColumnModel().getColumn(4).setCellEditor(new ActionPanelEditor());

        JScrollPane scrollPane = new JScrollPane(membersTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        tableCard.add(scrollPane, BorderLayout.CENTER);
        mainContent.add(tableCard);

        add(mainContent, BorderLayout.CENTER);

        // Load member data
        loadMembersData();
    }

    public ManageMembersView(String token) {
        this(1, token, null);
    }

    // --- FETCH & PARSE DATA ---
    private void loadMembersData() {
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() {
                try {
                    return ApiClient.get("/groups/" + groupId + "/members", authToken);
                } catch (Exception e) {
                    System.err.println("API Request failed: " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    String response = get();
                    if (response != null && response.startsWith("{")) {
                        parseAndPopulate(response);
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
        titleLbl.setText(groupName + " Discussion Group Members");
        createdByUserId = Integer.parseInt(parseJsonNumber(json, "created_by", "-1"));
        currentUserId = Integer.parseInt(parseJsonNumber(json, "auth_id", "-1"));

        currentMembersList.clear();
        tableModel.setRowCount(0);

        Pattern arrayPattern = Pattern.compile("\"members\"\\s*:\\s*\\[(.*?)\\]", Pattern.DOTALL);
        Matcher arrayMatcher = arrayPattern.matcher(json);

        if (arrayMatcher.find()) {
            String arrayContent = arrayMatcher.group(1);
            Pattern objPattern = Pattern.compile("\\{([^}]*)\\}");
            Matcher objMatcher = objPattern.matcher(arrayContent);

            while (objMatcher.find()) {
                String objStr = "{" + objMatcher.group(1) + "}";
                MemberData m = new MemberData();
                m.id = Integer.parseInt(parseJsonNumber(objStr, "id", parseJsonNumber(objStr, "user_id", "0")));
                m.name = parseJsonString(objStr, "name", "Unknown");
                m.userRole = parseJsonString(objStr, "user_role", "Student");
                m.groupRole = parseJsonString(objStr, "group_role", "member");
                m.warningCount = Integer.parseInt(parseJsonNumber(objStr, "warning_count", "0"));
                m.isBlacklisted = parseJsonString(objStr, "is_blacklisted", "false").equals("true");

                currentMembersList.add(m);
                addMemberToTable(m);
            }
        }

        if (currentMembersList.isEmpty()) {
            populateFallbackData();
        }
    }

    private void addMemberToTable(MemberData m) {
        String displayName = m.name + (m.id == createdByUserId ? " [Owner]" : "");
        String displayStatus = m.isBlacklisted ? "Blacklisted" :
                m.warningCount == 2 ? "Warning 2" :
                m.warningCount == 1 ? "Warning 1" : "Active";

        tableModel.addRow(new Object[]{
            displayName,
            capitalize(m.userRole),
            capitalize(m.groupRole),
            displayStatus,
            m
        });
    }

    private void populateFallbackData() {
        titleLbl.setText("BSSE Year II Discussion Group Members");
        createdByUserId = 101;
        currentUserId = 999;

        currentMembersList.clear();
        tableModel.setRowCount(0);

        MemberData m1 = new MemberData(101, "Gloria Ssozi", "Student", "admin", 0, false);
        MemberData m2 = new MemberData(102, "Sarah Namukasa", "Student", "member", 1, false);
        MemberData m3 = new MemberData(103, "Peter Okello", "Student", "member", 2, false);
        MemberData m4 = new MemberData(104, "John Musisi", "Student", "member", 0, true);

        currentMembersList.add(m1);
        currentMembersList.add(m2);
        currentMembersList.add(m3);
        currentMembersList.add(m4);

        for (MemberData m : currentMembersList) {
            addMemberToTable(m);
        }
    }

    // --- ACTIONS HANDLERS ---
    private void performAction(String action, MemberData member) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to " + action.toLowerCase() + " " + member.name + "?",
                "Confirm Action", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() {
                try {
                    String endpoint = "/groups/" + groupId + "/members/" + member.id + "/" + action.toLowerCase();
                    String response = ApiClient.post(endpoint, "{}", authToken);
                    return response != null;
                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            protected void done() {
                loadMembersData(); // Refresh list after action
            }
        };
        worker.execute();
    }

    // --- TABLE BUTTON RENDERER & EDITOR ---
    private class ActionPanelRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof MemberData) {
                return buildActionPanel((MemberData) value);
            }
            return new JLabel("N/A");
        }
    }

    private class ActionPanelEditor extends AbstractCellEditor implements javax.swing.table.TableCellEditor {
        private JPanel panel;

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value instanceof MemberData) {
                panel = buildActionPanel((MemberData) value);
                return panel;
            }
            return new JLabel("N/A");
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }

    private JPanel buildActionPanel(MemberData m) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        panel.setBackground(Color.WHITE);

        // Cannot perform management on owner or self
        if (m.id == createdByUserId || m.id == currentUserId) {
            JLabel na = new JLabel("N/A");
            na.setFont(new Font("SansSerif", Font.ITALIC, 11));
            na.setForeground(MUTED_TEXT);
            panel.add(na);
            return panel;
        }

        // Promote / Demote
        if ("member".equalsIgnoreCase(m.groupRole)) {
            JButton btn = createLinkButton("Make Admin", PRIMARY_BLUE);
            btn.addActionListener(e -> performAction("promote", m));
            panel.add(btn);
        } else if ("admin".equalsIgnoreCase(m.groupRole)) {
            JButton btn = createLinkButton("Demote", new Color(217, 119, 6));
            btn.addActionListener(e -> performAction("demote", m));
            panel.add(btn);
        }

        // Warning
        if (m.warningCount < 2 && !m.isBlacklisted) {
            JButton btn = createLinkButton("Warn", new Color(202, 138, 4));
            btn.addActionListener(e -> performAction("warning", m));
            panel.add(btn);
        }

        // Blacklist / Reinstate
        if (!m.isBlacklisted) {
            JButton btn = createLinkButton("Blacklist", new Color(220, 38, 38));
            btn.addActionListener(e -> performAction("blacklist", m));
            panel.add(btn);
        } else {
            JButton btn = createLinkButton("Reinstate", new Color(16, 185, 129));
            btn.addActionListener(e -> performAction("reinstate", m));
            panel.add(btn);
        }

        return panel;
    }

    private JButton createLinkButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 11));
        btn.setForeground(color);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // --- UI HELPERS ---
    private JPanel createWhiteCard() {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(16, 20, 16, 20)
        ));
        return card;
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return "";
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

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

    // Data model wrapper
    private static class MemberData {
        int id;
        String name;
        String userRole;
        String groupRole;
        int warningCount;
        boolean isBlacklisted;

        MemberData() {}

        MemberData(int id, String name, String userRole, String groupRole, int warningCount, boolean isBlacklisted) {
            this.id = id;
            this.name = name;
            this.userRole = userRole;
            this.groupRole = groupRole;
            this.warningCount = warningCount;
            this.isBlacklisted = isBlacklisted;
        }
    }
}