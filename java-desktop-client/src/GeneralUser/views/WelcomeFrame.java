package GeneralUser.views;

import javax.swing.*;
import java.awt.*;

public class WelcomeFrame extends JFrame {

    // --- COLOR PALETTE ---
    private final Color PRIMARY_BLUE = new Color(37, 99, 235);     // #2563EB
    private final Color LIGHT_BLUE_BG = new Color(239, 246, 255);  // #EFF6FF
    private final Color BORDER_COLOR = new Color(226, 232, 240);   // #E2E8F0
    private final Color DARK_TEXT = new Color(15, 23, 42);         // #0F172A
    private final Color MUTED_TEXT = new Color(100, 116, 139);     // #64748B
    private final Color PAGE_BG = new Color(248, 250, 252);        // #F8FAFC

    public WelcomeFrame() {
        setTitle("UniForum — Where University Minds Connect");
        setSize(1150, 750);
        setMinimumSize(new Dimension(900, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // System font antialiasing for clean text rendering
        System.setProperty("awt.useSystemAAFontSettings", "lcd");
        System.setProperty("swing.aatext", "true");

        // Main background panel with full-page scroll capability
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(PAGE_BG);

        JPanel contentContainer = new JPanel();
        contentContainer.setLayout(new BoxLayout(contentContainer, BoxLayout.Y_AXIS));
        contentContainer.setBackground(PAGE_BG);
        contentContainer.setBorder(BorderFactory.createEmptyBorder(28, 48, 36, 48));

        // Add UI sections
        contentContainer.add(createHeaderBar());
        contentContainer.add(Box.createRigidArea(new Dimension(0, 28)));
        contentContainer.add(createHeroSection());
        contentContainer.add(Box.createRigidArea(new Dimension(0, 28)));
        contentContainer.add(createFeaturesGridSection());
        contentContainer.add(Box.createRigidArea(new Dimension(0, 28)));
        contentContainer.add(createCallToActionCard());

        JScrollPane scrollPane = new JScrollPane(contentContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        outerPanel.add(scrollPane, BorderLayout.CENTER);

        add(outerPanel);
    }

    // --- 1. TOP HEADER ---
    private JPanel createHeaderBar() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Logo
        JLabel logo = new JLabel("🎓 UniForum");
        logo.setFont(new Font("SansSerif", Font.BOLD, 22));
        logo.setForeground(PRIMARY_BLUE);
        header.add(logo, BorderLayout.WEST);

        // Actions
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        actionPanel.setOpaque(false);

        JButton signInBtn = createOutlineButton("Sign In");
        signInBtn.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });

        JButton registerBtn = createPrimaryButton("Get Started");
        registerBtn.addActionListener(e -> {
            new RegisterForm().setVisible(true);
            dispose();
        });

        actionPanel.add(signInBtn);
        actionPanel.add(registerBtn);
        header.add(actionPanel, BorderLayout.EAST);

        return header;
    }

    // --- 2. HERO SECTION ---
    private JPanel createHeroSection() {
        JPanel hero = new JPanel(new GridBagLayout());
        hero.setOpaque(false);
        hero.setAlignmentX(Component.CENTER_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Left Content Column
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        RoundedPanel badge = new RoundedPanel(12, LIGHT_BLUE_BG, new Color(191, 219, 254));
        badge.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 4));
        badge.setMaximumSize(new Dimension(280, 28));
        badge.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel badgeText = new JLabel("⭐  AI-Powered Academic Collaboration");
        badgeText.setFont(new Font("SansSerif", Font.BOLD, 11));
        badgeText.setForeground(PRIMARY_BLUE);
        badge.add(badgeText);
        leftPanel.add(badge);

        leftPanel.add(Box.createRigidArea(new Dimension(0, 16)));

        JLabel title1 = new JLabel("Where university");
        title1.setFont(new Font("SansSerif", Font.BOLD, 32));
        title1.setForeground(DARK_TEXT);
        title1.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(title1);

        JLabel title2 = new JLabel("minds connect");
        title2.setFont(new Font("SansSerif", Font.BOLD, 32));
        title2.setForeground(PRIMARY_BLUE);
        title2.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(title2);

        JLabel title3 = new JLabel("and grow together.");
        title3.setFont(new Font("SansSerif", Font.BOLD, 32));
        title3.setForeground(DARK_TEXT);
        title3.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(title3);

        leftPanel.add(Box.createRigidArea(new Dimension(0, 14)));

        JLabel subtitle = new JLabel("<html><body style='width:380px;'>A collaborative platform built for students and lecturers, powered by AI to keep every conversation meaningful.</body></html>");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(MUTED_TEXT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(subtitle);

        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel heroBtnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        heroBtnRow.setOpaque(false);
        heroBtnRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton startBtn = createPrimaryButton("Get Started →");
        startBtn.addActionListener(e -> {
            new RegisterForm().setVisible(true);
            dispose();
        });

        JButton loginBtn = createOutlineButton("Sign In");
        loginBtn.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });

        heroBtnRow.add(startBtn);
        heroBtnRow.add(loginBtn);
        leftPanel.add(heroBtnRow);

        gbc.gridx = 0;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(0, 0, 0, 16);
        hero.add(leftPanel, gbc);

        // Right Mockup Card Column
        RoundedPanel rightCard = new RoundedPanel(18, Color.WHITE, BORDER_COLOR);
        rightCard.setLayout(new BorderLayout());

        // Card Top Banner
        RoundedPanel cardHeader = new RoundedPanel(18, PRIMARY_BLUE, PRIMARY_BLUE);
        cardHeader.setLayout(new FlowLayout(FlowLayout.LEFT, 14, 10));
        JLabel cardHeaderTitle = new JLabel("Advanced Algorithms — Week 7 Discussion");
        cardHeaderTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        cardHeaderTitle.setForeground(Color.WHITE);
        cardHeader.add(cardHeaderTitle);

        rightCard.add(cardHeader, BorderLayout.NORTH);

        // Card Content
        JPanel cardBody = new JPanel();
        cardBody.setLayout(new BoxLayout(cardBody, BoxLayout.Y_AXIS));
        cardBody.setOpaque(false);
        cardBody.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));

        JLabel post1 = new JLabel("<html><b>Dr. Sarah Chen</b> <font color='#2563EB'>[Lecturer]</font><br><span style='color:#475569;'>Can anyone explain Dijkstra's algorithm time complexity?</span></html>");
        post1.setFont(new Font("SansSerif", Font.PLAIN, 11));
        cardBody.add(post1);

        cardBody.add(Box.createRigidArea(new Dimension(0, 12)));

        JLabel post2 = new JLabel("<html><b>James Mitchell</b> <font color='#64748B'>[Student]</font><br><span style='color:#475569;'>Dijkstra runs in O((V + E) log V) with a min-heap.</span> <font color='#16A34A'><b>✓ Best Answer</b></font></html>");
        post2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        cardBody.add(post2);

        cardBody.add(Box.createRigidArea(new Dimension(0, 14)));

        RoundedPanel aiBox = new RoundedPanel(12, LIGHT_BLUE_BG, new Color(191, 219, 254));
        aiBox.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 8));
        JLabel aiText = new JLabel("🧠 AI suggested 3 graph algorithm resources for this thread");
        aiText.setFont(new Font("SansSerif", Font.BOLD, 10));
        aiText.setForeground(PRIMARY_BLUE);
        aiBox.add(aiText);

        cardBody.add(aiBox);
        rightCard.add(cardBody, BorderLayout.CENTER);

        gbc.gridx = 1;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(0, 16, 0, 0);
        hero.add(rightCard, gbc);

        return hero;
    }

    // --- 3. FEATURES GRID (Removed Header Words) ---
    private JPanel createFeaturesGridSection() {
        JPanel grid = new JPanel(new GridLayout(2, 3, 16, 16));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.CENTER_ALIGNMENT);

        grid.add(createFeatureCard("💬", "Threaded Discussions", "Course forums with upvoting, best-answer tags, and instructor pinning."));
        grid.add(createFeatureCard("🧠", "AI Recommendations", "Personalized resource and discussion suggestions powered by learning patterns."));
        grid.add(createFeatureCard("👥", "Study Groups", "Create private or public groups with shared notes, polls, and tracking."));
        grid.add(createFeatureCard("📝", "Quizzes & Reviews", "Auto-graded assessments with instant feedback and performance analytics."));
        grid.add(createFeatureCard("🔔", "Notifications", "Contextual alerts for replies, deadlines, quiz results, and announcements."));
        grid.add(createFeatureCard("📊", "Group Statistics", "Engagement metrics for lecturers and participation insights for users."));

        return grid;
    }

    private JPanel createFeatureCard(String icon, String title, String desc) {
        RoundedPanel card = new RoundedPanel(16, Color.WHITE, BORDER_COLOR);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel iconAndTitle = new JLabel(icon + "  " + title);
        iconAndTitle.setFont(new Font("SansSerif", Font.BOLD, 13));
        iconAndTitle.setForeground(DARK_TEXT);
        iconAndTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(iconAndTitle);

        card.add(Box.createRigidArea(new Dimension(0, 6)));

        JLabel descLbl = new JLabel("<html><body style='width:220px; color:#64748B;'>" + desc + "</body></html>");
        descLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        descLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(descLbl);

        return card;
    }

    // --- 4. CALL TO ACTION BAR ---
    private JPanel createCallToActionCard() {
        RoundedPanel cta = new RoundedPanel(20, PRIMARY_BLUE, PRIMARY_BLUE);
        cta.setLayout(new BorderLayout());
        cta.setBorder(BorderFactory.createEmptyBorder(22, 28, 22, 28));
        cta.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel ctaText = new JLabel("Ready to transform how your university collaborates?");
        ctaText.setFont(new Font("SansSerif", Font.BOLD, 15));
        ctaText.setForeground(Color.WHITE);
        cta.add(ctaText, BorderLayout.WEST);

        JButton ctaButton = new JButton("Create Account →") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        ctaButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        ctaButton.setForeground(PRIMARY_BLUE);
        ctaButton.setFocusPainted(false);
        ctaButton.setContentAreaFilled(false);
        ctaButton.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        ctaButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ctaButton.addActionListener(e -> {
            new RegisterForm().setVisible(true);
            dispose();
        });

        cta.add(ctaButton, BorderLayout.EAST);
        return cta;
    }

    // --- CUSTOM BUTTON FACTORIES ---
    private JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(PRIMARY_BLUE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createOutlineButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setForeground(DARK_TEXT);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // --- HELPER COMPONENT FOR ROUNDED CONTAINERS ---
    private static class RoundedPanel extends JPanel {
        private final int cornerRadius;
        private final Color backgroundColor;
        private final Color borderColor;

        public RoundedPanel(int radius, Color bgColor, Color bColor) {
            super();
            this.cornerRadius = radius;
            this.backgroundColor = bgColor;
            this.borderColor = bColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D graphics = (Graphics2D) g.create();
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Background
            graphics.setColor(backgroundColor);
            graphics.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

            // Border
            if (borderColor != null) {
                graphics.setColor(borderColor);
                graphics.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
            }
            graphics.dispose();
        }
    }

    // --- ENTRY POINT ---
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new WelcomeFrame().setVisible(true);
        });
    }
}