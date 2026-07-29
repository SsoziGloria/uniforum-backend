package GeneralUser.views;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterForm extends JFrame {

    // --- PALETTE matching Blade & Welcome Frame ---
    private final Color PRIMARY_BLUE = new Color(37, 99, 235);     // #2563EB
    private final Color LIGHT_BLUE_BG = new Color(239, 246, 255);  // #EFF6FF
    private final Color BORDER_COLOR = new Color(226, 232, 240);   // #E2E8F0
    private final Color DARK_TEXT = new Color(15, 23, 42);         // #0F172A
    private final Color MUTED_TEXT = new Color(100, 116, 139);     // #64748B
    private final Color PAGE_BG = new Color(248, 250, 252);        // #F8FAFC
    private final Color INPUT_BG = Color.WHITE;

    // Form inputs & State
    private String selectedRole = ""; 
    private JTextField nameField, emailField, tokenField;
    private JPasswordField passwordField, confirmField;
    private boolean isPasswordVisible = false;
    private boolean isConfirmVisible = false;

    private RoundedButtonCard studentRoleBtn, lecturerRoleBtn;
    private JButton registerButton;
    private JLabel strengthLabel, matchErrorLabel, serverErrorLabel;
    private JPanel lecturerTokenPanel;
    private RoundedPanel[] strengthBars = new RoundedPanel[4];

    public RegisterForm() {
        setTitle("UniForum — Create an Account");
        setSize(540, 780);
        setMinimumSize(new Dimension(480, 680));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Antialiasing for crisp visuals and text
        System.setProperty("awt.useSystemAAFontSettings", "lcd");
        System.setProperty("swing.aatext", "true");

        // Main Wrapper
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(PAGE_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(28, 40, 28, 40));

        // --- 1. HEADER SECTION ---
        JLabel titleLabel = new JLabel("Create your account");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(DARK_TEXT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(titleLabel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 4)));

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
        roleButtonsPanel.setOpaque(false);
        roleButtonsPanel.setMaximumSize(new Dimension(460, 78));
        roleButtonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        studentRoleBtn = new RoundedButtonCard("🎓", "Student", "Access discussions & quizzes");
        lecturerRoleBtn = new RoundedButtonCard("📚", "Lecturer", "Manage students & discussions");

        studentRoleBtn.addActionListener(e -> setRole("student"));
        lecturerRoleBtn.addActionListener(e -> setRole("lecturer"));

        roleButtonsPanel.add(studentRoleBtn);
        roleButtonsPanel.add(lecturerRoleBtn);
        mainPanel.add(roleButtonsPanel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 14)));

        // --- 3. LECTURER TOKEN PANEL ---
        lecturerTokenPanel = new JPanel();
        lecturerTokenPanel.setLayout(new BoxLayout(lecturerTokenPanel, BoxLayout.Y_AXIS));
        lecturerTokenPanel.setOpaque(false);
        lecturerTokenPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        lecturerTokenPanel.setVisible(false);

        JLabel tokenTitle = new JLabel("Lecturer Verification Token");
        tokenTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        tokenTitle.setForeground(DARK_TEXT);
        tokenTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        lecturerTokenPanel.add(tokenTitle);

        lecturerTokenPanel.add(Box.createRigidArea(new Dimension(0, 6)));

        tokenField = createRoundedTextField("Enter lecturer verification token");
        lecturerTokenPanel.add(tokenField);

        lecturerTokenPanel.add(Box.createRigidArea(new Dimension(0, 4)));

        JLabel tokenDesc = new JLabel("A verification token is required to create a lecturer account.");
        tokenDesc.setFont(new Font("SansSerif", Font.PLAIN, 11));
        tokenDesc.setForeground(MUTED_TEXT);
        tokenDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        lecturerTokenPanel.add(tokenDesc);

        mainPanel.add(lecturerTokenPanel);

        // --- 4. FORM FIELDS ---
        mainPanel.add(createInputLabel("Full Name"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        nameField = createRoundedTextField("Dr. Sarah Chen");
        mainPanel.add(nameField);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        mainPanel.add(createInputLabel("Email"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        emailField = createRoundedTextField("you@email.com");
        mainPanel.add(emailField);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        mainPanel.add(createInputLabel("Password"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 6)));

        passwordField = new JPasswordField();
        JPanel passwordWrapper = createPasswordFieldWithToggle(passwordField, "Min. 8 characters", true);
        mainPanel.add(passwordWrapper);

        // Password strength meter
        JPanel strengthContainer = new JPanel();
        strengthContainer.setLayout(new BoxLayout(strengthContainer, BoxLayout.Y_AXIS));
        strengthContainer.setOpaque(false);
        strengthContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        strengthContainer.setMaximumSize(new Dimension(460, 32));

        strengthContainer.add(Box.createRigidArea(new Dimension(0, 6)));

        JPanel barsPanel = new JPanel(new GridLayout(1, 4, 6, 0));
        barsPanel.setOpaque(false);
        barsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (int i = 0; i < 4; i++) {
            strengthBars[i] = new RoundedPanel(4, BORDER_COLOR, null);
            strengthBars[i].setPreferredSize(new Dimension(0, 6));
            barsPanel.add(strengthBars[i]);
        }
        strengthContainer.add(barsPanel);

        strengthContainer.add(Box.createRigidArea(new Dimension(0, 4)));

        strengthLabel = new JLabel("Password strength: ");
        strengthLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        strengthLabel.setForeground(MUTED_TEXT);
        strengthContainer.add(strengthLabel);

        mainPanel.add(strengthContainer);

        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { evaluateStrength(); }
            public void removeUpdate(DocumentEvent e) { evaluateStrength(); }
            public void changedUpdate(DocumentEvent e) { evaluateStrength(); }
        });

        mainPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        mainPanel.add(createInputLabel("Confirm Password"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 6)));

        confirmField = new JPasswordField();
        JPanel confirmWrapper = createPasswordFieldWithToggle(confirmField, "Re-enter password", false);
        mainPanel.add(confirmWrapper);

        matchErrorLabel = new JLabel("");
        matchErrorLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        matchErrorLabel.setForeground(new Color(220, 38, 38));
        matchErrorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(matchErrorLabel);

        confirmField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { checkMatch(); }
            public void removeUpdate(DocumentEvent e) { checkMatch(); }
            public void changedUpdate(DocumentEvent e) { checkMatch(); }
        });

        mainPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Terms of Service label
        JLabel termsLabel = new JLabel("<html><body style='color:#64748B; font-size:10px;'>By creating an account, you agree to our <font color='#2563EB'>Terms of Service</font> and <font color='#2563EB'>Privacy Policy</font>.</body></html>");
        termsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(termsLabel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 16)));

        // --- 5. SUBMIT BUTTON ---
        registerButton = createPrimaryButton("Create account →");
        mainPanel.add(registerButton);

        serverErrorLabel = new JLabel("");
        serverErrorLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        serverErrorLabel.setForeground(new Color(220, 38, 38));
        serverErrorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(serverErrorLabel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 16)));

        // --- 6. FOOTER SWITCH TO LOGIN ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        footerPanel.setOpaque(false);
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

        // Outer Scroll Pane
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    // --- UI BUILDER HELPERS ---

    private JLabel createInputLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(DARK_TEXT);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextField createRoundedTextField(String placeholder) {
        RoundedTextField field = new RoundedTextField(12);
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setForeground(DARK_TEXT);
        field.setCaretColor(PRIMARY_BLUE);
        field.setMaximumSize(new Dimension(460, 42));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }

    private JPanel createPasswordFieldWithToggle(JPasswordField pf, String placeholder, boolean isPrimaryPass) {
        RoundedPanel panel = new RoundedPanel(12, INPUT_BG, BORDER_COLOR);
        panel.setLayout(new BorderLayout());
        panel.setMaximumSize(new Dimension(460, 42));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        pf.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 8));
        pf.setOpaque(false);
        pf.setFont(new Font("SansSerif", Font.PLAIN, 13));
        pf.setForeground(DARK_TEXT);
        pf.setCaretColor(PRIMARY_BLUE);

        JButton toggleBtn = new JButton("👁");
        toggleBtn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        toggleBtn.setForeground(MUTED_TEXT);
        toggleBtn.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 10));
        toggleBtn.setContentAreaFilled(false);
        toggleBtn.setFocusPainted(false);
        toggleBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        toggleBtn.addActionListener(e -> {
            if (isPrimaryPass) {
                isPasswordVisible = !isPasswordVisible;
                pf.setEchoChar(isPasswordVisible ? (char) 0 : '•');
            } else {
                isConfirmVisible = !isConfirmVisible;
                pf.setEchoChar(isConfirmVisible ? (char) 0 : '•');
            }
        });

        panel.add(pf, BorderLayout.CENTER);
        panel.add(toggleBtn, BorderLayout.EAST);
        return panel;
    }

    private JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isEnabled() ? PRIMARY_BLUE : new Color(147, 197, 253));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        btn.setMaximumSize(new Dimension(460, 44));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> handleRegister());
        return btn;
    }

    private void setRole(String role) {
        this.selectedRole = role;
        if (role.equals("student")) {
            studentRoleBtn.setSelectedState(true);
            lecturerRoleBtn.setSelectedState(false);
            lecturerTokenPanel.setVisible(false);
        } else {
            lecturerRoleBtn.setSelectedState(true);
            studentRoleBtn.setSelectedState(false);
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
                strengthBars[i].setBackgroundColor(PRIMARY_BLUE);
            } else {
                strengthBars[i].setBackgroundColor(BORDER_COLOR);
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

    // --- BACKEND API SUBMISSION ---
    private void handleRegister() {
        if (selectedRole.isEmpty()) {
            serverErrorLabel.setText("Please select whether you are a Student or Lecturer.");
            return;
        }

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirm = new String(confirmField.getPassword());
        String token = tokenField != null ? tokenField.getText().trim() : "";

        if (!password.equals(confirm)) {
            serverErrorLabel.setText("Passwords do not match.");
            return;
        }

        new Thread(() -> {
            String jsonPayload = String.format(
                "{\"name\": \"%s\", \"email\": \"%s\", \"password\": \"%s\", \"password_confirmation\": \"%s\", \"role\": \"%s\", \"lecturer_passcode\": \"%s\"}",
                name, email, password, confirm, selectedRole, token
            );

            String rawResponse = GeneralUser.api.ApiClient.post("/register", jsonPayload);

            int splitIndex = rawResponse.indexOf(":");
            int responseCode = Integer.parseInt(rawResponse.substring(0, splitIndex));

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

    // --- CUSTOM ROUNDED COMPONENTS ---

    private static class RoundedTextField extends JTextField {
        private final int radius;

        public RoundedTextField(int radius) {
            this.radius = radius;
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

            g2.setColor(new Color(226, 232, 240));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    private class RoundedButtonCard extends JButton {
        private boolean isSelected = false;

        public RoundedButtonCard(String icon, String title, String subtitle) {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setOpaque(false);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            JLabel iconLbl = new JLabel(icon);
            iconLbl.setFont(new Font("SansSerif", Font.PLAIN, 18));
            iconLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel titleLbl = new JLabel(title);
            titleLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
            titleLbl.setForeground(DARK_TEXT);
            titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel subLbl = new JLabel(subtitle);
            subLbl.setFont(new Font("SansSerif", Font.PLAIN, 10));
            subLbl.setForeground(MUTED_TEXT);
            subLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

            add(iconLbl);
            add(Box.createRigidArea(new Dimension(0, 2)));
            add(titleLbl);
            add(subLbl);
        }

        public void setSelectedState(boolean selected) {
            this.isSelected = selected;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Card background
            g2.setColor(isSelected ? LIGHT_BLUE_BG : Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);

            // Card border
            g2.setColor(isSelected ? PRIMARY_BLUE : BORDER_COLOR);
            g2.setStroke(new BasicStroke(isSelected ? 2 : 1));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class RoundedPanel extends JPanel {
        private final int radius;
        private Color bgColor;
        private Color borderColor;

        public RoundedPanel(int radius, Color bgColor, Color borderColor) {
            this.radius = radius;
            this.bgColor = bgColor;
            this.borderColor = borderColor;
            setOpaque(false);
        }

        public void setBackgroundColor(Color color) {
            this.bgColor = color;
            repaint();
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegisterForm().setVisible(true));
    }
}