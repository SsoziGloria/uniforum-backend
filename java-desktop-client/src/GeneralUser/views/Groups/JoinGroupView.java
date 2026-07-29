package GeneralUser.views.Groups;

import GeneralUser.api.ApiClient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JoinGroupView extends JPanel {

    // --- Modern Slate & Blue Palette (Matching Blade UI) ---
    private final Color PRIMARY_BLUE   = new Color(37, 99, 235);   // #2563EB (blue-600)
    private final Color PRIMARY_HOVER  = new Color(29, 78, 216);   // #1D4ED8 (blue-700)
    private final Color PAGE_BG        = new Color(248, 250, 252); // #F8FAFC (slate-50)
    private final Color BORDER_COLOR   = new Color(226, 232, 240); // #E2E8F0 (slate-200)
    private final Color DARK_TEXT      = new Color(15, 23, 42);    // #0F172A (slate-900)
    private final Color BODY_TEXT      = new Color(71, 85, 105);   // #475569 (slate-600)
    private final Color MUTED_TEXT     = new Color(100, 116, 139); // #64748B (slate-500)
    private final Color CANCEL_BG      = new Color(241, 245, 249); // #F1F5F9 (slate-100)
    private final Color CANCEL_HOVER   = new Color(226, 232, 240); // #E2E8F0 (slate-200)

    // Tag Colors
    private final Color BLUE_TAG_BG    = new Color(219, 234, 254); // #DBEAFE (blue-100)
    private final Color BLUE_TAG_TEXT  = new Color(29, 78, 216);   // #1D4ED8 (blue-700)

    public JoinGroupView(int groupId, String groupTag, String groupName, String groupDescription,
                         String authToken, Runnable onBackClicked, Runnable onSuccessfullyJoined) {
        
        setLayout(new BorderLayout());
        setBackground(PAGE_BG);

        // Enable crisp subpixel text antialiasing
        System.setProperty("awt.useSystemAAFontSettings", "lcd");
        System.setProperty("swing.aatext", "true");

        // Main outer container
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(PAGE_BG);

        // --- 1. HEADER SECTION ---
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
            new EmptyBorder(28, 32, 28, 32)
        ));

        JPanel headerContent = new JPanel();
        headerContent.setLayout(new BoxLayout(headerContent, BoxLayout.Y_AXIS));
        headerContent.setBackground(Color.WHITE);
        headerContent.setMaximumSize(new Dimension(850, Integer.MAX_VALUE));
        headerContent.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Back Link
        JButton backBtn = new JButton("←  Back to Browse Groups");
        backBtn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        backBtn.setForeground(PRIMARY_BLUE);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.addActionListener(e -> {
            if (onBackClicked != null) onBackClicked.run();
        });
        headerContent.add(backBtn);

        headerContent.add(Box.createRigidArea(new Dimension(0, 12)));

        // Page Title
        JLabel titleLbl = new JLabel("Join Discussion Group");
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLbl.setForeground(DARK_TEXT);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerContent.add(titleLbl);

        headerContent.add(Box.createRigidArea(new Dimension(0, 6)));

        // Subtitle
        JLabel subLbl = new JLabel("Please review the group information and rules before joining.");
        subLbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subLbl.setForeground(MUTED_TEXT);
        subLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerContent.add(subLbl);

        headerPanel.add(headerContent);
        outerPanel.add(headerPanel, BorderLayout.NORTH);

        // --- 2. MAIN CARD SECTION ---
        JPanel contentContainer = new JPanel();
        contentContainer.setLayout(new BoxLayout(contentContainer, BoxLayout.Y_AXIS));
        contentContainer.setBackground(PAGE_BG);
        contentContainer.setBorder(new EmptyBorder(32, 32, 32, 32));

        RoundedPanel card = new RoundedPanel(16, Color.WHITE, BORDER_COLOR);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(32, 32, 32, 32));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(850, Integer.MAX_VALUE));

        // Group Tag / Badge
        String displayTag = (groupTag != null && !groupTag.trim().isEmpty()) ? groupTag : "Academic Group";
        JLabel badge = new JLabel(displayTag);
        badge.setFont(new Font("SansSerif", Font.BOLD, 11));
        badge.setForeground(BLUE_TAG_TEXT);
        badge.setOpaque(true);
        badge.setBackground(BLUE_TAG_BG);
        badge.setBorder(new EmptyBorder(4, 10, 4, 10));
        badge.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(badge);

        card.add(Box.createRigidArea(new Dimension(0, 16)));

        // Group Name
        JLabel nameLbl = new JLabel(groupName);
        nameLbl.setFont(new Font("SansSerif", Font.BOLD, 24));
        nameLbl.setForeground(DARK_TEXT);
        nameLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(nameLbl);

        card.add(Box.createRigidArea(new Dimension(0, 10)));

        // Group Description
        String safeDesc = (groupDescription != null && !groupDescription.trim().isEmpty()) 
            ? groupDescription 
            : "No description provided.";
            
        JLabel descLbl = new JLabel("<html><body style='width: 720px; color: #475569;'>" + safeDesc + "</body></html>");
        descLbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(descLbl);

        card.add(Box.createRigidArea(new Dimension(0, 32)));

        // --- GROUP RULES SECTION ---
        JLabel rulesHeader = new JLabel("Group Rules");
        rulesHeader.setFont(new Font("SansSerif", Font.BOLD, 18));
        rulesHeader.setForeground(DARK_TEXT);
        rulesHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(rulesHeader);

        card.add(Box.createRigidArea(new Dimension(0, 12)));

        RoundedPanel rulesBox = new RoundedPanel(12, PAGE_BG, BORDER_COLOR);
        rulesBox.setLayout(new BoxLayout(rulesBox, BoxLayout.Y_AXIS));
        rulesBox.setBorder(new EmptyBorder(20, 24, 20, 24));
        rulesBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        rulesBox.setMaximumSize(new Dimension(780, Integer.MAX_VALUE));

        String[] rules = {
            "Respect all members of the discussion group.",
            "Share only academic and relevant content.",
            "Avoid offensive, abusive or inappropriate language.",
            "Do not spam or flood discussions.",
            "Follow all instructions provided by group administrators."
        };

        for (int i = 0; i < rules.length; i++) {
            JLabel ruleLbl = new JLabel("•  " + rules[i]);
            ruleLbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
            ruleLbl.setForeground(BODY_TEXT);
            ruleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            rulesBox.add(ruleLbl);
            if (i < rules.length - 1) {
                rulesBox.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        card.add(rulesBox);

        card.add(Box.createRigidArea(new Dimension(0, 28)));

        // --- AGREEMENT CHECKBOX ---
        JCheckBox agreeCheckbox = new JCheckBox(" I have read, understood and agree to follow the rules of this discussion group.");
        agreeCheckbox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        agreeCheckbox.setForeground(DARK_TEXT);
        agreeCheckbox.setOpaque(false);
        agreeCheckbox.setFocusPainted(false);
        agreeCheckbox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        agreeCheckbox.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(agreeCheckbox);

        card.add(Box.createRigidArea(new Dimension(0, 32)));

        // --- BUTTONS ROW ---
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        btnRow.setOpaque(false);
        btnRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRow.setMaximumSize(new Dimension(780, 48));

        JButton cancelBtn = createRoundedButton("Cancel", CANCEL_BG, DARK_TEXT, BORDER_COLOR);
        cancelBtn.addActionListener(e -> {
            if (onBackClicked != null) onBackClicked.run();
        });

        JButton joinBtn = createRoundedButton("Join Group", PRIMARY_BLUE, Color.WHITE, null);
        joinBtn.addActionListener(e -> {
            if (!agreeCheckbox.isSelected()) {
                JOptionPane.showMessageDialog(
                    this, 
                    "You must agree to follow the rules before joining this group.", 
                    "Agreement Required", 
                    JOptionPane.WARNING_MESSAGE
                );
            } else {
                String requestBody = "{\"rules_accepted\": true}";
                String response = ApiClient.post("/groups/" + groupId + "/join", requestBody, authToken);

                if (response != null && (response.startsWith("200") || response.startsWith("201") || response.contains("success"))) {
                    JOptionPane.showMessageDialog(this, "Successfully joined group!");
                    if (onSuccessfullyJoined != null) {
                        onSuccessfullyJoined.run();
                    }
                } else {
                    JOptionPane.showMessageDialog(
                        this, 
                        "Failed to join group. Server response: " + response, 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        btnRow.add(cancelBtn);
        btnRow.add(joinBtn);
        card.add(btnRow);

        contentContainer.add(card);

        JScrollPane scrollPane = new JScrollPane(contentContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        outerPanel.add(scrollPane, BorderLayout.CENTER);

        add(outerPanel, BorderLayout.CENTER);
    }

    private JButton createRoundedButton(String text, Color bg, Color fg, Color border) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                if (border != null) {
                    g2.setColor(border);
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
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
        btn.setBorder(new EmptyBorder(12, 24, 12, 24));
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