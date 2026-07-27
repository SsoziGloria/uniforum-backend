package Student.Groups;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import GeneralUser.api.ApiClient;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainGroupsView extends JPanel {

    private final Color PRIMARY_BLUE = new Color(30, 64, 175);
    private final Color PAGE_BG = new Color(248, 250, 252);
    private final Color BORDER_COLOR = new Color(226, 232, 240);
    private final Color DARK_TEXT = new Color(15, 23, 42);
    private final Color MUTED_TEXT = new Color(100, 116, 139);

    private JPanel groupsContainer; // Reference to hold/refresh group rows or empty state
    private String authToken;
    private String currentUserName;
    

    // Constructor taking a simple callback action for clicking create group
    public MainGroupsView(String token, String currentUserName, Runnable onCreateGroupClicked, Runnable onBrowseGroupsClicked) {
        this.authToken = token;
        this.currentUserName = (currentUserName != null && !currentUserName.trim().isEmpty()) ? currentUserName : "User";

        setLayout(new BorderLayout());
        setBackground(PAGE_BG);
        setBorder(new EmptyBorder(24, 28, 24, 28));

        // Top Header Section
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PAGE_BG);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(PAGE_BG);

        JLabel titleLbl = new JLabel("Groups & Collaboration");
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLbl.setForeground(DARK_TEXT);
        titlePanel.add(titleLbl);

        titlePanel.add(Box.createRigidArea(new Dimension(0, 4)));

        JLabel subLbl = new JLabel("Manage your enrolled academic groups and access group discussions.");
        subLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subLbl.setForeground(MUTED_TEXT);
        titlePanel.add(subLbl);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        // Action Buttons Panel (Create Group an Browse Groups)
        JPanel btnWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnWrapper.setBackground(PAGE_BG);

        JButton browseGroupsBtn = new JButton("🔍 Browse Groups");
        browseGroupsBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        browseGroupsBtn.setBackground(Color.WHITE);
        browseGroupsBtn.setForeground(PRIMARY_BLUE);
        browseGroupsBtn.setFocusPainted(false);
        browseGroupsBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_BLUE, 1),
            new EmptyBorder(9, 14, 9, 14)
        ));
        browseGroupsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Callback to navigate to Browse Groups view
        browseGroupsBtn.addActionListener(e -> {
            if (onBrowseGroupsClicked != null) {
            onBrowseGroupsClicked.run(); // Triggers the card layout switch
        }
    });
    
        JButton createGroupBtn = new JButton("+ Create New Group");
        createGroupBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        createGroupBtn.setBackground(PRIMARY_BLUE);
        createGroupBtn.setForeground(Color.WHITE);
        createGroupBtn.setFocusPainted(false);
        createGroupBtn.setBorder(new EmptyBorder(10, 16, 10, 16));
        createGroupBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Trigger the passed callback safely
        createGroupBtn.addActionListener(e -> {
            if (onCreateGroupClicked != null) {
                onCreateGroupClicked.run();
            }
        });

        
        btnWrapper.add(browseGroupsBtn);
        btnWrapper.add(createGroupBtn);
        headerPanel.add(btnWrapper, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Center Groups Content Container
        groupsContainer = new JPanel();
        groupsContainer.setLayout(new BoxLayout(groupsContainer, BoxLayout.Y_AXIS));
        groupsContainer.setBackground(PAGE_BG);
        groupsContainer.setBorder(new EmptyBorder(20, 0, 0, 0));



        JScrollPane scrollPane = new JScrollPane(groupsContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        refreshData();
    }

    
    public void refreshData() {
    // Clear existing UI components safely
    groupsContainer.removeAll();
    
    // Show loading indicator
    JLabel loadingLbl = new JLabel("Loading your groups...");
    loadingLbl.setFont(new Font("SansSerif", Font.ITALIC, 13));
    loadingLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
    groupsContainer.add(loadingLbl);
    
    groupsContainer.revalidate();
    groupsContainer.repaint();

    // Trigger the actual API background fetch
    fetchUserGroups();
}
    // --- FETCH GROUPS FROM LARAVEL BACKEND (`GET /groups`) ---
    public void fetchUserGroups() {
        new Thread(() -> {
            String response = ApiClient.get("/groups", authToken);
            List<String[]> groupList = new ArrayList<>();

            try {
                if (response != null && !response.isEmpty()) {
                    // Strip HTTP status prefix if present (e.g., "200: [...]")
                    String jsonBody = response;
                    if (response.contains(":")) {
                        int firstColon = response.indexOf(":");
                        try {
                            Integer.parseInt(response.substring(0, firstColon).trim());
                            jsonBody = response.substring(firstColon + 1).trim();
                        } catch (NumberFormatException ignored) {}
                    }

                    // If Laravel returns pagination data {"data": [...]}, unwrap it
                    int dataIdx = jsonBody.indexOf("\"data\":");
                    if (dataIdx != -1) {
                        int startArr = jsonBody.indexOf("[", dataIdx);
                        int endArr = jsonBody.lastIndexOf("]");
                        if (startArr != -1 && endArr != -1 && endArr > startArr) {
                            jsonBody = jsonBody.substring(startArr + 1, endArr);
                        }
                    }

                    // Split individual objects by finding closing and opening braces
                    String[] rawGroups = jsonBody.split("\\},\\s*\\{");
                    
                    for (String groupStr : rawGroups) {
                        int id = extractInt(groupStr, "id");
                        if (id == 0) id = extractInt(groupStr, "group_id");

                        String name = extractJsonValue(groupStr, "group_name");
                        if (name.isEmpty()) name = extractJsonValue(groupStr, "name");
                        
                        String desc = extractJsonValue(groupStr, "description");
                        if (desc.isEmpty()) desc = extractJsonValue(groupStr, "group_desc");

                        if (!name.isEmpty()) {
                            groupList.add(new String[]{String.valueOf(id), name, desc.isEmpty() ? "No description provided." : desc});
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


    // --- DYNAMIC RENDER METHOD HANDLING GROUPS VS EMPTY STATE ---
    public void populateGroups(List<String[]> groupList) {
        groupsContainer.removeAll();

        if (groupList == null || groupList.isEmpty()) {
            // --- EMPTY STATE PANEL ---
            JPanel emptyPanel = new JPanel();
            emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
            emptyPanel.setBackground(Color.WHITE);
            emptyPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(40, 20, 40, 20)
            ));
            emptyPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
            emptyPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel emptyIcon = new JLabel("📂");
            emptyIcon.setFont(new Font("SansSerif", Font.PLAIN, 32));
            emptyIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
            emptyPanel.add(emptyIcon);

            emptyPanel.add(Box.createRigidArea(new Dimension(0, 10)));

            JLabel emptyText = new JLabel("You haven't joined any groups yet.");
            emptyText.setFont(new Font("SansSerif", Font.BOLD, 14));
            emptyText.setForeground(DARK_TEXT);
            emptyText.setAlignmentX(Component.CENTER_ALIGNMENT);
            emptyPanel.add(emptyText);

            emptyPanel.add(Box.createRigidArea(new Dimension(0, 4)));

            JLabel emptySubtext = new JLabel("Click '+ Create New Group' above to start one or join an existing group.");
            emptySubtext.setFont(new Font("SansSerif", Font.PLAIN, 12));
            emptySubtext.setForeground(MUTED_TEXT);
            emptySubtext.setAlignmentX(Component.CENTER_ALIGNMENT);
            emptyPanel.add(emptySubtext);

            groupsContainer.add(emptyPanel);
        } else {
            // --- POPULATED GROUPS LIST ---
            for (String[] groupData : groupList) {
               int groupId = Integer.parseInt(groupData[0]);
               String groupName = groupData[1];
                String groupDesc = groupData[2]; 

                JPanel card = createWebUIGroupCard(groupId, groupName, groupDesc);
                groupsContainer.add(card);
                groupsContainer.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        groupsContainer.revalidate();
        groupsContainer.repaint();
    }

   private JPanel createWebUIGroupCard(int groupId, String groupName, String description) {
    JPanel card = new JPanel(new BorderLayout());
    card.setBackground(Color.WHITE);
    card.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(BORDER_COLOR, 1),
        new EmptyBorder(20, 20, 20, 20))
    );
    card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 115));
    card.setCursor(new Cursor(Cursor.HAND_CURSOR));

    JPanel leftContent = new JPanel();
    leftContent.setLayout(new BoxLayout(leftContent, BoxLayout.Y_AXIS));
    leftContent.setBackground(Color.WHITE);
    leftContent.setCursor(new Cursor(Cursor.HAND_CURSOR));

    JLabel nameLbl = new JLabel(groupName);
    nameLbl.setFont(new Font("SansSerif", Font.BOLD, 15));
    nameLbl.setForeground(DARK_TEXT);
    leftContent.add(nameLbl);

    leftContent.add(Box.createRigidArea(new Dimension(0, 5)));

    JLabel descLbl = new JLabel(description);
    descLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
    descLbl.setForeground(MUTED_TEXT);
    leftContent.add(descLbl);

    card.add(leftContent, BorderLayout.CENTER);

    // Add click event listener to route user directly to ChatView for this group
    card.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            openChatView(groupId, groupName);
        }
    });
    
    // Ensure inner text components also pass clicks through to the card
    for (Component comp : leftContent.getComponents()) {
        comp.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                openChatView(groupId, groupName);
            }
        });
    }

    return card;
   }


private void openChatView(int groupId, String groupName) {
    // Navigate to ChatView inside your parent container/CardLayout
    Container parent = this.getParent();
    if (parent != null && parent.getLayout() instanceof CardLayout) {
        CardLayout cl = (CardLayout) parent.getLayout();
        
        // Create or show ChatView dynamically
       JPanel chatView = new ChatView(
    groupId, 
    null,                          // topicId (null for general chat stream)
    groupName,                     // group name string
    "Live Academic Discussion Stream", // subTitle string
    authToken,                     // auth token string
    currentUserName,           // logged-in user's name string
    () -> {                        // onBackClicked callback
        cl.show(parent, "GROUPS");
    }, 
    () -> {                        // onSwitchToTopics callback
        // Action to trigger when clicking "Topic Discussions"
        // e.g., switch to your topics view or open a topic selection dialog
        cl.show(parent, "TOPICS_VIEW_" + groupId);
    }
);

parent.add(chatView, "CHAT_VIEW_" + groupId);
cl.show(parent, "CHAT_VIEW_" + groupId);
    }
}
}
