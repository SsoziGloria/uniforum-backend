package Student.Groups;

import GeneralUser.api.ApiClient;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CreateGroupView extends JPanel {

    private final Color PRIMARY_BLUE = new Color(30, 64, 175);
    private final Color PAGE_BG = new Color(248, 250, 252);
    private final Color BORDER_COLOR = new Color(226, 232, 240);
    private final Color DARK_TEXT = new Color(15, 23, 42);
    private final Color MUTED_TEXT = new Color(100, 116, 139);

    private String authToken;
    private JTextField groupNameField;
    private JTextArea descriptionArea;
    private Runnable onGroupCreated;
    private Runnable onCancelClicked;

    public CreateGroupView(String token, Runnable onGroupCreated, Runnable onCancelClicked) {
        this.authToken = token;
        this.onGroupCreated = onGroupCreated;
        this.onCancelClicked = onCancelClicked;

        setLayout(new BorderLayout());
        setBackground(PAGE_BG);
        setBorder(new EmptyBorder(24, 28, 24, 28));

        // --- TOP HEADER ---
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(PAGE_BG);

        JButton backBtn = new JButton("← Back to Groups");
        backBtn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        backBtn.setForeground(PRIMARY_BLUE);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.addActionListener(e -> {
            if (onCancelClicked != null) onCancelClicked.run();
        });
        headerPanel.add(backBtn);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel titleLbl = new JLabel("Create New Group");
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLbl.setForeground(DARK_TEXT);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.add(titleLbl);

        headerPanel.add(Box.createRigidArea(new Dimension(0, 4)));

        JLabel subLbl = new JLabel("Create a collaborative study or project group for academic discussions.");
        subLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subLbl.setForeground(MUTED_TEXT);
        subLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.add(subLbl);

        add(headerPanel, BorderLayout.NORTH);

        // --- FORM CONTAINER ---
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(24, 24, 24, 24)
        ));
        formPanel.setMaximumSize(new Dimension(600, 400));
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Group Name Input
        JLabel nameLabel = new JLabel("Group Name");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        nameLabel.setForeground(DARK_TEXT);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(nameLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 6)));

        groupNameField = new JTextField();
        groupNameField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        groupNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        groupNameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(6, 10, 6, 10)
        ));
        formPanel.add(groupNameField);

        formPanel.add(Box.createRigidArea(new Dimension(0, 16)));

        // Description Input Column
        JLabel descLabel = new JLabel("Description");
        descLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        descLabel.setForeground(DARK_TEXT);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(descLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 6)));

        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        descScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        descScroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        formPanel.add(descScroll);

        formPanel.add(Box.createRigidArea(new Dimension(0, 24)));

        // Submit Button Row
        JButton submitBtn = new JButton("Create Group");
        submitBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setBackground(PRIMARY_BLUE);
        submitBtn.setBorderPainted(false);
        submitBtn.setFocusPainted(false);
        submitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        submitBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        submitBtn.addActionListener(e -> submitGroupCreation());
        formPanel.add(submitBtn);

        // Center Wrapper to avoid full-width expansion
        JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 20));
        centerWrapper.setBackground(PAGE_BG);
        centerWrapper.add(formPanel);

        JScrollPane mainScroll = new JScrollPane(centerWrapper);
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        add(mainScroll, BorderLayout.CENTER);
    }

    private void submitGroupCreation() {
        String name = groupNameField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a group name.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Construct JSON payload for Laravel backend
        String jsonPayload = String.format("{\"group_name\":\"%s\",\"description\":\"%s\"}", 
            name.replace("\"", "\\\""), 
            description.replace("\"", "\\\"").replace("\n", " ")
        );

        new Thread(() -> {
            // Sends a POST request to your Laravel Route::post('/groups', ...) endpoint
            String response = ApiClient.post("/groups", jsonPayload, authToken);

            SwingUtilities.invokeLater(() -> {
                if (response != null && (response.startsWith("200") || response.startsWith("201") || response.contains("success"))) {
                    JOptionPane.showMessageDialog(this, "Group created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    if (onGroupCreated != null) {
                        onGroupCreated.run();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to create group. Server response: " + response, "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        }).start();
    }
}