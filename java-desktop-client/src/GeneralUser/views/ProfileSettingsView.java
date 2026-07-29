package GeneralUser.views;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ProfileSettingsView extends JPanel {

    private final String authToken;
    private final Color PAGE_BG = new Color(248, 250, 252);
    private final Color CARD_BG = Color.WHITE;
    private final Color BORDER_COLOR = new Color(226, 232, 240);
    private final Color RED_BORDER = new Color(254, 202, 202);
    private final Color DARK_TEXT = new Color(30, 41, 59);
    private final Color MUTED_TEXT = new Color(100, 116, 139);
    private final Color PRIMARY_BLUE = new Color(37, 99, 235);

    public ProfileSettingsView(String authToken) {
        this.authToken = authToken;

        setLayout(new BorderLayout());
        setBackground(PAGE_BG);

        initComponents();
    }

    private void initComponents() {
        JPanel contentContainer = new JPanel();
        contentContainer.setLayout(new BoxLayout(contentContainer, BoxLayout.Y_AXIS));
        contentContainer.setBackground(PAGE_BG);
        contentContainer.setBorder(new EmptyBorder(32, 32, 32, 32));

        // --- HEADER CARD ---
        JPanel headerCard = createCardPanel();
        headerCard.setLayout(new BoxLayout(headerCard, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Profile Settings");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(DARK_TEXT);

        JLabel subtitle = new JLabel("Manage your personal information, account security, active sessions and account preferences.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(MUTED_TEXT);

        headerCard.add(title);
        headerCard.add(Box.createRigidArea(new Dimension(0, 6)));
        headerCard.add(subtitle);
        
        contentContainer.add(headerCard);
        contentContainer.add(Box.createRigidArea(new Dimension(0, 24)));

        // --- 1. UPDATE PROFILE INFORMATION ---
        contentContainer.add(createSectionCard(
                "Profile Information",
                "Update your account's profile information and email address.",
                createProfileFormFields()
        ));
        contentContainer.add(Box.createRigidArea(new Dimension(0, 24)));

        // --- 2. UPDATE PASSWORD ---
        contentContainer.add(createSectionCard(
                "Update Password",
                "Ensure your account is using a long, random password to stay secure.",
                createPasswordFormFields()
        ));
        contentContainer.add(Box.createRigidArea(new Dimension(0, 24)));

        // --- 3. TWO FACTOR AUTHENTICATION ---
        contentContainer.add(createSectionCard(
                "Two Factor Authentication",
                "Add additional security to your account using two factor authentication.",
                createTwoFactorPanel()
        ));
        contentContainer.add(Box.createRigidArea(new Dimension(0, 24)));

        // --- 4. BROWSER SESSIONS ---
        contentContainer.add(createSectionCard(
                "Browser Sessions",
                "Manage and log out your active sessions on other browsers and devices.",
                createBrowserSessionsPanel()
        ));
        contentContainer.add(Box.createRigidArea(new Dimension(0, 24)));

        // --- 5. ACCOUNT DELETION ---
        JPanel deleteCard = createCardPanel();
        deleteCard.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(RED_BORDER, 1, true),
                new EmptyBorder(24, 24, 24, 24)
        ));
        deleteCard.setLayout(new BoxLayout(deleteCard, BoxLayout.Y_AXIS));

        JLabel delTitle = new JLabel("Delete Account");
        delTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        delTitle.setForeground(new Color(185, 28, 28));
        
        JLabel delSub = new JLabel("Permanently delete your account.");
        delSub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        delSub.setForeground(MUTED_TEXT);

        JButton deleteBtn = new JButton("Delete Account");
        deleteBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setBackground(new Color(220, 38, 38));
        deleteBtn.setBorder(new EmptyBorder(10, 16, 10, 16));
        deleteBtn.setFocusPainted(false);
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        deleteCard.add(delTitle);
        deleteCard.add(Box.createRigidArea(new Dimension(0, 4)));
        deleteCard.add(delSub);
        deleteCard.add(Box.createRigidArea(new Dimension(0, 16)));
        deleteCard.add(deleteBtn);

        contentContainer.add(deleteCard);

        JScrollPane scrollPane = new JScrollPane(
                contentContainer,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createSectionCard(String titleText, String subtitleText, JComponent innerComponent) {
        JPanel card = createCardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(titleText);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(DARK_TEXT);

        JLabel subtitle = new JLabel(subtitleText);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(MUTED_TEXT);

        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(subtitle);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(innerComponent);

        return card;
    }

    private JPanel createProfileFormFields() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_BG);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nameLbl = new JLabel("Name");
        nameLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        nameLbl.setForeground(DARK_TEXT);
        JTextField nameField = new JTextField("Gloria S.");
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        JLabel emailLbl = new JLabel("Email");
        emailLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        emailLbl.setForeground(DARK_TEXT);
        JTextField emailField = new JTextField("gloria@student.uniforum.edu");
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        JButton saveBtn = new JButton("Save");
        saveBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setBackground(PRIMARY_BLUE);
        saveBtn.setBorder(new EmptyBorder(10, 20, 10, 20));
        saveBtn.setFocusPainted(false);
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panel.add(nameLbl);
        panel.add(Box.createRigidArea(new Dimension(0, 6)));
        panel.add(nameField);
        panel.add(Box.createRigidArea(new Dimension(0, 14)));
        panel.add(emailLbl);
        panel.add(Box.createRigidArea(new Dimension(0, 6)));
        panel.add(emailField);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(saveBtn);

        return panel;
    }

    private JPanel createPasswordFormFields() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_BG);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel currentLbl = new JLabel("Current Password");
        currentLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        JPasswordField currentField = new JPasswordField();
        currentField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        JLabel newLbl = new JLabel("New Password");
        newLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        JPasswordField newField = new JPasswordField();
        newField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        JButton saveBtn = new JButton("Save");
        saveBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setBackground(PRIMARY_BLUE);
        saveBtn.setBorder(new EmptyBorder(10, 20, 10, 20));
        saveBtn.setFocusPainted(false);
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panel.add(currentLbl);
        panel.add(Box.createRigidArea(new Dimension(0, 6)));
        panel.add(currentField);
        panel.add(Box.createRigidArea(new Dimension(0, 14)));
        panel.add(newLbl);
        panel.add(Box.createRigidArea(new Dimension(0, 6)));
        panel.add(newField);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(saveBtn);

        return panel;
    }

    private JPanel createTwoFactorPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_BG);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel desc = new JLabel("You have not enabled two factor authentication.");
        desc.setFont(new Font("SansSerif", Font.PLAIN, 13));
        desc.setForeground(MUTED_TEXT);

        JButton enableBtn = new JButton("Enable");
        enableBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        enableBtn.setForeground(Color.WHITE);
        enableBtn.setBackground(PRIMARY_BLUE);
        enableBtn.setBorder(new EmptyBorder(10, 20, 10, 20));
        enableBtn.setFocusPainted(false);
        enableBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panel.add(desc);
        panel.add(Box.createRigidArea(new Dimension(0, 14)));
        panel.add(enableBtn);

        return panel;
    }

    private JPanel createBrowserSessionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_BG);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel desc = new JLabel("If necessary, you may log out of all of your other browser sessions across all of your devices.");
        desc.setFont(new Font("SansSerif", Font.PLAIN, 13));
        desc.setForeground(MUTED_TEXT);

        JButton logoutBtn = new JButton("Log Out Other Browser Sessions");
        logoutBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        logoutBtn.setForeground(DARK_TEXT);
        logoutBtn.setBackground(new Color(241, 245, 249));
        logoutBtn.setBorder(new EmptyBorder(10, 16, 10, 16));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panel.add(desc);
        panel.add(Box.createRigidArea(new Dimension(0, 14)));
        panel.add(logoutBtn);

        return panel;
    }

    private JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_BG);
        panel.setOpaque(true);
        panel.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(24, 24, 24, 24)
        ));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }
}