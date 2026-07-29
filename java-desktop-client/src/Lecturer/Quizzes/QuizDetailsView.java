package Lecturer.Quizzes;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Consumer;

public class QuizDetailsView extends JPanel {

    private final String authToken;
    private final Consumer<String> onNavigate;

    private final Color PAGE_BG = new Color(248, 250, 252);
    private final Color CARD_BG = Color.WHITE;
    private final Color BORDER_COLOR = new Color(226, 232, 240);
    private final Color DARK_TEXT = new Color(30, 41, 59);
    private final Color MUTED_TEXT = new Color(100, 116, 139);
    private final Color PRIMARY_BLUE = new Color(37, 99, 235);

    public QuizDetailsView(String authToken, Consumer<String> onNavigate) {
        this.authToken = authToken;
        this.onNavigate = onNavigate;

        setLayout(new BorderLayout());
        setBackground(PAGE_BG);

        initComponents();
    }

    private void initComponents() {
        JPanel contentContainer = new JPanel();
        contentContainer.setLayout(new BoxLayout(contentContainer, BoxLayout.Y_AXIS));
        contentContainer.setBackground(PAGE_BG);
        contentContainer.setBorder(new EmptyBorder(32, 40, 40, 40));

        // --- TOP HEADER CARD ---
        JPanel topCard = createCardPanel();
        topCard.setLayout(new BorderLayout());

        JPanel titleBlock = new JPanel();
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.setBackground(CARD_BG);

        JButton backLink = new JButton("← Back to Quizzes");
        backLink.setFont(new Font("SansSerif", Font.PLAIN, 12));
        backLink.setForeground(PRIMARY_BLUE);
        backLink.setContentAreaFilled(false);
        backLink.setBorderPainted(false);
        backLink.setAlignmentX(Component.LEFT_ALIGNMENT);
        backLink.addActionListener(e -> onNavigate.accept("index"));

        JLabel title = new JLabel("OOP Assessment 1");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(DARK_TEXT);

        JLabel category = new JLabel("Software Engineering Year 2");
        category.setFont(new Font("SansSerif", Font.PLAIN, 13));
        category.setForeground(MUTED_TEXT);

        titleBlock.add(backLink);
        titleBlock.add(Box.createRigidArea(new Dimension(0, 6)));
        titleBlock.add(title);
        titleBlock.add(Box.createRigidArea(new Dimension(0, 2)));
        titleBlock.add(category);

        JLabel badge = new JLabel("  Published  ");
        badge.setFont(new Font("SansSerif", Font.BOLD, 12));
        badge.setForeground(new Color(22, 101, 52));
        badge.setBackground(new Color(220, 252, 231));
        badge.setOpaque(true);
        badge.setBorder(new EmptyBorder(6, 12, 6, 12));

        JPanel badgeWrap = new JPanel(new GridBagLayout());
        badgeWrap.setBackground(CARD_BG);
        badgeWrap.add(badge);

        topCard.add(titleBlock, BorderLayout.WEST);
        topCard.add(badgeWrap, BorderLayout.EAST);

        contentContainer.add(topCard);
        contentContainer.add(Box.createRigidArea(new Dimension(0, 24)));

        // --- CONFIGURATIONS CARD ---
        JPanel configCard = createCardPanel();
        configCard.setLayout(new BoxLayout(configCard, BoxLayout.Y_AXIS));
        JLabel cfgTitle = new JLabel("Quiz Configuration");
        cfgTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        cfgTitle.setForeground(DARK_TEXT);
        configCard.add(cfgTitle);
        configCard.add(Box.createRigidArea(new Dimension(0, 16)));

        JPanel cfgGrid = new JPanel(new GridLayout(2, 2, 20, 16));
        cfgGrid.setBackground(CARD_BG);
        cfgGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        cfgGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        cfgGrid.add(createConfigField("Date & Time", "15 Aug 2026, 09:00 AM"));
        cfgGrid.add(createConfigField("Duration", "30 Minutes"));
        cfgGrid.add(createConfigField("Student Category", "Software Engineering Year 2"));
        cfgGrid.add(createConfigField("Number of Questions", "5 Questions"));

        configCard.add(cfgGrid);
        contentContainer.add(configCard);
        contentContainer.add(Box.createRigidArea(new Dimension(0, 24)));

        // --- STATISTICS CARDS (3 columns) ---
        JPanel statsGrid = new JPanel(new GridLayout(1, 3, 16, 0));
        statsGrid.setBackground(PAGE_BG);
        statsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        statsGrid.add(createStatCard("Total Assigned Students", "48", PRIMARY_BLUE));
        statsGrid.add(createStatCard("Submissions", "42", new Color(22, 163, 74)));
        statsGrid.add(createStatCard("Average Score", "84.5%", new Color(147, 51, 234)));

        contentContainer.add(statsGrid);
        contentContainer.add(Box.createRigidArea(new Dimension(0, 24)));

        // --- ACTIONS PANEL ---
        JPanel actionsCard = createCardPanel();
        actionsCard.setLayout(new BoxLayout(actionsCard, BoxLayout.Y_AXIS));
        JLabel actTitle = new JLabel("Actions");
        actTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        actTitle.setForeground(DARK_TEXT);
        actionsCard.add(actTitle);
        actionsCard.add(Box.createRigidArea(new Dimension(0, 14)));

        JButton reportBtn = new JButton("View Performance Report");
        reportBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        reportBtn.setForeground(Color.WHITE);
        reportBtn.setBackground(PRIMARY_BLUE);
        reportBtn.setBorder(new EmptyBorder(10, 20, 10, 20));
        reportBtn.setFocusPainted(false);
        reportBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        reportBtn.addActionListener(e -> onNavigate.accept("results"));

        actionsCard.add(reportBtn);
        contentContainer.add(actionsCard);

        JScrollPane scrollPane = new JScrollPane(
                contentContainer,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createConfigField(String label, String value) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(CARD_BG);
        JLabel l1 = new JLabel(label);
        l1.setFont(new Font("SansSerif", Font.PLAIN, 12));
        l1.setForeground(MUTED_TEXT);
        JLabel l2 = new JLabel(value);
        l2.setFont(new Font("SansSerif", Font.BOLD, 14));
        l2.setForeground(DARK_TEXT);
        p.add(l1);
        p.add(Box.createRigidArea(new Dimension(0, 4)));
        p.add(l2);
        return p;
    }

    private JPanel createStatCard(String title, String value, Color valueColor) {
        JPanel card = createCardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel tLbl = new JLabel(title);
        tLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tLbl.setForeground(MUTED_TEXT);

        JLabel vLbl = new JLabel(value);
        vLbl.setFont(new Font("SansSerif", Font.BOLD, 24));
        vLbl.setForeground(valueColor);

        card.add(tLbl);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(vLbl);
        return card;
    }

    private JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_BG);
        panel.setOpaque(true);
        panel.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(24, 24, 24, 24)
        ));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }
}