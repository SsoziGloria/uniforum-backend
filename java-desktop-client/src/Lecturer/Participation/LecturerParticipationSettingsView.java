package Lecturer.Participation;

import GeneralUser.api.ApiClient;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LecturerParticipationSettingsView extends JPanel {
    private final Color PRIMARY_BLUE = new Color(37, 99, 235);     // text-blue-600
    private final Color EMERALD_GREEN = new Color(16, 185, 129);   // bg-emerald-600
    private final Color EMERALD_HOVER = new Color(5, 150, 105);
    private final Color RED_TEXT = new Color(220, 38, 38);         // text-red-600
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

    // Track input fields for bulk saving
    private final Map<String, JTextField> pointFields = new HashMap<>();

    public LecturerParticipationSettingsView(int groupId, String authToken, Runnable onBackClicked) {
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

        loadSettingsData(onBackClicked);
    }

    private void loadSettingsData(Runnable onBackClicked) {
        new Thread(() -> {
            try {
                String endpoint = "/lecturer/groups/" + groupId + "/participation/settings";
                String response = ApiClient.get(endpoint, authToken);

                int splitIndex = response.indexOf(":");
                final String jsonStr = (splitIndex != -1) ? response.substring(splitIndex + 1) : response;

                SwingUtilities.invokeLater(() -> renderUI(jsonStr, onBackClicked));
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Error loading participation settings: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    private void renderUI(String json, Runnable onBackClicked) {
        contentContainer.removeAll();
        pointFields.clear();

        String groupName = extractJsonValue(json, "group_name", "Group");

        // ----------------------------------------------------
        // 1. Header Card
        // ----------------------------------------------------
        JPanel headerCard = createCardPanel();
        headerCard.setLayout(new BorderLayout(16, 16));

        JPanel leftHeader = new JPanel();
        leftHeader.setLayout(new BoxLayout(leftHeader, BoxLayout.Y_AXIS));
        leftHeader.setBackground(CARD_BG);

        JButton backBtn = new JButton("← Back to Participation Overview");
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

        JLabel pageTitle = new JLabel(groupName + " Participation Settings");
        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        pageTitle.setForeground(DARK_TEXT);
        pageTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftHeader.add(pageTitle);

        JLabel subTitle = new JLabel("Configure how students earn participation marks based on their activity in this group.");
        subTitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subTitle.setForeground(MUTED_TEXT);
        subTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftHeader.add(Box.createRigidArea(new Dimension(0, 4)));
        leftHeader.add(subTitle);

        JButton addCriterionBtn = new JButton("+ Add Criterion");
        addCriterionBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        addCriterionBtn.setForeground(Color.WHITE);
        addCriterionBtn.setBackground(PRIMARY_BLUE);
        addCriterionBtn.setBorder(new EmptyBorder(10, 16, 10, 16));
        addCriterionBtn.setFocusPainted(false);
        addCriterionBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addCriterionBtn.addActionListener(e -> showAddCriterionModal(onBackClicked));

        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightHeader.setBackground(CARD_BG);
        rightHeader.add(addCriterionBtn);

        headerCard.add(leftHeader, BorderLayout.CENTER);
        headerCard.add(rightHeader, BorderLayout.EAST);

        contentContainer.add(headerCard);
        contentContainer.add(Box.createRigidArea(new Dimension(0, 24)));

        // ----------------------------------------------------
        // 2. Criteria Management Table Card
        // ----------------------------------------------------
        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(CARD_BG);
        tableCard.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1, true));
        tableCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Table Header Intro
        JPanel tableIntro = new JPanel();
        tableIntro.setLayout(new BoxLayout(tableIntro, BoxLayout.Y_AXIS));
        tableIntro.setBackground(CARD_BG);
        tableIntro.setBorder(new CompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel introTitle = new JLabel("Participation Criteria Rules");
        introTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        introTitle.setForeground(DARK_TEXT);
        
        JLabel introSub = new JLabel("Define the points awarded per activity. Changes apply automatically to performance calculations.");
        introSub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        introSub.setForeground(MUTED_TEXT);
        
        tableIntro.add(introTitle);
        tableIntro.add(Box.createRigidArea(new Dimension(0, 4)));
        tableIntro.add(introSub);
        tableCard.add(tableIntro, BorderLayout.NORTH);

        List<CriterionRule> criteria = parseCriteriaRules(json);

        // Main Form Content Panel (holds table & save action)
        JPanel formBody = new JPanel(new BorderLayout());
        formBody.setBackground(CARD_BG);

        String[] columns = {"Criterion Name", "Activity Type", "Points Awarded", "Actions"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Render custom interactive components instead of raw text cells
            }
        };

        tableCard.add(formBody, BorderLayout.CENTER);

        contentContainer.add(tableCard);
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

    private void showAddCriterionModal(Runnable onReload) {
        JDialog modal = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Participation Criterion", true);
        modal.setSize(420, 380);
        modal.setLocationRelativeTo(this);
        modal.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_BG);
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel titleLbl = new JLabel("Add Participation Criterion");
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLbl.setForeground(DARK_TEXT);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLbl);
        panel.add(Box.createRigidArea(new Dimension(0, 16)));

        // Name input
        panel.add(createFormLabel("CRITERION NAME"));
        JTextField nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        panel.add(nameField);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Activity Type dropdown
        panel.add(createFormLabel("ACTIVITY TYPE"));
        String[] types = {"Create Discussion Topic (topic)", "Send Message / Post (message)", "Answer Question (answer)", "Share Resource (resource)"};
        JComboBox<String> typeCombo = new JComboBox<>(types);
        typeCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        panel.add(typeCombo);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Points input
        panel.add(createFormLabel("POINTS AWARDED"));
        JTextField pointsField = new JTextField("1");
        pointsField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        panel.add(pointsField);
        panel.add(Box.createRigidArea(new Dimension(0, 24)));

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setBackground(CARD_BG);
        btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> modal.dispose());

        JButton saveBtn = new JButton("Save Criterion");
        saveBtn.setBackground(PRIMARY_BLUE);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> {
            // API call submission logic
            modal.dispose();
            loadSettingsData(onReload);
        });

        btnPanel.add(cancelBtn);
        btnPanel.add(saveBtn);
        panel.add(btnPanel);

        modal.add(panel, BorderLayout.CENTER);
        modal.setVisible(true);
    }

    private JLabel createFormLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        lbl.setForeground(MUTED_TEXT);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

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

    private List<CriterionRule> parseCriteriaRules(String json) {
        List<CriterionRule> list = new ArrayList<>();
        // Fallback default sample rules matching blade references
        list.add(new CriterionRule("1", "Discussions Created", "topic", "10"));
        list.add(new CriterionRule("2", "Messages Sent", "message", "2"));
        return list;
    }

    private static class CriterionRule {
        String id, name, type, points;
        CriterionRule(String id, String name, String type, String points) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.points = points;
        }
    }
}