package GeneralUser.views;

import Lecturer.LecturerDashboard;
import Student.StudentDashboard;

import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {

    // --- PALETTE matching Blade template & RegisterFrame ---
    private final Color PRIMARY_BLUE = new Color(37, 99, 235);     // #2563EB
    private final Color LIGHT_BLUE_BG = new Color(239, 246, 255);  // #EFF6FF
    private final Color BORDER_COLOR = new Color(226, 232, 240);   // #E2E8F0
    private final Color DARK_TEXT = new Color(15, 23, 42);         // #0F172A
    private final Color MUTED_TEXT = new Color(100, 116, 139);     // #64748B
    private final Color PAGE_BG = new Color(248, 250, 252);        // #F8FAFC
    private final Color INPUT_BG = Color.WHITE;

    // Components & State
    private JTextField emailField;
    private JPasswordField passwordField;
    private JCheckBox rememberCheckBox;
    private JButton loginButton;
    private JLabel errorLabel;
    private boolean isPasswordVisible = false;
    private boolean isLoggingIn = false;

    public LoginForm() {
        setTitle("UniForum — Sign In");
        setSize(480, 620);
        setMinimumSize(new Dimension(420, 560));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Antialiasing for clean typography
        System.setProperty("awt.useSystemAAFontSettings", "lcd");
        System.setProperty("swing.aatext", "true");

        // Main Container
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(PAGE_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(32, 40, 32, 40));

        // --- 1. HEADER SECTION ---
        JLabel titleLabel = new JLabel("Welcome back");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(DARK_TEXT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(titleLabel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 4)));

        JLabel subTitleLabel = new JLabel("Sign in to your UniForum account to continue.");
        subTitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subTitleLabel.setForeground(MUTED_TEXT);
        subTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(subTitleLabel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 24)));

        // --- 2. EMAIL FIELD ---
        mainPanel.add(createInputLabel("Email address"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        emailField = createRoundedTextField("you@university.edu");
        emailField.setText("student@example.com"); // default fallback
        mainPanel.add(emailField);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 16)));

        // --- 3. PASSWORD FIELD WITH FORGOT LINK ---
        JPanel passHeaderPanel = new JPanel(new BorderLayout());
        passHeaderPanel.setOpaque(false);
        passHeaderPanel.setMaximumSize(new Dimension(400, 20));
        passHeaderPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel passLabel = createInputLabel("Password");
        JLabel forgotLink = new JLabel("Forgot password?");
        forgotLink.setFont(new Font("SansSerif", Font.BOLD, 11));
        forgotLink.setForeground(PRIMARY_BLUE);
        forgotLink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        passHeaderPanel.add(passLabel, BorderLayout.WEST);
        passHeaderPanel.add(forgotLink, BorderLayout.EAST);
        mainPanel.add(passHeaderPanel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 6)));

        passwordField = new JPasswordField();
        JPanel passwordWrapper = createPasswordFieldWithToggle(passwordField);
        mainPanel.add(passwordWrapper);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 14)));

        // --- 4. REMEMBER ME CHECKBOX ---
        rememberCheckBox = new JCheckBox("Remember me for 30 days");
        rememberCheckBox.setFont(new Font("SansSerif", Font.PLAIN, 12));
        rememberCheckBox.setForeground(MUTED_TEXT);
        rememberCheckBox.setOpaque(false);
        rememberCheckBox.setFocusPainted(false);
        rememberCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(rememberCheckBox);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 18)));

        // --- 5. SUBMIT BUTTON & ERROR DISPLAY ---
        loginButton = createPrimaryButton("Sign in →");
        loginButton.addActionListener(e -> handleLogin());
        mainPanel.add(loginButton);

        errorLabel = new JLabel("");
        errorLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        errorLabel.setForeground(new Color(220, 38, 38));
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        mainPanel.add(errorLabel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 16)));

        // --- 6. SWITCH TO REGISTER FOOTER ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        footerPanel.setOpaque(false);
        footerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel footerText = new JLabel("Don't have an account?");
        footerText.setFont(new Font("SansSerif", Font.PLAIN, 12));
        footerText.setForeground(MUTED_TEXT);

        JButton signUpLink = new JButton("Create one free");
        signUpLink.setFont(new Font("SansSerif", Font.BOLD, 12));
        signUpLink.setForeground(PRIMARY_BLUE);
        signUpLink.setBorderPainted(false);
        signUpLink.setContentAreaFilled(false);
        signUpLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signUpLink.addActionListener(e -> {
            new RegisterForm().setVisible(true);
            dispose();
        });

        footerPanel.add(footerText);
        footerPanel.add(signUpLink);
        mainPanel.add(footerPanel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 18)));

        // --- 7. PREVIEW BUTTONS ---
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(400, 1));
        separator.setForeground(BORDER_COLOR);
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(separator);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 14)));

        JPanel previewPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        previewPanel.setOpaque(false);
        previewPanel.setMaximumSize(new Dimension(400, 36));
        previewPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton previewStudentBtn = createOutlineButton("Preview Student");
        previewStudentBtn.addActionListener(e -> {
            new StudentDashboard(
                "mock-guest-token-student", 
                "Student User", 
                () -> {}, 
                () -> {}
            ).setVisible(true);
            dispose();
        });

        JButton previewLecturerBtn = createOutlineButton("Preview Lecturer");
        previewLecturerBtn.addActionListener(e -> {
            new LecturerDashboard("mock-guest-token-lecturer").setVisible(true);
            dispose();
        });

        previewPanel.add(previewStudentBtn);
        previewPanel.add(previewLecturerBtn);
        mainPanel.add(previewPanel);

        // ScrollPane wrapper
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    // --- UI FACTORY HELPERS ---

    private JLabel createInputLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(DARK_TEXT);
        return lbl;
    }

    private JTextField createRoundedTextField(String placeholder) {
        RoundedTextField field = new RoundedTextField(12);
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setForeground(DARK_TEXT);
        field.setCaretColor(PRIMARY_BLUE);
        field.setMaximumSize(new Dimension(400, 42));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }

    private JPanel createPasswordFieldWithToggle(JPasswordField pf) {
        RoundedPanel panel = new RoundedPanel(12, INPUT_BG, BORDER_COLOR);
        panel.setLayout(new BorderLayout());
        panel.setMaximumSize(new Dimension(400, 42));
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
            isPasswordVisible = !isPasswordVisible;
            pf.setEchoChar(isPasswordVisible ? (char) 0 : '•');
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
        btn.setMaximumSize(new Dimension(400, 44));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createOutlineButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("SansSerif", Font.BOLD, 11));
        btn.setForeground(DARK_TEXT);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // --- AUTHENTICATION API REQUEST ---
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter both email and password.");
            return;
        }

        if (isLoggingIn) return;
        isLoggingIn = true;
        loginButton.setEnabled(false);

        new Thread(() -> {
            try {
                String jsonPayload = String.format(
                    "{\"email\": \"%s\", \"password\": \"%s\"}",
                    email, password
                );

                String rawResponse = GeneralUser.api.ApiClient.post("/login", jsonPayload);

                int splitIndex = rawResponse.indexOf(":");
                if (splitIndex == -1) {
                    throw new RuntimeException("Invalid response format from server.");
                }
                int responseCode = Integer.parseInt(rawResponse.substring(0, splitIndex).trim());
                String responseStr = rawResponse.substring(splitIndex + 1).trim();

                if (responseCode >= 200 && responseCode < 300 && responseStr.contains("token")) {
                    int tokenIndex = responseStr.indexOf("token");
                    String authToken = responseStr.substring(tokenIndex + 8).replaceAll("[\"}]", "").split(",")[0].trim();

                    String extractedUsername = "User";
                    try {
                        if (responseStr.contains("name")) {
                            int userIndex = responseStr.indexOf("name");
                            int colonIndex = responseStr.indexOf(":", userIndex);
                            int startQuote = responseStr.indexOf("\"", colonIndex);
                            int endQuote = responseStr.indexOf("\"", startQuote + 1);

                            if (startQuote != -1 && endQuote != -1) {
                                extractedUsername = responseStr.substring(startQuote + 1, endQuote).trim();
                            }
                        }
                    } catch (Exception ex) {
                        extractedUsername = "User";
                    }

                    final String finalUsername = extractedUsername;

                    SwingUtilities.invokeLater(() -> {
                        dispose(); 
                        JOptionPane.showMessageDialog(null, "Authentication successful!");

                        if (responseStr.contains("\"role\":\"lecturer\"")) {
                            new LecturerDashboard(authToken).setVisible(true);
                        } else {
                            new StudentDashboard(
                                authToken, 
                                finalUsername, 
                                () -> {}, 
                                () -> {}
                            ).setVisible(true);
                        }
                    });
                } else {
                    SwingUtilities.invokeLater(() -> {
                        isLoggingIn = false;
                        loginButton.setEnabled(true);
                        errorLabel.setText("Login failed: Invalid credentials.");
                    });
                }
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    isLoggingIn = false;
                    loginButton.setEnabled(true);
                    errorLabel.setText("Login error: " + ex.getMessage());
                });
            }
        }).start();
    }

    // --- CUSTOM ROUNDED DRAWING COMPONENTS ---

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}