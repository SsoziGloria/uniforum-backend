package Student.Quizzes;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Consumer;

public class StudentQuizReportView extends JPanel {

    private final String authToken;
    private final Consumer<String> onNavigateBack;

    private final Color PAGE_BG = new Color(248, 250, 252);
    private final Color CARD_BG = Color.WHITE;
    private final Color BORDER_COLOR = new Color(226, 232, 240);
    private final Color DARK_TEXT = new Color(30, 41, 59);
    private final Color MUTED_TEXT = new Color(100, 116, 139);
    private final Color PRIMARY_BLUE = new Color(37, 99, 235);

    public StudentQuizReportView(String authToken, Consumer<String> onNavigateBack) {
        this.authToken = authToken;
        this.onNavigateBack = onNavigateBack;

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

        JButton backLink = new JButton("← Back to Software Engineering Year 2 Quizzes");
        backLink.setFont(new Font("SansSerif", Font.PLAIN, 13));
        backLink.setForeground(PRIMARY_BLUE);
        backLink.setContentAreaFilled(false);
        backLink.setBorderPainted(false);
        backLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLink.setAlignmentX(Component.LEFT_ALIGNMENT);
        backLink.addActionListener(e -> onNavigateBack.accept("index"));

        JLabel title = new JLabel("Android Layouts Quiz Report");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(DARK_TEXT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Summary of your results and group score calculations.");
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

        // Summary Stats Grid (4 columns)
        JPanel statsGrid = new JPanel(new GridLayout(1, 4, 16, 0));
        statsGrid.setBackground(PAGE_BG);
        statsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        statsGrid.add(createStatCard("Score", "92%", PRIMARY_BLUE));
        statsGrid.add(createStatCard("Questions", "5", DARK_TEXT));
        statsGrid.add(createStatCard("Correct Answers", "4", new Color(22, 163, 74)));
        statsGrid.add(createStatCard("Time Taken", "22 min", DARK_TEXT));

        bodyWrap.add(statsGrid);
        bodyWrap.add(Box.createRigidArea(new Dimension(0, 24)));

        // Performance Overview Bar Card
        JPanel overviewCard = createCardPanel();
        overviewCard.setLayout(new BoxLayout(overviewCard, BoxLayout.Y_AXIS));
        JLabel ovTitle = new JLabel("Performance Overview");
        ovTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        ovTitle.setForeground(DARK_TEXT);

        JPanel ovRow = new JPanel(new BorderLayout());
        ovRow.setBackground(CARD_BG);
        JLabel accLbl = new JLabel("Overall Accuracy");
        accLbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        accLbl.setForeground(DARK_TEXT);
        JLabel pctLbl = new JLabel("92%");
        pctLbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        pctLbl.setForeground(PRIMARY_BLUE);
        ovRow.add(accLbl, BorderLayout.WEST);
        ovRow.add(pctLbl, BorderLayout.EAST);

        JProgressBar accBar = new JProgressBar(0, 100);
        accBar.setValue(92);
        accBar.setForeground(PRIMARY_BLUE);
        accBar.setBackground(new Color(241, 245, 249));
        accBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 16));

        overviewCard.add(ovTitle);
        overviewCard.add(Box.createRigidArea(new Dimension(0, 16)));
        overviewCard.add(ovRow);
        overviewCard.add(Box.createRigidArea(new Dimension(0, 6)));
        overviewCard.add(accBar);

        bodyWrap.add(overviewCard);
        bodyWrap.add(Box.createRigidArea(new Dimension(0, 24)));

        // Question Breakdown Card
        JPanel breakdownCard = createCardPanel();
        breakdownCard.setLayout(new BoxLayout(breakdownCard, BoxLayout.Y_AXIS));
        JLabel brTitle = new JLabel("Question Breakdown");
        brTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        brTitle.setForeground(DARK_TEXT);
        breakdownCard.add(brTitle);
        breakdownCard.add(Box.createRigidArea(new Dimension(0, 16)));

        breakdownCard.add(createBreakdownRow("Correct Answers", "4", new Color(22, 101, 52), new Color(220, 252, 231)));
        breakdownCard.add(Box.createRigidArea(new Dimension(0, 12)));
        breakdownCard.add(createBreakdownRow("Incorrect Answers", "1", new Color(185, 28, 28), new Color(254, 226, 226)));
        breakdownCard.add(Box.createRigidArea(new Dimension(0, 12)));
        breakdownCard.add(createBreakdownRow("Unanswered", "0", new Color(161, 98, 7), new Color(254, 249, 195)));

        bodyWrap.add(breakdownCard);
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

    private JPanel createBreakdownRow(String title, String count, Color textColor, Color bgColor) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(bgColor);
        row.setBorder(new EmptyBorder(14, 18, 14, 18));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel t = new JLabel(title);
        t.setFont(new Font("SansSerif", Font.BOLD, 13));
        t.setForeground(textColor);

        JLabel c = new JLabel(count);
        c.setFont(new Font("SansSerif", Font.BOLD, 16));
        c.setForeground(textColor);

        row.add(t, BorderLayout.WEST);
        row.add(c, BorderLayout.EAST);
        return row;
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