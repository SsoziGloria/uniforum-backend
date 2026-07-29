package Lecturer.Quizzes;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Consumer;

public class QuizCreateView extends JPanel {

    private final String authToken;
    private final Consumer<String> onNavigateBack;
    private JPanel questionsContainer;
    private int qnIdx = 1;

    private final Color PAGE_BG = new Color(248, 250, 252);
    private final Color CARD_BG = Color.WHITE;
    private final Color BORDER_COLOR = new Color(226, 232, 240);
    private final Color DARK_TEXT = new Color(30, 41, 59);
    private final Color MUTED_TEXT = new Color(100, 116, 139);
    private final Color PRIMARY_BLUE = new Color(37, 99, 235);

    public QuizCreateView(String authToken, Consumer<String> onNavigateBack) {
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

        JButton backLink = new JButton("← Back to Quizzes");
        backLink.setFont(new Font("SansSerif", Font.PLAIN, 13));
        backLink.setForeground(PRIMARY_BLUE);
        backLink.setContentAreaFilled(false);
        backLink.setBorderPainted(false);
        backLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLink.setAlignmentX(Component.LEFT_ALIGNMENT);
        backLink.addActionListener(e -> onNavigateBack.accept("index"));

        JLabel title = new JLabel("Create New Quiz");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(DARK_TEXT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Configure schedule and add assessment questions.");
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

        // --- BASIC INFORMATION CARD ---
        JPanel infoCard = createCardPanel();
        infoCard.setLayout(new BoxLayout(infoCard, BoxLayout.Y_AXIS));
        JLabel infoTitle = new JLabel("Basic Information");
        infoTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        infoTitle.setForeground(DARK_TEXT);
        infoCard.add(infoTitle);
        infoCard.add(Box.createRigidArea(new Dimension(0, 16)));

        JPanel gridFields = new JPanel(new GridLayout(1, 2, 20, 0));
        gridFields.setBackground(CARD_BG);
        gridFields.setAlignmentX(Component.LEFT_ALIGNMENT);
        gridFields.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JPanel p1 = new JPanel();
        p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
        p1.setBackground(CARD_BG);
        JLabel l1 = new JLabel("Quiz Title");
        l1.setFont(new Font("SansSerif", Font.BOLD, 12));
        JTextField f1 = new JTextField();
        f1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        p1.add(l1);
        p1.add(Box.createRigidArea(new Dimension(0, 6)));
        p1.add(f1);

        JPanel p2 = new JPanel();
        p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
        p2.setBackground(CARD_BG);
        JLabel l2 = new JLabel("Student Category");
        l2.setFont(new Font("SansSerif", Font.BOLD, 12));
        JTextField f2 = new JTextField();
        f2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        p2.add(l2);
        p2.add(Box.createRigidArea(new Dimension(0, 6)));
        p2.add(f2);

        gridFields.add(p1);
        gridFields.add(p2);
        infoCard.add(gridFields);
        infoCard.add(Box.createRigidArea(new Dimension(0, 16)));

        // Schedule Row (3 columns)
        JPanel scheduleGrid = new JPanel(new GridLayout(1, 3, 20, 0));
        scheduleGrid.setBackground(CARD_BG);
        scheduleGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        scheduleGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        scheduleGrid.add(createLabeledField("Start Date", new JTextField("2026-07-29")));
        scheduleGrid.add(createLabeledField("Start Time", new JTextField("10:00")));
        scheduleGrid.add(createLabeledField("Duration (Minutes)", new JTextField("30")));

        infoCard.add(scheduleGrid);
        bodyWrap.add(infoCard);
        bodyWrap.add(Box.createRigidArea(new Dimension(0, 24)));

        // --- DYNAMIC QUESTION BUILDER CARD ---
        JPanel qCard = createCardPanel();
        qCard.setLayout(new BoxLayout(qCard, BoxLayout.Y_AXIS));

        JPanel qHeaderRow = new JPanel(new BorderLayout());
        qHeaderRow.setBackground(CARD_BG);
        qHeaderRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        qHeaderRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JPanel qTitleBlock = new JPanel();
        qTitleBlock.setLayout(new BoxLayout(qTitleBlock, BoxLayout.Y_AXIS));
        qTitleBlock.setBackground(CARD_BG);
        JLabel qt = new JLabel("Quiz Questions");
        qt.setFont(new Font("SansSerif", Font.BOLD, 18));
        qt.setForeground(DARK_TEXT);
        JLabel qsub = new JLabel("Add questions, choices, and set the correct key.");
        qsub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        qsub.setForeground(MUTED_TEXT);
        qTitleBlock.add(qt);
        qTitleBlock.add(qsub);

        JButton addQnBtn = new JButton("+ Add Question");
        addQnBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        addQnBtn.setForeground(PRIMARY_BLUE);
        addQnBtn.setBackground(new Color(241, 245, 249));
        addQnBtn.setBorder(new EmptyBorder(8, 14, 8, 14));
        addQnBtn.setFocusPainted(false);
        addQnBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addQnBtn.addActionListener(e -> addQuestionRow());

        qHeaderRow.add(qTitleBlock, BorderLayout.WEST);
        qHeaderRow.add(addQnBtn, BorderLayout.EAST);

        qCard.add(qHeaderRow);
        qCard.add(Box.createRigidArea(new Dimension(0, 20)));

        questionsContainer = new JPanel();
        questionsContainer.setLayout(new BoxLayout(questionsContainer, BoxLayout.Y_AXIS));
        questionsContainer.setBackground(CARD_BG);
        questionsContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Initial default question row
        questionsContainer.add(createQuestionItemPanel(1));

        qCard.add(questionsContainer);
        bodyWrap.add(qCard);
        bodyWrap.add(Box.createRigidArea(new Dimension(0, 24)));

        // --- SUBMIT / ACTIONS FOOTER ---
        JPanel actionFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        actionFooter.setBackground(PAGE_BG);
        actionFooter.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionFooter.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        cancelBtn.setForeground(DARK_TEXT);
        cancelBtn.setBackground(new Color(241, 245, 249));
        cancelBtn.setBorder(new EmptyBorder(10, 20, 10, 20));
        cancelBtn.setFocusPainted(false);
        cancelBtn.addActionListener(e -> onNavigateBack.accept("index"));

        JButton publishBtn = new JButton("Publish Quiz");
        publishBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        publishBtn.setForeground(Color.WHITE);
        publishBtn.setBackground(PRIMARY_BLUE);
        publishBtn.setBorder(new EmptyBorder(10, 20, 10, 20));
        publishBtn.setFocusPainted(false);
        publishBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        actionFooter.add(cancelBtn);
        actionFooter.add(publishBtn);

        bodyWrap.add(actionFooter);
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

    private JPanel createQuestionItemPanel(int index) {
        JPanel qItem = new JPanel();
        qItem.setLayout(new BoxLayout(qItem, BoxLayout.Y_AXIS));
        qItem.setBackground(new Color(248, 250, 252));
        qItem.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(16, 16, 16, 16)
        ));
        qItem.setAlignmentX(Component.LEFT_ALIGNMENT);
        qItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        JLabel qNum = new JLabel("Question " + index);
        qNum.setFont(new Font("SansSerif", Font.BOLD, 13));
        qNum.setForeground(DARK_TEXT);

        JPanel marksWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        marksWrap.setOpaque(false);
        JLabel mLabel = new JLabel("Marks:");
        mLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        mLabel.setForeground(MUTED_TEXT);
        JTextField mField = new JTextField("1", 4);
        marksWrap.add(mLabel);
        marksWrap.add(mField);

        if (index > 1) {
            JButton remBtn = new JButton("Remove");
            remBtn.setFont(new Font("SansSerif", Font.PLAIN, 11));
            remBtn.setForeground(Color.RED);
            remBtn.setContentAreaFilled(false);
            remBtn.setBorderPainted(false);
            remBtn.addActionListener(e -> {
                questionsContainer.remove(qItem);
                questionsContainer.revalidate();
                questionsContainer.repaint();
            });
            marksWrap.add(remBtn);
        }

        topRow.add(qNum, BorderLayout.WEST);
        topRow.add(marksWrap, BorderLayout.EAST);

        JTextArea descArea = new JTextArea("Enter question description...", 2, 20);
        descArea.setLineWrap(true);
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JPanel optionsGrid = new JPanel(new GridLayout(2, 2, 12, 8));
        optionsGrid.setOpaque(false);
        optionsGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        optionsGrid.add(createLabeledField("Option A", new JTextField()));
        optionsGrid.add(createLabeledField("Option B", new JTextField()));
        optionsGrid.add(createLabeledField("Option C", new JTextField()));
        optionsGrid.add(createLabeledField("Option D", new JTextField()));

        JPanel dropdownRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        dropdownRow.setOpaque(false);
        JLabel keyLbl = new JLabel("Correct Choice Key:  ");
        keyLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        JComboBox<String> keyCombo = new JComboBox<>(new String[]{"Option A", "Option B", "Option C", "Option D"});
        dropdownRow.add(keyLbl);
        dropdownRow.add(keyCombo);

        qItem.add(topRow);
        qItem.add(Box.createRigidArea(new Dimension(0, 8)));
        qItem.add(descScroll);
        qItem.add(Box.createRigidArea(new Dimension(0, 10)));
        qItem.add(optionsGrid);
        qItem.add(Box.createRigidArea(new Dimension(0, 10)));
        qItem.add(dropdownRow);

        return qItem;
    }

    private void addQuestionRow() {
        qnIdx++;
        questionsContainer.add(Box.createRigidArea(new Dimension(0, 12)));
        questionsContainer.add(createQuestionItemPanel(qnIdx));
        questionsContainer.revalidate();
        questionsContainer.repaint();
    }

    private JPanel createLabeledField(String labelText, JTextField field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        lbl.setForeground(MUTED_TEXT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        panel.add(lbl);
        panel.add(Box.createRigidArea(new Dimension(0, 4)));
        panel.add(field);
        return panel;
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