package Student.Groups;

import GeneralUser.api.ApiClient;
import Student.Groups.Discussions.NewDiscussionView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Consumer;

public class GroupDetailsView extends JPanel {

    private final Color PRIMARY_BLUE = new Color(30, 64, 175);
    private final Color PAGE_BG = new Color(248, 250, 252);
    private final Color BORDER_COLOR = new Color(226, 232, 240);
    private final Color DARK_TEXT = new Color(15, 23, 42);
    private final Color MUTED_TEXT = new Color(100, 116, 139);

    private String authToken;
    private int groupId;
    private JPanel contentContainer;

    public GroupDetailsView(int groupId, 
                        String groupTag, 
                        String groupName, 
                        String groupDescription, 
                        boolean isMember,   
                        boolean isAdmin, 
                        String authToken, 
                        Runnable onBackClicked, 
                        Runnable onJoinClicked, 
                        Runnable onOpenChatClicked, 
                        Runnable onViewMembersClicked, 
                        Consumer<Integer> onTopicSelected) {
        this.groupId = groupId;
        this.authToken = authToken;

        setLayout(new BorderLayout());
        setBackground(PAGE_BG);

        contentContainer = new JPanel();
        contentContainer.setLayout(new BoxLayout(contentContainer, BoxLayout.Y_AXIS));
        contentContainer.setBackground(PAGE_BG);

        // --- TOP BANNER SECTION ---
        JPanel bannerPanel = new JPanel();
        bannerPanel.setLayout(new BoxLayout(bannerPanel, BoxLayout.Y_AXIS));
        bannerPanel.setBackground(PRIMARY_BLUE);
        bannerPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        bannerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        bannerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 240));

        JButton backBtn = new JButton("← Back to Groups");
        backBtn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        backBtn.setForeground(new Color(191, 219, 254));
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.addActionListener(e -> {
            if (onBackClicked != null) onBackClicked.run();
        });
        bannerPanel.add(backBtn);
        bannerPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        JLabel titleLbl = new JLabel(groupName);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLbl.setForeground(Color.WHITE);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        bannerPanel.add(titleLbl);

        bannerPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        JLabel descLbl = new JLabel("<html><body style='width: 750px'>" + groupDescription + "</body></html>");
        descLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        descLbl.setForeground(new Color(224, 242, 254));
        descLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        bannerPanel.add(descLbl);

        contentContainer.add(bannerPanel);

        // --- MAIN BODY CONTAINER ---
        JPanel bodyContainer = new JPanel();
        bodyContainer.setLayout(new BorderLayout(24, 0));
        bodyContainer.setBackground(PAGE_BG);
        bodyContainer.setBorder(new EmptyBorder(24, 40, 24, 40));
        bodyContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        bodyContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));

        // Left Column (Quick Actions & Discussion Previews)
        JPanel leftCol = new JPanel();
        leftCol.setLayout(new BoxLayout(leftCol, BoxLayout.Y_AXIS));
        leftCol.setBackground(PAGE_BG);
        leftCol.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Quick Actions Card
        JPanel actionsCard = createCardPanel("Quick Actions");
        
        int columnsCount = isMember ? 2 : 3;
        JPanel btnGrid = new JPanel(new GridLayout(1, columnsCount, 12, 0));
        btnGrid.setBackground(Color.WHITE);
        btnGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JButton newDiscBtn = createStyledButton("New Discussion", PRIMARY_BLUE, Color.WHITE);
        JButton chatBtn = createStyledButton("Group Chat", PRIMARY_BLUE, Color.WHITE);

        newDiscBtn.addActionListener(e -> {
            if (!isMember) {
                JOptionPane.showMessageDialog(this, "You must join this group before posting new discussions.", "Membership Required", JOptionPane.WARNING_MESSAGE);
            } else {
<<<<<<< HEAD:java-desktop-client/src/Student/Groups/GroupDetailsView.java
                // Handle new discussion logic here
            NewDiscussionView createView = new NewDiscussionView(groupId, authToken);
            createView.setVisible(true);
=======
                NewDiscussionView createView = new NewDiscussionView(groupId, authToken);
                createView.setVisible(true);
>>>>>>> Tracy-java-ui:java-desktop-client/src/GeneralUser/views/Groups/GroupDetailsView.java
            }
        });

        chatBtn.addActionListener(e -> {
            if (onOpenChatClicked != null) onOpenChatClicked.run();
        });

        btnGrid.add(newDiscBtn);

        if (!isMember) {
            JButton joinGroupBtn = createStyledButton("Join Group", PRIMARY_BLUE, Color.WHITE);
            joinGroupBtn.addActionListener(e -> {
<<<<<<< HEAD:java-desktop-client/src/Student/Groups/GroupDetailsView.java
                if (onJoinClicked != null) onJoinClicked.run();
=======
                if (onJoinClicked != null) {
                    onJoinClicked.run();
                }
>>>>>>> Tracy-java-ui:java-desktop-client/src/GeneralUser/views/Groups/GroupDetailsView.java
            });
            btnGrid.add(joinGroupBtn);
        }

        btnGrid.add(chatBtn);
        actionsCard.add(btnGrid);
        leftCol.add(actionsCard);

        leftCol.add(Box.createRigidArea(new Dimension(0, 20)));

        // Recent Discussions Card
        JPanel discussionsCard = createCardPanel(isMember ? "Topic Discussions" : "Topic Discussions (Read-Only Preview)");
        
        if (!isMember) {
            JLabel hintLbl = new JLabel("<html><div style='color: #64748B; font-size: 11px; margin-bottom: 8px;'>Non-members can view topic discussions to evaluate group activity before joining.</div></html>");
            hintLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            discussionsCard.add(hintLbl);
            discussionsCard.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        discussionsCard.add(createClickableDiscussionRow(101, "Laravel Authentication Problem", "18 replies • Last updated 20 mins ago", onTopicSelected));
        discussionsCard.add(Box.createRigidArea(new Dimension(0, 10)));
        discussionsCard.add(createClickableDiscussionRow(102, "Software Engineering Assignment Discussion", "27 replies • Yesterday", onTopicSelected));
        
        leftCol.add(discussionsCard);

        // Right Column (Sidebar Information & Admin Controls)
        JPanel rightCol = new JPanel();
        rightCol.setLayout(new BoxLayout(rightCol, BoxLayout.Y_AXIS));
        rightCol.setBackground(PAGE_BG);
        rightCol.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightCol.setPreferredSize(new Dimension(320, 0));

        // Group Information Card
        JPanel infoCard = createCardPanel("Group Information");
        infoCard.add(createInfoRow("Created", "Jan 2026"));
        infoCard.add(Box.createRigidArea(new Dimension(0, 8)));
        infoCard.add(createInfoRow("Group Creator", "Gloria Ssozi"));
        infoCard.add(Box.createRigidArea(new Dimension(0, 8)));
        infoCard.add(createInfoRow("Lecturer", "Dr. Karungi Shamim"));
        rightCol.add(infoCard);

        if (isAdmin) {
            rightCol.add(Box.createRigidArea(new Dimension(0, 20)));

            JPanel adminCard = createCardPanel("Admin Controls");
            JButton manageMembersBtn = createActionRowButton("Manage Members");
            manageMembersBtn.addActionListener(e -> {
                if (onViewMembersClicked != null) onViewMembersClicked.run();
            });
            adminCard.add(manageMembersBtn);
            adminCard.add(Box.createRigidArea(new Dimension(0, 8)));
            
            JButton viewStatsBtn = createActionRowButton("View Statistics");
            viewStatsBtn.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Loading analytics and metrics for Group #" + groupId, "Group Statistics", JOptionPane.INFORMATION_MESSAGE);
            });
            adminCard.add(viewStatsBtn);
            rightCol.add(adminCard);
        }

        // Split Layout Container
        JPanel splitLayout = new JPanel(new BorderLayout(24, 0));
        splitLayout.setBackground(PAGE_BG);
        splitLayout.add(leftCol, BorderLayout.CENTER);
        splitLayout.add(rightCol, BorderLayout.EAST);

        bodyContainer.add(splitLayout, BorderLayout.CENTER);
        contentContainer.add(bodyContainer);

        JScrollPane scrollPane = new JScrollPane(
            contentContainer,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setWheelScrollingEnabled(true);
        
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createCardPanel(String title) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLbl.setForeground(DARK_TEXT);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(titleLbl);
        card.add(Box.createRigidArea(new Dimension(0, 14)));

        return card;
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel createClickableDiscussionRow(int topicId, String title, String meta, Consumer<Integer> onTopicSelected) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
        row.setBackground(new Color(250, 250, 250));
        row.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(10, 12, 10, 12)
        ));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        row.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        titleLbl.setForeground(PRIMARY_BLUE);
        row.add(titleLbl);

        row.add(Box.createRigidArea(new Dimension(0, 4)));

        JLabel metaLbl = new JLabel(meta);
        metaLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        metaLbl.setForeground(MUTED_TEXT);
        row.add(metaLbl);

        row.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (onTopicSelected != null) {
                    onTopicSelected.accept(topicId);
                }
            }
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                row.setBackground(new Color(239, 246, 255));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                row.setBackground(new Color(250, 250, 250));
            }
        });

        return row;
    }

    private JPanel createInfoRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

        JLabel lblKey = new JLabel(label);
        lblKey.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblKey.setForeground(MUTED_TEXT);

        JLabel lblVal = new JLabel(value);
        lblVal.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblVal.setForeground(DARK_TEXT);

        row.add(lblKey, BorderLayout.WEST);
        row.add(lblVal, BorderLayout.EAST);
        return row;
    }

    private JButton createActionRowButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setForeground(PRIMARY_BLUE);
        btn.setBackground(new Color(239, 246, 255));
        btn.setBorder(new EmptyBorder(10, 14, 10, 14));
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        return btn;
    }
}