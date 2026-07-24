package GeneralUser.views;

import Lecturer.LecturerDashboard;
import Student.StudentDashboard;

import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel errorLabel;

    public LoginForm() {
        setTitle("UniForum — Sign In");
        setSize(420, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel matching the primary theme
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(30, 64, 175));
        JLabel titleLabel = new JLabel("Welcome Back to UniForum");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Form Fields Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Email Address:"), gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        emailField = new JTextField("student@example.com", 20);
        formPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        loginButton = new JButton("Sign In");
        loginButton.setBackground(new Color(30, 64, 175));
        loginButton.setForeground(Color.WHITE);
        formPanel.add(loginButton, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        formPanel.add(errorLabel, gbc);

        // --- QUICK PREVIEW BUTTONS SECTION ---
        gbc.gridx = 0; gbc.gridy = 6;
        JSeparator separator = new JSeparator();
        formPanel.add(separator, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        JPanel previewPanel = new JPanel(new GridLayout(1, 2, 8, 0));
        
        JButton previewStudentBtn = new JButton("Preview Student");
        previewStudentBtn.setFont(new Font("SansSerif", Font.BOLD, 11));
        previewStudentBtn.addActionListener(e -> {
            // Fixed: Provided a fallback preview name ("Student User") for the 4-argument constructor
            new StudentDashboard(
                "mock-guest-token-student", 
                "Student User", 
                () -> {}, 
                () -> {}
            ).setVisible(true);
            dispose();
        });

        JButton previewLecturerBtn = new JButton("Preview Lecturer");
        previewLecturerBtn.setFont(new Font("SansSerif", Font.BOLD, 11));
        previewLecturerBtn.addActionListener(e -> {
            new LecturerDashboard("mock-guest-token-lecturer").setVisible(true);
            dispose();
        });

        previewPanel.add(previewStudentBtn);
        previewPanel.add(previewLecturerBtn);
        formPanel.add(previewPanel, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Action Listener for Normal Login
        loginButton.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        
        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter both email and password.");
            return;
        }

        new Thread(() -> {
            // 1. Build JSON payload
            String jsonPayload = String.format(
                "{\"email\": \"%s\", \"password\": \"%s\"}",
                email, password
            );

            // 2. Call the modular ApiClient post method
            String rawResponse = GeneralUser.api.ApiClient.post("/login", jsonPayload);

            // 3. Parse response code and body
            int splitIndex = rawResponse.indexOf(":");
            int responseCode = Integer.parseInt(rawResponse.substring(0, splitIndex));
            String responseStr = rawResponse.substring(splitIndex + 1);

            // 4. Handle success or error routing
            if (responseCode >= 200 && responseCode < 300 && responseStr.contains("token")) {
                // Extract Auth Token
                int tokenIndex = responseStr.indexOf("token");
                String authToken = responseStr.substring(tokenIndex + 8).replaceAll("[\"}]", "").split(",")[0].trim();

                // Extract Username safely using response string parsing (matching your token extraction style)
                String extractedUsername = "User";
                try {
                    if (responseStr.contains("name")) {
    int userIndex = responseStr.indexOf("name");
    // Find the colon after 'name'
    int colonIndex = responseStr.indexOf(":", userIndex);
    // Find the opening quote of the value
    int startQuote = responseStr.indexOf("\"", colonIndex);
    // Find the closing quote of the value
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
                    JOptionPane.showMessageDialog(this, "Authentication successful!");
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
                    dispose(); // Close login window
                });
            } else {
                SwingUtilities.invokeLater(() -> {
                    errorLabel.setText("Login failed: Invalid credentials.");
                });
            }
        }).start();
    }
}