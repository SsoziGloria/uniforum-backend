package Student;

import GeneralUser.api.ApiClient;
import GeneralUser.views.LoginForm;
import GeneralUser.views.Groups.BrowseGroupsView;
import GeneralUser.views.Groups.ChatView;
import GeneralUser.views.Groups.CreateGroupView;
import GeneralUser.views.Groups.GroupDetailsView;
import GeneralUser.views.Groups.JoinGroupView;
import GeneralUser.views.Groups.MainGroupsView;
import Student.Discussions.TopicDetailView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class StudentDashboard extends JFrame {

    private final Color PRIMARY_BLUE = new Color(37, 99, 235);    // #2563EB
    private final Color PAGE_BG = new Color(243, 244, 246);        // #F3F4F6
    private final Color BORDER_COLOR = new Color(229, 231, 235);   // #E5E7EB
    private final Color DARK_TEXT = new Color(31, 41, 55);         // #1F2937
    private final Color MUTED_TEXT = new Color(107, 114, 128);     // #6B7280

    private String authToken;
    private MainGroupsView mainGroupsView;
    private BrowseGroupsView browseGroupsView;
    private String currentUserName; 
    
    private JPanel contentCards;
    private CardLayout cardLayout;
    
    private JPanel dashboardNavItem, discussionsNavItem, groupsNavItem, quizzesNavItem, aiNavItem, notifNavItem, settingsNavItem;
    private JLabel dashboardTextLbl, discussionsTextLbl, groupsTextLbl, quizzesTextLbl, aiTextLbl, notifTextLbl, settingsTextLbl;
    
    public StudentDashboard(String token, String userNameInput, Runnable onCreateGroupClicked, Runnable onBrowseGroupsClicked) {
        this.authToken = token;
        this.currentUserName = (userNameInput != null && !userNameInput.trim().isEmpty()) ? userNameInput.trim() : "User";

        setTitle("UniForum — Student Portal");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- SIDEBAR ---
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.WHITE);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_COLOR));
        sidebar.setPreferredSize(new Dimension(260, getHeight()));

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

        JLabel logoSubtitle = new JLabel("Student Portal");
        logoSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        logoSubtitle.setForeground(MUTED_TEXT);
        logoPanel.add(logoSubtitle);

        sidebar.add(logoPanel);

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

        dashboardNavItem = createSidebarNavItem("🏠", "Dashboard", true, "DASHBOARD");
        dashboardTextLbl = (JLabel) dashboardNavItem.getComponent(1);
        navMenu.add(dashboardNavItem);
        navMenu.add(Box.createRigidArea(new Dimension(0, 4)));

        discussionsNavItem = createSidebarNavItem("💬", "Discussions", false, "DISCUSSIONS");
        discussionsTextLbl = (JLabel) discussionsNavItem.getComponent(1);
        navMenu.add(discussionsNavItem);
        navMenu.add(Box.createRigidArea(new Dimension(0, 4)));

        groupsNavItem = createSidebarNavItem("👥", "Groups", false, "GROUPS");
        groupsTextLbl = (JLabel) groupsNavItem.getComponent(1);
        navMenu.add(groupsNavItem);
        navMenu.add(Box.createRigidArea(new Dimension(0, 4)));

        quizzesNavItem = createSidebarNavItem("📝", "Quizzes", false, "QUIZZES");
        quizzesTextLbl = (JLabel) quizzesNavItem.getComponent(1);
        navMenu.add(quizzesNavItem);
        navMenu.add(Box.createRigidArea(new Dimension(0, 4)));

        aiNavItem = createSidebarNavItem("🤖", "AI Recommendations", false, "AI");
        aiTextLbl = (JLabel) aiNavItem.getComponent(1);
        navMenu.add(aiNavItem);
        navMenu.add(Box.createRigidArea(new Dimension(0, 4)));

        notifNavItem = createSidebarNavItem("🔔", "Notifications", false, "NOTIFICATIONS");
        notifTextLbl = (JLabel) notifNavItem.getComponent(1);
        navMenu.add(notifNavItem);
        navMenu.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel accountHeading = new JLabel("ACCOUNT");
        accountHeading.setFont(new Font("SansSerif", Font.BOLD, 11));
        accountHeading.setForeground(MUTED_TEXT);
        accountHeading.setBorder(new EmptyBorder(0, 12, 10, 0));
        accountHeading.setAlignmentX(Component.LEFT_ALIGNMENT);
        navMenu.add(accountHeading);

        settingsNavItem = createSidebarNavItem("⚙️", "Profile Settings", false, "SETTINGS");
        settingsTextLbl = (JLabel) settingsNavItem.getComponent(1);
        navMenu.add(settingsNavItem);

        sidebar.add(navMenu);
        sidebar.add(Box.createVerticalGlue());

        JPanel userPanel = new JPanel(new BorderLayout(12, 0));
        userPanel.setBackground(Color.WHITE);
        userPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel userAvatar = new JLabel(String.valueOf(currentUserName.charAt(0)).toUpperCase(), JLabel.CENTER);
        userAvatar.setFont(new Font("SansSerif", Font.BOLD, 14));
        userAvatar.setForeground(Color.WHITE);
        userAvatar.setOpaque(true);
        userAvatar.setBackground(PRIMARY_BLUE);
        userAvatar.setPreferredSize(new Dimension(40, 40));
        userPanel.add(userAvatar, BorderLayout.WEST);

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

        userPanel.add(userInfo, BorderLayout.CENTER);
        sidebar.add(userPanel);

        add(sidebar, BorderLayout.WEST);

        // --- RIGHT CONTAINER ---
        JPanel rightContainer = new JPanel(new BorderLayout());
        rightContainer.setBackground(PAGE_BG);

        JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setBackground(Color.WHITE);
        topHeader.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
            new EmptyBorder(16, 32, 16, 32)
        ));

        JPanel headerTitlePanel = new JPanel();
        headerTitlePanel.setLayout(new BoxLayout(headerTitlePanel, BoxLayout.Y_AXIS));
        headerTitlePanel.setBackground(Color.WHITE);

        JLabel pageHeading = new JLabel("Dashboard");
        pageHeading.setFont(new Font("SansSerif", Font.BOLD, 20));
        pageHeading.setForeground(DARK_TEXT);
        headerTitlePanel.add(pageHeading);

        topHeader.add(headerTitlePanel, BorderLayout.WEST);

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

        // --- CARD LAYOUT CONTAINER ---
        cardLayout = new CardLayout();
        contentCards = new JPanel(cardLayout);
        contentCards.setBackground(PAGE_BG);

        contentCards.add(createDashboardHomePanel(), "DASHBOARD");
        
        mainGroupsView = new MainGroupsView(
    token, 
    currentUserName,
    () -> cardLayout.show(contentCards, "CREATE_GROUP"),    
    () -> cardLayout.show(contentCards, "BROWSE_GROUPS"),
    (groupId, gName) -> openChatFromMain(groupId, gName),       // Wire up chat callback
    (groupId, topicId) -> openTopicDetailFromMain(groupId, topicId, "GROUP_DETAILS_" + groupId) // Wire up topic callback
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

        item.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(contentCards, cardName);
                setActiveNavItem(cardName);
                if (cardName.equals("BROWSE_GROUPS") && browseGroupsView != null) {
                    browseGroupsView.refreshData();
                } else if (cardName.equals("GROUPS") && mainGroupsView != null) {
                    mainGroupsView.refreshData();
                }
            }
            @Override public void mousePressed(MouseEvent e) {}
            @Override public void mouseReleased(MouseEvent e) {}
            @Override public void mouseEntered(MouseEvent e) {
                if (!item.getBackground().equals(PRIMARY_BLUE)) {
                    item.setBackground(new Color(243, 244, 246));
                }
            }
            @Override public void mouseExited(MouseEvent e) {
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
        contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // --- WELCOME BANNER ---
        JPanel welcomeBanner = new JPanel();
        welcomeBanner.setLayout(new BoxLayout(welcomeBanner, BoxLayout.Y_AXIS));
        welcomeBanner.setBackground(PRIMARY_BLUE);
        welcomeBanner.setBorder(new EmptyBorder(28, 32, 28, 32));
        welcomeBanner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 190));
        welcomeBanner.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel welcomeTitle = new JLabel("Welcome back, " + currentUserName + " 👋"); 
        welcomeTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        welcomeTitle.setForeground(Color.WHITE);
        welcomeTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        welcomeBanner.add(welcomeTitle);

        welcomeBanner.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel welcomeDesc = new JLabel("<html><div style='width: 500px; color: #DBEAFE; font-size: 13px;'>Stay connected with your university discussions, discover recommended topics and improve your participation score.</div></html>");
        welcomeDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        welcomeBanner.add(welcomeDesc);
        
        welcomeBanner.add(Box.createRigidArea(new Dimension(0, 16)));
        JButton exploreBtn = new JButton("Explore Discussions");
        exploreBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        exploreBtn.setForeground(PRIMARY_BLUE);
        exploreBtn.setBackground(Color.WHITE);
        exploreBtn.setOpaque(true);
        exploreBtn.setFocusPainted(false);
        exploreBtn.setBorder(new EmptyBorder(10, 16, 10, 16));
        exploreBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exploreBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        exploreBtn.addActionListener(e -> {
            cardLayout.show(contentCards, "DISCUSSIONS");
            setActiveNavItem("DISCUSSIONS");
        });
        welcomeBanner.add(exploreBtn);

        contentPanel.add(welcomeBanner);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 24)));

        // --- STATISTICS CARDS ROW ---
        JPanel statsRow = new JPanel(new GridLayout(1, 4, 16, 0));
        statsRow.setBackground(PAGE_BG);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        statsRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        statsRow.add(createStatCard("💬", "Questions Asked", "24"));
        statsRow.add(createStatCard("⭐", "Participation Score", "85%"));
        statsRow.add(createStatCard("📚", "Topics Following", "12"));
        statsRow.add(createStatCard("📝", "Pending Quizzes", "3"));

        contentPanel.add(statsRow);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 24)));

        // --- RECOMMENDATIONS & NOTIFICATIONS GRID ---
        JPanel middleGrid = new JPanel(new GridLayout(1, 2, 24, 0));
        middleGrid.setBackground(PAGE_BG);
        middleGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
        middleGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Recommended Topics Card
        JPanel recTopicsCard = new JPanel();
        recTopicsCard.setLayout(new BoxLayout(recTopicsCard, BoxLayout.Y_AXIS));
        recTopicsCard.setBackground(Color.WHITE);
        recTopicsCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JPanel recHeader = new JPanel(new BorderLayout());
        recHeader.setBackground(Color.WHITE);
        JLabel recTitle = new JLabel("Recommended Topics");
        recTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        recTitle.setForeground(DARK_TEXT);
        JLabel aiBadge = new JLabel("AI Powered");
        aiBadge.setFont(new Font("SansSerif", Font.BOLD, 12));
        aiBadge.setForeground(PRIMARY_BLUE);
        recHeader.add(recTitle, BorderLayout.WEST);
        recHeader.add(aiBadge, BorderLayout.EAST);
        recTopicsCard.add(recHeader);
        recTopicsCard.add(Box.createRigidArea(new Dimension(0, 12)));

        recTopicsCard.add(createTopicRow("Laravel Authentication", "23 students discussing"));
        recTopicsCard.add(Box.createRigidArea(new Dimension(0, 8)));
        recTopicsCard.add(createTopicRow("Machine Learning Basics", "18 students discussing"));
        recTopicsCard.add(Box.createRigidArea(new Dimension(0, 8)));
        recTopicsCard.add(createTopicRow("Software Design Patterns", "31 students discussing"));

        middleGrid.add(recTopicsCard);

        // Notifications Card
        JPanel notifCard = new JPanel();
        notifCard.setLayout(new BoxLayout(notifCard, BoxLayout.Y_AXIS));
        notifCard.setBackground(Color.WHITE);
        notifCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel notifHeader = new JLabel("Notifications");
        notifHeader.setFont(new Font("SansSerif", Font.BOLD, 16));
        notifHeader.setForeground(DARK_TEXT);
        notifCard.add(notifHeader);
        notifCard.add(Box.createRigidArea(new Dimension(0, 14)));

        notifCard.add(createNotificationItem(PRIMARY_BLUE, "New quiz available", "Database Systems Quiz"));
        notifCard.add(Box.createRigidArea(new Dimension(0, 12)));
        notifCard.add(createNotificationItem(new Color(34, 197, 94), "Your question was answered", "Operating Systems"));
        notifCard.add(Box.createRigidArea(new Dimension(0, 12)));
        notifCard.add(createNotificationItem(new Color(234, 179, 8), "Participation reminder", "Engage more in discussions"));

        middleGrid.add(notifCard);
        contentPanel.add(middleGrid);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 24)));

        // --- RECENT ACTIVITY CARD ---
        JPanel recentActivityCard = new JPanel();
        recentActivityCard.setLayout(new BoxLayout(recentActivityCard, BoxLayout.Y_AXIS));
        recentActivityCard.setBackground(Color.WHITE);
        recentActivityCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        recentActivityCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        recentActivityCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel activityHeader = new JLabel("Recent Activity");
        activityHeader.setFont(new Font("SansSerif", Font.BOLD, 16));
        activityHeader.setForeground(DARK_TEXT);
        recentActivityCard.add(activityHeader);
        recentActivityCard.add(Box.createRigidArea(new Dimension(0, 12)));

        recentActivityCard.add(createActivityRow("Answered \"How does Laravel middleware work?\"", "2 hrs ago"));
        recentActivityCard.add(Box.createRigidArea(new Dimension(0, 8)));
        recentActivityCard.add(createActivityRow("Joined Database Optimization topic", "Yesterday"));
        recentActivityCard.add(Box.createRigidArea(new Dimension(0, 8)));
        recentActivityCard.add(createActivityRow("Completed Software Engineering quiz", "3 days ago"));

        contentPanel.add(recentActivityCard);

        // --- HIDE HORIZONTAL SCROLLBAR & ENABLE TOUCH/WHEEL SCROLLING ---
        JScrollPane scrollPane = new JScrollPane(
            contentPanel,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setWheelScrollingEnabled(true);
        
        outerContainer.add(scrollPane, BorderLayout.CENTER);
        return outerContainer;
    }

    private JPanel createStatCard(String icon, String label, String value) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("SansSerif", Font.PLAIN, 20));
        card.add(iconLbl);
        card.add(Box.createRigidArea(new Dimension(0, 6)));

        JLabel titleLbl = new JLabel(label);
        titleLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        titleLbl.setForeground(MUTED_TEXT);
        card.add(titleLbl);
        card.add(Box.createRigidArea(new Dimension(0, 4)));

        JLabel valLbl = new JLabel(value);
        valLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        valLbl.setForeground(DARK_TEXT);
        card.add(valLbl);

        return card;
    }

    private JPanel createTopicRow(String title, String subtitle) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(new Color(248, 250, 252));
        row.setBorder(new EmptyBorder(10, 12, 10, 12));

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(new Color(248, 250, 252));

        JLabel tLbl = new JLabel(title);
        tLbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        tLbl.setForeground(DARK_TEXT);
        info.add(tLbl);

        JLabel subLbl = new JLabel(subtitle);
        subLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        subLbl.setForeground(MUTED_TEXT);
        info.add(subLbl);

        row.add(info, BorderLayout.WEST);

        JButton viewBtn = new JButton("View");
        viewBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        viewBtn.setForeground(PRIMARY_BLUE);
        viewBtn.setBorderPainted(false);
        viewBtn.setContentAreaFilled(false);
        viewBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        row.add(viewBtn, BorderLayout.EAST);

        return row;
    }

    private JPanel createNotificationItem(Color barColor, String title, String subtitle) {
        JPanel item = new JPanel(new BorderLayout());
        item.setBackground(Color.WHITE);
        item.setBorder(BorderFactory.createMatteBorder(0, 4, 0, 0, barColor));

        JPanel textWrapper = new JPanel();
        textWrapper.setLayout(new BoxLayout(textWrapper, BoxLayout.Y_AXIS));
        textWrapper.setBackground(Color.WHITE);
        textWrapper.setBorder(new EmptyBorder(0, 10, 0, 0));

        JLabel tLbl = new JLabel(title);
        tLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        tLbl.setForeground(DARK_TEXT);
        textWrapper.add(tLbl);

        JLabel subLbl = new JLabel(subtitle);
        subLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        subLbl.setForeground(MUTED_TEXT);
        textWrapper.add(subLbl);

        item.add(textWrapper, BorderLayout.CENTER);
        return item;
    }

    private JPanel createActivityRow(String action, String time) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);

        JLabel actLbl = new JLabel(action);
        actLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        actLbl.setForeground(DARK_TEXT);
        row.add(actLbl, BorderLayout.WEST);

        JLabel timeLbl = new JLabel(time);
        timeLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        timeLbl.setForeground(MUTED_TEXT);
        row.add(timeLbl, BorderLayout.EAST);

        return row;
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

    private boolean extractBoolean(String json, String key) {
        String value = extractJsonValue(json, key);
        return Boolean.parseBoolean(value);
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
            String fetchedGroupName = "Discussion Group #" + groupId;
            String fetchedDescription = "Participate in academic discussions, share materials, and collaborate with peers.";

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

                    isAdmin = extractBoolean(jsonBody, "is_admin");
                    isMember = !jsonBody.contains("\"user_role\":null") && jsonBody.contains("\"user_role\":");
                    
                    String name = extractJsonValue(jsonBody, "group_name");
                    if (!name.isEmpty()) fetchedGroupName = name;

                    String desc = extractJsonValue(jsonBody, "description");
                    if (!desc.isEmpty() && !desc.equals("null")) fetchedDescription = desc;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            final boolean adminStatus = isAdmin;
            final boolean memberStatus = isMember;
            final String gName = fetchedGroupName;
            final String gDesc = fetchedDescription;

            SwingUtilities.invokeLater(() -> {
                String detailsCardName = "GROUP_DETAILS_VIEW_" + groupId;
                
                GroupDetailsView detailsView = new GroupDetailsView(
                    groupId, "Academic Group", gName, gDesc, memberStatus, adminStatus, authToken,
                    () -> cardLayout.show(contentCards, "BROWSE_GROUPS"),
                    () -> openBrowseGroupJoin(groupId),
                    () -> openChatFromBrowse(groupId, gName, detailsCardName),
                    () -> JOptionPane.showMessageDialog(this, "Opening Member Management controls for Group #" + groupId, "Admin Controls", JOptionPane.INFORMATION_MESSAGE),
                    topicId -> openTopicDetailFromBrowse(groupId, topicId, detailsCardName)
                );

                contentCards.add(detailsView, detailsCardName);
                cardLayout.show(contentCards, detailsCardName);
                contentCards.revalidate();
                contentCards.repaint();
            });
        }).start();
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
        () -> cardLayout.show(contentCards, detailsCardName), // Back button returns to group details
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
}