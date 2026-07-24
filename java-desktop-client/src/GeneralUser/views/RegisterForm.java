package GeneralUser.views;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RegisterForm extends JFrame {

    // --- THEME COLOURS ---
    private final Color PRIMARY_BLUE = new Color(37, 99, 235);     // #2563EB
    private final Color LIGHT_BLUE_BG = new Color(239, 246, 255);  // #EFF6FF
    private final Color BORDER_COLOR = new Color(203, 213, 225);   // #CBD5E1
    private final Color DARK_TEXT = new Color(15, 23, 42);         // #0F172A
    private final Color MUTED_TEXT = new Color(100, 116, 139);     // #64748B
    private final Color PAGE_BG = new Color(248, 250, 252);        // #F8FAFC

    // Form inputs & State variables
    private String selectedRole = ""; // "student" or "lecturer"
    private JTextField nameField, emailField, tokenField;
    private JPasswordField passwordField, confirmField;
    private JButton studentRoleBtn, lecturerRoleBtn, registerButton;
    private JLabel strengthLabel, matchErrorLabel, serverErrorLabel;
    private JPanel lecturerTokenPanel;

    // Password strength bar indicators
    private JPanel[] strengthBars = new JPanel[4];

    public RegisterForm() {
        setTitle("UniForum — Create an Account");
        setSize(520, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Main Wrapper with padding & background color
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(PAGE_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // --- 1. HEADER SECTION ---
        JLabel titleLabel = new JLabel("Create your account");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(DARK_TEXT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(titleLabel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JLabel subTitleLabel = new JLabel("Join your university's academic community today.");
        subTitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subTitleLabel.setForeground(MUTED_TEXT);
        subTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(subTitleLabel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- 2. ROLE SELECTION BUTTONS ---
        JLabel roleLabel = new JLabel("I am a...");
        roleLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        roleLabel.setForeground(DARK_TEXT);
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(roleLabel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        JPanel roleButtonsPanel = new JPanel(new GridLayout(1, 2, 12, 0));
        roleButtonsPanel.setBackground(PAGE_BG);
        roleButtonsPanel.setMaximumSize(new Dimension(440, 80));
        roleButtonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        studentRoleBtn = createRoleCard("🎓", "Student", "Access discussions & quizzes");
        lecturerRoleBtn = createRoleCard("📚", "Lecturer", "Manage students & discussions");

        studentRoleBtn.addActionListener(e -> setRole("student"));
        lecturerRoleBtn.addActionListener(e -> setRole("lecturer"));

        roleButtonsPanel.add(studentRoleBtn);
        roleButtonsPanel.add(lecturerRoleBtn);
        mainPanel.add(roleButtonsPanel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // --- 3. LECTURER TOKEN PANEL (Hidden by default) ---
        lecturerTokenPanel = new JPanel();
        lecturerTokenPanel.setLayout(new BoxLayout(lecturerTokenPanel, BoxLayout.Y_AXIS));
        lecturerTokenPanel.setBackground(PAGE_BG);
        lecturerTokenPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        lecturerTokenPanel.setVisible(false);

        JLabel tokenTitle = new JLabel("Lecturer Verification Token");
        tokenTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        tokenTitle.setForeground(DARK_TEXT);
        tokenTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        lecturerTokenPanel.add(tokenTitle);

        lecturerTokenPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        tokenField = new JTextField();
        tokenField.setMaximumSize(new Dimension(440, 38));
        tokenField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tokenField.setAlignmentX(Component.LEFT_ALIGNMENT);
        lecturerTokenPanel.add(tokenField);

        JLabel tokenDesc = new JLabel("A verification token is required to create a lecturer account.");
        tokenDesc.setFont(new Font("SansSerif", Font.PLAIN, 10));
        tokenDesc.setForeground(MUTED_TEXT);
        tokenDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        lecturerTokenPanel.add(tokenDesc);

        mainPanel.add(lecturerTokenPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // --- 4. TEXT FIELDS (Name, Email, Passwords) ---
        mainPanel.add(createInputLabel("Full Name"));
        nameField = createTextField("Dr. Sarah Chen");
        mainPanel.add(nameField);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(createInputLabel("Email"));
        emailField = createTextField("you@email.com");
        mainPanel.add(emailField);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(createInputLabel("Password"));
        passwordField = new JPasswordField();
        styleTextField(passwordField);
        mainPanel.add(passwordField);

        // Password strength meter layout
        JPanel strengthContainer = new JPanel();
        strengthContainer.setLayout(new BoxLayout(strengthContainer, BoxLayout.Y_AXIS));
        strengthContainer.setBackground(PAGE_BG);
        strengthContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        strengthContainer.setMaximumSize(new Dimension(440, 35));

        JPanel barsPanel = new JPanel(new GridLayout(1, 4, 4, 0));
        barsPanel.setBackground(PAGE_BG);
        barsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (int i = 0; i < 4; i++) {
            strengthBars[i] = new JPanel();
            strengthBars[i].setBackground(new Color(226, 232, 240));
            barsPanel.add(strengthBars[i]);
        }
        strengthContainer.add(barsPanel);

        strengthLabel = new JLabel("Password strength: ");
        strengthLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        strengthLabel.setForeground(MUTED_TEXT);
        strengthContainer.add(strengthLabel);

        mainPanel.add(strengthContainer);

        // Password listener for strength evaluation
        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { evaluateStrength(); }
            public void removeUpdate(DocumentEvent e) { evaluateStrength(); }
            public void changedUpdate(DocumentEvent e) { evaluateStrength(); }
        });

        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(createInputLabel("Confirm Password"));
        confirmField = new JPasswordField();
        styleTextField(confirmField);
        mainPanel.add(confirmField);

        matchErrorLabel = new JLabel("");
        matchErrorLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        matchErrorLabel.setForeground(Color.RED);
        matchErrorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(matchErrorLabel);

        confirmField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { checkMatch(); }
            public void removeUpdate(DocumentEvent e) { checkMatch(); }
            public void changedUpdate(DocumentEvent e) { checkMatch(); }
        });

        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // --- 5. SUBMIT BUTTON ---
        registerButton = new JButton("Create account →");
        registerButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        registerButton.setBackground(PRIMARY_BLUE);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setMaximumSize(new Dimension(440, 42));
        registerButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerButton.addActionListener(e -> handleRegister());
        mainPanel.add(registerButton);

        serverErrorLabel = new JLabel("");
        serverErrorLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        serverErrorLabel.setForeground(Color.RED);
        serverErrorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(serverErrorLabel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // --- 6. FOOTER SWITCH TO LOGIN ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        footerPanel.setBackground(PAGE_BG);
        footerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel footerText = new JLabel("Already have an account?");
        footerText.setFont(new Font("SansSerif", Font.PLAIN, 12));
        footerText.setForeground(MUTED_TEXT);

        JButton signInLink = new JButton("Sign in");
        signInLink.setFont(new Font("SansSerif", Font.BOLD, 12));
        signInLink.setForeground(PRIMARY_BLUE);
        signInLink.setBorderPainted(false);
        signInLink.setContentAreaFilled(false);
        signInLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signInLink.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });

        footerPanel.add(footerText);
        footerPanel.add(signInLink);
        mainPanel.add(footerPanel);

        // Wrap everything inside a JScrollPane for clean scrolling
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    // --- HELPER UI BUILDERS ---
    private JButton createRoleCard(String emoji, String title, String subtitle) {
        JButton btn = new JButton();
        btn.setLayout(new BoxLayout(btn, BoxLayout.Y_AXIS));
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        btn.setFocusPainted(false);

        JLabel iconLbl = new JLabel(emoji);
        iconLbl.setFont(new Font("SansSerif", Font.PLAIN, 18));
        iconLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.add(iconLbl);

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        titleLbl.setForeground(DARK_TEXT);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.add(titleLbl);

        JLabel subLbl = new JLabel(subtitle);
        subLbl.setFont(new Font("SansSerif", Font.PLAIN, 9));
        subLbl.setForeground(MUTED_TEXT);
        subLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.add(subLbl);

        return btn;
    }

    private JLabel createInputLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(DARK_TEXT);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextField createTextField(String placeholder) {
        JTextField tf = new JTextField();
        styleTextField(tf);
        return tf;
    }

    private void styleTextField(JComponent tf) {
        tf.setMaximumSize(new Dimension(440, 36));
        tf.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tf.setBackground(Color.WHITE);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
    }

    private void setRole(String role) {
        this.selectedRole = role;
        if (role.equals("student")) {
            studentRoleBtn.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(PRIMARY_BLUE, 2), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
            studentRoleBtn.setBackground(LIGHT_BLUE_BG);
            lecturerRoleBtn.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER_COLOR, 2), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
            lecturerRoleBtn.setBackground(Color.WHITE);
            lecturerTokenPanel.setVisible(false);
        } else {
            lecturerRoleBtn.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(PRIMARY_BLUE, 2), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
            lecturerRoleBtn.setBackground(LIGHT_BLUE_BG);
            studentRoleBtn.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER_COLOR, 2), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
            studentRoleBtn.setBackground(Color.WHITE);
            lecturerTokenPanel.setVisible(true);
        }
        revalidate();
        repaint();
    }

    private void evaluateStrength() {
        String pass = new String(passwordField.getPassword());
        int score = 0;
        if (pass.length() >= 8) score++;
        if (pass.matches(".*[A-Z].*")) score++;
        if (pass.matches(".*[0-9].*")) score++;
        if (pass.matches(".*[^A-Za-z0-9].*")) score++;

        for (int i = 0; i < 4; i++) {
            if (i < score) {
                strengthBars[i].setBackground(PRIMARY_BLUE);
            } else {
                strengthBars[i].setBackground(new Color(226, 232, 240));
            }
        }

        String[] labels = {"", "Weak", "Fair", "Good", "Strong"};
        strengthLabel.setText("Password strength: " + labels[score]);
    }

    private void checkMatch() {
        String pass = new String(passwordField.getPassword());
        String confirm = new String(confirmField.getPassword());
        if (!confirm.isEmpty() && !pass.equals(confirm)) {
            matchErrorLabel.setText("Passwords do not match.");
        } else {
            matchErrorLabel.setText("");
        }
    }

    // --- BACKEND HTTP API CALL ---
    private void handleRegister() {
    if (selectedRole.isEmpty()) {
        serverErrorLabel.setText("Please select whether you are a Student or Lecturer.");
        return;
    }

    String name = nameField.getText().trim();
    String email = emailField.getText().trim();
    String password = new String(passwordField.getPassword());
    String confirm = new String(confirmField.getPassword());
    String token = tokenField.getText().trim();

    if (!password.equals(confirm)) {
        serverErrorLabel.setText("Passwords do not match.");
        return;
    }

    new Thread(() -> {
        // Build JSON payload matching Laravel's expected keys (including lecturer_passcode)
        String jsonPayload = String.format(
            "{\"name\": \"%s\", \"email\": \"%s\", \"password\": \"%s\", \"password_confirmation\": \"%s\", \"role\": \"%s\", \"lecturer_passcode\": \"%s\"}",
            name, email, password, confirm, selectedRole, token
        );

        // Call our modular ApiClient in the api folder
        String rawResponse = GeneralUser.api.ApiClient.post("/register", jsonPayload);

        int splitIndex = rawResponse.indexOf(":");
        int responseCode = Integer.parseInt(rawResponse.substring(0, splitIndex));
        String responseBody = rawResponse.substring(splitIndex + 1);

        if (responseCode >= 200 && responseCode < 300) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this, "Account created successfully! Please sign in.");
                new LoginForm().setVisible(true);
                dispose();
            });
        } else {
            SwingUtilities.invokeLater(() -> {
                serverErrorLabel.setText("Registration failed. Check inputs or token.");
            });
        }
    }).start();
}
}