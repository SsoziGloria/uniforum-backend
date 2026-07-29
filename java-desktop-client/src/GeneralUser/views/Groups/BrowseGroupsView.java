package GeneralUser.views.Groups;

import GeneralUser.api.ApiClient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BrowseGroupsView extends JPanel {

    // --- Tailwind Slate, Blue, Purple & Emerald Palette (Matching Blade) ---
    private final Color PRIMARY_BLUE   = new Color(37, 99, 235);    // #2563EB (blue-600)
    private final Color PRIMARY_HOVER  = new Color(29, 78, 216);    // #1D4ED8 (blue-700)
    private final Color SLATE_SEARCH   = new Color(30, 41, 59);     // #1E293B (slate-800)
    private final Color SLATE_HOVER    = new Color(15, 23, 42);     // #0F172A (slate-900)
    private final Color PAGE_BG        = new Color(248, 250, 252);  // #F8FAFC (slate-50)
    private final Color BORDER_COLOR   = new Color(226, 232, 240);  // #E2E8F0 (slate-200)
    private final Color DARK_TEXT      = new Color(15, 23, 42);     // #0F172A (slate-900)
    private final Color MUTED_TEXT     = new Color(100, 116, 139);  // #64748B (slate-500)
    
    // Purple Badge (Academic Group)
    private final Color PURPLE_BG      = new Color(243, 232, 255);  // #F3E8FF (purple-100)
    private final Color PURPLE_TEXT    = new Color(107, 33, 168);   // #6B21A8 (purple-700)
    
    // Emerald Badge (Joined State)
    private final Color EMERALD_BG     = new Color(236, 253, 245);  // #ECFDF5 (emerald-50)
    private final Color EMERALD_TEXT   = new Color(4, 120, 87);     // #047857 (emerald-700)
    private final Color EMERALD_BORDER = new Color(167, 243, 208);  // #A7F3D0 (emerald-200)

    private String authToken;
    private JPanel cardsGrid;
    private JPanel contentContainer;
    private JTextField searchField;
    private Runnable onCreateGroupClicked;
    private java.util.function.Consumer<Integer> onViewDetailsClicked;
    private java.util.function.Consumer<Integer> onJoinClicked;

    // --- OVERLOADED CONSTRUCTOR (4 Parameters for backward compatibility) ---
    public BrowseGroupsView(String token, Runnable onBackClicked,
                            java.util.function.Consumer<Integer> onViewDetailsClicked,
                            java.util.function.Consumer<Integer> onJoinClicked) {
        this(token, onBackClicked, null, onViewDetailsClicked, onJoinClicked);
    }

    // --- MAIN CONSTRUCTOR (5 Parameters with onCreateGroupClicked) ---
    public BrowseGroupsView(String token, Runnable onBackClicked, Runnable onCreateGroupClicked,
                            java.util.function.Consumer<Integer> onViewDetailsClicked,
                            java.util.function.Consumer<Integer> onJoinClicked) {
        this.authToken = token;
        this.onCreateGroupClicked = onCreateGroupClicked;
        this.onViewDetailsClicked = onViewDetailsClicked;
        this.onJoinClicked = onJoinClicked;

        setLayout(new BorderLayout());
        setBackground(PAGE_BG);

        // Anti-Aliasing for clean modern fonts & shapes
        System.setProperty("awt.useSystemAAFontSettings", "lcd");
        System.setProperty("swing.aatext", "true");

        // Outer Container
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(PAGE_BG);

        // --- 1. HEADER SECTION ---
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
            new EmptyBorder(28, 32, 28, 32)
        ));

        // Center Alignment Max Width Wrapper
        JPanel headerContent = new JPanel();
        headerContent.setLayout(new BoxLayout(headerContent, BoxLayout.Y_AXIS));
        headerContent.setBackground(Color.WHITE);
        headerContent.setMaximumSize(new Dimension(1100, Integer.MAX_VALUE));
        headerContent.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Back Link
        JButton backBtn = new JButton("←  Back to Groups");
        backBtn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        backBtn.setForeground(PRIMARY_BLUE);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.addActionListener(e -> {
            if (onBackClicked != null) onBackClicked.run();
        });
        headerContent.add(backBtn);

        headerContent.add(Box.createRigidArea(new Dimension(0, 12)));

        // Title
        JLabel titleLbl = new JLabel("Browse Groups");
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLbl.setForeground(DARK_TEXT);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerContent.add(titleLbl);

        headerContent.add(Box.createRigidArea(new Dimension(0, 6)));

        // Subtitle
        JLabel subLbl = new JLabel("Discover academic groups and join discussions with other members.");
        subLbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subLbl.setForeground(MUTED_TEXT);
        subLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerContent.add(subLbl);

        headerPanel.add(headerContent);
        outerPanel.add(headerPanel, BorderLayout.NORTH);

        // --- 2. SEARCH & ACTIONS TOOLBAR ---
        contentContainer = new JPanel();
        contentContainer.setLayout(new BoxLayout(contentContainer, BoxLayout.Y_AXIS));
        contentContainer.setBackground(PAGE_BG);
        contentContainer.setBorder(new EmptyBorder(32, 32, 32, 32));

        JPanel toolbarWrapper = new JPanel();
        toolbarWrapper.setLayout(new BorderLayout(16, 0));
        toolbarWrapper.setOpaque(false);
        toolbarWrapper.setMaximumSize(new Dimension(1100, 48));
        toolbarWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Search Input & Search Button Group
        JPanel searchGroup = new JPanel(new BorderLayout(8, 0));
        searchGroup.setOpaque(false);

        searchField = new JTextField("Search groups by name or description...");
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        searchField.setForeground(MUTED_TEXT);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(10, 14, 10, 14)
        ));

        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (searchField.getText().equals("Search groups by name or description...")) {
                    searchField.setText("");
                    searchField.setForeground(DARK_TEXT);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(MUTED_TEXT);
                    searchField.setText("Search groups by name or description...");
                }
            }
        });

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String query = searchField.getText().trim();
                if (query.equals("Search groups by name or description...")) query = "";
                loadGroupsFromDatabase(query);
            }
        });

        JButton searchBtn = createRoundedButton("Search", SLATE_SEARCH, Color.WHITE, null);
        searchBtn.addActionListener(e -> {
            String query = searchField.getText().trim();
            if (query.equals("Search groups by name or description...")) query = "";
            loadGroupsFromDatabase(query);
        });

        searchGroup.add(searchField, BorderLayout.CENTER);
        searchGroup.add(searchBtn, BorderLayout.EAST);

        toolbarWrapper.add(searchGroup, BorderLayout.CENTER);

        // Show (+ Create Group) button if a handler is supplied
        if (onCreateGroupClicked != null) {
            JButton createGroupBtn = createRoundedButton("+ Create Group", PRIMARY_BLUE, Color.WHITE, null);
            createGroupBtn.addActionListener(e -> onCreateGroupClicked.run());
            toolbarWrapper.add(createGroupBtn, BorderLayout.EAST);
        }

        contentContainer.add(toolbarWrapper);
        contentContainer.add(Box.createRigidArea(new Dimension(0, 24)));

        // --- 3. CARDS GRID ---
        cardsGrid = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 24));
        cardsGrid.setOpaque(false);
        cardsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardsGrid.setMaximumSize(new Dimension(1100, Integer.MAX_VALUE));

        contentContainer.add(cardsGrid);

        JScrollPane scrollPane = new JScrollPane(contentContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        outerPanel.add(scrollPane, BorderLayout.CENTER);

        add(outerPanel, BorderLayout.CENTER);

        refreshData();
    }

    public void refreshData() {
        loadGroupsFromDatabase("");
    }

    private void loadGroupsFromDatabase(String searchQuery) {
        if (searchQuery == null || searchQuery.equals("Search groups by name or description...")) {
            searchQuery = "";
        }
        
        final String query = searchQuery;

        new Thread(() -> {
            String endpoint = query.isEmpty() ? "/groups/search" : "/groups/search?search=" + query;
            String rawResponse = ApiClient.get(endpoint, authToken);

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

                                int membersCount = extractInt(groupJson, "members_count");
                                
                                // Fix: Ensure membership flag correctly checks "is_member" from API
                                boolean isMember = extractBoolean(groupJson, "is_member");
                                if (!isMember) {
                                    isMember = extractBoolean(groupJson, "isMember");
                                }

                                final int groupId = id;
                                cardsGrid.add(createBrowseCard(
                                    "Academic Group", 
                                    name, 
                                    desc, 
                                    membersCount,
                                    isMember,
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
                    JPanel emptyCard = new RoundedPanel(16, Color.WHITE, BORDER_COLOR);
                    emptyCard.setLayout(new GridBagLayout());
                    emptyCard.setBorder(new EmptyBorder(40, 20, 40, 20));
                    emptyCard.setPreferredSize(new Dimension(1050, 120));
                    
                    String msg = query.isEmpty() 
                        ? "No discussion groups found." 
                        : "No discussion groups found matching \"" + query + "\".";
                        
                    JLabel emptyLbl = new JLabel(msg);
                    emptyLbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
                    emptyLbl.setForeground(MUTED_TEXT);
                    emptyCard.add(emptyLbl);

                    cardsGrid.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
                    cardsGrid.add(emptyCard);
                } else {
                    cardsGrid.setLayout(new FlowLayout(FlowLayout.LEFT, 24, 24));
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

    private boolean extractBoolean(String json, String key) {
        try {
            String searchKey = "\"" + key + "\":";
            int start = json.indexOf(searchKey);
            if (start == -1) return false;
            start += searchKey.length();
            
            // Skip any whitespace after the colon
            while (start < json.length() && Character.isWhitespace(json.charAt(start))) {
                start++;
            }
            
            if (start >= json.length()) return false;
            
            // Check if it starts with true (ignoring case just in case)
            String sub = json.substring(start);
            if (sub.toLowerCase().startsWith("true")) {
                return true;
            }
        } catch (Exception e) {}
        return false;
    }

    private JPanel createBrowseCard(String tag, String groupName, String description, int membersCount,
                                    boolean isMember, int groupId, Runnable onJoin, Runnable onViewDetails) {
        RoundedPanel card = new RoundedPanel(16, Color.WHITE, BORDER_COLOR);
        
        // Fix: Restrict card height and width so they don't stretch downward in GridLayout/FlowLayout
        card.setPreferredSize(new Dimension(340, 220));
        card.setMaximumSize(new Dimension(340, 220));
        card.setMinimumSize(new Dimension(340, 220));
        
        card.setLayout(new BorderLayout(0, 12));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setOpaque(false);

        JLabel badge = new JLabel(tag);
        badge.setFont(new Font("SansSerif", Font.BOLD, 11));
        badge.setForeground(PURPLE_TEXT);
        badge.setOpaque(true);
        badge.setBackground(PURPLE_BG);
        badge.setBorder(new EmptyBorder(4, 10, 4, 10));
        badge.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContent.add(badge);

        mainContent.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel nameLbl = new JLabel(groupName);
        nameLbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        nameLbl.setForeground(DARK_TEXT);
        nameLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContent.add(nameLbl);

        mainContent.add(Box.createRigidArea(new Dimension(0, 6)));

        JLabel descLbl = new JLabel("<html><body style='width: 280px; color: #475569;'>" + description + "</body></html>");
        descLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        descLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContent.add(descLbl);

        card.add(mainContent, BorderLayout.CENTER);

        JPanel footer = new JPanel();
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.setOpaque(false);

        JLabel membersLbl = new JLabel((membersCount > 0 ? membersCount : "0") + " Members");
        membersLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        membersLbl.setForeground(MUTED_TEXT);
        membersLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        footer.add(membersLbl);

        footer.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel btnRow = new JPanel(new GridLayout(1, 2, 10, 0));
        btnRow.setOpaque(false);
        btnRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        JButton viewDetailsBtn = createRoundedButton("Details", Color.WHITE, DARK_TEXT, BORDER_COLOR);
        viewDetailsBtn.addActionListener(e -> {
            if (onViewDetails != null) onViewDetails.run();
        });
        btnRow.add(viewDetailsBtn);

        if (isMember) {
            JButton joinedBtn = createRoundedButton("✓ Joined", EMERALD_BG, EMERALD_TEXT, EMERALD_BORDER);
            joinedBtn.setEnabled(false); // Make non-interactive to match Blade layout
            btnRow.add(joinedBtn);
        } else {
            JButton joinBtn = createRoundedButton("Join", PRIMARY_BLUE, Color.WHITE, null);
            joinBtn.addActionListener(e -> {
                if (onJoin != null) onJoin.run();
            });
            btnRow.add(joinBtn);
        }

        footer.add(btnRow);
        card.add(footer, BorderLayout.SOUTH);

        return card;
    }

    private JButton createRoundedButton(String text, Color bg, Color fg, Color border) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                if (border != null) {
                    g2.setColor(border);
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(new EmptyBorder(8, 12, 8, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (bg.equals(PRIMARY_BLUE)) btn.setBackground(PRIMARY_HOVER);
                else if (bg.equals(SLATE_SEARCH)) btn.setBackground(SLATE_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
            }
        });

        return btn;
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
}