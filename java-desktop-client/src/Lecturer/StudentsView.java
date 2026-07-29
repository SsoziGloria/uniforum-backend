package Lecturer;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentsView extends JPanel {

    private final String authToken;
    private final Color PAGE_BG = new Color(248, 250, 252);
    private final Color CARD_BG = Color.WHITE;
    private final Color BORDER_COLOR = new Color(226, 232, 240);
    private final Color DARK_TEXT = new Color(30, 41, 59);
    private final Color MUTED_TEXT = new Color(100, 116, 139);
    private final Color PRIMARY_BLUE = new Color(37, 99, 235);

    public StudentsView(String authToken) {
        this.authToken = authToken;

        setLayout(new BorderLayout());
        setBackground(PAGE_BG);

        initComponents();
    }

    private void initComponents() {
        JPanel contentContainer = new JPanel();
        contentContainer.setLayout(new BoxLayout(contentContainer, BoxLayout.Y_AXIS));
        contentContainer.setBackground(PAGE_BG);
        contentContainer.setBorder(new EmptyBorder(0, 0, 40, 0));

        // --- HEADER ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                new EmptyBorder(24, 40, 24, 40)
        ));
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel titleBlock = new JPanel();
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.setOpaque(false);

        JLabel title = new JLabel("Student Performance");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(DARK_TEXT);

        JLabel subtitle = new JLabel("Monitor participation, engagement, and academic activity.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(MUTED_TEXT);

        titleBlock.add(title);
        titleBlock.add(Box.createRigidArea(new Dimension(0, 4)));
        titleBlock.add(subtitle);
        headerPanel.add(titleBlock, BorderLayout.WEST);

        contentContainer.add(headerPanel);

        // --- BODY WRAPPER ---
        JPanel bodyWrap = new JPanel();
        bodyWrap.setLayout(new BoxLayout(bodyWrap, BoxLayout.Y_AXIS));
        bodyWrap.setBackground(PAGE_BG);
        bodyWrap.setBorder(new EmptyBorder(28, 40, 0, 40));
        bodyWrap.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Overview Cards Grid (4 columns)
        JPanel statsGrid = new JPanel(new GridLayout(1, 4, 16, 0));
        statsGrid.setBackground(PAGE_BG);
        statsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        statsGrid.add(createStatCard("Total Students", "48", DARK_TEXT));
        statsGrid.add(createStatCard("Active This Week", "41", new Color(22, 163, 74)));
        statsGrid.add(createStatCard("Average Participation", "16.4/20", PRIMARY_BLUE));
        statsGrid.add(createStatCard("Average Quiz Score", "82.5%", new Color(147, 51, 234)));

        bodyWrap.add(statsGrid);
        bodyWrap.add(Box.createRigidArea(new Dimension(0, 28)));

        // Students Table Card
        JPanel tableCard = createCardPanel();
        tableCard.setLayout(new BoxLayout(tableCard, BoxLayout.Y_AXIS));
        tableCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));

        JPanel tableHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 16));
        tableHeader.setBackground(CARD_BG);
        tableHeader.setOpaque(true);
        JLabel tableTitle = new JLabel("Student Activity");
        tableTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        tableTitle.setForeground(DARK_TEXT);
        tableHeader.add(tableTitle);
        tableCard.add(tableHeader);

        // Table Model Setup matching the Blade rows
        String[] columns = {"Student", "Discussions", "Answers", "Quiz Average", "Participation Mark", "Status"};
        Object[][] data = {
            {"Gloria S.\ngloria@student.uniforum.edu", "5", "12", "92%", "18/20", "Active"},
            {"Rita Okello\nrita@student.uniforum.edu", "8", "19", "95%", "20/20", "Active"},
            {"Tina S.\ntina@student.uniforum.edu", "2", "4", "74%", "14/20", "Inactive"}
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

        bodyWrap.add(tableCard);
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
                new EmptyBorder(20, 20, 20, 20)
        ));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }
}

