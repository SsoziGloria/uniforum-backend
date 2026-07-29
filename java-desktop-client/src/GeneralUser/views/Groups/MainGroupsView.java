package GeneralUser.views.Groups;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import GeneralUser.api.ApiClient;
import Lecturer.Participation.LecturerParticipationScoresView;
import Lecturer.Participation.LecturerParticipationSettingsView;
import Student.Participation.ParticipationResultsView;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MainGroupsView extends JPanel {

    private final Color PRIMARY_BLUE  = new Color(37, 99, 235);    
    private final Color PRIMARY_HOVER = new Color(29, 78, 216);    
    private final Color PAGE_BG       = new Color(248, 250, 252);  
    private final Color BORDER_COLOR  = new Color(226, 232, 240);  
    private final Color DARK_TEXT     = new Color(15, 23, 42);     
    private final Color MUTED_TEXT    = new Color(100, 116, 139);  
    private final Color BADGE_GREEN   = new Color(21, 128, 61);    
    private final Color BADGE_BG      = new Color(220, 252, 231);  
    private final Color ORANGE_ACTIVE = new Color(249, 115, 22);   

    private JPanel groupsContainer; 
    private String authToken;
    private String currentUserName;
    private String accountRole;
    private final Runnable onCreateGroupClicked;
    private final Runnable onBrowseGroupsClicked;
    private final java.util.function.BiConsumer<Integer, String> onOpenChatClicked;
    private final java.util.function.BiConsumer<Integer, Integer> onOpenTopicClicked;

    public MainGroupsView(String token, String currentUserName, String accountRole,
                          Runnable onCreateGroupClicked, Runnable onBrowseGroupsClicked,
                          java.util.function.BiConsumer<Integer, String> onOpenChatClicked,
                          java.util.function.BiConsumer<Integer, Integer> onOpenTopicClicked) {
        this.authToken = token;
        this.currentUserName = (currentUserName != null && !currentUserName.trim().isEmpty()) ? currentUserName : "User";
        this.accountRole = (accountRole != null && !accountRole.trim().isEmpty()) ? accountRole : "Student";
        this.onCreateGroupClicked = onCreateGroupClicked;
        this.onBrowseGroupsClicked = onBrowseGroupsClicked;
        this.onOpenChatClicked = onOpenChatClicked;
        this.onOpenTopicClicked = onOpenTopicClicked;

        setLayout(new BorderLayout());
        setBackground(PAGE_BG);

        System.setProperty("awt.useSystemAAFontSettings", "lcd");
        System.setProperty("swing.aatext", "true");

        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(PAGE_BG);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
            new EmptyBorder(28, 32, 28, 32)
        ));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);

        JLabel titleLbl = new JLabel("My Discussion Groups");
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLbl.setForeground(DARK_TEXT);
        titlePanel.add(titleLbl);

        titlePanel.add(Box.createRigidArea(new Dimension(0, 6)));

        JLabel subLbl = new JLabel("Browse your academic groups and continue collaborating with classmates.");
        subLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subLbl.setForeground(MUTED_TEXT);
        titlePanel.add(subLbl);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        outerPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        bodyPanel.setBackground(PAGE_BG);
        bodyPanel.setBorder(new EmptyBorder(32, 32, 32, 32));

        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.X_AXIS));
        toolbar.setBackground(PAGE_BG);
        toolbar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        toolbar.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField searchField = new JTextField() {
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
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        searchField.setForeground(DARK_TEXT);
        searchField.setOpaque(false);
        searchField.setBorder(new EmptyBorder(10, 14, 10, 14));
        searchField.setToolTipText("Search groups...");
        
        searchField.setText("Search groups...");
        searchField.setForeground(MUTED_TEXT);
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals("Search groups...")) {
                    searchField.setText("");
                    searchField.setForeground(DARK_TEXT);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(MUTED_TEXT);
                    searchField.setText("Search groups...");
                }
            }
        });

        toolbar.add(searchField);
        toolbar.add(Box.createRigidArea(new Dimension(16, 0)));

        JButton browseGroupsBtn = createRoundedButton("Browse Groups", Color.WHITE, PRIMARY_BLUE, BORDER_COLOR);
        browseGroupsBtn.addActionListener(e -> {
            if (onBrowseGroupsClicked != null) {
                onBrowseGroupsClicked.run();
            }
        });
        toolbar.add(browseGroupsBtn);

        toolbar.add(Box.createRigidArea(new Dimension(12, 0)));

        JButton createGroupBtn = createRoundedButton("+ Create Group", PRIMARY_BLUE, Color.WHITE, null);
        createGroupBtn.addActionListener(e -> {
            if (onCreateGroupClicked != null) {
                onCreateGroupClicked.run();
            }
        });
        toolbar.add(createGroupBtn);

        bodyPanel.add(toolbar);
        bodyPanel.add(Box.createRigidArea(new Dimension(0, 24)));

        groupsContainer = new JPanel();
        groupsContainer.setLayout(new BoxLayout(groupsContainer, BoxLayout.Y_AXIS));
        groupsContainer.setBackground(PAGE_BG);
        groupsContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        bodyPanel.add(groupsContainer);

        JScrollPane scrollPane = new JScrollPane(bodyPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        outerPanel.add(scrollPane, BorderLayout.CENTER);

        add(outerPanel, BorderLayout.CENTER);

        refreshData();
    }

    public void refreshData() {
        groupsContainer.removeAll();

        JLabel loadingLbl = new JLabel("Loading your groups...");
        loadingLbl.setFont(new Font("SansSerif", Font.ITALIC, 13));
        loadingLbl.setForeground(MUTED_TEXT);
        loadingLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        groupsContainer.add(loadingLbl);

        groupsContainer.revalidate();
        groupsContainer.repaint();

        fetchUserGroups();
    }

    public void fetchUserGroups() {
        new Thread(() -> {
            String response = ApiClient.get("/groups", authToken);
            List<String[]> groupList = new ArrayList<>();

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

                    int dataIdx = jsonBody.indexOf("\"data\":");
                    if (dataIdx != -1) {
                        int startArr = jsonBody.indexOf("[", dataIdx);
                        int endArr = jsonBody.lastIndexOf("]");
                        if (startArr != -1 && endArr != -1 && endArr > startArr) {
                            jsonBody = jsonBody.substring(startArr + 1, endArr);
                        }
                    }

                    String[] rawGroups = jsonBody.split("\\},\\s*\\{");

                    for (String groupStr : rawGroups) {
                        int id = extractInt(groupStr, "id");
                        if (id == 0) id = extractInt(groupStr, "group_id");

                        String name = extractJsonValue(groupStr, "group_name");
                        if (name.isEmpty()) name = extractJsonValue(groupStr, "name");

                        String desc = extractJsonValue(groupStr, "description");
                        if (desc.isEmpty()) desc = extractJsonValue(groupStr, "group_desc");

                        int membersCount = extractInt(groupStr, "members_count");
                        if (membersCount == 0) membersCount = extractInt(groupStr, "members_count_count");

                        if (!name.isEmpty()) {
                            groupList.add(new String[]{
                                String.valueOf(id), 
                                name, 
                                desc.isEmpty() ? "No description provided." : desc,
                                String.valueOf(membersCount > 0 ? membersCount : 1)
                            });
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            SwingUtilities.invokeLater(() -> {
                populateGroups(groupList);
            });
        }).start();
    }

    private String extractJsonValue(String json, String key) {
        try {
            String searchKey = "\"" + key + "\":";
            int start = json.indexOf(searchKey);
            if (start == -1) return "";
            start += searchKey.length();
            if (json.charAt(start) == '"') {
                start++;
                int end = json.indexOf("\"", start);
                return json.substring(start, end);
            } else {
                int end = json.indexOf(",", start);
                if (end == -1) end = json.indexOf("}", start);
                return json.substring(start, end).trim();
            }
        } catch (Exception e) {
            return "";
        }
    }

    private int extractInt(String json, String key) {
        try {
            String searchKey = "\"" + key + "\":";
            int start = json.indexOf(searchKey);
            if (start == -1) return 0;
            start += searchKey.length();
            int end = start;
            while (end < json.length() && Character.isDigit(json.charAt(end))) {
                end++;
            }
            return Integer.parseInt(json.substring(start, end));
        } catch (Exception e) {
            return 0;
        }
    }

    public void populateGroups(List<String[]> groupList) {
        groupsContainer.removeAll();

        if (groupList == null || groupList.isEmpty()) {
            RoundedPanel emptyPanel = new RoundedPanel(16, Color.WHITE, BORDER_COLOR);
            emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
            emptyPanel.setBorder(new EmptyBorder(36, 24, 36, 24));
            emptyPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
            emptyPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel emptyText = new JLabel("You have not joined any groups yet.");
            emptyText.setFont(new Font("SansSerif", Font.PLAIN, 14));
            emptyText.setForeground(MUTED_TEXT);
            emptyText.setAlignmentX(Component.CENTER_ALIGNMENT);
            emptyPanel.add(emptyText);

            emptyPanel.add(Box.createRigidArea(new Dimension(0, 10)));

            JButton createFirstGroupBtn = createRoundedButton("+ Create Group", PRIMARY_BLUE, Color.WHITE, null);
            createFirstGroupBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            createFirstGroupBtn.addActionListener(e -> {
                if (onCreateGroupClicked != null) onCreateGroupClicked.run();
            });
            emptyPanel.add(createFirstGroupBtn);

            groupsContainer.add(emptyPanel);
        } else {
            for (String[] groupData : groupList) {
                int groupId = Integer.parseInt(groupData[0]);
                String groupName = groupData[1];
                String groupDesc = groupData[2];
                String memberCount = groupData.length > 3 ? groupData[3] : "1";

                JPanel card = createWebUIGroupCard(groupId, groupName, groupDesc, memberCount);
                groupsContainer.add(card);
                groupsContainer.add(Box.createRigidArea(new Dimension(0, 16)));
            }
        }

        groupsContainer.revalidate();
        groupsContainer.repaint();
    }

    private JPanel createWebUIGroupCard(int groupId, String groupName, String description, String memberCount) {
        RoundedPanel card = new RoundedPanel(16, Color.WHITE, BORDER_COLOR);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(22, 24, 22, 24));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);

        JLabel groupBadge = new JLabel(" Academic Group ");
        groupBadge.setFont(new Font("SansSerif", Font.BOLD, 10));
        groupBadge.setForeground(BADGE_GREEN);
        groupBadge.setOpaque(true);
        groupBadge.setBackground(BADGE_BG);
        groupBadge.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        topRow.add(groupBadge, BorderLayout.WEST);

        JLabel membersLbl = new JLabel(memberCount + " Members");
        membersLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        membersLbl.setForeground(MUTED_TEXT);
        topRow.add(membersLbl, BorderLayout.EAST);

        contentPanel.add(topRow);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 14)));

        JLabel nameLbl = new JLabel(groupName);
        nameLbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        nameLbl.setForeground(DARK_TEXT);
        contentPanel.add(nameLbl);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        JLabel descLbl = new JLabel("<html><div style='width: 680px; color: #475569; font-size: 12px;'>" + description + "</div></html>");
        contentPanel.add(descLbl);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 16)));

        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setOpaque(false);

        JLabel activeLbl = new JLabel("● Active");
        activeLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        activeLbl.setForeground(ORANGE_ACTIVE);
        bottomRow.add(activeLbl, BorderLayout.WEST);

        JLabel openLbl = new JLabel("Open →");
        openLbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        openLbl.setForeground(PRIMARY_BLUE);
        bottomRow.add(openLbl, BorderLayout.EAST);

        contentPanel.add(bottomRow);

        card.add(contentPanel, BorderLayout.CENTER);

        MouseAdapter clickAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fetchAndOpenGroupDetails(groupId);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                openLbl.setText("Open → ");
            }
            @Override
            public void mouseExited(MouseEvent e) {
                openLbl.setText("Open →");
            }
        };

        card.addMouseListener(clickAdapter);

        return card;
    }

    private void fetchAndOpenGroupDetails(int groupId) {
        new Thread(() -> {
            String response = ApiClient.get("/groups/" + groupId, authToken);

            boolean isAdmin = false;
            boolean isMember = false; 
            String fetchedGroupName = "";
            String fetchedDescription = "";

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
                    isMember = extractBoolean(jsonBody, "is_member");

                    if (!isMember) {
                        if (extractBoolean(jsonBody, "joined") || extractBoolean(jsonBody, "is_joined")) {
                            isMember = true;
                        }
                    }

                    if (!isMember) {
                        String userRole = extractJsonValue(jsonBody, "user_role");
                        if (userRole != null && !userRole.isEmpty() && !userRole.equalsIgnoreCase("null")) {
                            isMember = true;
                        }
                    }

                    fetchedGroupName = extractJsonValue(jsonBody, "group_name");
                    if (fetchedGroupName.isEmpty()) fetchedGroupName = extractJsonValue(jsonBody, "name");
                    
                    fetchedDescription = extractJsonValue(jsonBody, "description");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            final boolean adminStatus = isAdmin;
            final boolean memberStatus = isMember; 
            final String gName = fetchedGroupName.isEmpty() ? "Group Details" : fetchedGroupName;
            final String gDesc = fetchedDescription.isEmpty() ? "No description provided." : fetchedDescription;

            SwingUtilities.invokeLater(() -> {
                Container parent = this.getParent();
                if (parent != null && parent.getLayout() instanceof CardLayout) {
                    CardLayout cl = (CardLayout) parent.getLayout();

                    
                    GroupDetailsView detailsView = new GroupDetailsView(
                        groupId,
                        authToken,                   
                        gName,
                        gDesc,
                        memberStatus,                
                        adminStatus,                 
                        false,                       
                        1,                           
                        "",                          
                        "",                          
                        "Member",                    
                        accountRole,
                        null,                        
                        null,                        
                        () -> cl.show(parent, "GROUPS"), // onBack
                        
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
                            openChatView(groupId, gName, authToken);
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
                                    JOptionPane.showMessageDialog(parent, "You have left the group.");
                                    cl.show(parent, "GROUPS");
                                });
                            }).start();
                        },
                        
                        // onOpenTopic callback
                        topicId -> {                 
                            if (onOpenTopicClicked != null) {
                                onOpenTopicClicked.accept(groupId, topicId);
                            }
                        },

                        // onManageMembers
                        () -> {
                            ManageMembersView view = new ManageMembersView(groupId, authToken,
                                () -> cl.show(parent, "GROUP_DETAILS_" + groupId));
                            String cardName = "MANAGE_MEMBERS_" + groupId;
                            parent.add(view, cardName);
                            cl.show(parent, cardName);
                        },

                        // onStatistics
                        () -> {
                            StatisticsView view = new StatisticsView(groupId, authToken,
                                () -> cl.show(parent, "GROUP_DETAILS_" + groupId));
                            String cardName = "GROUP_STATISTICS_" + groupId;
                            parent.add(view, cardName);
                            cl.show(parent, cardName);
                        },

                        // onDelete
                        () -> {
                            new Thread(() -> {
                                ApiClient.delete("/groups/" + groupId, authToken);
                                SwingUtilities.invokeLater(() -> {
                                    JOptionPane.showMessageDialog(parent, "Group deleted successfully.");
                                    cl.show(parent, "GROUPS");
                                });
                            }).start();
                        },

                        // onParticipationSettings (lecturer only — GroupDetailsView hides this button for students)
                        () -> {
                            LecturerParticipationSettingsView view = new LecturerParticipationSettingsView(
                                groupId, authToken, () -> cl.show(parent, "GROUP_DETAILS_" + groupId));
                            String cardName = "PARTICIPATION_SETTINGS_" + groupId;
                            parent.add(view, cardName);
                            cl.show(parent, cardName);
                        },

                        // onParticipationResults — branches by account role since the destination view differs
                        "Lecturer".equalsIgnoreCase(accountRole)
                            ? (Runnable) () -> {
                                LecturerParticipationScoresView view = new LecturerParticipationScoresView(
                                    groupId, authToken,
                                    () -> cl.show(parent, "GROUP_DETAILS_" + groupId),
                                    () -> {
                                        LecturerParticipationSettingsView settingsView = new LecturerParticipationSettingsView(
                                            groupId, authToken, () -> cl.show(parent, "GROUP_DETAILS_" + groupId));
                                        String cardName = "PARTICIPATION_SETTINGS_" + groupId;
                                        parent.add(settingsView, cardName);
                                        cl.show(parent, cardName);
                                    }
                                );
                                String cardName = "PARTICIPATION_SCORES_" + groupId;
                                parent.add(view, cardName);
                                cl.show(parent, cardName);
                            }
                            : (Runnable) () -> {
                                ParticipationResultsView view = new ParticipationResultsView(
                                    groupId, authToken, () -> cl.show(parent, "GROUP_DETAILS_" + groupId));
                                String cardName = "PARTICIPATION_RESULTS_" + groupId;
                                parent.add(view, cardName);
                                cl.show(parent, cardName);
                            },

                        // onJoinGroup
                        () -> {
                            String joinCardName = "JOIN_GROUP_" + groupId;
                            JoinGroupView joinView = new JoinGroupView(
                                groupId, "Academic Group", gName,
                                gDesc,
                                authToken,
                                () -> cl.show(parent, "GROUPS"),
                                () -> cl.show(parent, "GROUP_DETAILS_" + groupId)
                            );
                            parent.add(joinView, joinCardName);
                            cl.show(parent, joinCardName);
                        }
                    );
                    String detailCardName = "GROUP_DETAILS_" + groupId;
                    
                    parent.add(detailsView, detailCardName);
                    cl.show(parent, detailCardName);
                    parent.revalidate();
                    parent.repaint();
                }
            });
        }).start();
    }

    private boolean extractBoolean(String json, String key) {
        try {
            int dataIdx = json.indexOf("\"data\":");
            String targetJson = json;
            if (dataIdx != -1) {
                int braceIdx = json.indexOf("{", dataIdx);
                if (braceIdx != -1) {
                    targetJson = json.substring(braceIdx);
                }
            }

            String searchKey = "\"" + key + "\":";
            int start = targetJson.indexOf(searchKey);
            if (start == -1) return false;
            
            start += searchKey.length();
            int end = start;
            while (end < targetJson.length() && targetJson.charAt(end) != ',' && targetJson.charAt(end) != '}') {
                end++;
            }
            
            String rawVal = targetJson.substring(start, end).trim();
            rawVal = rawVal.replace("\"", "").replace("'", "").toLowerCase();
            
            return rawVal.equals("true") || rawVal.equals("1");
        } catch (Exception e) {
            return false;
        }
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
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(new EmptyBorder(10, 18, 10, 18));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (bg.equals(PRIMARY_BLUE)) btn.setBackground(PRIMARY_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (bg.equals(PRIMARY_BLUE)) btn.setBackground(PRIMARY_BLUE);
            }
        });

        return btn;
    }

    private void openGroupTopicsView(int groupId, String topicsResponse) {
        // Placeholder: implement group topics view navigation when available.
    }

    private void openNewDiscussionView(int groupId, String authToken) {
        // Placeholder: implement new discussion creation view navigation when available.
    }

    private void openChatView(int groupId, String groupName, String authToken) {
        if (onOpenChatClicked != null) {
            onOpenChatClicked.accept(groupId, groupName);
        }
    }

    private void openQuizzesView(int groupId, String authToken) {
        // Placeholder: implement quizzes view navigation when available.
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