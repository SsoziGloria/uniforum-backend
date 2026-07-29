package Lecturer.Quizzes;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.function.Consumer;

public class QuizResultsView extends JPanel {

    private final String authToken;
    private final Consumer<String> onNavigateBack;

    private final Color PAGE_BG = new Color(248, 250, 252);
    private final Color CARD_BG = Color.WHITE;
    private final Color BORDER_COLOR = new Color(226, 232, 240);
    private final Color DARK_TEXT = new Color(30, 41, 59);
    private final Color MUTED_TEXT = new Color(100, 116, 139);
    private final Color PRIMARY_BLUE = new Color(37, 99, 235);

    public QuizResultsView(String authToken, Consumer<String> onNavigateBack) {
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
        contentContainer.setBorder(new EmptyBorder(32, 40, 40, 40));

        // --- TOP HEADER CARD ---
        JPanel topCard = createCardPanel();
        topCard.setLayout(new BoxLayout(topCard, BoxLayout.Y_AXIS));

        JButton backLink = new JButton("← Back to Quiz Details");
        backLink.setFont(new Font("SansSerif", Font.PLAIN, 12));
        backLink.setForeground(PRIMARY_BLUE);
        backLink.setContentAreaFilled(false);
        backLink.setBorderPainted(false);
        backLink.setAlignmentX(Component.LEFT_ALIGNMENT);
        backLink.addActionListener(e -> onNavigateBack.accept("show"));

        JLabel title = new JLabel("OOP Assessment 1 Results");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(DARK_TEXT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Review student scores and attempt details.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(MUTED_TEXT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        topCard.add(backLink);
        topCard.add(Box.createRigidArea(new Dimension(0, 6)));
        topCard.add(title);
        topCard.add(Box.createRigidArea(new Dimension(0, 2)));
        topCard.add(subtitle);

        contentContainer.add(topCard);
        contentContainer.add(Box.createRigidArea(new Dimension(0, 24)));

        // --- SUMMARY STATS (4 columns) ---
        JPanel statsGrid = new JPanel(new GridLayout(1, 4, 16, 0));
        statsGrid.setBackground(PAGE_BG);
        statsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        statsGrid.add(createStatCard("Total Students", "48", PRIMARY_BLUE));
        statsGrid.add(createStatCard("Submitted", "42", new Color(22, 163, 74)));
        statsGrid.add(createStatCard("Average Score", "84.5%", new Color(147, 51, 234)));
        statsGrid.add(createStatCard("Completion Rate", "87.5%", new Color(249, 115, 22)));

        contentContainer.add(statsGrid);
        contentContainer.add(Box.createRigidArea(new Dimension(0, 24)));

        // --- TABLE CARD ---
        JPanel tableCard = createCardPanel();
        tableCard.setLayout(new BoxLayout(tableCard, BoxLayout.Y_AXIS));
        tableCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));

        JPanel tableHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 16));
        tableHeader.setBackground(CARD_BG);
        tableHeader.setOpaque(true);
        JLabel tableTitle = new JLabel("Student Performance Overview");
        tableTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        tableTitle.setForeground(DARK_TEXT);
        tableHeader.add(tableTitle);
        tableCard.add(tableHeader);

        String[] columns = {"Student", "Score", "Status", "Submitted At"};
        Object[][] data = {
            {"Gloria S.\ngloria@student.uniforum.edu", "92% (5/5)", "Submitted", "15 Aug 2026, 09:25 AM"},
            {"Rita Okello\nrita@student.uniforum.edu", "100% (5/5)", "Submitted", "15 Aug 2026, 09:18 AM"},
            {"Tina S.\ntina@student.uniforum.edu", "--", "In Progress", "N/A"}
        };

        JTable table = new JTable(new DefaultTableModel(data, columns)) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setRowHeight(50);
        table.setShowVerticalLines(false);
        table.setGridColor(BORDER_COLOR);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(241, 245, 249));

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));
        tableCard.add(tableScroll);

        contentContainer.add(tableCard);

        JScrollPane scrollPane = new JScrollPane(
                contentContainer,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
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
