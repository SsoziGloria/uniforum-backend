package Student.Quizzes;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Consumer;

public class StudentQuizIndexView extends JPanel {

    private final String authToken;
    private final Consumer<String> onNavigate;

    private final Color PAGE_BG = new Color(248, 250, 252);
    private final Color CARD_BG = Color.WHITE;
    private final Color BORDER_COLOR = new Color(226, 232, 240);
    private final Color DARK_TEXT = new Color(30, 41, 59);
    private final Color MUTED_TEXT = new Color(100, 116, 139);
    private final Color PRIMARY_BLUE = new Color(37, 99, 235);

    public StudentQuizIndexView(String authToken, Consumer<String> onNavigate) {
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
        contentContainer.setBorder(new EmptyBorder(0, 0, 40, 0));

        // --- TOP HEADER ---
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                new EmptyBorder(24, 40, 24, 40)
        ));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        JButton backLink = new JButton("← Back to Group");
        backLink.setFont(new Font("SansSerif", Font.PLAIN, 13));
        backLink.setForeground(PRIMARY_BLUE);
        backLink.setContentAreaFilled(false);
        backLink.setBorderPainted(false);
        backLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLink.setAlignmentX(Component.LEFT_ALIGNMENT);
        backLink.addActionListener(e -> onNavigate.accept("group_show"));

        JLabel title = new JLabel("Quizzes for Software Engineering Year 2");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(DARK_TEXT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("View active, scheduled, and completed assessments.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(MUTED_TEXT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerPanel.add(backLink);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        headerPanel.add(title);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        headerPanel.add(subtitle);

        contentContainer.add(headerPanel);

        // --- BODY WRAPPER ---
        JPanel bodyWrap = new JPanel();
        bodyWrap.setLayout(new BoxLayout(bodyWrap, BoxLayout.Y_AXIS));
        bodyWrap.setBackground(PAGE_BG);
        bodyWrap.setBorder(new EmptyBorder(28, 40, 0, 40));
        bodyWrap.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Summary Cards (3 columns)
        JPanel statsGrid = new JPanel(new GridLayout(1, 3, 20, 0));
        statsGrid.setBackground(PAGE_BG);
        statsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        statsGrid.add(createStatCard("Active Quizzes", "1", PRIMARY_BLUE));
        statsGrid.add(createStatCard("Completed", "2", new Color(22, 163, 74)));
        statsGrid.add(createStatCard("Average Score", "84.5%", new Color(147, 51, 234)));

        bodyWrap.add(statsGrid);
        bodyWrap.add(Box.createRigidArea(new Dimension(0, 28)));

        // --- QUIZ LISTS SECTION ---
        JPanel listSection = new JPanel();
        listSection.setLayout(new BoxLayout(listSection, BoxLayout.Y_AXIS));
        listSection.setBackground(PAGE_BG);
        listSection.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 1. Active Quiz Item Card
        listSection.add(createActiveQuizCard(
                "OOP Assessment 1",
                "5 Questions • 30 Minutes",
                e -> onNavigate.accept("take")
        ));
        listSection.add(Box.createRigidArea(new Dimension(0, 16)));

        // 2. Upcoming Quiz Item Card
        listSection.add(createUpcomingQuizCard(
                "Database Fundamentals",
                "Scheduled: Aug 20, 2026 @ 02:00 PM • 45 Mins"
        ));
        listSection.add(Box.createRigidArea(new Dimension(0, 16)));

        // 3. Completed Quiz Item Card
        listSection.add(createCompletedQuizCard(
                "Android Layouts Quiz",
                "Assessment finished",
                "92%",
                e -> onNavigate.accept("report")
        ));

        bodyWrap.add(listSection);
        contentContainer.add(bodyWrap);

        JScrollPane scrollPane = new JScrollPane(
                contentContainer,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createActiveQuizCard(String title, String details, java.awt.event.ActionListener onTakeAction) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(new Color(191, 219, 254), 1, true),
                new EmptyBorder(24, 24, 24, 24)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        JPanel leftBlock = new JPanel();
        leftBlock.setLayout(new BoxLayout(leftBlock, BoxLayout.Y_AXIS));
        leftBlock.setBackground(CARD_BG);

        JLabel badge = new JLabel("  ACTIVE NOW  ");
        badge.setFont(new Font("SansSerif", Font.BOLD, 10));
        badge.setForeground(new Color(29, 78, 216));
        badge.setBackground(new Color(219, 234, 254));
        badge.setOpaque(true);
        badge.setBorder(new EmptyBorder(4, 8, 4, 8));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLbl.setForeground(DARK_TEXT);

        JLabel subLbl = new JLabel(details);
        subLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subLbl.setForeground(MUTED_TEXT);

        leftBlock.add(badge);
        leftBlock.add(Box.createRigidArea(new Dimension(0, 10)));
        leftBlock.add(titleLbl);
        leftBlock.add(Box.createRigidArea(new Dimension(0, 4)));
        leftBlock.add(subLbl);

        JButton takeBtn = new JButton("Start / Resume Quiz");
        takeBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        takeBtn.setForeground(Color.WHITE);
        takeBtn.setBackground(PRIMARY_BLUE);
        takeBtn.setBorder(new EmptyBorder(12, 20, 12, 20));
        takeBtn.setFocusPainted(false);
        takeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        takeBtn.addActionListener(onTakeAction);

        JPanel rightBlock = new JPanel(new GridBagLayout());
        rightBlock.setBackground(CARD_BG);
        rightBlock.add(takeBtn);

        card.add(leftBlock, BorderLayout.WEST);
        card.add(rightBlock, BorderLayout.EAST);
        return card;
    }

    private JPanel createUpcomingQuizCard(String title, String details) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(24, 24, 24, 24)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        JPanel leftBlock = new JPanel();
        leftBlock.setLayout(new BoxLayout(leftBlock, BoxLayout.Y_AXIS));
        leftBlock.setBackground(CARD_BG);

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLbl.setForeground(DARK_TEXT);

        JLabel subLbl = new JLabel(details);
        subLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subLbl.setForeground(MUTED_TEXT);

        leftBlock.add(titleLbl);
        leftBlock.add(Box.createRigidArea(new Dimension(0, 6)));
        leftBlock.add(subLbl);

        JLabel statusBadge = new JLabel("  Upcoming  ");
        statusBadge.setFont(new Font("SansSerif", Font.BOLD, 12));
        statusBadge.setForeground(new Color(161, 98, 7));
        statusBadge.setBackground(new Color(254, 249, 195));
        statusBadge.setOpaque(true);
        statusBadge.setBorder(new EmptyBorder(6, 12, 6, 12));

        JPanel rightBlock = new JPanel(new GridBagLayout());
        rightBlock.setBackground(CARD_BG);
        rightBlock.add(statusBadge);

        card.add(leftBlock, BorderLayout.WEST);
        card.add(rightBlock, BorderLayout.EAST);
        return card;
    }

    private JPanel createCompletedQuizCard(String title, String details, String score, java.awt.event.ActionListener onReportAction) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(24, 24, 24, 24)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        JPanel leftBlock = new JPanel();
        leftBlock.setLayout(new BoxLayout(leftBlock, BoxLayout.Y_AXIS));
        leftBlock.setBackground(CARD_BG);

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLbl.setForeground(DARK_TEXT);

        JLabel subLbl = new JLabel(details);
        subLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subLbl.setForeground(MUTED_TEXT);

        leftBlock.add(titleLbl);
        leftBlock.add(Box.createRigidArea(new Dimension(0, 6)));
        leftBlock.add(subLbl);

        JPanel rightBlock = new JPanel();
        rightBlock.setLayout(new BoxLayout(rightBlock, BoxLayout.Y_AXIS));
        rightBlock.setBackground(CARD_BG);

        JLabel scoreLbl = new JLabel(score);
        scoreLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        scoreLbl.setForeground(new Color(22, 163, 74));
        scoreLbl.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JButton reportLink = new JButton("View Report");
        reportLink.setFont(new Font("SansSerif", Font.PLAIN, 12));
        reportLink.setForeground(PRIMARY_BLUE);
        reportLink.setContentAreaFilled(false);
        reportLink.setBorderPainted(false);
        reportLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        reportLink.setAlignmentX(Component.RIGHT_ALIGNMENT);
        reportLink.addActionListener(onReportAction);

        rightBlock.add(scoreLbl);
        rightBlock.add(Box.createRigidArea(new Dimension(0, 2)));
        rightBlock.add(reportLink);

        card.add(leftBlock, BorderLayout.WEST);
        card.add(rightBlock, BorderLayout.EAST);
        return card;
    }

    private JPanel createStatCard(String title, String value, Color valueColor) {
        JPanel card = new JPanel();
        card.setBackground(CARD_BG);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

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
}
