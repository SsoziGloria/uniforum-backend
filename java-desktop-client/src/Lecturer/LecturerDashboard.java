package Lecturer;

import GeneralUser.views.Groups.MainGroupsView;
import GeneralUser.views.Groups.Discussions.GroupTopicsView;
import GeneralUser.views.Groups.Discussions.NewDiscussionView;
import GeneralUser.views.Groups.BrowseGroupsView;
import GeneralUser.views.Groups.CreateGroupView;
import GeneralUser.views.Groups.GroupDetailsView;
import GeneralUser.views.Groups.ManageMembersView;
import GeneralUser.views.Groups.StatisticsView;
import GeneralUser.views.Groups.ChatView;
import GeneralUser.views.Groups.JoinGroupView;
import GeneralUser.api.ApiClient;
import GeneralUser.views.LoginForm;
import Lecturer.Participation.LecturerParticipationScoresView;
import Lecturer.Participation.LecturerParticipationSettingsView;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LecturerDashboard extends JFrame {

    private final String authToken;
    private final JPanel contentPanel;
    private final CardLayout cardLayout;

    private final Color PRIMARY_BLUE = new Color(37, 99, 235);     
    private final Color SIDEBAR_BG = Color.WHITE;                  
    private final Color SIDEBAR_BORDER = new Color(226, 232, 240); 
    private final Color PAGE_BG = new Color(248, 250, 252);        
    private final Color CARD_BG = Color.WHITE;
    private final Color BORDER_COLOR = new Color(226, 232, 240);
    private final Color DARK_TEXT = new Color(30, 41, 59);         
    private final Color MUTED_TEXT = new Color(100, 116, 139);     
    private final Color GREEN_BG = new Color(220, 252, 231);
    private final Color GREEN_TEXT = new Color(21, 128, 61);
    private final Color YELLOW_BG = new Color(254, 243, 199);
    private final Color YELLOW_TEXT = new Color(180, 83, 9);

    public LecturerDashboard(String token) {
        this.authToken = token;

        setTitle("Lecturer Dashboard - UniForum");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        JPanel dashboardHomePanel = createDashboardHomeView();

        MainGroupsView mainGroupsView = new MainGroupsView(
            authToken,
            "Lecturer",
            "Lecturer", // accountRole
            () -> cardLayout.show(contentPanel, "CREATE_GROUP"),
            () -> cardLayout.show(contentPanel, "BROWSE_GROUPS"),
            (groupId, groupName) -> openChatView(groupId, groupName, "General Chat", null),
            (groupId, topicId) -> openChatView(groupId, "Group Discussion", "Topic Discussion", topicId)
        );

        BrowseGroupsView browseGroupsView = new BrowseGroupsView(
            authToken,
            () -> cardLayout.show(contentPanel, "MAIN_GROUPS"),
            groupId -> fetchAndOpenGroupDetails(groupId),
            groupId -> openJoinGroupView(groupId)
        );

        CreateGroupView createGroupView = new CreateGroupView(
            authToken,
            () -> {
                cardLayout.show(contentPanel, "MAIN_GROUPS");
                mainGroupsView.refreshData();
            },
            () -> cardLayout.show(contentPanel, "MAIN_GROUPS")
        );

        contentPanel.add(dashboardHomePanel, "DASHBOARD_HOME");
        contentPanel.add(mainGroupsView, "MAIN_GROUPS");
        contentPanel.add(browseGroupsView, "BROWSE_GROUPS");
        contentPanel.add(createGroupView, "CREATE_GROUP");

        JPanel rootLayout = new JPanel(new BorderLayout());
        rootLayout.add(createSidebar(), BorderLayout.WEST);
        
        JPanel rightContainer = new JPanel(new BorderLayout());
        rightContainer.add(createTopNavbar(), BorderLayout.NORTH);
        rightContainer.add(contentPanel, BorderLayout.CENTER);
        
        rootLayout.add(rightContainer, BorderLayout.CENTER);
        add(rootLayout);

        cardLayout.show(contentPanel, "DASHBOARD_HOME");
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, SIDEBAR_BORDER));

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        logoPanel.setBackground(SIDEBAR_BG);
        logoPanel.setMaximumSize(new Dimension(260, 70));
        
        JLabel logoText = new JLabel("UniForum");
        logoText.setFont(new Font("SansSerif", Font.BOLD, 20));
        logoText.setForeground(PRIMARY_BLUE);
        logoPanel.add(logoText);
        sidebar.add(logoPanel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        sidebar.add(createSidebarButton("📊", "Dashboard", e -> cardLayout.show(contentPanel, "DASHBOARD_HOME")));
        sidebar.add(createSidebarButton("👥", "Manage Groups", e -> cardLayout.show(contentPanel, "MAIN_GROUPS")));
        sidebar.add(createSidebarButton("💬", "Discussions", e -> cardLayout.show(contentPanel, "MAIN_GROUPS")));
        sidebar.add(createSidebarButton("📝", "Quizzes & Assessments", e -> cardLayout.show(contentPanel, "MAIN_GROUPS")));
        
        sidebar.add(Box.createVerticalGlue());

        sidebar.add(createSidebarButton("🚪", "Sign Out", e -> {
            dispose();
            new LoginForm().setVisible(true);
        }));
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        return sidebar;
    }

    private JButton createSidebarButton(String emoji, String title, java.awt.event.ActionListener action) {
        JButton btn = new JButton(emoji + "   " + title);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn.setForeground(DARK_TEXT);
        btn.setBackground(SIDEBAR_BG);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(240, 44));
        btn.setPreferredSize(new Dimension(240, 44));
        btn.setBorder(new EmptyBorder(0, 20, 0, 20));
        btn.addActionListener(action);
        return btn;
    }

    private JPanel createTopNavbar() {
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(Color.WHITE);
        navbar.setPreferredSize(new Dimension(0, 70));
        navbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, SIDEBAR_BORDER));

        JPanel titleWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 22));
        titleWrap.setBackground(Color.WHITE);
        
        JLabel pageTitle = new JLabel("Dashboard");
        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        pageTitle.setForeground(DARK_TEXT);
        titleWrap.add(pageTitle);

        JPanel userWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 20));
        userWrap.setBackground(Color.WHITE);
        
        JLabel userBadge = new JLabel("Lecturer Portal");
        userBadge.setFont(new Font("SansSerif", Font.BOLD, 12));
        userBadge.setForeground(PRIMARY_BLUE);
        userWrap.add(userBadge);

        navbar.add(titleWrap, BorderLayout.WEST);
        navbar.add(userWrap, BorderLayout.EAST);
        return navbar;
    }

    private JPanel createDashboardHomeView() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(PAGE_BG);

        JPanel contentContainer = new JPanel();
        contentContainer.setLayout(new BoxLayout(contentContainer, BoxLayout.Y_AXIS));
        contentContainer.setBackground(PAGE_BG);
        contentContainer.setBorder(new EmptyBorder(24, 32, 24, 32));

        JScrollPane scrollPane = new JScrollPane(
            contentContainer,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        wrapper.add(scrollPane, BorderLayout.CENTER);

        JPanel banner = new JPanel();
        banner.setLayout(new BoxLayout(banner, BoxLayout.Y_AXIS));
        banner.setBackground(PRIMARY_BLUE);
        banner.setBorder(new EmptyBorder(32, 32, 32, 32));
        banner.setAlignmentX(Component.LEFT_ALIGNMENT);
        banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        JLabel welcomeTitle = new JLabel("Welcome back, Lecturer 👋");
        welcomeTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        welcomeTitle.setForeground(Color.WHITE);
        welcomeTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        banner.add(welcomeTitle);
        banner.add(Box.createRigidArea(new Dimension(0, 12)));

        JLabel welcomeSub = new JLabel("Manage discussions, monitor student participation, review quizzes, and guide academic conversations.");
        welcomeSub.setFont(new Font("SansSerif", Font.PLAIN, 14));
        welcomeSub.setForeground(new Color(219, 234, 254));
        welcomeSub.setAlignmentX(Component.LEFT_ALIGNMENT);
        banner.add(welcomeSub);
        banner.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton exploreBtn = new JButton("Explore Groups");
        exploreBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        exploreBtn.setForeground(PRIMARY_BLUE);
        exploreBtn.setBackground(Color.WHITE);
        exploreBtn.setBorder(new EmptyBorder(12, 20, 12, 20));
        exploreBtn.setFocusPainted(false);
        exploreBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exploreBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        exploreBtn.addActionListener(e -> cardLayout.show(contentPanel, "MAIN_GROUPS"));
        banner.add(exploreBtn);

        contentContainer.add(banner);
        contentContainer.add(Box.createRigidArea(new Dimension(0, 24)));

        JPanel statsGrid = new JPanel(new GridLayout(1, 4, 20, 0));
        statsGrid.setBackground(PAGE_BG);
        statsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        statsGrid.add(createStatCard("💬", "Active Discussions", "12"));
        statsGrid.add(createStatCard("👨‍🎓", "Students Engaged", "148"));
        statsGrid.add(createStatCard("📝", "Active Quizzes", "5"));
        statsGrid.add(createStatCard("⭐", "Average Participation", "84%"));

        contentContainer.add(statsGrid);
        contentContainer.add(Box.createRigidArea(new Dimension(0, 24)));

        JPanel contentGrid = new JPanel(new GridLayout(1, 2, 24, 0));
        contentGrid.setBackground(PAGE_BG);
        contentGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel quizzesCard = createCardPanel();
        quizzesCard.setLayout(new BoxLayout(quizzesCard, BoxLayout.Y_AXIS));
        
        JLabel qTitle = new JLabel("Recent Quizzes");
        qTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        qTitle.setForeground(DARK_TEXT);
        qTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel qSub = new JLabel("Quizzes managed within your assigned groups");
        qSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        qSub.setForeground(MUTED_TEXT);
        qSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        quizzesCard.add(qTitle);
        quizzesCard.add(Box.createRigidArea(new Dimension(0, 2)));
        quizzesCard.add(qSub);
        quizzesCard.add(Box.createRigidArea(new Dimension(0, 16)));

        JPanel quizListPanel = new JPanel();
        quizListPanel.setLayout(new BoxLayout(quizListPanel, BoxLayout.Y_AXIS));
        quizListPanel.setBackground(CARD_BG);
        quizListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        quizListPanel.add(createQuizRow("Advanced Mobile Architecture Quiz", "Group: Mobile Dev Team • 14 Jul 2026", true));
        quizListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        quizListPanel.add(createQuizRow("Backend Routing & Middlewares Assessment", "Group: Web Engineering • 10 Jul 2026", false));

        quizzesCard.add(quizListPanel);

        JPanel participationCard = createCardPanel();
        participationCard.setLayout(new BoxLayout(participationCard, BoxLayout.Y_AXIS));

        JLabel pTitle = new JLabel("Group Participation Overview");
        pTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        pTitle.setForeground(DARK_TEXT);
        pTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        participationCard.add(pTitle);
        participationCard.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel barsPanel = new JPanel();
        barsPanel.setLayout(new BoxLayout(barsPanel, BoxLayout.Y_AXIS));
        barsPanel.setBackground(CARD_BG);
        barsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        barsPanel.add(createProgressBar("Mobile Application Dev - Grp 30", 92));
        barsPanel.add(Box.createRigidArea(new Dimension(0, 16)));
        barsPanel.add(createProgressBar("Advanced Software Engineering", 78));
        barsPanel.add(Box.createRigidArea(new Dimension(0, 16)));
        barsPanel.add(createProgressBar("Database Systems & Architecture", 85));

        participationCard.add(barsPanel);

        contentGrid.add(quizzesCard);
        contentGrid.add(participationCard);
        contentContainer.add(contentGrid);

        return wrapper;
    }

    private JPanel createStatCard(String emoji, String label, String value) {
        JPanel card = createCardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel icon = new JLabel(emoji);
        icon.setFont(new Font("SansSerif", Font.PLAIN, 24));
        icon.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lbl.setForeground(MUTED_TEXT);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel val = new JLabel(value);
        val.setFont(new Font("SansSerif", Font.BOLD, 26));
        val.setForeground(DARK_TEXT);
        val.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(icon);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(lbl);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(val);

        return card;
    }

    private JPanel createQuizRow(String title, String subtitle, boolean isPublished) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setBackground(PAGE_BG);
        row.setBorder(new EmptyBorder(12, 16, 12, 16));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(PAGE_BG);

        JLabel t = new JLabel(title);
        t.setFont(new Font("SansSerif", Font.BOLD, 13));
        t.setForeground(DARK_TEXT);
        t.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel(subtitle);
        sub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        sub.setForeground(MUTED_TEXT);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        info.add(t);
        info.add(Box.createRigidArea(new Dimension(0, 2)));
        info.add(sub);

        JLabel badge = new JLabel(isPublished ? " Published " : " Draft ");
        badge.setFont(new Font("SansSerif", Font.BOLD, 11));
        badge.setOpaque(true);
        badge.setBackground(isPublished ? GREEN_BG : YELLOW_BG);
        badge.setForeground(isPublished ? GREEN_TEXT : YELLOW_TEXT);
        badge.setBorder(new EmptyBorder(4, 8, 4, 8));

        JPanel badgeWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        badgeWrapper.setBackground(PAGE_BG);
        badgeWrapper.add(badge);

        row.add(info, BorderLayout.CENTER);
        row.add(badgeWrapper, BorderLayout.EAST);

        return row;
    }

    private JPanel createProgressBar(String groupName, int percentage) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(CARD_BG);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CARD_BG);

        JLabel nameLbl = new JLabel(groupName);
        nameLbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        nameLbl.setForeground(DARK_TEXT);

        JLabel pctLbl = new JLabel(percentage + "%");
        pctLbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        pctLbl.setForeground(MUTED_TEXT);

        header.add(nameLbl, BorderLayout.WEST);
        header.add(pctLbl, BorderLayout.EAST);
        wrapper.add(header);
        wrapper.add(Box.createRigidArea(new Dimension(0, 6)));

        JPanel barTrack = new JPanel(null);
        barTrack.setBackground(new Color(241, 245, 249));
        barTrack.setPreferredSize(new Dimension(Integer.MAX_VALUE, 12));
        barTrack.setMaximumSize(new Dimension(Integer.MAX_VALUE, 12));
        barTrack.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel barFill = new JPanel();
        barFill.setBackground(PRIMARY_BLUE);
        barFill.setBounds(0, 0, (int)(percentage * 2.2), 12);
        barTrack.add(barFill);

        wrapper.add(barTrack);
        return wrapper;
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

    private void openChatView(int groupId, String groupName, String subTitle, Integer topicId) {
        ChatView chatView = new ChatView(
            groupId,
            topicId,
            groupName,
            subTitle,
            authToken,
            "Lecturer",
            () -> cardLayout.show(contentPanel, "MAIN_GROUPS"),
            selectedTopicId -> openChatView(groupId, groupName, "Discussion Topic #" + selectedTopicId, selectedTopicId)
        );
        
        String chatCardName = "CHAT_" + groupId + "_" + (topicId != null ? topicId : "gen");
        contentPanel.add(chatView, chatCardName);
        cardLayout.show(contentPanel, chatCardName);
    }

    private void fetchAndOpenGroupDetails(int groupId) {
        Runnable backToDetails = () -> cardLayout.show(contentPanel, "GROUP_DETAILS_" + groupId);
        Runnable backToBrowse = () -> cardLayout.show(contentPanel, "BROWSE_GROUPS");

        GroupDetailsView detailsView = new GroupDetailsView(
                groupId,
                authToken,
                "",
                "",
                false,
                false,
                false,
                1,
                "",
                "",
                "Member",
                "Lecturer", // accountRole — used to show lecturer-only admin/participation controls
                null,
                null,
                backToBrowse, // onBack

                // ACTION 1: Open Discussions (Fetches GET /groups/{group}/topics)
                () -> {
                    new Thread(() -> {
                        String topicsResponse = ApiClient.get("/groups/" + groupId + "/topics", authToken);
                        SwingUtilities.invokeLater(() -> openGroupTopicsView(groupId, topicsResponse));
                    }).start();
                },

                // ACTION 2: Open New Discussion Creation View (POST /groups/{group}/topics)
                () -> {
                    openNewDiscussionView(groupId, authToken);
                },

                // ACTION 3: Open Group Chat (GET /groups/{group}/messages)
                () -> {
                    openChatView(groupId, "Group Chat", authToken);
                },

                // ACTION 4: Open Quizzes (GET /groups/{group}/quizzes)
                () -> {
                    openQuizIndexView(groupId, authToken);
                },

                // action5: Leave Group (DELETE /groups/{group}/leave)
                () -> {
                    new Thread(() -> {
                        String response = ApiClient.delete("/groups/" + groupId + "/leave", authToken);
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(this, "You have left the group.");
                            cardLayout.show(contentPanel, "MAIN_GROUPS");
                        });
                    }).start();
                },

                // onOpenTopic callback
                topicId -> openChatView(groupId, "Group Discussion", "Discussion Topic #" + topicId, topicId),

                // onManageMembers: GET /groups/{group}/members via existing ManageMembersView
                () -> {
                    ManageMembersView manageMembersView = new ManageMembersView(groupId, authToken, backToDetails);
                    String cardName = "MANAGE_MEMBERS_" + groupId;
                    contentPanel.add(manageMembersView, cardName);
                    cardLayout.show(contentPanel, cardName);
                },

                // onStatistics: GET /groups/{group}/statistics via existing StatisticsView
                () -> {
                    StatisticsView statisticsView = new StatisticsView(groupId, authToken, backToDetails);
                    String cardName = "GROUP_STATISTICS_" + groupId;
                    contentPanel.add(statisticsView, cardName);
                    cardLayout.show(contentPanel, cardName);
                },

                // onDelete: DELETE /groups/{group}
                () -> {
                    new Thread(() -> {
                        String response = ApiClient.delete("/groups/" + groupId, authToken);
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(this, "Group deleted successfully.");
                            cardLayout.show(contentPanel, "MAIN_GROUPS");
                        });
                    }).start();
                },

                // onParticipationSettings: opens existing LecturerParticipationSettingsView
                () -> {
                    LecturerParticipationSettingsView settingsView =
                        new LecturerParticipationSettingsView(groupId, authToken, backToDetails);
                    String cardName = "PARTICIPATION_SETTINGS_" + groupId;
                    contentPanel.add(settingsView, cardName);
                    cardLayout.show(contentPanel, cardName);
                },

                // onParticipationResults: opens existing LecturerParticipationScoresView
                () -> {
                    LecturerParticipationScoresView scoresView = new LecturerParticipationScoresView(
                        groupId,
                        authToken,
                        backToDetails,
                        () -> {
                            LecturerParticipationSettingsView settingsView =
                                new LecturerParticipationSettingsView(groupId, authToken, backToDetails);
                            String cardName = "PARTICIPATION_SETTINGS_" + groupId;
                            contentPanel.add(settingsView, cardName);
                            cardLayout.show(contentPanel, cardName);
                        }
                    );
                    String cardName = "PARTICIPATION_SCORES_" + groupId;
                    contentPanel.add(scoresView, cardName);
                    cardLayout.show(contentPanel, cardName);
                },

                // onJoinGroup: reuses the existing openJoinGroupView helper
                () -> openJoinGroupView(groupId)
        );
        String detailCardName = "GROUP_DETAILS_" + groupId;
        contentPanel.add(detailsView, detailCardName);
        cardLayout.show(contentPanel, detailCardName);
    }

    private void openGroupTopicsView(int groupId, String topicsResponse) {
        // Create the topics view for the group and add it to your main content cards
        GroupTopicsView topicsView = new GroupTopicsView(
            groupId,
            authToken,
            () -> cardLayout.show(contentPanel, "GROUP_DETAILS_" + groupId),
            topicId -> openChatView(groupId, "Group Discussion", "Discussion Topic #" + topicId, topicId),
            () -> openNewDiscussionView(groupId, authToken)
        );
        
        String cardName = "GROUP_TOPICS_" + groupId;
        contentPanel.add(topicsView, cardName);
        cardLayout.show(contentPanel, cardName);
    }

    private void openTopicDetailFromBrowse(int groupId, Integer topicId, String backCardName) {
        openChatView(groupId, "Group Discussion", "Discussion Topic #" + topicId, topicId);
    }

    private void openNewDiscussionView(int groupId, String authToken) {
        try {
            Runnable onSuccess = () -> JOptionPane.showMessageDialog(
                this,
                "Discussion created successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );

            Object createDialog = null;
            try {
                createDialog = NewDiscussionView.class.getConstructor(int.class, String.class, Runnable.class)
                    .newInstance(groupId, authToken, onSuccess);
            } catch (NoSuchMethodException ignored) {
                try {
                    createDialog = NewDiscussionView.class.getConstructor(int.class, String.class, java.util.function.Consumer.class)
                        .newInstance(groupId, authToken, (java.util.function.Consumer<Object>) ignoredValue -> onSuccess.run());
                } catch (NoSuchMethodException ignoredAgain) {
                    createDialog = NewDiscussionView.class.getConstructor(int.class, String.class)
                        .newInstance(groupId, authToken);
                }
            }

            createDialog.getClass().getMethod("setVisible", boolean.class).invoke(createDialog, true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to open discussion form: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openChatView(int groupId, String groupName, String authToken) {
        // Navigate to the general group chat view using ChatView constructor signature used elsewhere
        ChatView chatView = new ChatView(
            groupId,
            null, // topicId for general chat
            groupName,
            "General Chat",
            authToken,
            "Lecturer",
            () -> cardLayout.show(contentPanel, "GROUP_DETAILS_" + groupId),
            selectedTopicId -> openChatView(groupId, groupName, "Discussion Topic #" + selectedTopicId, selectedTopicId)
        );

        String cardName = "GROUP_CHAT_" + groupId;
        contentPanel.add(chatView, cardName);
        cardLayout.show(contentPanel, cardName);
    }

    private void openQuizIndexView(int groupId, String authToken) {
        // Navigate to the group quizzes view but the quizzes have different views for the Student and Lecturer
        
    }
    private void openJoinGroupView(int groupId) {
        JoinGroupView joinView = new JoinGroupView(
            groupId,
            "Academic Group",
            "Target Group",
            "Group description...",
            authToken,
            () -> cardLayout.show(contentPanel, "BROWSE_GROUPS"),
            () -> cardLayout.show(contentPanel, "MAIN_GROUPS")
        );
        contentPanel.add(joinView, "JOIN_GROUP");
        cardLayout.show(contentPanel, "JOIN_GROUP");
    }

    public void openParticipationScores(int groupId) {
        LecturerParticipationScoresView scoresView = new LecturerParticipationScoresView(
            groupId,
            authToken,
            () -> cardLayout.show(contentPanel, "MAIN_GROUPS"),
            () -> openParticipationSettings(groupId)
        );
        String cardName = "PARTICIPATION_SCORES_" + groupId;
        contentPanel.add(scoresView, cardName);
        cardLayout.show(contentPanel, cardName);
    }

    public void openParticipationSettings(int groupId) {
        LecturerParticipationSettingsView settingsView = new LecturerParticipationSettingsView(
            groupId,
            authToken,
            () -> openParticipationScores(groupId)
        );
        String cardName = "PARTICIPATION_SETTINGS_" + groupId;
        contentPanel.add(settingsView, cardName);
        cardLayout.show(contentPanel, cardName);
    }
}