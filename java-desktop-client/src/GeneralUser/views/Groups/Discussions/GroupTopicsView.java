package GeneralUser.views.Groups.Discussions;

import GeneralUser.api.ApiClient;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GroupTopicsView extends JPanel {
    private final Color PRIMARY_BLUE = new Color(37, 99, 235);
    private final Color PAGE_BG = new Color(248, 250, 252);
    private final Color CARD_BG = Color.WHITE;
    private final Color BORDER_COLOR = new Color(226, 232, 240);
    private final Color DARK_TEXT = new Color(15, 23, 42);
    private final Color MUTED_TEXT = new Color(100, 116, 139);
    private final Color BADGE_BG = new Color(219, 234, 254);
    private final Color BADGE_TEXT = new Color(29, 78, 216);

    private final int groupId;
    private final String authToken;
    private final Runnable onBackClicked;
    private final java.util.function.Consumer<Integer> onTopicSelected;
    private final Runnable onCreateTopicClicked;

    private JTextField searchField;
    private JPanel topicsFeedContainer;
    private JPanel recommendationsContainer;
    private JPanel categoriesContainer;
    private JPanel recommendationsSection;

    private final List<TopicData> allTopics = new ArrayList<>();

    public GroupTopicsView(int groupId, String authToken, Runnable onBackClicked, 
                           java.util.function.Consumer<Integer> onTopicSelected,
                           Runnable onCreateTopicClicked) {
        this.groupId = groupId;
        this.authToken = authToken;
        this.onBackClicked = onBackClicked;
        this.onTopicSelected = onTopicSelected;
        this.onCreateTopicClicked = onCreateTopicClicked;

        setLayout(new BorderLayout());
        setBackground(PAGE_BG);

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(PAGE_BG);

        mainContent.add(createHeaderPanel());
        recommendationsSection = createRecommendationsPanel();
        mainContent.add(recommendationsSection);
        mainContent.add(createBodyGrid());

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);
        loadData();
    }

    private JPanel createHeaderPanel() {
        JPanel headerWrapper = new JPanel();
        headerWrapper.setLayout(new BoxLayout(headerWrapper, BoxLayout.Y_AXIS));
        headerWrapper.setBackground(CARD_BG);
        headerWrapper.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                new EmptyBorder(24, 40, 24, 40)
        ));

        JButton backBtn = new JButton("← Back to Group Overview");
        backBtn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        backBtn.setForeground(PRIMARY_BLUE);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.addActionListener(e -> {
            if (onBackClicked != null) onBackClicked.run();
        });

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setBackground(CARD_BG);
        titleRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel textGroup = new JPanel();
        textGroup.setLayout(new BoxLayout(textGroup, BoxLayout.Y_AXIS));
        textGroup.setBackground(CARD_BG);

        JLabel titleLbl = new JLabel("Discussions");
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLbl.setForeground(DARK_TEXT);

        JLabel subLbl = new JLabel("Moderate discussions, answer questions, and connect with students.");
        subLbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subLbl.setForeground(MUTED_TEXT);

        textGroup.add(titleLbl);
        textGroup.add(Box.createRigidArea(new Dimension(0, 4)));
        textGroup.add(subLbl);

        JButton createBtn = new JButton("+ Create Discussion");
        createBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        createBtn.setForeground(Color.WHITE);
        createBtn.setBackground(PRIMARY_BLUE);
        createBtn.setFocusPainted(false);
        createBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createBtn.setBorder(new EmptyBorder(10, 18, 10, 18));
        createBtn.addActionListener(e -> {
            if (onCreateTopicClicked != null) onCreateTopicClicked.run();
        });

        titleRow.add(textGroup, BorderLayout.CENTER);
        titleRow.add(createBtn, BorderLayout.EAST);

        JPanel searchPanel = new JPanel(new BorderLayout(8, 0));
        searchPanel.setBackground(CARD_BG);
        searchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        searchField = new JTextField();
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));

        JButton searchBtn = new JButton("Search");
        searchBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setBackground(PRIMARY_BLUE);
        searchBtn.setFocusPainted(false);
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchBtn.addActionListener(e -> filterTopics(searchField.getText()));

        JButton clearBtn = new JButton("Clear");
        clearBtn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        clearBtn.setForeground(MUTED_TEXT);
        clearBtn.setFocusPainted(false);
        clearBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearBtn.addActionListener(e -> {
            searchField.setText("");
            filterTopics("");
        });

        JPanel searchBtnGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        searchBtnGroup.setBackground(CARD_BG);
        searchBtnGroup.add(clearBtn);
        searchBtnGroup.add(searchBtn);

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchBtnGroup, BorderLayout.EAST);

        headerWrapper.add(backBtn);
        headerWrapper.add(Box.createRigidArea(new Dimension(0, 12)));
        headerWrapper.add(titleRow);
        headerWrapper.add(Box.createRigidArea(new Dimension(0, 20)));
        headerWrapper.add(searchPanel);

        return headerWrapper;
    }

    private JPanel createRecommendationsPanel() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(PAGE_BG);
        section.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                new EmptyBorder(20, 40, 20, 40)
        ));
        section.setVisible(false);

        JLabel sectionTitle = new JLabel("✨ Recommended for you");
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        sectionTitle.setForeground(DARK_TEXT);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        recommendationsContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        recommendationsContainer.setBackground(PAGE_BG);

        JScrollPane recScroll = new JScrollPane(recommendationsContainer);
        recScroll.setBorder(null);
        recScroll.setBackground(PAGE_BG);
        recScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        recScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        section.add(sectionTitle);
        section.add(Box.createRigidArea(new Dimension(0, 12)));
        section.add(recScroll);

        return section;
    }

    private JPanel createBodyGrid() {
        JPanel bodyGrid = new JPanel(new BorderLayout(32, 0));
        bodyGrid.setBackground(PAGE_BG);
        bodyGrid.setBorder(new EmptyBorder(32, 40, 40, 40));

        topicsFeedContainer = new JPanel();
        topicsFeedContainer.setLayout(new BoxLayout(topicsFeedContainer, BoxLayout.Y_AXIS));
        topicsFeedContainer.setBackground(PAGE_BG);

        JPanel sidebar = createSidebarPanel();
        sidebar.setPreferredSize(new Dimension(320, 0));

        bodyGrid.add(topicsFeedContainer, BorderLayout.CENTER);
        bodyGrid.add(sidebar, BorderLayout.EAST);

        return bodyGrid;
    }

    private JPanel createSidebarPanel() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(PAGE_BG);

        JPanel catCard = new JPanel();
        catCard.setLayout(new BoxLayout(catCard, BoxLayout.Y_AXIS));
        catCard.setBackground(CARD_BG);
        catCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel catTitle = new JLabel("Popular Topics");
        catTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        catTitle.setForeground(DARK_TEXT);
        catTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        categoriesContainer = new JPanel();
        categoriesContainer.setLayout(new BoxLayout(categoriesContainer, BoxLayout.Y_AXIS));
        categoriesContainer.setBackground(CARD_BG);
        categoriesContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        catCard.add(catTitle);
        catCard.add(Box.createRigidArea(new Dimension(0, 16)));
        catCard.add(categoriesContainer);

        sidebar.add(catCard);
        return sidebar;
    }

    private void loadData() {
        new Thread(() -> {
            try {
                String response = ApiClient.get("/groups/" + groupId + "/topics", authToken);
                int splitIndex = response.indexOf(":");
                final String jsonStr = (splitIndex != -1) ? response.substring(splitIndex + 1) : response;

                SwingUtilities.invokeLater(() -> {
                    parseAndPopulateTopics(jsonStr);
                    renderSidebarCategories();
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    topicsFeedContainer.removeAll();
                    JLabel errorLbl = new JLabel("Error loading discussions: " + ex.getMessage());
                    errorLbl.setForeground(Color.RED);
                    topicsFeedContainer.add(errorLbl);
                    topicsFeedContainer.revalidate();
                    topicsFeedContainer.repaint();
                });
            }
        }).start();
    }

    private void parseAndPopulateTopics(String rawResponse) {
        allTopics.clear();
        topicsFeedContainer.removeAll();
        recommendationsContainer.removeAll();

        if (rawResponse != null && rawResponse.contains("title")) {
            String[] items = rawResponse.split("\\},\\s*\\{");

            for (String item : items) {
                TopicData topic = new TopicData();
                topic.id = extractIntField(item, "topic_id", "id");
                topic.title = extractStringField(item, "title", "Discussion Topic");
                topic.description = extractStringField(item, "description", "No description provided.");
                topic.category = extractStringField(item, "ml_category", "General");
                topic.messagesCount = extractIntField(item, "messages_count", "replies_count");
                topic.createdAt = extractStringField(item, "created_at", "Recently");
                topic.isRecommended = item.contains("\"is_recommended\":true") || item.contains("\"recommended\":true");

                allTopics.add(topic);

                if (topic.isRecommended) {
                    recommendationsContainer.add(createRecommendationCard(topic));
                }
            }
        }

        recommendationsSection.setVisible(recommendationsContainer.getComponentCount() > 0);
        renderFeed(allTopics);
    }

    private void filterTopics(String query) {
        if (query == null || query.trim().isEmpty()) {
            renderFeed(allTopics);
            return;
        }

        String lowerQuery = query.toLowerCase().trim();
        List<TopicData> filtered = new ArrayList<>();
        for (TopicData topic : allTopics) {
            if (topic.title.toLowerCase().contains(lowerQuery) ||
                topic.description.toLowerCase().contains(lowerQuery) ||
                topic.category.toLowerCase().contains(lowerQuery)) {
                filtered.add(topic);
            }
        }
        renderFeed(filtered);
    }

    private void renderFeed(List<TopicData> topics) {
        topicsFeedContainer.removeAll();

        if (topics.isEmpty()) {
            JPanel emptyCard = new JPanel(new FlowLayout(FlowLayout.CENTER));
            emptyCard.setBackground(CARD_BG);
            emptyCard.setBorder(new CompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                    new EmptyBorder(32, 20, 32, 20)
            ));

            String searchVal = searchField.getText().trim();
            String msg = searchVal.isEmpty() ? "No discussions yet in this group." : "No discussions found matching \"" + searchVal + "\".";
            JLabel emptyLbl = new JLabel(msg);
            emptyLbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
            emptyLbl.setForeground(MUTED_TEXT);
            emptyCard.add(emptyLbl);

            topicsFeedContainer.add(emptyCard);
        } else {
            for (TopicData topic : topics) {
                topicsFeedContainer.add(createDiscussionCard(topic));
                topicsFeedContainer.add(Box.createRigidArea(new Dimension(0, 16)));
            }
        }

        topicsFeedContainer.revalidate();
        topicsFeedContainer.repaint();
    }

    private void renderSidebarCategories() {
        categoriesContainer.removeAll();

        Map<String, Integer> catCounts = new HashMap<>();
        for (TopicData topic : allTopics) {
            catCounts.put(topic.category, catCounts.getOrDefault(topic.category, 0) + 1);
        }

        if (catCounts.isEmpty()) {
            JLabel empty = new JLabel("No categorized discussions yet.");
            empty.setFont(new Font("SansSerif", Font.PLAIN, 13));
            empty.setForeground(MUTED_TEXT);
            categoriesContainer.add(empty);
        } else {
            for (Map.Entry<String, Integer> entry : catCounts.entrySet()) {
                JPanel row = new JPanel(new BorderLayout());
                row.setBackground(CARD_BG);
                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));

                JLabel catLbl = new JLabel(entry.getKey());
                catLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
                catLbl.setForeground(DARK_TEXT);

                JLabel countLbl = new JLabel(String.valueOf(entry.getValue()));
                countLbl.setFont(new Font("SansSerif", Font.BOLD, 13));
                countLbl.setForeground(PRIMARY_BLUE);

                row.add(catLbl, BorderLayout.WEST);
                row.add(countLbl, BorderLayout.EAST);

                categoriesContainer.add(row);
                categoriesContainer.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        categoriesContainer.revalidate();
        categoriesContainer.repaint();
    }

    private JPanel createRecommendationCard(TopicData topic) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setPreferredSize(new Dimension(240, 100));
        card.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(12, 12, 12, 12)
        ));

        JLabel badge = new JLabel(" " + topic.category + " ");
        badge.setOpaque(true);
        badge.setBackground(BADGE_BG);
        badge.setForeground(BADGE_TEXT);
        badge.setFont(new Font("SansSerif", Font.BOLD, 10));
        badge.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("<html>" + topic.title + "</html>");
        title.setFont(new Font("SansSerif", Font.BOLD, 13));
        title.setForeground(DARK_TEXT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(badge);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(title);

        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (onTopicSelected != null) onTopicSelected.accept(topic.id);
            }
        });

        return card;
    }

    private JPanel createDiscussionCard(TopicData topic) {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(CARD_BG);
        card.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setBackground(CARD_BG);

        JLabel badge = new JLabel(" " + topic.category + " ");
        badge.setOpaque(true);
        badge.setBackground(BADGE_BG);
        badge.setForeground(BADGE_TEXT);
        badge.setFont(new Font("SansSerif", Font.BOLD, 11));

        JLabel dateLbl = new JLabel(topic.createdAt);
        dateLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        dateLbl.setForeground(MUTED_TEXT);

        topRow.add(badge, BorderLayout.WEST);
        topRow.add(dateLbl, BorderLayout.EAST);

        JPanel centerGroup = new JPanel();
        centerGroup.setLayout(new BoxLayout(centerGroup, BoxLayout.Y_AXIS));
        centerGroup.setBackground(CARD_BG);

        JLabel titleLbl = new JLabel(topic.title);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLbl.setForeground(DARK_TEXT);

        JLabel descLbl = new JLabel("<html>" + topic.description + "</html>");
        descLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        descLbl.setForeground(MUTED_TEXT);

        centerGroup.add(titleLbl);
        centerGroup.add(Box.createRigidArea(new Dimension(0, 6)));
        centerGroup.add(descLbl);

        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setBackground(CARD_BG);

        JLabel repliesLbl = new JLabel(topic.messagesCount + " replies");
        repliesLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        repliesLbl.setForeground(MUTED_TEXT);

        JButton viewBtn = new JButton("View Discussion →");
        viewBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        viewBtn.setForeground(PRIMARY_BLUE);
        viewBtn.setBorderPainted(false);
        viewBtn.setContentAreaFilled(false);
        viewBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewBtn.addActionListener(e -> {
            if (onTopicSelected != null) onTopicSelected.accept(topic.id);
        });

        bottomRow.add(repliesLbl, BorderLayout.WEST);
        bottomRow.add(viewBtn, BorderLayout.EAST);

        card.add(topRow, BorderLayout.NORTH);
        card.add(centerGroup, BorderLayout.CENTER);
        card.add(bottomRow, BorderLayout.SOUTH);

        return card;
    }

    private int extractIntField(String json, String... keys) {
        for (String key : keys) {
            Pattern pattern = Pattern.compile("\"" + key + "\":\\s*(\\d+)");
            Matcher matcher = pattern.matcher(json);
            if (matcher.find()) {
                return Integer.parseInt(matcher.group(1));
            }
        }
        return 0;
    }

    private String extractStringField(String json, String key, String defaultValue) {
        Pattern pattern = Pattern.compile("\"" + key + "\":\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return defaultValue;
    }

    private static class TopicData {
        int id;
        String title;
        String description;
        String category;
        int messagesCount;
        String createdAt;
        boolean isRecommended;
    }
}