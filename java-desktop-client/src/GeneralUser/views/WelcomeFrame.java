package GeneralUser.views;

import GeneralUser.views.LoginForm;
import GeneralUser.views.RegisterForm;

// ... rest of your imports and class code

import javax.swing.*;

import java.awt.*;

public class WelcomeFrame extends JFrame {

    // --- DESIGN CONSTANTS  ---
    private final Color PRIMARY_BLUE = new Color(37, 99, 235);   // Exact web primary blue (#1D4ED8)
    private final Color LIGHT_BLUE_BG = new Color(239, 246, 255);  // Soft accent tint (#EFF6FF)
    private final Color DARK_TEXT = new Color(15, 23, 42);         // Main heading text color
    private final Color MUTED_TEXT = new Color(100, 116, 139);     // Description paragraph color
    private final Color PAGE_BG = new Color(248, 250, 252);        // Background canvas color

    public WelcomeFrame() {
        setTitle("UniForum — Where University Minds Connect");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- 1. TOP NAVIGATION BAR ---
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(Color.WHITE);
        navPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel brandLabel = new JLabel("UniForum");
        brandLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        brandLabel.setForeground(PRIMARY_BLUE);
        navPanel.add(brandLabel, BorderLayout.WEST);

        // Top right action links
        JPanel navRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        navRightPanel.setBackground(Color.WHITE);
        
        JButton navSignIn = new JButton("Sign In");
        navSignIn.setFont(new Font("SansSerif", Font.BOLD, 12));
        navSignIn.setForeground(DARK_TEXT);
        navSignIn.setContentAreaFilled(false);
        navSignIn.setBorderPainted(false);

        JButton navGetStarted = new JButton("Get Started");
        navGetStarted.setFont(new Font("SansSerif", Font.BOLD, 12));
        navGetStarted.setBackground(PRIMARY_BLUE);
        navGetStarted.setForeground(Color.WHITE);
        navGetStarted.setFocusPainted(false);
        navGetStarted.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        navRightPanel.add(navSignIn);
        navRightPanel.add(navGetStarted);
        navPanel.add(navRightPanel, BorderLayout.EAST);

        add(navPanel, BorderLayout.NORTH);

        // --- 2. HERO SECTION (Split 2 Columns: Left Text, Right Mockup Card) ---
        JPanel heroPanel = new JPanel(new GridLayout(1, 2, 40, 0));
        heroPanel.setBackground(PAGE_BG);
        heroPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        // --- LEFT CONTENT PANEL ---
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(PAGE_BG);

        // AI Badge
        JLabel badgeLabel = new JLabel("  Now with AI-powered learning insights  ");
        badgeLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        badgeLabel.setForeground(PRIMARY_BLUE);
        badgeLabel.setBackground(LIGHT_BLUE_BG);
        badgeLabel.setOpaque(true);
        badgeLabel.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        badgeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(badgeLabel);

        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Main Headings
        JLabel titleLabel1 = new JLabel("Where university");
        titleLabel1.setFont(new Font("SansSerif", Font.BOLD, 36));
        titleLabel1.setForeground(DARK_TEXT);
        titleLabel1.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(titleLabel1);

        JLabel titleLabel2 = new JLabel("minds connect");
        titleLabel2.setFont(new Font("SansSerif", Font.BOLD, 36));
        titleLabel2.setForeground(PRIMARY_BLUE);
        titleLabel2.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(titleLabel2);

        JLabel titleLabel3 = new JLabel("and grow together.");
        titleLabel3.setFont(new Font("SansSerif", Font.BOLD, 36));
        titleLabel3.setForeground(DARK_TEXT);
        titleLabel3.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(titleLabel3);

        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Description Paragraph
        JTextArea descArea = new JTextArea("A collaborative discussion platform built for students, lecturers, and administrators, powered by AI to keep every conversation meaningful.");
        descArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descArea.setForeground(MUTED_TEXT);
        descArea.setBackground(PAGE_BG);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setMaximumSize(new Dimension(480, 70));
        descArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(descArea);

        leftPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Action Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        buttonPanel.setBackground(PAGE_BG);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton getStartedButton = new JButton("Get Started  →");
        getStartedButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        getStartedButton.setBackground(PRIMARY_BLUE);
        getStartedButton.setForeground(Color.WHITE);
        getStartedButton.setFocusPainted(false);
        getStartedButton.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JButton signInButton = new JButton("Sign in to your account");
        signInButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        signInButton.setBackground(Color.WHITE);
        signInButton.setForeground(DARK_TEXT);
        signInButton.setFocusPainted(false);
        signInButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225)),
            BorderFactory.createEmptyBorder(11, 20, 11, 20)
        ));

        buttonPanel.add(getStartedButton);
        buttonPanel.add(signInButton);
        leftPanel.add(buttonPanel);

        heroPanel.add(leftPanel);

        // --- 3. RIGHT MOCKUP CARD PANEL ---
        JPanel rightCardWrapper = new JPanel(new GridBagLayout());
        rightCardWrapper.setBackground(PAGE_BG);

        JPanel mockupCard = new JPanel();
        mockupCard.setLayout(new BoxLayout(mockupCard, BoxLayout.Y_AXIS));
        mockupCard.setBackground(Color.WHITE);
        mockupCard.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true));
        mockupCard.setPreferredSize(new Dimension(460, 420));

        // Card Header Bar
        JPanel cardHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        cardHeader.setBackground(PRIMARY_BLUE);
        JLabel cardTitle = new JLabel("Advanced Algorithms — Week 7 Discussion");
        cardTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        cardTitle.setForeground(Color.WHITE);
        cardHeader.add(cardTitle);
        mockupCard.add(cardHeader);

        // Card Body Content
        JPanel cardBody = new JPanel();
        cardBody.setLayout(new BoxLayout(cardBody, BoxLayout.Y_AXIS));
        cardBody.setBackground(Color.WHITE);
        cardBody.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Lecturer post snippet
        JTextArea post1 = new JTextArea("Dr. Sarah Chen (Lecturer)\nCan anyone explain the time complexity of Dijkstra's algorithm and when we should prefer A* instead?\n💬 8 replies   ⭐ 12 upvotes");
        post1.setFont(new Font("SansSerif", Font.PLAIN, 11));
        post1.setForeground(new Color(51, 65, 85));
        post1.setBackground(new Color(241, 245, 249));
        post1.setLineWrap(true);
        post1.setWrapStyleWord(true);
        post1.setEditable(false);
        post1.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        post1.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardBody.add(post1);

        cardBody.add(Box.createRigidArea(new Dimension(0, 10)));

        // Student reply snippet
        JTextArea post2 = new JTextArea("James Mitchell (Student)\nDijkstra's runs in O((V + E) log V). A* is preferred when we have a good heuristic function...\n✓ Best Answer");
        post2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        post2.setForeground(new Color(51, 65, 85));
        post2.setBackground(new Color(236, 253, 245)); // Soft green tint
        post2.setLineWrap(true);
        post2.setWrapStyleWord(true);
        post2.setEditable(false);
        post2.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        post2.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardBody.add(post2);

        cardBody.add(Box.createRigidArea(new Dimension(0, 10)));

        // AI Insight Box
        JLabel aiBox = new JLabel(" 🧠 AI suggests 3 related resources on graph algorithms");
        aiBox.setFont(new Font("SansSerif", Font.BOLD, 10));
        aiBox.setForeground(PRIMARY_BLUE);
        aiBox.setBackground(LIGHT_BLUE_BG);
        aiBox.setOpaque(true);
        aiBox.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        aiBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardBody.add(aiBox);

        mockupCard.add(cardBody);
        rightCardWrapper.add(mockupCard);

        heroPanel.add(rightCardWrapper);
        add(heroPanel, BorderLayout.CENTER);

        // --- 4. BUTTON EVENT LISTENERS (Connecting to your Login Form) ---
        signInButton.addActionListener(e -> openLogin());
        navSignIn.addActionListener(e -> openLogin());

        getStartedButton.addActionListener(e -> openRegister());
        navGetStarted.addActionListener(e -> openRegister());

    }

    private void openLogin() {
        new LoginForm().setVisible(true);
        dispose(); // Closes the welcome frame and opens the sign-in window
    }

    private void openRegister() {
        new RegisterForm().setVisible(true);
        dispose(); // Closes the welcome frame and opens your RegisterForm window
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new WelcomeFrame().setVisible(true);
        });
    }
}