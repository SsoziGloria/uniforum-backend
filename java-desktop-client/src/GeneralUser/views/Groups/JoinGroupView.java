package GeneralUser.views.Groups;

import GeneralUser.api.ApiClient;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class JoinGroupView extends JPanel {

    private final Color PRIMARY_BLUE = new Color(30, 64, 175);
    private final Color PAGE_BG = new Color(248, 250, 252);
    private final Color BORDER_COLOR = new Color(226, 232, 240);
    private final Color DARK_TEXT = new Color(15, 23, 42);
    private final Color MUTED_TEXT = new Color(100, 116, 139);

    public JoinGroupView(int groupId, String groupTag, String groupName, String groupDescription, String authToken, Runnable onBackClicked, Runnable onSuccessfullyJoined) {
        setLayout(new BorderLayout());
        setBackground(PAGE_BG);
        setBorder(new EmptyBorder(24, 28, 24, 28));

        JPanel contentContainer = new JPanel();
        contentContainer.setLayout(new BoxLayout(contentContainer, BoxLayout.Y_AXIS));
        contentContainer.setBackground(PAGE_BG);
        contentContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton backBtn = new JButton("← Back to Browse Groups");
        backBtn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        backBtn.setForeground(PRIMARY_BLUE);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.addActionListener(e -> {
            if (onBackClicked != null) onBackClicked.run();
        });
        contentContainer.add(backBtn);
        contentContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        JLabel titleLbl = new JLabel("Join Discussion Group");
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLbl.setForeground(DARK_TEXT);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentContainer.add(titleLbl);

        contentContainer.add(Box.createRigidArea(new Dimension(0, 4)));

        JLabel subLbl = new JLabel("Please review the group information and rules before joining.");
        subLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subLbl.setForeground(MUTED_TEXT);
        subLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentContainer.add(subLbl);

        contentContainer.add(Box.createRigidArea(new Dimension(0, 24)));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(30, 30, 30, 30)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(850, 580));

        JLabel badge = new JLabel(groupTag);
        badge.setFont(new Font("SansSerif", Font.BOLD, 11));
        badge.setForeground(new Color(37, 99, 235));
        badge.setOpaque(true);
        badge.setBackground(new Color(219, 234, 254));
        badge.setBorder(new EmptyBorder(4, 12, 4, 12));
        badge.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(badge);

        card.add(Box.createRigidArea(new Dimension(0, 12)));

        JLabel nameLbl = new JLabel(groupName);
        nameLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        nameLbl.setForeground(DARK_TEXT);
        nameLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(nameLbl);

        card.add(Box.createRigidArea(new Dimension(0, 8)));

        JLabel descLbl = new JLabel("<html><body style='width: 700px'>" + groupDescription + "</body></html>");
        descLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        descLbl.setForeground(MUTED_TEXT);
        descLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(descLbl);

        card.add(Box.createRigidArea(new Dimension(0, 25)));

        JLabel rulesHeader = new JLabel("Group Rules");
        rulesHeader.setFont(new Font("SansSerif", Font.BOLD, 16));
        rulesHeader.setForeground(DARK_TEXT);
        rulesHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(rulesHeader);

        card.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel rulesBox = new JPanel();
        rulesBox.setLayout(new BoxLayout(rulesBox, BoxLayout.Y_AXIS));
        rulesBox.setBackground(PAGE_BG);
        rulesBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(16, 20, 16, 20)
        ));
        rulesBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        rulesBox.setMaximumSize(new Dimension(780, 160));

        String[] rules = {
            "Respect all members of the discussion group.",
            "Share only academic and relevant content.",
            "Avoid offensive, abusive or inappropriate language.",
            "Do not spam or flood discussions.",
            "Follow all instructions provided by group administrators."
        };

        for (int i = 0; i < rules.length; i++) {
            JLabel ruleLbl = new JLabel("•  " + rules[i]);
            ruleLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
            ruleLbl.setForeground(MUTED_TEXT);
            ruleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            rulesBox.add(ruleLbl);
            if (i < rules.length - 1) {
                rulesBox.add(Box.createRigidArea(new Dimension(0, 6)));
            }
        }
        card.add(rulesBox);

        card.add(Box.createRigidArea(new Dimension(0, 25)));

        JCheckBox agreeCheckbox = new JCheckBox(" I have read, understood and agree to follow the rules of this discussion group.");
        agreeCheckbox.setFont(new Font("SansSerif", Font.PLAIN, 13));
        agreeCheckbox.setForeground(DARK_TEXT);
        agreeCheckbox.setBackground(Color.WHITE);
        agreeCheckbox.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(agreeCheckbox);

        card.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        btnRow.setBackground(Color.WHITE);
        btnRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRow.setMaximumSize(new Dimension(780, 45));

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        cancelBtn.setForeground(DARK_TEXT);
        cancelBtn.setBackground(new Color(241, 245, 249));
        cancelBtn.setBorder(new EmptyBorder(10, 20, 10, 20));
        cancelBtn.setFocusPainted(false);
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.addActionListener(e -> {
            if (onBackClicked != null) onBackClicked.run();
        });

        JButton joinBtn = new JButton("Join Group");
        joinBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        joinBtn.setForeground(Color.WHITE);
        joinBtn.setBackground(PRIMARY_BLUE);
        joinBtn.setBorder(new EmptyBorder(10, 20, 10, 20));
        joinBtn.setFocusPainted(false);
        joinBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        joinBtn.addActionListener(e -> {
            if (!agreeCheckbox.isSelected()) {
                JOptionPane.showMessageDialog(
                    this, 
                    "You must agree to follow the rules before joining this group.", 
                    "Agreement Required", 
                    JOptionPane.WARNING_MESSAGE
                );
            } else {
                String requestBody = "{\"rules_accepted\": true}";
                String response = ApiClient.post("/groups/" + groupId + "/join", requestBody, authToken);

                if (response != null && (response.startsWith("200") || response.startsWith("201") || response.contains("success"))) {
                    JOptionPane.showMessageDialog(this, "Successfully joined group!");
                    
                    if (onSuccessfullyJoined != null) {
                        onSuccessfullyJoined.run();
                    }
                } else {
                    JOptionPane.showMessageDialog(
                        this, 
                        "Failed to join group. Server response: " + response, 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        btnRow.add(cancelBtn);
        btnRow.add(joinBtn);
        card.add(btnRow);

        contentContainer.add(card);

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
}