package Lecturer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import GeneralUser.views.LoginForm;

import java.awt.*;

public class LecturerDashboard extends JFrame {

    private final Color PRIMARY_BLUE = new Color(37, 99, 235);    // #2563EB
    private final Color PAGE_BG = new Color(248, 250, 252);        // #F8FAFC
    private final Color BORDER_COLOR = new Color(226, 232, 240);   // #E2E8F0
    private final Color DARK_TEXT = new Color(15, 23, 42);         // #0F172A
    private final Color MUTED_TEXT = new Color(100, 116, 139);     // #64748B

    private String authToken;

    public LecturerDashboard(String token) {
        this.authToken = token;

        setTitle("UniForum — Lecturer Dashboard");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- SIDEBAR ---
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.WHITE);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_COLOR));
        sidebar.setPreferredSize(new Dimension(240, getHeight()));

        // Logo Header Section
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBackground(Color.WHITE);
        logoPanel.setBorder(new EmptyBorder(24, 24, 24, 24));
        logoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel logoTitle = new JLabel("UniForum");
        logoTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        logoTitle.setForeground(PRIMARY_BLUE);
        logoPanel.add(logoTitle);

        logoPanel.add(Box.createRigidArea(new Dimension(0, 4)));

        JLabel logoSubtitle = new JLabel("Lecturer Portal");
        logoSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        logoSubtitle.setForeground(MUTED_TEXT);
        logoPanel.add(logoSubtitle);

        sidebar.add(logoPanel);

        // Navigation Menu Items
        JPanel navMenu = new JPanel();
        navMenu.setLayout(new BoxLayout(navMenu, BoxLayout.Y_AXIS));
        navMenu.setBackground(Color.WHITE);
        navMenu.setBorder(new EmptyBorder(0, 16, 16, 16));
        navMenu.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel menuHeading = new JLabel("MAIN MENU");
        menuHeading.setFont(new Font("SansSerif", Font.BOLD, 11));
        menuHeading.setForeground(MUTED_TEXT);
        menuHeading.setBorder(new EmptyBorder(0, 12, 10, 0));
        menuHeading.setAlignmentX(Component.LEFT_ALIGNMENT);
        navMenu.add(menuHeading);

        navMenu.add(createSidebarNavItem("🏠", "Dashboard", true));
        navMenu.add(Box.createRigidArea(new Dimension(0, 4)));
        navMenu.add(createSidebarNavItem("💬", "Discussions", false));
        navMenu.add(Box.createRigidArea(new Dimension(0, 4)));
        navMenu.add(createSidebarNavItem("👥", "Groups", false));
        navMenu.add(Box.createRigidArea(new Dimension(0, 4)));
        navMenu.add(createSidebarNavItem("📝", "Quizzes", false));
        navMenu.add(Box.createRigidArea(new Dimension(0, 4)));
        navMenu.add(createSidebarNavItem("👨‍🎓", "Students", false));
        navMenu.add(Box.createRigidArea(new Dimension(0, 4)));
        navMenu.add(createSidebarNavItem("🔔", "Notifications", false));

        navMenu.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel accountHeading = new JLabel("ACCOUNT");
        accountHeading.setFont(new Font("SansSerif", Font.BOLD, 11));
        accountHeading.setForeground(MUTED_TEXT);
        accountHeading.setBorder(new EmptyBorder(0, 12, 10, 0));
        accountHeading.setAlignmentX(Component.LEFT_ALIGNMENT);
        navMenu.add(accountHeading);

        navMenu.add(createSidebarNavItem("⚙️", "Profile Settings", false));

        sidebar.add(navMenu);

        // Push User Profile to the Bottom of the Sidebar
        sidebar.add(Box.createVerticalGlue());

        // User Footer Panel
        JPanel userPanel = new JPanel(new BorderLayout(12, 0));
        userPanel.setBackground(Color.WHITE);
        userPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel userAvatar = new JLabel("L", JLabel.CENTER);
        userAvatar.setFont(new Font("SansSerif", Font.BOLD, 14));
        userAvatar.setForeground(Color.WHITE);
        userAvatar.setOpaque(true);
        userAvatar.setBackground(PRIMARY_BLUE);
        userAvatar.setPreferredSize(new Dimension(40, 40));
        userPanel.add(userAvatar, BorderLayout.WEST);

        JPanel userInfo = new JPanel();
        userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.Y_AXIS));
        userInfo.setBackground(Color.WHITE);

        JLabel userName = new JLabel("Lecturer");
        userName.setFont(new Font("SansSerif", Font.BOLD, 13));
        userName.setForeground(DARK_TEXT);
        userInfo.add(userName);

        JLabel userRole = new JLabel("Lecturer");
        userRole.setFont(new Font("SansSerif", Font.PLAIN, 11));
        userRole.setForeground(MUTED_TEXT);
        userInfo.add(userRole);

        userPanel.add(userInfo, BorderLayout.CENTER);
        sidebar.add(userPanel);

        add(sidebar, BorderLayout.WEST);

        // --- RIGHT CONTAINER (Top Navbar + Main Content) ---
        JPanel rightContainer = new JPanel(new BorderLayout());
        rightContainer.setBackground(PAGE_BG);

        // --- TOP NAVIGATION HEADER ---
        JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setBackground(Color.WHITE);
        topHeader.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
            new EmptyBorder(16, 24, 16, 24)
        ));

        JPanel headerTitlePanel = new JPanel();
        headerTitlePanel.setLayout(new BoxLayout(headerTitlePanel, BoxLayout.Y_AXIS));
        headerTitlePanel.setBackground(Color.WHITE);

        JLabel pageHeading = new JLabel("Dashboard");
        pageHeading.setFont(new Font("SansSerif", Font.BOLD, 18));
        pageHeading.setForeground(DARK_TEXT);
        headerTitlePanel.add(pageHeading);

        JLabel pageSubheading = new JLabel("Welcome back, Lecturer");
        pageSubheading.setFont(new Font("SansSerif", Font.PLAIN, 12));
        pageSubheading.setForeground(MUTED_TEXT);
        headerTitlePanel.add(pageSubheading);

        topHeader.add(headerTitlePanel, BorderLayout.WEST);

        // Right Header Actions (Sign Out / Logout)
        JPanel rightHeaderActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightHeaderActions.setBackground(Color.WHITE);

        JButton logoutBtn = new JButton("Sign Out");
        logoutBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        logoutBtn.setForeground(Color.RED);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });
        rightHeaderActions.add(logoutBtn);

        topHeader.add(rightHeaderActions, BorderLayout.EAST);
        rightContainer.add(topHeader, BorderLayout.NORTH);

        // --- MAIN SCROLLABLE CONTENT ---
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(PAGE_BG);
        mainContent.setBorder(new EmptyBorder(24, 28, 24, 28));

        // 1. WELCOME BANNER PANEL
        JPanel welcomeBanner = new JPanel();
        welcomeBanner.setLayout(new BoxLayout(welcomeBanner, BoxLayout.Y_AXIS));
        welcomeBanner.setBackground(PRIMARY_BLUE);
        welcomeBanner.setBorder(new EmptyBorder(24, 24, 24, 24));
        welcomeBanner.setMaximumSize(new Dimension(820, 170));
        welcomeBanner.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel welcomeTitle = new JLabel("Welcome back, Lecturer 👋");
        welcomeTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        welcomeTitle.setForeground(Color.WHITE);
        welcomeBanner.add(welcomeTitle);

        welcomeBanner.add(Box.createRigidArea(new Dimension(0, 6)));

        JTextArea welcomeDesc = new JTextArea("Manage discussions, create quizzes, monitor student participation, and guide academic conversations.");
        welcomeDesc.setFont(new Font("SansSerif", Font.PLAIN, 12));
        welcomeDesc.setForeground(new Color(219, 234, 254));
        welcomeDesc.setBackground(PRIMARY_BLUE);
        welcomeDesc.setLineWrap(true);
        welcomeDesc.setWrapStyleWord(true);
        welcomeDesc.setEditable(false);
        welcomeDesc.setMaximumSize(new Dimension(650, 40));
        welcomeDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        welcomeBanner.add(welcomeDesc);

        welcomeBanner.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton exploreBtn = new JButton("Explore Discussions");
        exploreBtn.setFont(new Font("SansSerif", Font.BOLD, 11));
        exploreBtn.setBackground(Color.WHITE);
        exploreBtn.setForeground(PRIMARY_BLUE);
        exploreBtn.setFocusPainted(false);
        exploreBtn.setBorder(new EmptyBorder(8, 14, 8, 14));
        exploreBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        welcomeBanner.add(exploreBtn);

        mainContent.add(welcomeBanner);
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        // 2. STATISTICS GRID (4 Columns)
        JPanel statsGrid = new JPanel(new GridLayout(1, 4, 12, 0));
        statsGrid.setBackground(PAGE_BG);
        statsGrid.setMaximumSize(new Dimension(820, 95));
        statsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        statsGrid.add(createStatCard("💬", "Active Discussions", "--"));
        statsGrid.add(createStatCard("👨‍🎓", "Students Engaged", "--"));
        statsGrid.add(createStatCard("📝", "Active Quizzes", "--"));
        statsGrid.add(createStatCard("⭐", "Average Participation", "--"));

        mainContent.add(statsGrid);
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        // 3. LOWER SECTION (Grid layout: Quiz Management & Analytics Side-by-Side)
        JPanel lowerGrid = new JPanel(new GridLayout(1, 2, 15, 0));
        lowerGrid.setBackground(PAGE_BG);
        lowerGrid.setMaximumSize(new Dimension(820, 230));
        lowerGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Quiz Management Card
        JPanel quizCard = createWhiteCard();
        quizCard.setLayout(new BoxLayout(quizCard, BoxLayout.Y_AXIS));

        JPanel quizHeader = new JPanel(new BorderLayout());
        quizHeader.setBackground(Color.WHITE);
        JLabel quizTitle = new JLabel("Quiz Management");
        quizTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        quizTitle.setForeground(DARK_TEXT);
        quizHeader.add(quizTitle, BorderLayout.WEST);

        JButton createQuizBtn = new JButton("Create Quiz");
        createQuizBtn.setFont(new Font("SansSerif", Font.BOLD, 10));
        createQuizBtn.setBackground(PRIMARY_BLUE);
        createQuizBtn.setForeground(Color.WHITE);
        createQuizBtn.setFocusPainted(false);
        createQuizBtn.setBorder(new EmptyBorder(5, 10, 5, 10));
        quizHeader.add(createQuizBtn, BorderLayout.EAST);
        quizCard.add(quizHeader);

        quizCard.add(Box.createRigidArea(new Dimension(0, 12)));
        quizCard.add(createQuizRow("Software Engineering Quiz", "Available: 15 July 2026", "Active", new Color(22, 163, 74)));
        quizCard.add(Box.createRigidArea(new Dimension(0, 8)));
        quizCard.add(createQuizRow("Database Systems Assessment", "Starts tomorrow", "Scheduled", new Color(202, 138, 4)));

        lowerGrid.add(quizCard);

        // Participation Analytics Card
        JPanel analyticsCard = createWhiteCard();
        analyticsCard.setLayout(new BoxLayout(analyticsCard, BoxLayout.Y_AXIS));

        JLabel analyticsTitle = new JLabel("Student Participation Overview");
        analyticsTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        analyticsTitle.setForeground(DARK_TEXT);
        analyticsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        analyticsCard.add(analyticsTitle);

        analyticsCard.add(Box.createRigidArea(new Dimension(0, 16)));
        analyticsCard.add(createProgressBar("Computer Science Group", "85%"));
        analyticsCard.add(Box.createRigidArea(new Dimension(0, 12)));
        analyticsCard.add(createProgressBar("Software Engineering Group", "72%"));

        lowerGrid.add(analyticsCard);

        mainContent.add(lowerGrid);

        // Wrap main content in a scroll pane
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setWheelScrollingEnabled(true);

        rightContainer.add(scrollPane, BorderLayout.CENTER);
        add(rightContainer, BorderLayout.CENTER);
    }

    // --- HELPER COMPONENT BUILDERS ---
    private JPanel createSidebarNavItem(String icon, String text, boolean active) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        item.setBackground(active ? PRIMARY_BLUE : Color.WHITE);
        item.setAlignmentX(Component.LEFT_ALIGNMENT);
        item.setMaximumSize(new Dimension(210, 40));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        item.add(iconLbl);

        JLabel textLbl = new JLabel(text);
        textLbl.setFont(new Font("SansSerif", active ? Font.BOLD : Font.PLAIN, 13));
        textLbl.setForeground(active ? Color.WHITE : DARK_TEXT);
        item.add(textLbl);

        return item;
    }

    private JPanel createStatCard(String emoji, String labelText, String valueText) {
        JPanel card = createWhiteCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel iconLbl = new JLabel(emoji);
        iconLbl.setFont(new Font("SansSerif", Font.PLAIN, 18));
        iconLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(iconLbl);

        card.add(Box.createRigidArea(new Dimension(0, 4)));

        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lbl.setForeground(MUTED_TEXT);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lbl);

        JLabel val = new JLabel(valueText);
        val.setFont(new Font("SansSerif", Font.BOLD, 18));
        val.setForeground(DARK_TEXT);
        val.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(val);

        return card;
    }

    private JPanel createWhiteCard() {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(12, 12, 12, 12)
        ));
        return card;
    }

    private JPanel createQuizRow(String title, String date, String status, Color statusColor) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(new Color(248, 250, 252));
        row.setBorder(new EmptyBorder(6, 8, 6, 8));

        JPanel leftText = new JPanel();
        leftText.setLayout(new BoxLayout(leftText, BoxLayout.Y_AXIS));
        leftText.setBackground(new Color(248, 250, 252));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        titleLbl.setForeground(DARK_TEXT);
        leftText.add(titleLbl);

        JLabel dateLbl = new JLabel(date);
        dateLbl.setFont(new Font("SansSerif", Font.PLAIN, 10));
        dateLbl.setForeground(MUTED_TEXT);
        leftText.add(dateLbl);

        row.add(leftText, BorderLayout.WEST);

        JLabel statusLbl = new JLabel(status);
        statusLbl.setFont(new Font("SansSerif", Font.BOLD, 10));
        statusLbl.setForeground(statusColor);
        row.add(statusLbl, BorderLayout.EAST);

        return row;
    }

    private JPanel createProgressBar(String groupName, String percentage) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        JLabel nameLbl = new JLabel(groupName);
        nameLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        nameLbl.setForeground(DARK_TEXT);
        header.add(nameLbl, BorderLayout.WEST);

        JLabel percLbl = new JLabel(percentage);
        percLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        percLbl.setForeground(MUTED_TEXT);
        header.add(percLbl, BorderLayout.EAST);

        panel.add(header);
        panel.add(Box.createRigidArea(new Dimension(0, 4)));

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(Integer.parseInt(percentage.replace("%", "")));
        bar.setForeground(PRIMARY_BLUE);
        bar.setBackground(new Color(226, 232, 240));
        bar.setMaximumSize(new Dimension(400, 8));
        bar.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(bar);

        return panel;
    }

}