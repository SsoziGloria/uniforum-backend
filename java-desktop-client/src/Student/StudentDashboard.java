package Student;

import GeneralUser.api.ApiClient;
import GeneralUser.views.LoginForm;
import GeneralUser.views.Groups.BrowseGroupsView;
import GeneralUser.views.Groups.ChatView;
import GeneralUser.views.Groups.CreateGroupView;
import GeneralUser.views.Groups.GroupDetailsView;
import GeneralUser.views.Groups.ManageMembersView;
import GeneralUser.views.Groups.StatisticsView;
import GeneralUser.views.Groups.JoinGroupView;
import GeneralUser.views.Groups.MainGroupsView;
import GeneralUser.views.Groups.Discussions.TopicDetailView;
import Student.Participation.ParticipationResultsView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StudentDashboard extends JFrame {

    private final Color PRIMARY_BLUE  = new Color(37, 99, 235);    
    private final Color BG_SLATE      = new Color(248, 250, 252);  
    private final Color PAGE_BG       = new Color(241, 245, 249);  
    private final Color BORDER_COLOR  = new Color(226, 232, 240);  
    private final Color DARK_TEXT     = new Color(15, 23, 42);     
    private final Color MUTED_TEXT    = new Color(100, 116, 139);  
    private final Color EMERALD_GREEN = new Color(10, 185, 129);  
    private final Color EMERALD_BG    = new Color(236, 253, 245);  

    private String authToken;
    private MainGroupsView mainGroupsView;
    private BrowseGroupsView browseGroupsView;
    private String currentUserName;

    private JPanel contentCards;
    private CardLayout cardLayout;

    private JPanel dashboardNavItem, discussionsNavItem, groupsNavItem, quizzesNavItem, aiNavItem, notifNavItem, settingsNavItem;
    private JLabel dashboardTextLbl, discussionsTextLbl, groupsTextLbl, quizzesTextLbl, aiTextLbl, notifTextLbl, settingsTextLbl;

    private JLabel lblQuestionsAsked;
    private JLabel lblParticipationScore;
    private JLabel lblTopicsAvailable;
    private JLabel lblPendingQuizzes;
    private JPanel recommendedTopicsContainer;
    private JPanel recentActivityContainer;

    public StudentDashboard(String token, String userNameInput, Runnable onCreateGroupClicked, Runnable onBrowseGroupsClicked) {
        this.authToken = token;
        this.currentUserName = (userNameInput != null && !userNameInput.trim().isEmpty()) ? userNameInput.trim() : "Student";

        System.setProperty("awt.useSystemAAFontSettings", "lcd");
        System.setProperty("swing.aatext", "true");

        Font modernFont = new Font("SansSerif", Font.PLAIN, 13);
        UIManager.put("Label.font", modernFont);
        UIManager.put("Button.font", modernFont);
        UIManager.put("TextField.font", modernFont);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        setTitle("UniForum — Student Portal");
        setSize(1240, 840);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(Color.WHITE);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_COLOR));
        sidebar.setPreferredSize(new Dimension(240, getHeight()));

        JPanel topSidebarContainer = new JPanel();
        topSidebarContainer.setLayout(new BoxLayout(topSidebarContainer, BoxLayout.Y_AXIS));
        topSidebarContainer.setBackground(Color.WHITE);

        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBackground(Color.WHITE);
        logoPanel.setBorder(new EmptyBorder(24, 24, 16, 24));
        logoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel logoTitle = new JLabel("UniForum");
        logoTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        logoTitle.setForeground(PRIMARY_BLUE);
        logoPanel.add(logoTitle);

        logoPanel.add(Box.createRigidArea(new Dimension(0, 2)));

        JLabel logoSubtitle = new JLabel("Student Portal");
        logoSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        logoSubtitle.setForeground(MUTED_TEXT);
        logoPanel.add(logoSubtitle);

        topSidebarContainer.add(logoPanel);

        JPanel navMenu = new JPanel();
        navMenu.setLayout(new BoxLayout(navMenu, BoxLayout.Y_AXIS));
        navMenu.setBackground(Color.WHITE);
        navMenu.setBorder(new EmptyBorder(0, 16, 16, 16));
        navMenu.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel menuHeading = new JLabel("MAIN MENU");
        menuHeading.setFont(new Font("SansSerif", Font.BOLD, 10));
        menuHeading.setForeground(MUTED_TEXT);
        menuHeading.setBorder(new EmptyBorder(0, 10, 8, 0));
        menuHeading.setAlignmentX(Component.LEFT_ALIGNMENT);
        navMenu.add(menuHeading);

        dashboardNavItem = createSidebarNavItem("🏠", "Dashboard", true, "DASHBOARD");
        dashboardTextLbl = (JLabel) dashboardNavItem.getComponent(1);
        navMenu.add(dashboardNavItem);
        navMenu.add(Box.createRigidArea(new Dimension(0, 2)));

        discussionsNavItem = createSidebarNavItem("💬", "Discussions", false, "DISCUSSIONS");
        discussionsTextLbl = (JLabel) discussionsNavItem.getComponent(1);
        navMenu.add(discussionsNavItem);
        navMenu.add(Box.createRigidArea(new Dimension(0, 2)));

        groupsNavItem = createSidebarNavItem("👥", "Groups", false, "GROUPS");
        groupsTextLbl = (JLabel) groupsNavItem.getComponent(1);
        navMenu.add(groupsNavItem);
        navMenu.add(Box.createRigidArea(new Dimension(0, 2)));

        quizzesNavItem = createSidebarNavItem("📝", "Quizzes", false, "QUIZZES");
        quizzesTextLbl = (JLabel) quizzesNavItem.getComponent(1);
        navMenu.add(quizzesNavItem);
        navMenu.add(Box.createRigidArea(new Dimension(0, 2)));

        aiNavItem = createSidebarNavItem("🤖", "AI Recommendations", false, "AI");
        aiTextLbl = (JLabel) aiNavItem.getComponent(1);
        navMenu.add(aiNavItem);
        navMenu.add(Box.createRigidArea(new Dimension(0, 2)));

        notifNavItem = createSidebarNavItem("🔔", "Notifications", false, "NOTIFICATIONS");
        notifTextLbl = (JLabel) notifNavItem.getComponent(1);
        navMenu.add(notifNavItem);
        navMenu.add(Box.createRigidArea(new Dimension(0, 16)));

        JLabel accountHeading = new JLabel("ACCOUNT");
        accountHeading.setFont(new Font("SansSerif", Font.BOLD, 10));
        accountHeading.setForeground(MUTED_TEXT);
        accountHeading.setBorder(new EmptyBorder(0, 10, 8, 0));
        accountHeading.setAlignmentX(Component.LEFT_ALIGNMENT);
        navMenu.add(accountHeading);

        settingsNavItem = createSidebarNavItem("⚙️", "Profile Settings", false, "SETTINGS");
        settingsTextLbl = (JLabel) settingsNavItem.getComponent(1);
        navMenu.add(settingsNavItem);

        topSidebarContainer.add(navMenu);
        sidebar.add(topSidebarContainer, BorderLayout.NORTH);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        userPanel.setBackground(Color.WHITE);
        userPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR),
            new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel userAvatar = new JLabel(String.valueOf(currentUserName.charAt(0)).toUpperCase(), JLabel.CENTER);
        userAvatar.setFont(new Font("SansSerif", Font.BOLD, 14));
        userAvatar.setForeground(Color.WHITE);
        userAvatar.setOpaque(true);
        userAvatar.setBackground(PRIMARY_BLUE);
        userAvatar.setPreferredSize(new Dimension(36, 36));
        userPanel.add(userAvatar);

        JPanel userInfo = new JPanel();
        userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.Y_AXIS));
        userInfo.setBackground(Color.WHITE);

        JLabel userNameLbl = new JLabel(currentUserName);
        userNameLbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        userNameLbl.setForeground(DARK_TEXT);
        userInfo.add(userNameLbl);

        JLabel userRole = new JLabel("Student");
        userRole.setFont(new Font("SansSerif", Font.PLAIN, 11));
        userRole.setForeground(MUTED_TEXT);
        userInfo.add(userRole);

        userPanel.add(userInfo);
        sidebar.add(userPanel, BorderLayout.SOUTH);

        add(sidebar, BorderLayout.WEST);

        JPanel rightContainer = new JPanel(new BorderLayout());
        rightContainer.setBackground(PAGE_BG);

        JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setBackground(Color.WHITE);
        topHeader.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
            new EmptyBorder(16, 32, 16, 32)
        ));

        JLabel pageHeading = new JLabel("Dashboard");
        pageHeading.setFont(new Font("SansSerif", Font.BOLD, 20));
        pageHeading.setForeground(DARK_TEXT);
        topHeader.add(pageHeading, BorderLayout.WEST);

        JPanel rightHeaderActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightHeaderActions.setBackground(Color.WHITE);

        JButton logoutBtn = new JButton("Sign Out");
        logoutBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        logoutBtn.setForeground(new Color(220, 38, 38));
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

        cardLayout = new CardLayout();
        contentCards = new JPanel(cardLayout);
        contentCards.setBackground(PAGE_BG);

        contentCards.add(createDashboardHomePanel(), "DASHBOARD");

        mainGroupsView = new MainGroupsView(
            token, 
            currentUserName,
            "Student", // accountRole
            () -> cardLayout.show(contentCards, "CREATE_GROUP"),    
            () -> cardLayout.show(contentCards, "BROWSE_GROUPS"),
            (groupId, gName) -> openChatFromMain(groupId, gName),
            (groupId, topicId) -> openTopicDetailFromMain(groupId, topicId, "GROUP_DETAILS_" + groupId)
        );
        contentCards.add(mainGroupsView, "GROUPS");

        contentCards.add(new CreateGroupView(
            authToken,
            () -> {
                mainGroupsView.refreshData();
                cardLayout.show(contentCards, "GROUPS");
            },
            () -> cardLayout.show(contentCards, "GROUPS")
        ), "CREATE_GROUP");

        browseGroupsView = new BrowseGroupsView(
            authToken,
            () -> cardLayout.show(contentCards, "GROUPS"),
            groupId -> openBrowseGroupDetails(groupId),
            groupId -> openBrowseGroupJoin(groupId)
        );
        contentCards.add(browseGroupsView, "BROWSE_GROUPS");

        contentCards.add(createPlaceholderPanel("Discussions Module"), "DISCUSSIONS");
        contentCards.add(createPlaceholderPanel("Quizzes Module"), "QUIZZES");
        contentCards.add(createPlaceholderPanel("AI Recommendations Module"), "AI");
        contentCards.add(createPlaceholderPanel("Notifications Module"), "NOTIFICATIONS");
        contentCards.add(createPlaceholderPanel("Profile Settings Module"), "SETTINGS");

        rightContainer.add(contentCards, BorderLayout.CENTER);
        add(rightContainer, BorderLayout.CENTER);

        fetchDashboardDataFromApi();
    }

    private JPanel createSidebarNavItem(String icon, String text, boolean active, String cardName) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        item.setOpaque(true);
        item.setBackground(active ? PRIMARY_BLUE : Color.WHITE);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        item.setAlignmentX(Component.LEFT_ALIGNMENT);
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setForeground(active ? Color.WHITE : DARK_TEXT);
        item.add(iconLbl);

        JLabel textLbl = new JLabel(text);
        textLbl.setFont(new Font("SansSerif", active ? Font.BOLD : Font.PLAIN, 13));
        textLbl.setForeground(active ? Color.WHITE : DARK_TEXT);
        item.add(textLbl);

        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(contentCards, cardName);
                setActiveNavItem(cardName);
                if (cardName.equals("BROWSE_GROUPS") && browseGroupsView != null) {
                    browseGroupsView.refreshData();
                } else if (cardName.equals("GROUPS") && mainGroupsView != null) {
                    mainGroupsView.refreshData();
                } else if (cardName.equals("DASHBOARD")) {
                    fetchDashboardDataFromApi();
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!item.getBackground().equals(PRIMARY_BLUE)) {
                    item.setBackground(BG_SLATE);
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (!item.getBackground().equals(PRIMARY_BLUE)) {
                    item.setBackground(Color.WHITE);
                }
            }
        });

        return item;
    }

    private void setActiveNavItem(String cardName) {
        JPanel[] items = { dashboardNavItem, discussionsNavItem, groupsNavItem, quizzesNavItem, aiNavItem, notifNavItem, settingsNavItem };
        JLabel[] textLabels = { dashboardTextLbl, discussionsTextLbl, groupsTextLbl, quizzesTextLbl, aiTextLbl, notifTextLbl, settingsTextLbl };
        String[] names = { "DASHBOARD", "DISCUSSIONS", "GROUPS", "QUIZZES", "AI", "NOTIFICATIONS", "SETTINGS" };

        for (int i = 0; i < items.length; i++) {
            if (items[i] == null || textLabels[i] == null) continue;
            boolean isActive = names[i].equals(cardName);
            items[i].setOpaque(true);
            items[i].setBackground(isActive ? PRIMARY_BLUE : Color.WHITE);
            JLabel iconLbl = (JLabel) items[i].getComponent(0);
            iconLbl.setForeground(isActive ? Color.WHITE : DARK_TEXT);
            textLabels[i].setFont(new Font("SansSerif", isActive ? Font.BOLD : Font.PLAIN, 13));
            textLabels[i].setForeground(isActive ? Color.WHITE : DARK_TEXT);
            items[i].revalidate();
            items[i].repaint();
        }
    }

    private JComponent createDashboardHomePanel() {
        JPanel outerContainer = new JPanel(new BorderLayout());
        outerContainer.setBackground(PAGE_BG);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(PAGE_BG);
        contentPanel.setBorder(new EmptyBorder(28, 32, 28, 32));

        RoundedPanel welcomeBanner = new RoundedPanel(24, PRIMARY_BLUE, null);
        welcomeBanner.setLayout(new BoxLayout(welcomeBanner, BoxLayout.Y_AXIS));
        welcomeBanner.setBorder(new EmptyBorder(32, 32, 32, 32));
        welcomeBanner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        welcomeBanner.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel welcomeTitle = new JLabel("Welcome back, " + currentUserName + " 👋");
        welcomeTitle.setFont(new Font("SansSerif", Font.BOLD, 26));
        welcomeTitle.setForeground(Color.WHITE);
        welcomeBanner.add(welcomeTitle);

        welcomeBanner.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel welcomeDesc = new JLabel("<html><div style='width: 550px; color: #DBEAFE; font-size: 13px;'>Stay connected with your university discussions, discover recommended topics, and track your group participation score.</div></html>");
        welcomeBanner.add(welcomeDesc);

        welcomeBanner.add(Box.createRigidArea(new Dimension(0, 18)));

        JButton exploreBtn = new JButton("Explore Groups →") {
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
        exploreBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        exploreBtn.setForeground(PRIMARY_BLUE);
        exploreBtn.setFocusPainted(false);
        exploreBtn.setContentAreaFilled(false);
        exploreBtn.setBorder(new EmptyBorder(10, 20, 10, 20));
        exploreBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exploreBtn.addActionListener(e -> {
            cardLayout.show(contentCards, "GROUPS");
            setActiveNavItem("GROUPS");
        });
        welcomeBanner.add(exploreBtn);

        contentPanel.add(welcomeBanner);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 24)));

        JPanel statsRow = new JPanel(new GridLayout(1, 4, 18, 0));
        statsRow.setBackground(PAGE_BG);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        statsRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblQuestionsAsked = new JLabel("0");
        lblParticipationScore = new JLabel("0%");
        lblTopicsAvailable = new JLabel("0");
        lblPendingQuizzes = new JLabel("0");

        statsRow.add(createStatCard("💬", "Questions Asked", lblQuestionsAsked, null));
        statsRow.add(createStatCard("⭐", "Participation Score", lblParticipationScore, "Active"));
        statsRow.add(createStatCard("📚", "Topics Available", lblTopicsAvailable, null));
        statsRow.add(createStatCard("📝", "Pending Quizzes", lblPendingQuizzes, null));

        contentPanel.add(statsRow);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 24)));

        JPanel mainGrid = new JPanel(new GridBagLayout());
        mainGrid.setBackground(PAGE_BG);
        mainGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        RoundedPanel recTopicsCard = new RoundedPanel(16, Color.WHITE, BORDER_COLOR);
        recTopicsCard.setLayout(new BoxLayout(recTopicsCard, BoxLayout.Y_AXIS));
        recTopicsCard.setBorder(new EmptyBorder(22, 22, 22, 22));

        JPanel recHeader = new JPanel(new BorderLayout());
        recHeader.setOpaque(false);
        JLabel recTitle = new JLabel("Recommended Topics");
        recTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        recTitle.setForeground(DARK_TEXT);

        JLabel aiBadge = new JLabel(" AI POWERED ");
        aiBadge.setFont(new Font("SansSerif", Font.BOLD, 10));
        aiBadge.setForeground(PRIMARY_BLUE);
        aiBadge.setOpaque(true);
        aiBadge.setBackground(new Color(239, 246, 255));
        aiBadge.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));

        recHeader.add(recTitle, BorderLayout.WEST);
        recHeader.add(aiBadge, BorderLayout.EAST);
        recTopicsCard.add(recHeader);
        recTopicsCard.add(Box.createRigidArea(new Dimension(0, 16)));

        recommendedTopicsContainer = new JPanel();
        recommendedTopicsContainer.setLayout(new BoxLayout(recommendedTopicsContainer, BoxLayout.Y_AXIS));
        recommendedTopicsContainer.setOpaque(false);
        recTopicsCard.add(recommendedTopicsContainer);

        gbc.gridx = 0;
        gbc.weightx = 0.65;
        gbc.insets = new Insets(0, 0, 0, 12);
        mainGrid.add(recTopicsCard, gbc);

        RoundedPanel recentActivityCard = new RoundedPanel(16, Color.WHITE, BORDER_COLOR);
        recentActivityCard.setLayout(new BoxLayout(recentActivityCard, BoxLayout.Y_AXIS));
        recentActivityCard.setBorder(new EmptyBorder(22, 22, 22, 22));

        JLabel activityHeader = new JLabel("Recent Activity");
        activityHeader.setFont(new Font("SansSerif", Font.BOLD, 18));
        activityHeader.setForeground(DARK_TEXT);
        recentActivityCard.add(activityHeader);
        recentActivityCard.add(Box.createRigidArea(new Dimension(0, 16)));

        recentActivityContainer = new JPanel();
        recentActivityContainer.setLayout(new BoxLayout(recentActivityContainer, BoxLayout.Y_AXIS));
        recentActivityContainer.setOpaque(false);
        recentActivityCard.add(recentActivityContainer);

        gbc.gridx = 1;
        gbc.weightx = 0.35;
        gbc.insets = new Insets(0, 12, 0, 0);
        mainGrid.add(recentActivityCard, gbc);

        contentPanel.add(mainGrid);

        JScrollPane scrollPane = new JScrollPane(
            contentPanel,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        outerContainer.add(scrollPane, BorderLayout.CENTER);
        return outerContainer;
    }

    private JPanel createStatCard(String icon, String label, JLabel valueLabel, String badgeText) {
        RoundedPanel card = new RoundedPanel(16, Color.WHITE, BORDER_COLOR);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("SansSerif", Font.PLAIN, 24));
        card.add(iconLbl);

        card.add(Box.createRigidArea(new Dimension(0, 8)));

        JLabel titleLbl = new JLabel(label);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        titleLbl.setForeground(MUTED_TEXT);
        card.add(titleLbl);

        card.add(Box.createRigidArea(new Dimension(0, 6)));

        if (badgeText != null) {
            JPanel valRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
            valRow.setOpaque(false);

            valueLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
            valueLabel.setForeground(DARK_TEXT);
            valRow.add(valueLabel);

            JLabel activeBadge = new JLabel(badgeText);
            activeBadge.setFont(new Font("SansSerif", Font.BOLD, 10));
            activeBadge.setForeground(EMERALD_GREEN);
            activeBadge.setOpaque(true);
            activeBadge.setBackground(EMERALD_BG);
            activeBadge.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
            valRow.add(activeBadge);

            card.add(valRow);
        } else {
            valueLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
            valueLabel.setForeground(DARK_TEXT);
            card.add(valueLabel);
        }

        return card;
    }

    private JPanel createTopicRow(String title, String subtitle, int groupId, int topicId) {
        RoundedPanel row = new RoundedPanel(12, BG_SLATE, BORDER_COLOR);
        row.setLayout(new BorderLayout());
        row.setBorder(new EmptyBorder(12, 16, 12, 16));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);

        JLabel tLbl = new JLabel(title);
        tLbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        tLbl.setForeground(DARK_TEXT);
        info.add(tLbl);

        JLabel subLbl = new JLabel(subtitle);
        subLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        subLbl.setForeground(MUTED_TEXT);
        info.add(subLbl);

        row.add(info, BorderLayout.WEST);

        JButton viewBtn = new JButton("View →");
        viewBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        viewBtn.setForeground(PRIMARY_BLUE);
        viewBtn.setBorderPainted(false);
        viewBtn.setContentAreaFilled(false);
        viewBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewBtn.addActionListener(e -> openTopicDetailFromMain(groupId, topicId, "DASHBOARD"));
        row.add(viewBtn, BorderLayout.EAST);

        return row;
    }

    private JPanel createActivityRow(String icon, String action, String time) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(8, 0, 8, 0));

        JLabel actLbl = new JLabel(icon + "  " + action);
        actLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        actLbl.setForeground(DARK_TEXT);
        row.add(actLbl, BorderLayout.WEST);

        JLabel timeLbl = new JLabel(time);
        timeLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        timeLbl.setForeground(MUTED_TEXT);
        row.add(timeLbl, BorderLayout.EAST);

        return row;
    }

    private void fetchDashboardDataFromApi() {
        new Thread(() -> {
            String response = ApiClient.get("/student/dashboard", authToken);
            if (response != null && !response.isEmpty()) {
                String qAsked = extractJsonValue(response, "questionsAskedCount");
                String score = extractJsonValue(response, "participationScore");
                String topics = extractJsonValue(response, "topicsFollowingCount");
                String quizzes = extractJsonValue(response, "pendingQuizzesCount");

                SwingUtilities.invokeLater(() -> {
                    if (!qAsked.isEmpty()) lblQuestionsAsked.setText(qAsked);
                    if (!score.isEmpty()) lblParticipationScore.setText(score + "%");
                    if (!topics.isEmpty()) lblTopicsAvailable.setText(topics);
                    if (!quizzes.isEmpty()) lblPendingQuizzes.setText(quizzes);

                    recommendedTopicsContainer.removeAll();
                    recommendedTopicsContainer.add(createTopicRow("Laravel Authentication", "General Discussion", 1, 101));
                    recommendedTopicsContainer.add(Box.createRigidArea(new Dimension(0, 8)));
                    recommendedTopicsContainer.add(createTopicRow("Machine Learning Basics", "AI & Data Science", 2, 102));
                    recommendedTopicsContainer.add(Box.createRigidArea(new Dimension(0, 8)));
                    recommendedTopicsContainer.add(createTopicRow("Software Design Patterns", "Software Engineering", 1, 103));
                    recommendedTopicsContainer.revalidate();
                    recommendedTopicsContainer.repaint();

                    recentActivityContainer.removeAll();
                    recentActivityContainer.add(createActivityRow("💬", "Asked in Laravel Discussion", "2 hrs ago"));
                    recentActivityContainer.add(Box.createRigidArea(new Dimension(0, 10)));
                    recentActivityContainer.add(createActivityRow("⭐", "Earned +15 participation score", "Yesterday"));
                    recentActivityContainer.add(Box.createRigidArea(new Dimension(0, 10)));
                    recentActivityContainer.add(createActivityRow("📝", "Submitted Software Eng Quiz", "3 days ago"));
                    recentActivityContainer.revalidate();
                    recentActivityContainer.repaint();
                });
            }
        }).start();
    }

    private JComponent createPlaceholderPanel(String title) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(PAGE_BG);
        JLabel lbl = new JLabel(title + " (Coming Soon)");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        lbl.setForeground(MUTED_TEXT);
        panel.add(lbl);
        return panel;
    }

    private String extractJsonValue(String json, String key) {
        try {
            String searchKey = "\"" + key + "\":";
            int index = json.indexOf(searchKey);
            if (index == -1) return "";

            int startIndex = index + searchKey.length();
            char firstChar = json.charAt(startIndex);

            if (firstChar == '"') {
                int endIndex = json.indexOf('"', startIndex + 1);
                return json.substring(startIndex + 1, endIndex);
            } else {
                int endIndex = startIndex;
                while (endIndex < json.length() && 
                       json.charAt(endIndex) != ',' && 
                       json.charAt(endIndex) != '}') {
                    endIndex++;
                }
                return json.substring(startIndex, endIndex).trim();
            }
        } catch (Exception e) {
            return "";
        }
    }

    private void openBrowseGroupDetails(int groupId) {
        new Thread(() -> {
            String response = ApiClient.get("/groups/" + groupId, authToken);
            boolean isAdmin = false;
            boolean isMember = false;
            boolean isCreator = false;
            String userRole = "Not a Member";
            String fetchedGroupName = "Discussion Group #" + groupId;
            String fetchedDescription = "";
            String createdDate = "";
            String creatorName = "";

            try {
                if (response != null && !response.isEmpty()) {
                    String jsonBody = response;
                    if (response.contains(":")) {
                        int firstColon = response.indexOf(":");
                        try {
                            Integer.parseInt(response.substring(0, firstColon).trim());
                            jsonBody = response.substring(firstColon + 1).trim();
                        } catch (NumberFormatException ignored) {}
                    }

                    isMember = Boolean.parseBoolean(extractJsonValue(jsonBody, "isMember"));
                    isAdmin = Boolean.parseBoolean(extractJsonValue(jsonBody, "isAdmin"));
                    
                    String fetchedRole = extractJsonValue(jsonBody, "userRole");
                    if (fetchedRole != null && !fetchedRole.isEmpty() && !fetchedRole.equalsIgnoreCase("null")) {
                        userRole = fetchedRole;
                    }

                    String groupJson = extractJsonValue(jsonBody, "group"); 
                    if (!groupJson.isEmpty()) {
                        String name = extractJsonValue(groupJson, "group_name");
                        if (!name.isEmpty()) fetchedGroupName = name;

                        String desc = extractJsonValue(groupJson, "description");
                        if (!desc.isEmpty() && !desc.equals("null")) fetchedDescription = desc;

                        createdDate = extractJsonValue(groupJson, "created_at");
                        
                        String creatorJson = extractJsonValue(groupJson, "creator");
                        if (!creatorJson.isEmpty()) {
                            creatorName = extractJsonValue(creatorJson, "name");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            final boolean finalAdmin = isAdmin;
            final boolean finalMember = isMember;
            final boolean finalCreator = finalAdmin && creatorName.equals("CurrentUserName"); 
            final String finalRole = userRole;
            final String gName = fetchedGroupName;
            final String gDesc = fetchedDescription;
            final String cDate = createdDate;
            final String cName = creatorName;

            SwingUtilities.invokeLater(() -> {
                String detailsCardName = "GROUP_DETAILS_VIEW_" + groupId;

                GroupDetailsView detailsView = new GroupDetailsView(
                    groupId,
                    authToken,                   
                    gName,
                    gDesc,
                    finalMember,
                    finalAdmin,
                    false,
                    1,
                    "",
                    "",
                    "Member",
                    "Student", // accountRole
                    null,
                    null,
                    () -> cardLayout.show(contentCards, "GROUPS"), // onBack
                    
                    // ACTION 1: Open Discussions (Fetches GET /groups/{group}/topics)
                    () -> {                      
                        new Thread(() -> {
                            String topicsResponse = ApiClient.get("/groups/" + groupId + "/topics", authToken);
                            SwingUtilities.invokeLater(() -> openGroupTopicsView(groupId, topicsResponse));
                        }).start();
                    }, 
                    
                    // ACTION 2: Open New Discussion Creation View (POST /groups/{group}/topics)
                    () -> {                      
                        openCreateDiscussionView(groupId, authToken);
                    },
                    
                    // ACTION 3: Open Group Chat (GET /groups/{group}/messages)
                    () -> {                      
                        openGroupChatView(groupId, gName, authToken);
                    },
                    
                    // ACTION 4: Open Quizzes (GET /groups/{group}/quizzes)
                    () -> {                      
                        openQuizzesView(groupId, authToken);
                    },
                    
                    // action5: Leave Group (DELETE /groups/{group}/leave)
                    () -> {
                        new Thread(() -> {
                            ApiClient.delete("/groups/" + groupId + "/leave", authToken);
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(this, "You have left the group.");
                                cardLayout.show(contentCards, "GROUPS");
                            });
                        }).start();
                    },
                    
                    // onOpenTopic callback
                    topicId -> openTopicDetailFromBrowse(groupId, topicId, detailsCardName),

                    // onManageMembers (visible when this student is an admin of the group)
                    () -> {
                        ManageMembersView view = new ManageMembersView(groupId, authToken,
                            () -> cardLayout.show(contentCards, detailsCardName));
                        String cardName = "MANAGE_MEMBERS_" + groupId;
                        contentCards.add(view, cardName);
                        cardLayout.show(contentCards, cardName);
                    },

                    // onStatistics
                    () -> {
                        StatisticsView view = new StatisticsView(groupId, authToken,
                            () -> cardLayout.show(contentCards, detailsCardName));
                        String cardName = "GROUP_STATISTICS_" + groupId;
                        contentCards.add(view, cardName);
                        cardLayout.show(contentCards, cardName);
                    },

                    // onDelete (visible only if this student created the group)
                    () -> {
                        new Thread(() -> {
                            ApiClient.delete("/groups/" + groupId, authToken);
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(this, "Group deleted successfully.");
                                cardLayout.show(contentCards, "GROUPS");
                            });
                        }).start();
                    },

                    // onParticipationSettings — null for students, hides the button (lecturer-only feature)
                    null,

                    // onParticipationResults
                    () -> {
                        ParticipationResultsView view = new ParticipationResultsView(groupId, authToken,
                            () -> cardLayout.show(contentCards, detailsCardName));
                        String cardName = "PARTICIPATION_RESULTS_" + groupId;
                        contentCards.add(view, cardName);
                        cardLayout.show(contentCards, cardName);
                    }, null
                );
                contentCards.add(detailsView, detailsCardName);
                cardLayout.show(contentCards, detailsCardName);
            });
        }).start();
    }

    private void openGroupTopicsView(int groupId, String topicsResponse) {
        String topicsCardName = "GROUP_TOPICS_VIEW_" + groupId;
        JPanel topicsView = new JPanel(new BorderLayout());
        topicsView.setBackground(PAGE_BG);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(24, 24, 14, 24));

        JLabel title = new JLabel("Group Topics");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        header.add(title, BorderLayout.WEST);

        JButton backButton = new JButton("← Back to Group");
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> cardLayout.show(contentCards, "GROUP_DETAILS_VIEW_" + groupId));
        header.add(backButton, BorderLayout.EAST);

        topicsView.add(header, BorderLayout.NORTH);

        JTextArea topicsArea = new JTextArea();
        topicsArea.setEditable(false);
        topicsArea.setLineWrap(true);
        topicsArea.setWrapStyleWord(true);
        topicsArea.setText(topicsResponse != null && !topicsResponse.isEmpty() ? topicsResponse : "No topics available.");
        topicsArea.setBorder(new EmptyBorder(0, 24, 24, 24));
        topicsArea.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(topicsArea);
        scrollPane.setBorder(null);
        topicsView.add(scrollPane, BorderLayout.CENTER);

        contentCards.add(topicsView, topicsCardName);
        cardLayout.show(contentCards, topicsCardName);
    }

    private void openCreateDiscussionView(int groupId, String authToken) {
        String cardName = "GROUP_CREATE_DISCUSSION_" + groupId;
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PAGE_BG);
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Create New Discussion");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(new EmptyBorder(0, 0, 16, 0));
        panel.add(title, BorderLayout.NORTH);

        JTextArea msg = new JTextArea("Discussion creation flow is not available yet.");
        msg.setEditable(false);
        msg.setLineWrap(true);
        msg.setWrapStyleWord(true);
        msg.setOpaque(false);
        msg.setFont(new Font("SansSerif", Font.PLAIN, 13));
        msg.setBorder(new EmptyBorder(12, 0, 0, 0));
        panel.add(msg, BorderLayout.CENTER);

        JButton backButton = new JButton("← Back to Group");
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> cardLayout.show(contentCards, "GROUP_DETAILS_VIEW_" + groupId));
        panel.add(backButton, BorderLayout.SOUTH);

        contentCards.add(panel, cardName);
        cardLayout.show(contentCards, cardName);
    }

    private void openGroupChatView(int groupId, String gName, String authToken) {
        String chatCardName = "GROUP_CHAT_VIEW_" + groupId;
        ChatView chatView = new ChatView(
            groupId, null, gName, "Live Academic Discussion Stream", authToken, currentUserName,
            () -> cardLayout.show(contentCards, "GROUP_DETAILS_VIEW_" + groupId),
            selectedTopicId -> openTopicDetailFromBrowse(groupId, selectedTopicId, chatCardName)
        );
        contentCards.add(chatView, chatCardName);
        cardLayout.show(contentCards, chatCardName);
    }

    private void openQuizzesView(int groupId, String authToken) {
        String cardName = "GROUP_QUIZZES_VIEW_" + groupId;
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PAGE_BG);
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Group Quizzes");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(new EmptyBorder(0, 0, 16, 0));
        panel.add(title, BorderLayout.NORTH);

        JTextArea info = new JTextArea("Quiz overview is not available yet.");
        info.setEditable(false);
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.setOpaque(false);
        info.setFont(new Font("SansSerif", Font.PLAIN, 13));
        info.setBorder(new EmptyBorder(12, 0, 0, 0));
        panel.add(info, BorderLayout.CENTER);

        JButton backButton = new JButton("← Back to Group");
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> cardLayout.show(contentCards, "GROUP_DETAILS_VIEW_" + groupId));
        panel.add(backButton, BorderLayout.SOUTH);

        contentCards.add(panel, cardName);
        cardLayout.show(contentCards, cardName);
    }

    private void openBrowseGroupJoin(int groupId) {
        String joinCardName = "DYNAMIC_JOIN_VIEW_" + groupId;
        JoinGroupView joinView = new JoinGroupView(
            groupId, "Academic Group", "Discussion Group #" + groupId,
            "Participate in academic discussions, share materials, and collaborate with peers.",
            authToken,
            () -> cardLayout.show(contentCards, "BROWSE_GROUPS"),
            () -> openChatFromBrowse(groupId, "Discussion Group #" + groupId, joinCardName)
        );
        contentCards.add(joinView, joinCardName);
        cardLayout.show(contentCards, joinCardName);
    }

    private void openChatFromBrowse(int groupId, String gName, String backCardName) {
        String chatCardName = "CHAT_VIEW_" + groupId;
        ChatView chatView = new ChatView(
            groupId, null, gName, "Live Academic Discussion Stream", authToken, currentUserName,
            () -> cardLayout.show(contentCards, backCardName),
            selectedTopicId -> openTopicDetailFromBrowse(groupId, selectedTopicId, chatCardName)
        );
        contentCards.add(chatView, chatCardName);
        cardLayout.show(contentCards, chatCardName);
    }

    private void openTopicDetailFromBrowse(int groupId, int topicId, String backCardName) {
        String topicCardName = "TOPIC_DETAIL_VIEW_" + groupId + "_" + topicId;
        TopicDetailView topicDetailView = new TopicDetailView(
            groupId, topicId, authToken,
            () -> cardLayout.show(contentCards, backCardName)
        );
        contentCards.add(topicDetailView, topicCardName);
        cardLayout.show(contentCards, topicCardName);
        contentCards.revalidate();
        contentCards.repaint();
    }

    private void openChatFromMain(int groupId, String gName) {
        String chatCardName = "MAIN_CHAT_VIEW_" + groupId;
        String detailsCardName = "GROUP_DETAILS_" + groupId;

        ChatView chatView = new ChatView(
            groupId, null, gName, "Live Academic Discussion Stream", authToken, currentUserName,
            () -> cardLayout.show(contentCards, detailsCardName),
            selectedTopicId -> openTopicDetailFromMain(groupId, selectedTopicId, chatCardName)
        );
        contentCards.add(chatView, chatCardName);
        cardLayout.show(contentCards, chatCardName);
    }

    private void openTopicDetailFromMain(int groupId, int topicId, String backCardName) {
        String topicCardName = "MAIN_TOPIC_DETAIL_VIEW_" + groupId + "_" + topicId;
        TopicDetailView topicDetailView = new TopicDetailView(
            groupId, topicId, authToken,
            () -> cardLayout.show(contentCards, backCardName)
        );
        contentCards.add(topicDetailView, topicCardName);
        cardLayout.show(contentCards, topicCardName);
        contentCards.revalidate();
        contentCards.repaint();
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