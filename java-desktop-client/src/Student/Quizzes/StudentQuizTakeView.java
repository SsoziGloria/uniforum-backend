package Student.Quizzes;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Consumer;

public class StudentQuizTakeView extends JPanel {

    private final String authToken;
    private final Consumer<String> onNavigateBack;
    
    private int currentIdx = 0;
    private final int totalQuestions = 3;
    private JPanel[] questionCards;
    private JButton[] navButtons;
    private JRadioButton[][] optionRadios;
    
    private JLabel progressTextLabel;
    private JProgressBar progressBar;
    private JLabel trackerLabel;
    private JLabel timerLabel;

    private final Color PAGE_BG = new Color(248, 250, 252);
    private final Color CARD_BG = Color.WHITE;
    private final Color BORDER_COLOR = new Color(226, 232, 240);
    private final Color DARK_TEXT = new Color(30, 41, 59);
    private final Color MUTED_TEXT = new Color(100, 116, 139);
    private final Color PRIMARY_BLUE = new Color(37, 99, 235);

    public StudentQuizTakeView(String authToken, Consumer<String> onNavigateBack) {
        this.authToken = authToken;
        this.onNavigateBack = onNavigateBack;

        setLayout(new BorderLayout());
        setBackground(PAGE_BG);

        initComponents();
    }

    private void initComponents() {
        JPanel mainWrap = new JPanel(new BorderLayout());
        mainWrap.setBackground(PAGE_BG);

        // --- STICKY TOP HEADER ---
        JPanel stickyHeader = new JPanel(new BorderLayout());
        stickyHeader.setBackground(Color.WHITE);
        stickyHeader.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                new EmptyBorder(16, 40, 16, 40)
        ));

        JPanel headerTitleBlock = new JPanel();
        headerTitleBlock.setLayout(new BoxLayout(headerTitleBlock, BoxLayout.Y_AXIS));
        headerTitleBlock.setBackground(Color.WHITE);

        JLabel quizTitle = new JLabel("OOP Assessment 1");
        quizTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        quizTitle.setForeground(DARK_TEXT);

        trackerLabel = new JLabel("Question 1 of 3");
        trackerLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        trackerLabel.setForeground(MUTED_TEXT);

        headerTitleBlock.add(quizTitle);
        headerTitleBlock.add(Box.createRigidArea(new Dimension(0, 2)));
        headerTitleBlock.add(trackerLabel);

        JPanel headerRightBlock = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        headerRightBlock.setBackground(Color.WHITE);

        timerLabel = new JLabel("⏱ 00:29:45");
        timerLabel.setFont(new Font("Monospaced", Font.BOLD, 16));
        timerLabel.setForeground(new Color(185, 28, 28));
        timerLabel.setBackground(new Color(254, 226, 226));
        timerLabel.setOpaque(true);
        timerLabel.setBorder(new EmptyBorder(8, 14, 8, 14));

        JButton finishBtn = new JButton("Finish Quiz");
        finishBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        finishBtn.setForeground(Color.WHITE);
        finishBtn.setBackground(PRIMARY_BLUE);
        finishBtn.setBorder(new EmptyBorder(10, 18, 10, 18));
        finishBtn.setFocusPainted(false);
        finishBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        finishBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to submit your quiz?", "Confirm Submission", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                onNavigateBack.accept("index");
            }
        });

        headerRightBlock.add(timerLabel);
        headerRightBlock.add(finishBtn);

        stickyHeader.add(headerTitleBlock, BorderLayout.WEST);
        stickyHeader.add(headerRightBlock, BorderLayout.EAST);
        mainWrap.add(stickyHeader, BorderLayout.NORTH);

        // --- BODY SPLIT GRID ---
        JPanel bodyGrid = new JPanel(new GridBagLayout());
        bodyGrid.setBackground(PAGE_BG);
        bodyGrid.setBorder(new EmptyBorder(32, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 24);

        // Left Side: Questions Area (weightx = 0.75)
        gbc.gridx = 0;
        gbc.weightx = 0.75;
        gbc.weighty = 1.0;

        JPanel questionsAreaWrap = new JPanel(new CardLayout());
        questionsAreaWrap.setBackground(PAGE_BG);

        questionCards = new JPanel[totalQuestions];
        optionRadios = new JRadioButton[totalQuestions][4];

        String[] sampleQuestions = {
            "Which of the following is a core pillar of Object-Oriented Programming?",
            "What keyword is used to inherit a class in Java?",
            "Which access modifier makes a variable accessible only within its own class?"
        };

        String[][] sampleOptions = {
            {"Compilation", "Encapsulation", "Interpretation", "Allocation"},
            {"implements", "extends", "inherits", "super"},
            {"public", "protected", "private", "default"}
        };

        CardLayout cl = (CardLayout) questionsAreaWrap.getLayout();

        for (int i = 0; i < totalQuestions; i++) {
            JPanel card = createQuestionCard(i, sampleQuestions[i], sampleOptions[i]);
            questionCards[i] = card;
            questionsAreaWrap.add(card, "Q" + i);
        }

        bodyGrid.add(questionsAreaWrap, gbc);

        // Right Side: Sidebar Navigation & Rules (weightx = 0.25)
        gbc.gridx = 1;
        gbc.weightx = 0.25;
        gbc.insets = new Insets(0, 0, 0, 0);

        JPanel sidebarWrap = new JPanel();
        sidebarWrap.setLayout(new BoxLayout(sidebarWrap, BoxLayout.Y_AXIS));
        sidebarWrap.setBackground(PAGE_BG);

        // Progress Widget Card
        JPanel progressCard = createCardPanel();
        progressCard.setLayout(new BoxLayout(progressCard, BoxLayout.Y_AXIS));
        JLabel pTitle = new JLabel("Progress");
        pTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        pTitle.setForeground(DARK_TEXT);

        JPanel pRow = new JPanel(new BorderLayout());
        pRow.setBackground(CARD_BG);
        JLabel pLabelAns = new JLabel("Answered");
        pLabelAns.setFont(new Font("SansSerif", Font.PLAIN, 12));
        pLabelAns.setForeground(MUTED_TEXT);
        progressTextLabel = new JLabel("0 / 3");
        progressTextLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        progressTextLabel.setForeground(DARK_TEXT);
        pRow.add(pLabelAns, BorderLayout.WEST);
        pRow.add(progressTextLabel, BorderLayout.EAST);

        progressBar = new JProgressBar(0, 3);
        progressBar.setValue(0);
        progressBar.setForeground(PRIMARY_BLUE);
        progressBar.setBackground(new Color(226, 232, 240));
        progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 12));

        progressCard.add(pTitle);
        progressCard.add(Box.createRigidArea(new Dimension(0, 12)));
        progressCard.add(pRow);
        progressCard.add(Box.createRigidArea(new Dimension(0, 6)));
        progressCard.add(progressBar);

        sidebarWrap.add(progressCard);
        sidebarWrap.add(Box.createRigidArea(new Dimension(0, 20)));

        // Question Navigator Grid Card
        JPanel navCard = createCardPanel();
        navCard.setLayout(new BoxLayout(navCard, BoxLayout.Y_AXIS));
        JLabel navTitle = new JLabel("Question Navigator");
        navTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        navTitle.setForeground(DARK_TEXT);
        navCard.add(navTitle);
        navCard.add(Box.createRigidArea(new Dimension(0, 14)));

        JPanel gridBtns = new JPanel(new GridLayout(1, 5, 8, 0));
        gridBtns.setBackground(CARD_BG);
        gridBtns.setAlignmentX(Component.LEFT_ALIGNMENT);
        gridBtns.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        navButtons = new JButton[totalQuestions];
        for (int i = 0; i < totalQuestions; i++) {
            final int target = i;
            JButton b = new JButton(String.valueOf(i + 1));
            b.setFont(new Font("SansSerif", Font.BOLD, 13));
            b.setFocusPainted(false);
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            b.addActionListener(e -> switchToQuestion(target));
            navButtons[i] = b;
            gridBtns.add(b);
        }
        updateNavigatorStyles();

        navCard.add(gridBtns);
        sidebarWrap.add(navCard);
        sidebarWrap.add(Box.createRigidArea(new Dimension(0, 20)));

        // Rules Card
        JPanel rulesCard = new JPanel();
        rulesCard.setBackground(new Color(254, 249, 195));
        rulesCard.setLayout(new BoxLayout(rulesCard, BoxLayout.Y_AXIS));
        rulesCard.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(new Color(253, 224, 71), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));
        rulesCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel rulesTitle = new JLabel("Quiz Rules");
        rulesTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        rulesTitle.setForeground(new Color(133, 77, 14));

        JTextArea rulesText = new JTextArea("• Timer auto-submits when time reaches 0.\n• No extra time for late entries.\n• Do not close your window.");
        rulesText.setFont(new Font("SansSerif", Font.PLAIN, 12));
        rulesText.setForeground(new Color(161, 98, 7));
        rulesText.setBackground(new Color(254, 249, 195));
        rulesText.setEditable(false);
        rulesText.setLineWrap(true);

        rulesCard.add(rulesTitle);
        rulesCard.add(Box.createRigidArea(new Dimension(0, 8)));
        rulesCard.add(rulesText);

        sidebarWrap.add(rulesCard);

        bodyGrid.add(sidebarWrap, gbc);
        mainWrap.add(bodyGrid, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(
                mainWrap,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createQuestionCard(int index, String qText, String[] options) {
        JPanel card = createCardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel qNum = new JLabel("QUESTION " + (index + 1) + " (1 MARK)");
        qNum.setFont(new Font("SansSerif", Font.BOLD, 12));
        qNum.setForeground(PRIMARY_BLUE);

        JLabel qDesc = new JLabel("<html><body style='width: 500px;'><b>" + qText + "</b></body></html>");
        qDesc.setFont(new Font("SansSerif", Font.PLAIN, 18));
        qDesc.setForeground(DARK_TEXT);

        card.add(qNum);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(qDesc);
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        ButtonGroup group = new ButtonGroup();
        JPanel optsWrap = new JPanel();
        optsWrap.setLayout(new BoxLayout(optsWrap, BoxLayout.Y_AXIS));
        optsWrap.setBackground(CARD_BG);
        optsWrap.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] keys = {"A", "B", "C", "D"};
        for (int j = 0; j < 4; j++) {
            JRadioButton radio = new JRadioButton(keys[j] + ".  " + options[j]);
            radio.setFont(new Font("SansSerif", Font.PLAIN, 14));
            radio.setForeground(DARK_TEXT);
            radio.setBackground(CARD_BG);
            radio.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                    new EmptyBorder(12, 16, 12, 16)
            ));
            radio.setFocusPainted(false);
            radio.setAlignmentX(Component.LEFT_ALIGNMENT);
            radio.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

            radio.addActionListener(e -> {
                updateNavigatorStyles();
                updateProgressCount();
            });

            group.add(radio);
            optionRadios[index][j] = radio;
            optsWrap.add(radio);
            optsWrap.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        card.add(optsWrap);
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        // Navigation Footer within Card
        JPanel cardFooter = new JPanel(new BorderLayout());
        cardFooter.setBackground(CARD_BG);
        cardFooter.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardFooter.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JButton prevBtn = new JButton("Previous");
        prevBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        prevBtn.setForeground(MUTED_TEXT);
        prevBtn.setBackground(new Color(241, 245, 249));
        prevBtn.setBorder(new EmptyBorder(8, 16, 8, 16));
        prevBtn.setFocusPainted(false);
        if (index == 0) {
            prevBtn.setVisible(false);
        } else {
            prevBtn.addActionListener(e -> switchToQuestion(index - 1));
        }

        JButton nextBtn = new JButton(index == totalQuestions - 1 ? "Submit Answer" : "Next Question");
        nextBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        nextBtn.setForeground(Color.WHITE);
        nextBtn.setBackground(index == totalQuestions - 1 ? new Color(22, 163, 74) : PRIMARY_BLUE);
        nextBtn.setBorder(new EmptyBorder(8, 16, 8, 16));
        nextBtn.setFocusPainted(false);
        nextBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nextBtn.addActionListener(e -> {
            if (index < totalQuestions - 1) {
                switchToQuestion(index + 1);
            } else {
                int choice = JOptionPane.showConfirmDialog(this, "Ready to submit your quiz?", "Submit All Answers", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    onNavigateBack.accept("index");
                }
            }
        });

        cardFooter.add(prevBtn, BorderLayout.WEST);
        cardFooter.add(nextBtn, BorderLayout.EAST);
        card.add(cardFooter);

        return card;
    }

    private void switchToQuestion(int targetIdx) {
        currentIdx = targetIdx;
        trackerLabel.setText("Question " + (currentIdx + 1) + " of " + totalQuestions);
        
        Container parent = questionCards[0].getParent();
        CardLayout cl = (CardLayout) parent.getLayout();
        cl.show(parent, "Q" + currentIdx);

        updateNavigatorStyles();
    }

    private void updateNavigatorStyles() {
        for (int i = 0; i < totalQuestions; i++) {
            boolean answered = false;
            for (int j = 0; j < 4; j++) {
                if (optionRadios[i][j].isSelected()) {
                    answered = true;
                    break;
                }
            }

            if (i == currentIdx) {
                navButtons[i].setBackground(PRIMARY_BLUE);
                navButtons[i].setForeground(Color.WHITE);
            } else if (answered) {
                navButtons[i].setBackground(new Color(220, 252, 231));
                navButtons[i].setForeground(new Color(22, 101, 52));
            } else {
                navButtons[i].setBackground(new Color(241, 245, 249));
                navButtons[i].setForeground(DARK_TEXT);
            }
        }
    }

    private void updateProgressCount() {
        int count = 0;
        for (int i = 0; i < totalQuestions; i++) {
            for (int j = 0; j < 4; j++) {
                if (optionRadios[i][j].isSelected()) {
                    count++;
                    break;
                }
            }
        }
        progressTextLabel.setText(count + " / " + totalQuestions);
        progressBar.setValue(count);
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
