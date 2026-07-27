
package Student.Discussions;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;

public class NewDiscussionView extends JFrame {
    private JTextField titleField;
    private JComboBox<String> categoryComboBox;
    private JTextArea descriptionArea;
    private JLabel selectedFileLabel;
    private File attachedFile;
    private int groupId; // Optional: pass group ID if creating within a group context
    private String authToken;

    public NewDiscussionView(int groupId, String authToken) {
        this.groupId = groupId;
        this.authToken = authToken;
        setTitle("Start a New Discussion - UniForum");
        setSize(700, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main container with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(248, 250, 252)); // slate-50 background
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Back link
        JButton backBtn = new JButton("← Back to Discussions");
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setForeground(new Color(37, 99, 235)); // blue-600
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> dispose());
        mainPanel.add(backBtn);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Title Header
        JLabel headerLabel = new JLabel("Start a New Discussion");
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        headerLabel.setForeground(new Color(15, 23, 42)); // slate-900
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(headerLabel);

        JLabel subHeader = new JLabel("Ask questions, share ideas, and start conversations with your university community.");
        subHeader.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subHeader.setForeground(new Color(100, 116, 139)); // slate-500
        subHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(subHeader);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Form Card Container
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)), // slate-200
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        cardPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 1. Discussion Title
        cardPanel.add(createFieldLabel("Discussion Title"));
        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        titleField = new JTextField();
        styleTextField(titleField);
        cardPanel.add(titleField);
        
        JLabel titleHint = new JLabel("Use a clear title that describes your question.");
        titleHint.setFont(new Font("SansSerif", Font.PLAIN, 11));
        titleHint.setForeground(new Color(148, 163, 184));
        cardPanel.add(titleHint);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // 2. Category
        cardPanel.add(createFieldLabel("Category"));
        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        String[] categories = {
            "Select category", 
            "Software Engineering", 
            "Artificial Intelligence", 
            "Database Systems", 
            "Web Development"
        };
        categoryComboBox = new JComboBox<>(categories);
        categoryComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        categoryComboBox.setBackground(Color.WHITE);
        cardPanel.add(categoryComboBox);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // 3. Description
        cardPanel.add(createFieldLabel("Description"));
        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        descriptionArea = new JTextArea(6, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        cardPanel.add(descScroll);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // 4. Attachment section
        cardPanel.add(createFieldLabel("Attachment"));
        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        JPanel uploadPanel = new JPanel();
        uploadPanel.setLayout(new BoxLayout(uploadPanel, BoxLayout.Y_AXIS));
        uploadPanel.setBackground(new Color(250, 250, 250));
        uploadPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createDashedBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        uploadPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        selectedFileLabel = new JLabel("Upload supporting files (optional)");
        selectedFileLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        selectedFileLabel.setForeground(new Color(100, 116, 139));
        selectedFileLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadPanel.add(selectedFileLabel);

        JButton chooseFileBtn = new JButton("Choose File");
        chooseFileBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        chooseFileBtn.setForeground(new Color(37, 99, 235));
        chooseFileBtn.setBorderPainted(false);
        chooseFileBtn.setContentAreaFilled(false);
        chooseFileBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        chooseFileBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                attachedFile = fileChooser.getSelectedFile();
                selectedFileLabel.setText(attachedFile.getName());
            }
        });
        uploadPanel.add(chooseFileBtn);
        cardPanel.add(uploadPanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Action Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(new Color(241, 245, 249));
        cancelBtn.setForeground(new Color(51, 65, 85));
        cancelBtn.setFocusPainted(false);
        cancelBtn.addActionListener(e -> dispose());

        JButton postBtn = new JButton("Post Discussion");
        postBtn.setBackground(new Color(37, 99, 235));
        postBtn.setForeground(Color.WHITE);
        postBtn.setFocusPainted(false);
        postBtn.addActionListener(e -> submitDiscussion());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(postBtn);
        cardPanel.add(buttonPanel);

        mainPanel.add(cardPanel);
        add(new JScrollPane(mainPanel));
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setForeground(new Color(51, 65, 85));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void styleTextField(JTextField field) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        field.setPreferredSize(new Dimension(0, 38));
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
    }

    private void submitDiscussion() {
        String title = titleField.getText().trim();
        String category = (String) categoryComboBox.getSelectedItem();
        String description = descriptionArea.getText().trim();

        if (title.isEmpty() || category.equals("Select category") || description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Setup Multipart HTTP POST request to your Laravel backend endpoint
            String boundary = "===" + System.currentTimeMillis() + "===";
            URL url = new URL("http://127.0.0.1:8000/api/groups/" + groupId + "/topics"); // Match your API route
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            // Include user bearer token if you handle auth headers:
            if (authToken != null && !authToken.isEmpty()) {
            conn.setRequestProperty("Authorization", "Bearer " + authToken);
        }
            // conn.setRequestProperty("Authorization", "Bearer " + userToken);

            var outputStream = conn.getOutputStream();
            var writer = new java.io.PrintWriter(new java.io.OutputStreamWriter(outputStream, "UTF-8"), true);

            // Add fields
            addFormField(writer, boundary, "title", title);
            addFormField(writer, boundary, "ml_category", category);
            addFormField(writer, boundary, "description", description);
            if (groupId > 0) {
                addFormField(writer, boundary, "group_id", String.valueOf(groupId));
            }

            // Add file if selected
            if (attachedFile != null) {
                writer.println("--" + boundary);
                writer.println("Content-Disposition: form-data; name=\"attachment\"; filename=\"" + attachedFile.getName() + "\"");
                writer.println("Content-Type: " + java.net.URLConnection.guessContentTypeFromName(attachedFile.getName()));
                writer.println("Content-Transfer-Encoding: binary");
                writer.println();
                writer.flush();

                Files.copy(attachedFile.toPath(), outputStream);
                outputStream.flush();
                writer.println();
                writer.flush();
            }
            writer.append("--").append(boundary).append("--\r\n");
            writer.flush();
            writer.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                JOptionPane.showMessageDialog(this, "Discussion posted successfully!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to post discussion. Server responded with code: " + responseCode, "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Network error: " + ex.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addFormField(java.io.PrintWriter writer, String boundary, String name, String value) {
        writer.append("--").append(boundary).append("\r\n");
        writer.append("Content-Disposition: form-data; name=\"").append(name).append("\"\r\n\r\n");
        writer.append(value).append("\r\n");
        writer.flush();
    }
}
