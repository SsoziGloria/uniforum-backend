package GeneralUser.views.Groups;

import GeneralUser.api.ApiClient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GroupDetailsView extends JPanel {
    private final int groupId;
    private final String authToken; // Declared authToken to capture session state
    private final String accountRole; // "Lecturer" or "Student" — the logged-in user's account type,
                                       // distinct from their role within THIS group (userRole/isAdmin)
    private boolean isMember;       // Declared isMember as suggested
    private boolean isUserAdmin;
    private boolean isUserCreator;

    // Dynamic UI Components
    private JLabel groupNameLabel;
    private JTextArea descriptionArea;
    private JLabel membersCountLabel;
    private JLabel groupRoleLabel;
    private JPanel quickActionsPanel;
    private JPanel topicsContainer;
    private JPanel membersContainer;
    private JLabel createdDateLabel;
    private JLabel creatorNameLabel;
    private JPanel adminControlsPanel;
    private JPanel leaveGroupPanel;
    private JPanel participationPanel;

    // Callbacks for actions
    private Runnable onDiscussionsCallback;
    private Runnable onNewDiscussionCallback;
    private Runnable onGroupChatCallback;
    private Runnable onQuizzesCallback;
    private Runnable onManageMembersCallback;
    private Runnable onStatisticsCallback;
    private Runnable onDeleteCallback;
    private Runnable onParticipationSettingsCallback; // lecturer only — pass null to hide the button
    private Runnable onParticipationResultsCallback;
    private Runnable onJoinGroupCallback;
    private Runnable onLeaveGroupCallback;
    private Consumer<Integer> onOpenTopic;

    // 2. Full constructor updated to accept, store authToken, isMember, and correct quick action callbacks
    public GroupDetailsView(int groupId, String authToken, String groupName, String description, boolean isMember,
                            boolean isAdmin, boolean isCreator, int membersCount, String createdAt,
                            String creatorName, String userRole, String accountRole, String extra2,
                            String extra3, Runnable onBack, Runnable action1, Runnable action2,
                            Runnable action3, Runnable action4, Runnable action5,
                            Consumer<Integer> onOpenTopic,
                            Runnable onManageMembers, Runnable onStatistics, Runnable onDelete,
                            Runnable onParticipationSettings, Runnable onParticipationResults,
                            Runnable onJoinGroup) {
        this.groupId = groupId;
        this.authToken = authToken;
        this.accountRole = (accountRole != null && !accountRole.trim().isEmpty()) ? accountRole : "Student";
        this.isMember = isMember;
        this.isUserAdmin = isAdmin || (userRole != null && userRole.equalsIgnoreCase("admin"));
        this.isUserCreator = isCreator || (userRole != null && userRole.equalsIgnoreCase("creator"));
        this.onOpenTopic = onOpenTopic;
        this.onJoinGroupCallback = onJoinGroup;
        this.onLeaveGroupCallback = action5;

        // These three were previously declared but never assigned anywhere —
        // "Manage Members" / "View Statistics" / "Delete Group" were dead
        // buttons regardless of who was viewing this screen. Fixed here.
        this.onManageMembersCallback = onManageMembers;
        this.onStatisticsCallback = onStatistics;
        this.onDeleteCallback = onDelete;

        this.onParticipationSettingsCallback = onParticipationSettings;
        this.onParticipationResultsCallback = onParticipationResults;

        // Store quick action callbacks matching your updated buttons:
        // action1 = Discussions, action2 = New Discussion, action3 = Group Chat, action4 = Quizzes
        this.onDiscussionsCallback = action1;
        this.onNewDiscussionCallback = action2;
        this.onGroupChatCallback = action3;
        this.onQuizzesCallback = action4;

        initUI(groupName, description, this.isMember, this.isUserAdmin, this.isUserCreator, membersCount,
               createdAt, creatorName, userRole, onBack, action1, action2, action3, action4, action5, onOpenTopic);

        loadGroupDetails();
    }

    /**
     * True if the logged-in user's ACCOUNT is a lecturer, regardless of
     * their membership role in this specific group. Mirrors the Blade
     * condition: auth()->user()->role === 'lecturer'
     */
    private boolean isLecturerAccount() {
        return "lecturer".equalsIgnoreCase(accountRole);
    }

    private void initUI(String initialName, String initialDesc, boolean initialIsMember,
                        boolean initialIsAdmin, boolean initialIsCreator, int initialCount,
                        String initialCreated, String initialCreator, String initialRole,
                        Runnable onBack, Runnable action1, Runnable action2, Runnable action3,
                        Runnable action4, Runnable action5, Consumer<Integer> onOpenTopic) {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252));

        JScrollPane scrollPane = new JScrollPane(createMainContentPanel(
            initialName, initialDesc, initialCount, initialRole, initialCreated, initialCreator,
            initialIsMember, initialIsAdmin, initialIsCreator, onBack, action1, action2, action3, action4, action5, onOpenTopic));
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createMainContentPanel(String initialName, String initialDesc, int initialCount,
                                         String initialRole, String initialCreated, String initialCreator,
                                         boolean initialIsMember, boolean initialIsAdmin, boolean initialIsCreator,
                                         Runnable onBack, Runnable action1, Runnable action2, Runnable action3,
                                         Runnable action4, Runnable action5, Consumer<Integer> onOpenTopic) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(248, 250, 252));

        // ---------------- 1. Left-Aligned Group Banner ----------------
        JPanel bannerPanel = new JPanel(new BorderLayout());
        bannerPanel.setBackground(new Color(37, 99, 235));
        bannerPanel.setBorder(new EmptyBorder(32, 40, 32, 40));

        JPanel bannerInner = new JPanel();
        bannerInner.setLayout(new BoxLayout(bannerInner, BoxLayout.Y_AXIS));
        bannerInner.setOpaque(false);

        JLabel backLabel = new JLabel("← Back to Groups");
        backLabel.setForeground(new Color(219, 234, 254));
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        backLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (onBack != null) {
            backLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    onBack.run();
                }
            });
        }
        bannerInner.add(backLabel);
        bannerInner.add(Box.createVerticalStrut(14));

        groupNameLabel = new JLabel(initialName != null && !initialName.isEmpty() ? initialName : "Loading Group...");
        groupNameLabel.setForeground(Color.WHITE);
        groupNameLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        groupNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        bannerInner.add(groupNameLabel);
        bannerInner.add(Box.createVerticalStrut(8));

        descriptionArea = new JTextArea(initialDesc != null && !initialDesc.isEmpty() ? initialDesc : "Discuss....");
        descriptionArea.setForeground(new Color(224, 231, 255));
        descriptionArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descriptionArea.setOpaque(false);
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        descriptionArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        bannerInner.add(descriptionArea);
        bannerInner.add(Box.createVerticalStrut(24));

        JPanel statsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 48, 0));
        statsRow.setOpaque(false);
        statsRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel membersStatPanel = new JPanel();
        membersStatPanel.setLayout(new BoxLayout(membersStatPanel, BoxLayout.Y_AXIS));
        membersStatPanel.setOpaque(false);
        JLabel membersTitle = new JLabel("Members");
        membersTitle.setForeground(new Color(191, 219, 254));
        membersTitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        membersCountLabel = new JLabel(String.valueOf(initialCount > 0 ? initialCount : 1));
        membersCountLabel.setForeground(Color.WHITE);
        membersCountLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        membersStatPanel.add(membersTitle);
        membersStatPanel.add(Box.createVerticalStrut(2));
        membersStatPanel.add(membersCountLabel);

        JPanel roleStatPanel = new JPanel();
        roleStatPanel.setLayout(new BoxLayout(roleStatPanel, BoxLayout.Y_AXIS));
        roleStatPanel.setOpaque(false);
        JLabel roleTitle = new JLabel("Group Role");
        roleTitle.setForeground(new Color(191, 219, 254));
        roleTitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        groupRoleLabel = new JLabel(initialRole != null && !initialRole.isEmpty() ? initialRole : "Member");
        groupRoleLabel.setForeground(Color.WHITE);
        groupRoleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        roleStatPanel.add(roleTitle);
        roleStatPanel.add(Box.createVerticalStrut(2));
        roleStatPanel.add(groupRoleLabel);

        statsRow.add(membersStatPanel);
        statsRow.add(roleStatPanel);
        bannerInner.add(statsRow);

        bannerPanel.add(bannerInner, BorderLayout.CENTER);
        mainPanel.add(bannerPanel);

        // ---------------- 2. Main Grid Layout ----------------
        JPanel bodyWrapper = new JPanel(new GridBagLayout());
        bodyWrapper.setBackground(new Color(248, 250, 252));
        bodyWrapper.setBorder(new EmptyBorder(32, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 24);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Left Column
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.65;

        JPanel leftContentPanel = new JPanel();
        leftContentPanel.setLayout(new BoxLayout(leftContentPanel, BoxLayout.Y_AXIS));
        leftContentPanel.setOpaque(false);

        quickActionsPanel = new RoundedCardPanel("Quick Actions");
        quickActionsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 10));
        updateQuickActions(action1, action2, action3, action4);
        leftContentPanel.add(quickActionsPanel);
        leftContentPanel.add(Box.createVerticalStrut(24));

        JPanel discussionsCard = new RoundedCardPanel("Recent Discussions");
        discussionsCard.setLayout(new BorderLayout());
        topicsContainer = new JPanel();
        topicsContainer.setLayout(new BoxLayout(topicsContainer, BoxLayout.Y_AXIS));
        topicsContainer.setOpaque(false);

        JLabel emptyLbl = new JLabel("No discussions have been created yet.");
        emptyLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        emptyLbl.setForeground(new Color(100, 116, 139));
        emptyLbl.setBorder(new EmptyBorder(5, 0, 10, 0));
        topicsContainer.add(emptyLbl);

        discussionsCard.add(topicsContainer, BorderLayout.CENTER);
        leftContentPanel.add(discussionsCard);
        leftContentPanel.add(Box.createVerticalStrut(24));

        JPanel membersCard = new RoundedCardPanel("Active Members");
        membersCard.setLayout(new BorderLayout());
        membersContainer = new JPanel();
        membersContainer.setLayout(new BoxLayout(membersContainer, BoxLayout.Y_AXIS));
        membersContainer.setOpaque(false);
        membersCard.add(membersContainer, BorderLayout.CENTER);
        leftContentPanel.add(membersCard);

        bodyWrapper.add(leftContentPanel, gbc);

        // Right Sidebar
        gbc.gridx = 1;
        gbc.weightx = 0.35;
        gbc.insets = new Insets(0, 0, 0, 0);

        JPanel rightSidebarPanel = new JPanel();
        rightSidebarPanel.setLayout(new BoxLayout(rightSidebarPanel, BoxLayout.Y_AXIS));
        rightSidebarPanel.setOpaque(false);

        JPanel infoCard = new RoundedCardPanel("Group Information");
        infoCard.setLayout(new BoxLayout(infoCard, BoxLayout.Y_AXIS));

        JPanel createdRow = createInfoRow("Created", initialCreated != null && !initialCreated.equals("---") ? initialCreated : "---");
        createdDateLabel = (JLabel) createdRow.getComponent(1);

        JPanel creatorRow = createInfoRow("Group Creator", initialCreator != null && !initialCreator.equals("---") ? initialCreator : "---");
        creatorNameLabel = (JLabel) creatorRow.getComponent(1);

        infoCard.add(createdRow);
        infoCard.add(Box.createVerticalStrut(12));
        infoCard.add(creatorRow);

        rightSidebarPanel.add(infoCard);
        rightSidebarPanel.add(Box.createVerticalStrut(20));

        adminControlsPanel = new JPanel(new BorderLayout());
        adminControlsPanel.setOpaque(false);
        rightSidebarPanel.add(adminControlsPanel);

        leaveGroupPanel = new JPanel(new BorderLayout());
        leaveGroupPanel.setOpaque(false);
        updateLeaveGroup(action5);
        rightSidebarPanel.add(leaveGroupPanel);
        rightSidebarPanel.add(Box.createVerticalStrut(20));

        participationPanel = new JPanel(new BorderLayout());
        participationPanel.setOpaque(false);
        updateParticipation();
        rightSidebarPanel.add(participationPanel);

        bodyWrapper.add(rightSidebarPanel, gbc);
        mainPanel.add(bodyWrapper);

        renderAdminControlsIfEligible();
        return mainPanel;
    }

    /**
     * Matches the Blade:
     *   @if($isMember) Discussions / New Discussion / Group Chat / Quizzes
     *   @else Join Group @endif
     */
    private void updateQuickActions(Runnable onDiscussions, Runnable onNewDiscussion, Runnable onGroupChat, Runnable onQuizzes) {
        quickActionsPanel.removeAll();
        if (isMember) {
            quickActionsPanel.add(new RoundedPillButton("Discussions", new Color(37, 99, 235), Color.WHITE, onDiscussions));
            quickActionsPanel.add(new RoundedPillButton("New Discussion", new Color(37, 99, 235), Color.WHITE, onNewDiscussion));
            quickActionsPanel.add(new RoundedPillButton("Group Chat", new Color(37, 99, 235), Color.WHITE, onGroupChat));
            quickActionsPanel.add(new RoundedPillButton("Quizzes", new Color(37, 99, 235), Color.WHITE, onQuizzes));
        } else {
            RoundedPillButton joinBtn = new RoundedPillButton("Join Group", new Color(37, 99, 235), Color.WHITE, onJoinGroupCallback);
            quickActionsPanel.add(joinBtn);
        }
        quickActionsPanel.revalidate();
        quickActionsPanel.repaint();
    }

    /**
     * Matches the Blade condition exactly:
     *   $isMember && ($group->created_by == auth()->id() || $isAdmin || auth()->user()->role === 'lecturer')
     * (isMember is already guaranteed by this panel only being reachable
     * once a user has opened a group they belong to.)
     */
    private void renderAdminControlsIfEligible() {
        adminControlsPanel.removeAll();
        if (isMember && (isUserAdmin || isUserCreator || isLecturerAccount())) {
            JPanel card = new RoundedCardPanel("Admin Controls");
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

            RoundedPillButton manageBtn = new RoundedPillButton("Manage Members", new Color(239, 246, 255), new Color(29, 78, 216), onManageMembersCallback);
            manageBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

            RoundedPillButton statsBtn = new RoundedPillButton("View Statistics", new Color(239, 246, 255), new Color(29, 78, 216), onStatisticsCallback);
            statsBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

            card.add(manageBtn);
            card.add(Box.createVerticalStrut(10));
            card.add(statsBtn);

            if (isUserCreator) {
                card.add(Box.createVerticalStrut(12));
                RoundedPillButton deleteBtn = new RoundedPillButton("Delete Group", new Color(254, 242, 242), new Color(220, 38, 38), () -> {
                    // Matches the Blade's onsubmit confirm() dialog before deleting
                    int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete this group? All topics and data will be permanently removed.",
                        "Delete Group",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                    );
                    if (confirm == JOptionPane.YES_OPTION && onDeleteCallback != null) {
                        onDeleteCallback.run();
                    }
                });
                deleteBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
                card.add(deleteBtn);
            }

            adminControlsPanel.add(card, BorderLayout.CENTER);
            adminControlsPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        }
        adminControlsPanel.revalidate();
        adminControlsPanel.repaint();
    }

    /**
     * Matches the Blade:
     *   @if($isMember && $group->created_by != auth()->id())
     */
    private void updateLeaveGroup(Runnable onLeave) {
        leaveGroupPanel.removeAll();
        if (!isMember || isUserCreator) {
            leaveGroupPanel.revalidate();
            leaveGroupPanel.repaint();
            return;
        }
        JPanel card = new RoundedCardPanel(null, new Color(254, 252, 232), new Color(254, 240, 138));
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel leaveBtn = new JLabel("Leave Group", SwingConstants.CENTER);
        leaveBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        leaveBtn.setForeground(new Color(161, 98, 7));
        leaveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (onLeave != null) {
            leaveBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Matches the Blade's onsubmit confirm() dialog before leaving
                    int confirm = JOptionPane.showConfirmDialog(
                        GroupDetailsView.this,
                        "Are you sure you want to leave this group?",
                        "Leave Group",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        onLeave.run();
                    }
                }
            });
        }
        card.add(leaveBtn, BorderLayout.CENTER);
        leaveGroupPanel.add(card, BorderLayout.CENTER);
        leaveGroupPanel.revalidate();
        leaveGroupPanel.repaint();
    }

    /**
     * Renders one of two variants depending on the logged-in user's
     * ACCOUNT role — matching the two different Blade files:
     *   Lecturer: "Configure participation criteria and monitor student
     *              performance." + Participation Settings + View Scores
     *   Student:  "View your participation performance in this group."
     *              + View My Results
     */
    /**
     * Matches the Blade's outer @if($isMember) wrapping the whole
     * Participation card, plus branches on account role for the content.
     */
    private void updateParticipation() {
        participationPanel.removeAll();
        if (!isMember) {
            participationPanel.revalidate();
            participationPanel.repaint();
            return;
        }
        JPanel partCard = new RoundedCardPanel("Participation");
        partCard.setLayout(new BoxLayout(partCard, BoxLayout.Y_AXIS));

        if (isLecturerAccount()) {
            JLabel partDesc = new JLabel("<html><body style='width: 200px; color: #64748b; font-size: 11px;'>Configure participation criteria and monitor student performance.</body></html>");
            partDesc.setFont(new Font("SansSerif", Font.PLAIN, 12));
            partCard.add(partDesc);
            partCard.add(Box.createVerticalStrut(16));

            RoundedPillButton settingsBtn = new RoundedPillButton("Participation Settings", new Color(239, 246, 255), new Color(29, 78, 216), onParticipationSettingsCallback);
            settingsBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            partCard.add(settingsBtn);
            partCard.add(Box.createVerticalStrut(10));

            RoundedPillButton scoresBtn = new RoundedPillButton("View Participation Scores", new Color(239, 246, 255), new Color(29, 78, 216), onParticipationResultsCallback);
            scoresBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            partCard.add(scoresBtn);
        } else {
            JLabel partDesc = new JLabel("<html><body style='width: 200px; color: #64748b; font-size: 11px;'>View your participation performance in this group.</body></html>");
            partDesc.setFont(new Font("SansSerif", Font.PLAIN, 12));
            partCard.add(partDesc);
            partCard.add(Box.createVerticalStrut(16));

            RoundedPillButton viewBtn = new RoundedPillButton("View My Results", new Color(239, 246, 255), new Color(29, 78, 216), onParticipationResultsCallback);
            viewBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            partCard.add(viewBtn);
        }

        participationPanel.add(partCard, BorderLayout.CENTER);
        participationPanel.revalidate();
        participationPanel.repaint();
    }

    private JPanel createInfoRow(String labelText, String valueText) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);

        JLabel lbl = new JLabel(labelText);
        lbl.setForeground(new Color(148, 163, 184));
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JLabel val = new JLabel(valueText);
        val.setForeground(new Color(15, 23, 42));
        val.setFont(new Font("SansSerif", Font.BOLD, 13));

        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.EAST);
        return row;
    }

    private JPanel createMemberRow(String name, String role) {
        JPanel row = new JPanel(new BorderLayout(14, 0));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(8, 0, 8, 0));

        CircleAvatar avatar = new CircleAvatar(name);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel nameLbl = new JLabel(name);
        nameLbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        nameLbl.setForeground(new Color(15, 23, 42));

        JLabel roleLbl = new JLabel(role);
        roleLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        roleLbl.setForeground(new Color(100, 116, 139));

        textPanel.add(nameLbl);
        textPanel.add(Box.createVerticalStrut(2));
        textPanel.add(roleLbl);

        row.add(avatar, BorderLayout.WEST);
        row.add(textPanel, BorderLayout.CENTER);
        return row;
    }

    /**
     * One row per discussion in the "Recent Discussions" card — mirrors
     * the Blade's clickable <a> per topic, title + "N replies • date".
     */
    private JPanel createTopicRow(Integer topicId, String title, String repliesCount, String createdAt) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(10, 0, 10, 0));
        row.setCursor(new Cursor(Cursor.HAND_CURSOR));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLbl = new JLabel(title != null && !title.isEmpty() ? title : "Untitled discussion");
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLbl.setForeground(new Color(15, 23, 42));
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        String replies = (repliesCount == null || repliesCount.isEmpty()) ? "0" : repliesCount;
        String subtitleText = replies + " replies" + (createdAt != null && !createdAt.isEmpty() ? " • " + createdAt : "");
        JLabel subtitleLbl = new JLabel(subtitleText);
        subtitleLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitleLbl.setForeground(new Color(100, 116, 139));
        subtitleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        row.add(titleLbl);
        row.add(Box.createVerticalStrut(4));
        row.add(subtitleLbl);

        if (topicId != null && onOpenTopic != null) {
            row.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    onOpenTopic.accept(topicId);
                }
            });
        }

        return row;
    }

    // Dynamic API Data Fetcher with Exception Logging & passing the instance authToken
    private void loadGroupDetails() {
        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                return ApiClient.get("/groups/" + groupId, authToken);
            }

            @Override
            protected void done() {
                try {
                    String fullResponse = get();
                    if (fullResponse != null && !fullResponse.isEmpty()) {
                        String jsonResponse = fullResponse;
                        int colonIdx = fullResponse.indexOf(":");
                        if (colonIdx != -1 && colonIdx < 5) {
                            jsonResponse = fullResponse.substring(colonIdx + 1);
                        }

                        final String cleanJson = jsonResponse;
                        SwingUtilities.invokeLater(() -> populateDynamicData(cleanJson));
                    }
                } catch (Exception e) {
                    System.err.println("Failed to fetch group details for ID " + groupId + ": " + e.getMessage());
                }
            }
        }.execute();

        System.out.println("DEBUG: Loading group details for ID -> " + groupId);
    }

    private void populateDynamicData(String json) {
        String name = extractJsonValue(json, "group_name");
        if (name.isEmpty()) name = extractJsonValue(json, "name");
        String desc = extractJsonValue(json, "description");
        String count = extractJsonValue(json, "members_count");
        String role = extractJsonValue(json, "userRole");
        if (role.isEmpty()) role = extractJsonValue(json, "role");
        String created = extractJsonValue(json, "created_at");
        String creator = extractJsonValue(json, "creator_name");
        if (creator.isEmpty()) creator = extractJsonValue(json, "creator");

        // This is the key fix: isMember was previously only ever set once,
        // from a hardcoded `false` passed in by every dashboard, and never
        // refreshed once the real API response came back — which silently
        // broke Quick Actions, Leave Group, and Participation, all of
        // which depend on it. Now actually read from the response and
        // re-render everything that depends on it.
        String isMemberRaw = extractJsonValue(json, "isMember");
        if (isMemberRaw.isEmpty()) isMemberRaw = extractJsonValue(json, "is_member");
        boolean memberChanged = false;
        if (!isMemberRaw.isEmpty()) {
            boolean fetchedIsMember = isMemberRaw.equalsIgnoreCase("true") || isMemberRaw.equals("1");
            if (fetchedIsMember != this.isMember) {
                this.isMember = fetchedIsMember;
                memberChanged = true;
            }
        }

        if (!name.isEmpty() && groupNameLabel != null) groupNameLabel.setText(name);
        if (!desc.isEmpty() && descriptionArea != null) descriptionArea.setText(desc);
        if (!count.isEmpty() && membersCountLabel != null) membersCountLabel.setText(count);
        if (!created.isEmpty() && createdDateLabel != null) createdDateLabel.setText(created);
        if (!creator.isEmpty() && creatorNameLabel != null) creatorNameLabel.setText(creator);

        boolean roleChanged = false;
        if (!role.isEmpty() && groupRoleLabel != null && !role.equalsIgnoreCase("null")) {
            groupRoleLabel.setText(role.substring(0, 1).toUpperCase() + role.substring(1));
            if (role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("creator")) {
                this.isUserAdmin = true;
                roleChanged = true;
            }
        }

        // Re-render every section gated by isMember/role now that we have
        // the real values instead of the constructor's initial placeholders.
        if (memberChanged || roleChanged) {
            updateQuickActions(onDiscussionsCallback, onNewDiscussionCallback, onGroupChatCallback, onQuizzesCallback);
            renderAdminControlsIfEligible();
            updateLeaveGroup(onLeaveGroupCallback);
            updateParticipation();
        }

        List<String[]> members = extractMembersList(json);
        if (!members.isEmpty() && membersContainer != null) {
            membersContainer.removeAll();
            for (String[] m : members) {
                membersContainer.add(createMemberRow(m[0], m[1]));
            }
            membersContainer.revalidate();
            membersContainer.repaint();
        }

        List<Object[]> topics = extractTopicsList(json);
        if (!topics.isEmpty() && topicsContainer != null) {
            topicsContainer.removeAll();
            for (Object[] t : topics) {
                topicsContainer.add(createTopicRow((Integer) t[0], (String) t[1], (String) t[2], (String) t[3]));
            }
            topicsContainer.revalidate();
            topicsContainer.repaint();
        }
    }

    private String extractJsonValue(String json, String key) {
        try {
            String searchKey = "\"" + key + "\":";
            int startIndex = json.indexOf(searchKey);
            if (startIndex == -1) return "";
            startIndex += searchKey.length();

            while (startIndex < json.length() && (json.charAt(startIndex) == ' ' || json.charAt(startIndex) == ':')) {
                startIndex++;
            }

            if (startIndex < json.length() && json.charAt(startIndex) == '"') {
                int endIndex = json.indexOf("\"", startIndex + 1);
                return json.substring(startIndex + 1, endIndex);
            } else {
                int endIndex = json.indexOf(",", startIndex);
                if (endIndex == -1) endIndex = json.indexOf("}", startIndex);
                return json.substring(startIndex, endIndex).replace("\"", "").trim();
            }
        } catch (Exception e) {
            return "";
        }
    }

    private List<String[]> extractMembersList(String json) {
        List<String[]> members = new ArrayList<>();
        try {
            int membersIdx = json.indexOf("\"members\":");
            if (membersIdx != -1) {
                int startArr = json.indexOf("[", membersIdx);
                int endArr = json.indexOf("]", startArr);
                if (startArr != -1 && endArr != -1) {
                    String arrContent = json.substring(startArr + 1, endArr);
                    String[] items = arrContent.split("}");
                    for (String item : items) {
                        String name = extractJsonValue(item, "name");
                        if (name.isEmpty()) name = extractJsonValue(item, "username");
                        String role = extractJsonValue(item, "role");
                        if (role.isEmpty()) role = "Member";
                        if (!name.isEmpty()) {
                            members.add(new String[]{name, role});
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Ignore parsing errors
        }
        return members;
    }

    /**
     * Parses the "topics" array from GroupService::getGroupDetails()'s JSON
     * response. Each element: [topicId (Integer), title (String),
     * messagesCount (String), createdAt (String)].
     */
    private List<Object[]> extractTopicsList(String json) {
        List<Object[]> topics = new ArrayList<>();
        try {
            int topicsIdx = json.indexOf("\"topics\":");
            if (topicsIdx == -1) return topics;

            int startArr = json.indexOf("[", topicsIdx);
            if (startArr == -1) return topics;

            // Find the matching closing bracket for this array (topics can
            // contain nested objects/arrays, so a naive indexOf("]") could
            // stop too early).
            int depth = 0;
            int endArr = -1;
            for (int i = startArr; i < json.length(); i++) {
                char c = json.charAt(i);
                if (c == '[') depth++;
                if (c == ']') {
                    depth--;
                    if (depth == 0) {
                        endArr = i;
                        break;
                    }
                }
            }
            if (endArr == -1) return topics;

            String arrContent = json.substring(startArr + 1, endArr);

            // Split on "},{" boundaries between top-level topic objects.
            String[] items = arrContent.split("\\},\\s*\\{");
            for (String rawItem : items) {
                String item = rawItem;
                if (!item.trim().startsWith("{")) item = "{" + item;
                if (!item.trim().endsWith("}")) item = item + "}";

                String idStr = extractJsonValue(item, "topic_id");
                if (idStr.isEmpty()) idStr = extractJsonValue(item, "id");
                String title = extractJsonValue(item, "title");
                String repliesCount = extractJsonValue(item, "messages_count");
                String createdAt = extractJsonValue(item, "created_at");

                if (!title.isEmpty()) {
                    Integer topicId = null;
                    try {
                        topicId = Integer.parseInt(idStr.trim());
                    } catch (NumberFormatException ignored) {
                    }
                    topics.add(new Object[]{topicId, title, repliesCount, createdAt});
                }
            }
        } catch (Exception e) {
            // Ignore parsing errors — falls back to the "No discussions yet" empty state
        }
        return topics;
    }

    // --- UI DRAWING HELPERS ---

    private static class RoundedCardPanel extends JPanel {
        private final Color bgColor;
        private final Color borderColor;

        public RoundedCardPanel(String title) {
            this(title, Color.WHITE, new Color(226, 232, 240));
        }

        public RoundedCardPanel(String title, Color bg, Color border) {
            this.bgColor = bg;
            this.borderColor = border;
            setLayout(new BorderLayout());
            setOpaque(false);
            setBorder(new EmptyBorder(20, 24, 20, 24));

            if (title != null && !title.isEmpty()) {
                JLabel titleLabel = new JLabel(title);
                titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
                titleLabel.setForeground(new Color(15, 23, 42));

                JPanel titleWrapper = new JPanel(new BorderLayout());
                titleWrapper.setOpaque(false);
                titleWrapper.setBorder(new EmptyBorder(0, 0, 16, 0));
                titleWrapper.add(titleLabel, BorderLayout.WEST);
                add(titleWrapper, BorderLayout.NORTH);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
            g2.setColor(borderColor);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class RoundedPillButton extends JButton {
        private final Color normalBg;

        public RoundedPillButton(String text, Color bg, Color fg, Runnable action) {
            super(text);
            this.normalBg = bg;
            setForeground(fg);
            setFont(new Font("SansSerif", Font.BOLD, 13));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(10, 20, 10, 20));

            if (action != null) {
                addActionListener(e -> action.run());
            } else {
                setEnabled(false);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(normalBg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class CircleAvatar extends JPanel {
        private final String initial;

        public CircleAvatar(String name) {
            this.initial = (name != null && !name.trim().isEmpty()) ? name.trim().substring(0, 1).toUpperCase() : "?";
            setPreferredSize(new Dimension(40, 40));
            setMinimumSize(new Dimension(40, 40));
            setMaximumSize(new Dimension(40, 40));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(37, 99, 235));
            g2.fillOval(0, 0, getWidth(), getHeight());
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 15));
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(initial)) / 2;
            int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
            g2.drawString(initial, x, y);
            g2.dispose();
        }
    }
}