package Student.Groups.Discussions;

import GeneralUser.api.ApiClient;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GroupTopicsView extends JPanel {
    private final Color PRIMARY_BLUE = new Color(30, 64, 175);
    private final Color PAGE_BG = new Color(248, 250, 252);
    private final Color BORDER_COLOR = new Color(226, 232, 240);
    private final Color DARK_TEXT = new Color(15, 23, 42);
    private final Color MUTED_TEXT = new Color(100, 116, 139);

    private int groupId;
    private String authToken;
    private JPanel listContainer;

    public GroupTopicsView(int groupId, String authToken, Runnable onBackClicked, java.util.function.Consumer<Integer> onTopicSelected) {
        this.groupId = groupId;
        this.authToken = authToken;

        setLayout(new BorderLayout());
        setBackground(PAGE_BG);
        setBorder(new EmptyBorder(24, 40, 24, 40));

        // Header Panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(PAGE_BG);
        topPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton backBtn = new JButton("← Back to Group Details");
        backBtn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        backBtn.setForeground(PRIMARY_BLUE);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.addActionListener(e -> {
            if (onBackClicked != null) onBackClicked.run();
        });
        topPanel.add(backBtn);
        topPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        JLabel titleLbl = new JLabel("Topic Discussions");
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLbl.setForeground(DARK_TEXT);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        topPanel.add(titleLbl);

        add(topPanel, BorderLayout.NORTH);

        // Scrollable List Container for Topics
        listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBackground(PAGE_BG);

        JScrollPane scrollPane = new JScrollPane(
            listContainer,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Fetch topics from backend
        loadTopics(onTopicSelected);
    }

    private void loadTopics(java.util.function.Consumer<Integer> onTopicSelected) {
        new Thread(() -> {
            try {
                String response = ApiClient.get("/groups/" + groupId + "/topics", authToken);
                
                int splitIndex = response.indexOf(":");
                String responseStr = (splitIndex != -1) ? response.substring(splitIndex + 1) : response;

                SwingUtilities.invokeLater(() -> {
                    listContainer.removeAll();
                    
                    if (!responseStr.contains("\"data\":[]") && responseStr.contains("title")) {
                        String[] items = responseStr.split("\\},\\s*\\{");
                        
                        for (String item : items) {
                           int id = 0;
try {
    // Look for "id" explicitly followed by a colon and numbers, ignoring nesting quirks
    int idIdx = item.indexOf("\"id\":");
    if (idIdx == -1) idIdx = item.indexOf("\"topic_id\":");
    if (idIdx != -1) {
        int colonIdx = item.indexOf(':', idIdx);
        int start = colonIdx + 1;
        while (start < item.length() && (item.charAt(start) == ' ' || item.charAt(start) == '"')) {
            start++;
        }
        int end = start;
        while (end < item.length() && Character.isDigit(item.charAt(end))) {
            end++;
        }
        if (end > start) {
            id = Integer.parseInt(item.substring(start, end));
        }
    }
} catch (Exception ignored) {}

                            String title = "Discussion Topic";
                            try {
                                int titleIdx = item.indexOf("\"title\":\"");
                                if (titleIdx != -1) {
                                    int start = titleIdx + 9;
                                    int end = item.indexOf("\"", start);
                                    title = item.substring(start, end);
                                }
                            } catch (Exception ignored) {}

                            String description = "Click to view discussion details.";
                            try {
                                int descIdx = item.indexOf("\"description\":\"");
                                if (descIdx != -1) {
                                    int start = descIdx + 15;
                                    int end = item.indexOf("\"", start);
                                    description = item.substring(start, end);
                                }
                            } catch (Exception ignored) {}

                            final int topicIdFinal = id;
                            JPanel row = createTopicRow(topicIdFinal, title, description, onTopicSelected);
                            listContainer.add(row);
                            listContainer.add(Box.createRigidArea(new Dimension(0, 10)));
                        }
                    } else {
                        JLabel emptyLbl = new JLabel("No discussions found for this group yet.");
                        emptyLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
                        emptyLbl.setForeground(MUTED_TEXT);
                        listContainer.add(emptyLbl);
                    }

                    listContainer.revalidate();
                    listContainer.repaint();
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Error loading topics: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

  private JPanel createTopicRow(int topicId, String title, String description, java.util.function.Consumer<Integer> onTopicSelected) {
        // We use a JButton styled as a panel because Swing buttons never fail to register clicks
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(PAGE_BG);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));

        JButton rowBtn = new JButton();
        rowBtn.setLayout(new BoxLayout(rowBtn, BoxLayout.Y_AXIS));
        rowBtn.setBackground(Color.WHITE);
        rowBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(14, 16, 14, 16)
        ));
        rowBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rowBtn.setFocusPainted(false);

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLbl.setForeground(PRIMARY_BLUE);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        rowBtn.add(titleLbl);

        rowBtn.add(Box.createRigidArea(new Dimension(0, 4)));

        JLabel descLbl = new JLabel(description);
        descLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        descLbl.setForeground(MUTED_TEXT);
        descLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        rowBtn.add(descLbl);

        // Action listener guarantees the click fires instantly
        rowBtn.addActionListener(e -> {
            System.out.println("DEBUG: Topic button clicked! ID = " + topicId);
            if (onTopicSelected != null) {
                onTopicSelected.accept(topicId);
            } else {
                System.out.println("ERROR: onTopicSelected callback is null!");
            }
        });

        wrapper.add(rowBtn, BorderLayout.CENTER);
        return wrapper;
    }
}