package GeneralUser.views.Groups;

import GeneralUser.api.ApiClient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CreateGroupView extends JPanel {

    // --- Tailwind Slate & Blue Palette (Matching Blade Theme) ---
    private final Color PRIMARY_BLUE  = new Color(37, 99, 235);    // #2563EB (blue-600)
    private final Color PRIMARY_HOVER = new Color(29, 78, 216);    // #1D4ED8 (blue-700)
    private final Color PAGE_BG       = new Color(248, 250, 252);  // #F8FAFC (slate-50)
    private final Color BORDER_COLOR  = new Color(226, 232, 240);  // #E2E8F0 (slate-200)
    private final Color DARK_TEXT     = new Color(15, 23, 42);     // #0F172A (slate-900)
    private final Color LABEL_TEXT    = new Color(51, 65, 85);     // #334155 (slate-700)
    private final Color MUTED_TEXT    = new Color(100, 116, 139);  // #64748B (slate-500)
    private final Color CANCEL_BG     = new Color(241, 245, 249);  // #F1F5F9 (slate-100)
    private final Color CANCEL_HOVER  = new Color(226, 232, 240);  // #E2E8F0 (slate-200)

    private String authToken;
    private JTextField groupNameField;
    private JTextArea descriptionArea;
    private Runnable onGroupCreated;
    private Runnable onCancelClicked;

    public CreateGroupView(String token, Runnable onGroupCreated, Runnable onCancelClicked) {
        this.authToken = token;
        this.onGroupCreated = onGroupCreated;
        this.onCancelClicked = onCancelClicked;

        setLayout(new BorderLayout());
        setBackground(PAGE_BG);

        // Enable Anti-Aliasing for crisp modern text and shapes
        System.setProperty("awt.useSystemAAFontSettings", "lcd");
        System.setProperty("swing.aatext", "true");

        // Outer Panel
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(PAGE_BG);

        // --- 1. HEADER SECTION (Blade Template Header) ---
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
            new EmptyBorder(28, 32, 28, 32)
        ));

        // Max Width Wrapper for Header Content
        JPanel headerContent = new JPanel();
        headerContent.setLayout(new BoxLayout(headerContent, BoxLayout.Y_AXIS));
        headerContent.setBackground(Color.WHITE);
        headerContent.setMaximumSize(new Dimension(800, Integer.MAX_VALUE));
        headerContent.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Back Link Button
        JButton backBtn = new JButton("←  Back to Groups");
        backBtn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        backBtn.setForeground(PRIMARY_BLUE);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.addActionListener(e -> {
            if (onCancelClicked != null) onCancelClicked.run();
        });
        headerContent.add(backBtn);

        headerContent.add(Box.createRigidArea(new Dimension(0, 16)));

        // Title
        JLabel titleLbl = new JLabel("Create a New Group");
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLbl.setForeground(DARK_TEXT);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerContent.add(titleLbl);

        headerContent.add(Box.createRigidArea(new Dimension(0, 8)));

        // Subtitle
        JLabel subLbl = new JLabel("Create an academic discussion group and invite other members to collaborate.");
        subLbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subLbl.setForeground(MUTED_TEXT);
        subLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerContent.add(subLbl);

        headerPanel.add(headerContent);
        outerPanel.add(headerPanel, BorderLayout.NORTH);

        // --- 2. FORM CONTAINER SECTION ---
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        bodyPanel.setBackground(PAGE_BG);
        bodyPanel.setBorder(new EmptyBorder(32, 32, 32, 32));

        // Centered form wrapper matching max-w-5xl styling
        JPanel formCardWrapper = new JPanel();
        formCardWrapper.setLayout(new BoxLayout(formCardWrapper, BoxLayout.Y_AXIS));
        formCardWrapper.setOpaque(false);
        formCardWrapper.setMaximumSize(new Dimension(800, Integer.MAX_VALUE));
        formCardWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedPanel formCard = new RoundedPanel(16, Color.WHITE, BORDER_COLOR);
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBorder(new EmptyBorder(32, 32, 32, 32));
        formCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Group Information Heading
        JLabel sectionTitle = new JLabel("Group Information");
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        sectionTitle.setForeground(DARK_TEXT);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(sectionTitle);

        formCard.add(Box.createRigidArea(new Dimension(0, 24)));

        // --- Field 1: Group Name ---
        JLabel nameLabel = new JLabel("Group Name");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        nameLabel.setForeground(LABEL_TEXT);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(nameLabel);

        formCard.add(Box.createRigidArea(new Dimension(0, 8)));

        groupNameField = new JTextField();
        groupNameField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        groupNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        groupNameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        groupNameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(10, 14, 10, 14)
        ));

        // Field Placeholder setup
        setPlaceholder(groupNameField, "e.g. Software Engineering Year 2");
        formCard.add(groupNameField);

        formCard.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- Field 2: Description ---
        JLabel descLabel = new JLabel("Description");
        descLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        descLabel.setForeground(LABEL_TEXT);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(descLabel);

        formCard.add(Box.createRigidArea(new Dimension(0, 8)));

        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(new EmptyBorder(10, 12, 10, 12));

        // Area Placeholder setup
        setPlaceholderArea(descriptionArea, "Describe the purpose of this group...");

        JScrollPane descScroll = new JScrollPane(
            descriptionArea,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        descScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        descScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        descScroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1, true));
        formCard.add(descScroll);

        formCard.add(Box.createRigidArea(new Dimension(0, 32)));

        // --- Actions Toolbar (Cancel & Create Group) ---
        JPanel actionRow = new JPanel();
        actionRow.setLayout(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        actionRow.setOpaque(false);
        actionRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        actionRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Cancel Button
        JButton cancelBtn = createRoundedButton("Cancel", CANCEL_BG, LABEL_TEXT, BORDER_COLOR);
        cancelBtn.addActionListener(e -> {
            if (onCancelClicked != null) onCancelClicked.run();
        });
        actionRow.add(cancelBtn);

        // Submit Button
        JButton submitBtn = createRoundedButton("Create Group", PRIMARY_BLUE, Color.WHITE, null);
        submitBtn.addActionListener(e -> submitGroupCreation());
        actionRow.add(submitBtn);

        formCard.add(actionRow);

        formCardWrapper.add(formCard);
        bodyPanel.add(formCardWrapper);

        JScrollPane mainScroll = new JScrollPane(bodyPanel);
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        outerPanel.add(mainScroll, BorderLayout.CENTER);

        add(outerPanel, BorderLayout.CENTER);
    }

    private void submitGroupCreation() {
        String name = groupNameField.getText().trim();
        String description = descriptionArea.getText().trim();

        // Clear out text if user didn't modify placeholder
        if (name.equals("e.g. Software Engineering Year 2")) name = "";
        if (description.equals("Describe the purpose of this group...")) description = "";

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a group name.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Construct JSON payload for Laravel backend
        String jsonPayload = String.format("{\"group_name\":\"%s\",\"description\":\"%s\"}", 
            name.replace("\"", "\\\""), 
            description.replace("\"", "\\\"").replace("\n", " ")
        );

        new Thread(() -> {
            // Sends POST request to Laravel Route::post('/groups', ...) endpoint
            String response = ApiClient.post("/groups", jsonPayload, authToken);

            SwingUtilities.invokeLater(() -> {
                if (response != null && (response.startsWith("200") || response.startsWith("201") || response.contains("success"))) {
                    JOptionPane.showMessageDialog(this, "Group created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    if (onGroupCreated != null) {
                        onGroupCreated.run();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to create group. Server response: " + response, "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        }).start();
    }

    // --- UI HELPERS ---

    private void setPlaceholder(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(MUTED_TEXT);
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(DARK_TEXT);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(MUTED_TEXT);
                    field.setText(placeholder);
                }
            }
        });
    }

    private void setPlaceholderArea(JTextArea area, String placeholder) {
        area.setText(placeholder);
        area.setForeground(MUTED_TEXT);
        area.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (area.getText().equals(placeholder)) {
                    area.setText("");
                    area.setForeground(DARK_TEXT);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (area.getText().isEmpty()) {
                    area.setForeground(MUTED_TEXT);
                    area.setText(placeholder);
                }
            }
        });
    }

    private JButton createRoundedButton(String text, Color bg, Color fg, Color border) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                if (border != null) {
                    g2.setColor(border);
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(new EmptyBorder(12, 22, 12, 22));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (bg.equals(PRIMARY_BLUE)) btn.setBackground(PRIMARY_HOVER);
                else if (bg.equals(CANCEL_BG)) btn.setBackground(CANCEL_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
            }
        });

        return btn;
    }

    // --- CUSTOM ROUNDED PANEL CONTAINER CLASS ---
    private static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color bgColor;
        private final Color borderColor;

        public RoundedPanel(int radius, Color bgColor, Color borderColor) {
            this.radius = radius;
            this.bgColor = bgColor;
            this.borderColor = borderColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

            if (borderColor != null) {
                g2.setColor(borderColor);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }
}