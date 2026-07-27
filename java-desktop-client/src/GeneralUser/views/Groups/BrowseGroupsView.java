package Student.Groups;

import GeneralUser.api.ApiClient;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class BrowseGroupsView extends JPanel {

    private final Color PRIMARY_BLUE = new Color(30, 64, 175);
    private final Color PAGE_BG = new Color(248, 250, 252);
    private final Color BORDER_COLOR = new Color(226, 232, 240);
    private final Color DARK_TEXT = new Color(15, 23, 42);
    private final Color MUTED_TEXT = new Color(100, 116, 139);

    private String authToken;
    private JPanel cardsGrid;
    private JPanel contentContainer;
    private JTextField searchField;
    private java.util.function.Consumer<Integer> onViewDetailsClicked;
    private java.util.function.Consumer<Integer> onJoinClicked;

    public BrowseGroupsView(String token, Runnable onBackClicked, java.util.function.Consumer<Integer> onViewDetailsClicked, java.util.function.Consumer<Integer> onJoinClicked) {
        this.authToken = token;
        this.onViewDetailsClicked = onViewDetailsClicked;
        this.onJoinClicked = onJoinClicked;

        setLayout(new BorderLayout());
        setBackground(PAGE_BG);
        setBorder(new EmptyBorder(24, 28, 24, 28));

        // --- HEADER SECTION ---
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(PAGE_BG);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JButton backBtn = new JButton("← Back to Groups");
        backBtn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        backBtn.setForeground(PRIMARY_BLUE);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.addActionListener(e -> {
            if (onBackClicked != null) onBackClicked.run();
        });
        headerPanel.add(backBtn);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel titleLbl = new JLabel("Browse Groups");
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLbl.setForeground(DARK_TEXT);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.add(titleLbl);

        headerPanel.add(Box.createRigidArea(new Dimension(0, 4)));

        JLabel subLbl = new JLabel("Discover academic groups and join discussions with other members.");
        subLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subLbl.setForeground(MUTED_TEXT);
        subLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.add(subLbl);

        add(headerPanel, BorderLayout.NORTH);

        // --- CONTENT GRID CONTAINER ---
        contentContainer = new JPanel();
        contentContainer.setLayout(new BoxLayout(contentContainer, BoxLayout.Y_AXIS));
        contentContainer.setBackground(PAGE_BG);
        contentContainer.setBorder(new EmptyBorder(20, 0, 0, 0));
        contentContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Search Bar Field
        searchField = new JTextField("Search available groups...");
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        searchField.setForeground(MUTED_TEXT);
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        searchField.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (searchField.getText().equals("Search available groups...")) {
                    searchField.setText("");
                    searchField.setForeground(DARK_TEXT);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(MUTED_TEXT);
                    searchField.setText("Search available groups...");
                }
            }
        });

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String query = searchField.getText().trim();
                if (query.equals("Search available groups...")) query = "";
                loadGroupsFromDatabase(query);
            }
        });

        JPanel topSearchPanel = new JPanel();
        topSearchPanel.setLayout(new BoxLayout(topSearchPanel, BoxLayout.Y_AXIS));
        topSearchPanel.setBackground(PAGE_BG);
        topSearchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        topSearchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        topSearchPanel.add(searchField);
        topSearchPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        contentContainer.add(topSearchPanel);

        // Cards Grid (2 Column layout)
        cardsGrid = new JPanel(new GridLayout(0, 2, 16, 16));
        cardsGrid.setBackground(PAGE_BG);
        cardsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentContainer.add(cardsGrid);

        JScrollPane scrollPane = new JScrollPane(
            contentContainer,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setWheelScrollingEnabled(true);
        add(scrollPane, BorderLayout.CENTER);
    
        refreshData();
    }

    public void refreshData() {
        loadGroupsFromDatabase("");
    }

    private void loadGroupsFromDatabase(String searchQuery) {
        if (searchQuery == null || searchQuery.equals("Search available groups...")) {
            searchQuery = "";
        }
        
        final String query = searchQuery;

        new Thread(() -> {
            String endpoint = query.isEmpty() ? "/groups/search" : "/groups/search?search=" + query;
            String rawResponse = ApiClient.get(endpoint, authToken);
            
            System.out.println("API RAW RESPONSE: " + rawResponse); 

            SwingUtilities.invokeLater(() -> {
                cardsGrid.removeAll();

                try {
                    if (rawResponse != null && !rawResponse.isEmpty()) {
                        String jsonStr = rawResponse;
                        if (rawResponse.contains(":")) {
                            int firstColon = rawResponse.indexOf(":");
                            try {
                                Integer.parseInt(rawResponse.substring(0, firstColon).trim());
                                jsonStr = rawResponse.substring(firstColon + 1).trim();
                            } catch (NumberFormatException ignored) {}
                        }

                        int arrayStart = jsonStr.indexOf("[");
                        int arrayEnd = jsonStr.lastIndexOf("]");
                        
                        if (arrayStart != -1 && arrayEnd != -1 && arrayEnd > arrayStart) {
                            String dataArrayStr = jsonStr.substring(arrayStart + 1, arrayEnd);

                            java.util.List<String> jsonObjects = new java.util.ArrayList<>();
                            int braceDepth = 0;
                            int objStart = -1;
                            for (int i = 0; i < dataArrayStr.length(); i++) {
                                char c = dataArrayStr.charAt(i);
                                if (c == '{') {
                                    if (braceDepth == 0) objStart = i;
                                    braceDepth++;
                                } else if (c == '}') {
                                    braceDepth--;
                                    if (braceDepth == 0 && objStart != -1) {
                                        jsonObjects.add(dataArrayStr.substring(objStart, i + 1));
                                        objStart = -1;
                                    }
                                }
                            }

                            for (String groupJson : jsonObjects) {
                                int id = extractInt(groupJson, "group_id");
                                if (id == 0) id = extractInt(groupJson, "id");
                                
                                String name = extractString(groupJson, "group_name");
                                if (name.isEmpty()) name = extractString(groupJson, "name");
                                
                                String desc = extractString(groupJson, "description");
                                if (desc == null || desc.isEmpty() || desc.equals("null")) {
                                    desc = "No description provided.";
                                }

                                final int groupId = id;
                                cardsGrid.add(createBrowseCard(
                                    "Academic Group", 
                                    name, 
                                    desc, 
                                    groupId, 
                                    () -> { if (onJoinClicked != null) onJoinClicked.accept(groupId); }, 
                                    () -> { if (onViewDetailsClicked != null) onViewDetailsClicked.accept(groupId); }
                                ));
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (cardsGrid.getComponentCount() == 0) {
                    JLabel emptyLbl = new JLabel("No groups found.");
                    emptyLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
                    emptyLbl.setForeground(MUTED_TEXT);
                    cardsGrid.add(emptyLbl);
                }

                cardsGrid.revalidate();
                cardsGrid.repaint();
                contentContainer.revalidate();
                contentContainer.repaint();
                revalidate();
                repaint();
            });
        }).start();
    }

    private String extractString(String json, String key) {
        try {
            String searchKey = "\"" + key + "\":";
            int start = json.indexOf(searchKey);
            if (start == -1) return "";
            start += searchKey.length();
            if (json.charAt(start) == '"') {
                int end = json.indexOf("\"", start + 1);
                return json.substring(start + 1, end);
            }
        } catch (Exception e) {}
        return "";
    }

    private int extractInt(String json, String key) {
        try {
            String searchKey = "\"" + key + "\":";
            int start = json.indexOf(searchKey);
            if (start == -1) return 0;
            start += searchKey.length();
            int end = start;
            while (end < json.length() && (Character.isDigit(json.charAt(end)))) {
                end++;
            }
            return Integer.parseInt(json.substring(start, end));
        } catch (Exception e) {}
        return 0;
    }

    private JPanel createBrowseCard(String tag, String groupName, String description, int groupId, Runnable onJoin, Runnable onViewDetails) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 210));

        JLabel badge = new JLabel(tag);
        badge.setFont(new Font("SansSerif", Font.BOLD, 11));
        badge.setForeground(new Color(37, 99, 235));
        badge.setOpaque(true);
        badge.setBackground(new Color(219, 234, 254));
        badge.setBorder(new EmptyBorder(4, 10, 4, 10));
        badge.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(badge);

        card.add(Box.createRigidArea(new Dimension(0, 12)));

        JLabel nameLbl = new JLabel(groupName);
        nameLbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        nameLbl.setForeground(DARK_TEXT);
        nameLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(nameLbl);

        card.add(Box.createRigidArea(new Dimension(0, 6)));

        JLabel descLbl = new JLabel("<html><body style='width: 250px'>" + description + "</body></html>");
        descLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        descLbl.setForeground(MUTED_TEXT);
        descLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(descLbl);

        card.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel btnRow = new JPanel(new GridLayout(1, 2, 10, 0));
        btnRow.setBackground(Color.WHITE);
        btnRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        JButton viewDetailsBtn = new JButton("View Details");
        viewDetailsBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        viewDetailsBtn.setForeground(DARK_TEXT);
        viewDetailsBtn.setBackground(Color.WHITE);
        viewDetailsBtn.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        viewDetailsBtn.setFocusPainted(false);
        viewDetailsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewDetailsBtn.addActionListener(e -> {
            if (onViewDetails != null) {
                onViewDetails.run();
            }
        });

        JButton joinBtn = new JButton("Join");
        joinBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        joinBtn.setForeground(Color.WHITE);
        joinBtn.setBackground(PRIMARY_BLUE);
        joinBtn.setBorderPainted(false);
        joinBtn.setFocusPainted(false);
        joinBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        joinBtn.addActionListener(e -> {
            if (onJoin != null) {
                onJoin.run();
            }
        });

        btnRow.add(viewDetailsBtn);
        btnRow.add(joinBtn);
        card.add(btnRow);

        return card;
    }
}