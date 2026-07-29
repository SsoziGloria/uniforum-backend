package Student;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Consumer;

public class RecommendationsView extends JPanel {

    private final String authToken;
    private final Consumer<String> onNavigateDiscussion;

    private final Color PAGE_BG = new Color(248, 250, 252);
    private final Color CARD_BG = Color.WHITE;
    private final Color BORDER_COLOR = new Color(226, 232, 240);
    private final Color DARK_TEXT = new Color(30, 41, 59);
    private final Color MUTED_TEXT = new Color(100, 116, 139);
    private final Color PRIMARY_BLUE = new Color(37, 99, 235);

    public RecommendationsView(String authToken, Consumer<String> onNavigateDiscussion) {
        this.authToken = authToken;
        this.onNavigateDiscussion = onNavigateDiscussion;

        setLayout(new BorderLayout());
        setBackground(PAGE_BG);

        initComponents();
    }

    private void initComponents() {
        JPanel contentContainer = new JPanel();
        contentContainer.setLayout(new BoxLayout(contentContainer, BoxLayout.Y_AXIS));
        contentContainer.setBackground(PAGE_BG);
        contentContainer.setBorder(new EmptyBorder(0, 0, 40, 0));

        // --- GRADIENT BANNER HEADER ---
        JPanel banner = new JPanel();
        banner.setLayout(new BoxLayout(banner, BoxLayout.Y_AXIS));
        banner.setBackground(PRIMARY_BLUE);
        banner.setBorder(new EmptyBorder(36, 40, 36, 40));
        banner.setAlignmentX(Component.LEFT_ALIGNMENT);
        banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        JLabel badge = new JLabel("  🤖 AI Powered  ");
        badge.setFont(new Font("SansSerif", Font.BOLD, 12));
        badge.setForeground(Color.WHITE);
        badge.setOpaque(true);
        badge.setBackground(new Color(255, 255, 255, 40));
        badge.setBorder(new EmptyBorder(6, 12, 6, 12));
        badge.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Personalized Recommendations");
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Based on your previous discussions, quiz performance and topics you frequently engage with, UniForum recommends these discussions for you.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(new Color(219, 234, 254));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        banner.add(badge);
        banner.add(Box.createRigidArea(new Dimension(0, 12)));
        banner.add(title);
        banner.add(Box.createRigidArea(new Dimension(0, 8)));
        banner.add(subtitle);

        contentContainer.add(banner);

        // --- INNER WRAPPER ---
        JPanel innerWrap = new JPanel();
        innerWrap.setLayout(new BoxLayout(innerWrap, BoxLayout.Y_AXIS));
        innerWrap.setBackground(PAGE_BG);
        innerWrap.setBorder(new EmptyBorder(32, 40, 0, 40));
        innerWrap.setAlignmentX(Component.LEFT_ALIGNMENT);

        // --- SUMMARY METRICS GRID ---
        JPanel statsGrid = new JPanel(new GridLayout(1, 4, 20, 0));
        statsGrid.setBackground(PAGE_BG);
        statsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        statsGrid.add(createStatCard("Recommended Today", "12", PRIMARY_BLUE));
        statsGrid.add(createStatCard("Topics Classified", "28", new Color(22, 163, 74)));
        statsGrid.add(createStatCard("Discussions Read", "45", new Color(147, 51, 234)));
        statsGrid.add(createStatCard("Topics Followed", "8", new Color(249, 115, 22)));

        innerWrap.add(statsGrid);
        innerWrap.add(Box.createRigidArea(new Dimension(0, 32)));

        // --- AI EXPLANATION BOX ---
        JPanel explanationCard = new JPanel();
        explanationCard.setLayout(new BoxLayout(explanationCard, BoxLayout.Y_AXIS));
        explanationCard.setBackground(new Color(239, 246, 255));
        explanationCard.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(new Color(191, 219, 254), 1, true),
                new EmptyBorder(24, 24, 24, 24)
        ));
        explanationCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel expTitle = new JLabel("Why am I seeing these recommendations?");
        expTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        expTitle.setForeground(new Color(30, 58, 138));

        JLabel expBody = new JLabel("<html>Our recommendation engine analyzes your previous discussions, topics you interact with, and engagement patterns to suggest relevant discussions.</html>");
        expBody.setFont(new Font("SansSerif", Font.PLAIN, 13));
        expBody.setForeground(new Color(29, 78, 216));

        explanationCard.add(expTitle);
        explanationCard.add(Box.createRigidArea(new Dimension(0, 8)));
        explanationCard.add(expBody);

        innerWrap.add(explanationCard);
        innerWrap.add(Box.createRigidArea(new Dimension(0, 32)));

        // --- RECOMMENDED DISCUSSIONS SECTION ---
        JLabel recHeading = new JLabel("Recommended Discussions");
        recHeading.setFont(new Font("SansSerif", Font.BOLD, 22));
        recHeading.setForeground(DARK_TEXT);
        recHeading.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerWrap.add(recHeading);
        innerWrap.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel cardsGrid = new JPanel(new GridLayout(1, 2, 20, 0));
        cardsGrid.setBackground(PAGE_BG);
        cardsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardsGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 240));

        cardsGrid.add(createDiscussionCard("Advanced Embedded Systems Architecture", "Embedded Systems", "Recommended because it's similar to discussions you've engaged with (94% match).", "#"));
        cardsGrid.add(createDiscussionCard("Optimizing Laravel Database Migrations", "Backend Development", "Recommended because it's similar to discussions you've engaged with (89% match).", "#"));

        innerWrap.add(cardsGrid);
        innerWrap.add(Box.createRigidArea(new Dimension(0, 32)));

        // --- TOPICS CHIPS SECTION ---
        JLabel topicsHeading = new JLabel("Topics You May Like");
        topicsHeading.setFont(new Font("SansSerif", Font.BOLD, 22));
        topicsHeading.setForeground(DARK_TEXT);
        topicsHeading.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerWrap.add(topicsHeading);
        innerWrap.add(Box.createRigidArea(new Dimension(0, 16)));

        JPanel chipsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
        chipsPanel.setBackground(PAGE_BG);
        chipsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        chipsPanel.add(createChip("Android Development", new Color(219, 234, 254), new Color(29, 78, 216)));
        chipsPanel.add(createChip("Laravel & PHP", new Color(220, 252, 231), new Color(21, 128, 61)));
        chipsPanel.add(createChip("Database Optimization", new Color(243, 232, 255), new Color(126, 34, 206)));
        chipsPanel.add(createChip("UI/UX Layouts", new Color(254, 243, 199), new Color(180, 83, 9)));
        chipsPanel.add(createChip("Machine Learning APIs", new Color(252, 231, 243), new Color(190, 24, 93)));

        innerWrap.add(chipsPanel);
        contentContainer.add(innerWrap);

        JScrollPane scrollPane = new JScrollPane(
                contentContainer,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String label, String value, Color valueColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setForeground(MUTED_TEXT);

        JLabel val = new JLabel(value);
        val.setFont(new Font("SansSerif", Font.BOLD, 28));
        val.setForeground(valueColor);

        card.add(lbl);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(val);
        return card;
    }

    private JPanel createDiscussionCard(String titleText, String category, String description, String link) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(24, 24, 24, 24)
        ));

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel catBadge = new JLabel(" Recommended for you ");
        catBadge.setFont(new Font("SansSerif", Font.BOLD, 11));
        catBadge.setForeground(new Color(29, 78, 216));
        catBadge.setOpaque(true);
        catBadge.setBackground(new Color(219, 234, 254));
        catBadge.setBorder(new EmptyBorder(4, 10, 4, 10));

        JLabel catLbl = new JLabel(category);
        catLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        catLbl.setForeground(MUTED_TEXT);

        topRow.add(catBadge, BorderLayout.WEST);
        topRow.add(catLbl, BorderLayout.EAST);

        JLabel title = new JLabel(titleText);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(DARK_TEXT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel desc = new JLabel("<html>" + description + "</html>");
        desc.setFont(new Font("SansSerif", Font.PLAIN, 13));
        desc.setForeground(MUTED_TEXT);
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton viewBtn = new JButton("View Discussion");
        viewBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        viewBtn.setForeground(Color.WHITE);
        viewBtn.setBackground(PRIMARY_BLUE);
        viewBtn.setBorder(new EmptyBorder(10, 16, 10, 16));
        viewBtn.setFocusPainted(false);
        viewBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        viewBtn.addActionListener(e -> onNavigateDiscussion.accept(link));

        card.add(topRow);
        card.add(Box.createRigidArea(new Dimension(0, 16)));
        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(desc);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(viewBtn);

        return card;
    }

    private JLabel createChip(String text, Color bg, Color fg) {
        JLabel chip = new JLabel("  " + text + "  ");
        chip.setFont(new Font("SansSerif", Font.BOLD, 13));
        chip.setForeground(fg);
        chip.setOpaque(true);
        chip.setBackground(bg);
        chip.setBorder(new EmptyBorder(10, 16, 10, 16));
        return chip;
    }
}
