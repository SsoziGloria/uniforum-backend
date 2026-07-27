package Lecturer;

import javax.swing.*;
import java.awt.*;

import GeneralUser.views.Groups.MainGroupsView;
import GeneralUser.views.Groups.BrowseGroupsView;
import GeneralUser.views.Groups.CreateGroupView;
import GeneralUser.views.Groups.GroupDetailsView;
import GeneralUser.views.Groups.ChatView;
import GeneralUser.views.Groups.JoinGroupView;

public class LecturerDashboard extends JFrame {

    private String authToken;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public LecturerDashboard(String token) {
        this.authToken = token;

        setTitle("Lecturer Dashboard - Academic Collaboration");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // --- 1. SETUP CARDLAYOUT CONTAINER ---
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // --- 2. INITIALIZE VIEWS & REGISTER THEM TO CARDLAYOUT ---

        // View 1: Main Groups View (Lecturer's Enrolled/Managed Groups)
        MainGroupsView mainGroupsView = new MainGroupsView(
            authToken,
            "Lecturer", // current user name fallback
            () -> cardLayout.show(contentPanel, "CREATE_GROUP"),   // On Create Group clicked
            () -> cardLayout.show(contentPanel, "BROWSE_GROUPS"),   // On Browse Groups clicked
            (groupId, groupName) -> {                             // On Open Chat clicked
                openChatView(groupId, groupName, "General Chat", null);
            },
            (groupId, topicId) -> {                               // On Open Topic clicked
                openChatView(groupId, "Group Discussion", "Topic Discussion", topicId);
            }
        );

        // View 2: Browse Groups View
        BrowseGroupsView browseGroupsView = new BrowseGroupsView(
            authToken,
            () -> cardLayout.show(contentPanel, "MAIN_GROUPS"), // Back clicked
            groupId -> {
                // View Details clicked from browse
                fetchAndOpenGroupDetails(groupId);
            },
            groupId -> {
                // Join clicked from browse (or request join)
                openJoinGroupView(groupId);
            }
        );

        // View 3: Create Group View
        CreateGroupView createGroupView = new CreateGroupView(
            authToken,
            () -> {
                // On group created successfully -> return to main groups and refresh
                cardLayout.show(contentPanel, "MAIN_GROUPS");
                mainGroupsView.refreshData();
            },
            () -> cardLayout.show(contentPanel, "MAIN_GROUPS") // On Cancel clicked
        );

        // Add views to the CardLayout container
        contentPanel.add(mainGroupsView, "MAIN_GROUPS");
        contentPanel.add(browseGroupsView, "BROWSE_GROUPS");
        contentPanel.add(createGroupView, "CREATE_GROUP");

        add(contentPanel, BorderLayout.CENTER);
        cardLayout.show(contentPanel, "MAIN_GROUPS");
    }

    // --- HELPER TO DYNAMICALLY LOAD CHAT VIEWS ---
    private void openChatView(int groupId, String groupName, String subTitle, Integer topicId) {
        ChatView chatView = new ChatView(
            groupId,
            topicId,
            groupName,
            subTitle,
            authToken,
            "Lecturer",
            () -> cardLayout.show(contentPanel, "MAIN_GROUPS"), // Back action
            selectedTopicId -> {
                // When a topic is chosen from list
                openChatView(groupId, groupName, "Discussion Topic #" + selectedTopicId, selectedTopicId);
            }
        );
        
        String chatCardName = "CHAT_" + groupId + "_" + (topicId != null ? topicId : "gen");
        contentPanel.add(chatView, chatCardName);
        cardLayout.show(contentPanel, chatCardName);
    }

    // --- HELPER TO DYNAMICALLY LOAD GROUP DETAILS ---
    private void fetchAndOpenGroupDetails(int groupId) {
        // You can query your ApiClient here for group details just like MainGroupsView does, 
        // then pass the fetched parameters into GroupDetailsView:
        
        GroupDetailsView detailsView = new GroupDetailsView(
            groupId,
            "Academic Group",
            "Group Details",
            "Loading group description...",
            true,  // isMember
            true,  // isAdmin (Lecturers have full admin/moderation control)
            authToken,
            () -> cardLayout.show(contentPanel, "MAIN_GROUPS"),
            () -> {}, // Join action if not member
            () -> openChatView(groupId, "Group Chat", "General Chat", null),
            () -> JOptionPane.showMessageDialog(this, "Manage Members View"),
            topicId -> openChatView(groupId, "Group Discussion", "Topic View", topicId)
        );

        String detailCardName = "GROUP_DETAILS_" + groupId;
        contentPanel.add(detailsView, detailCardName);
        cardLayout.show(contentPanel, detailCardName);
    }

    // --- HELPER TO LOAD JOIN GROUP VIEW ---
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
}